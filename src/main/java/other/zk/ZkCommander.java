package other.zk;

import java.io.*;
import java.nio.charset.StandardCharsets;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

/**
 * A simple example program to use {@link DataMonitor} to start and
 * stop executables based on a znode. The program watches the
 * specified znode and saves the data that corresponds to the
 * znode in the filesystem. The data should be linux command,
 * such as "cowsay HelloKuGou". It also starts this command when
 * the znode exists and kills the program if the znode goes away.
 * When znode data changes, program restarts.
 */
public class ZkCommander implements Watcher, Runnable, DataMonitor.DataMonitorListener {

    private DataMonitor dataMonitor;

    private String filename;

    private Process child;

    private ZkCommander(String hostPort, String znode, String filename) throws IOException {
        this.filename = filename;

        // 把自己注册为watcher（需要实现Watcher接口），**但是此时还没有设置watch回调**。
        // 之后的各种getData或者exists需要设置watcher的话，**才开始把这个watcher设置watch回调**，
        // 当回调的时候，调用process方法。
        ZooKeeper zk = new ZooKeeper(hostPort, 3000, this);

        // 由于把process委托给DataMonitor了，所以把自己注册为DataMonitor的listener，
        // 当DataMonitor处理znode变动时，需要ZkCommander有所回应（调用ZkCommander的回调）
        dataMonitor = new DataMonitor(zk, znode, null, this);
    }

    /**
     * java {@link ZkCommander} localhost:2181 znode recordFile
     */
    public static void main(String[] args) {
        if (args.length < 3) {
            System.err.println("USAGE: ZkCommander zkHostPort znode recordFile");
            System.exit(2);
        }
        String hostPort = args[0];
        String znode = args[1];
        String filename = args[2];
        try {
            new ZkCommander(hostPort, znode, filename).run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * We don't process any events ourselves, we just need to forward them on.
     *
     * @see Watcher#process(WatchedEvent)
     */
    @Override
    public void process(WatchedEvent event) {
        dataMonitor.process(event);
    }

    @Override
    public void run() {
        try {
            synchronized (this) {
                while (!dataMonitor.dead) {
                    wait();
                }
            }
        } catch (InterruptedException e) {
            // do nothing...
        }
    }

    @Override
    public void closing(int rc) {
        synchronized (this) {
            notifyAll();
        }
    }

    @Override
    public void nodeChange(byte[] data) {
        if (data == null) {
            if (child != null) {
                System.out.println("Killing process");
                child.destroy();
                try {
                    child.waitFor();
                } catch (InterruptedException e) {
                    // do nothing...
                }
            }
            child = null;
        } else {
            if (child != null) {
                System.out.println("Stopping child");
                child.destroy();
                try {
                    child.waitFor();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    // do nothing...
                }
            }
            try {
                FileOutputStream fos = new FileOutputStream(filename);
                fos.write(data);
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                String exec = new String(data, StandardCharsets.UTF_8);
                System.out.println("Starting child: " + exec);
                child = Runtime.getRuntime().exec(exec);
                new StreamWriter(child.getInputStream(), System.out);
                new StreamWriter(child.getErrorStream(), System.err);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static class StreamWriter extends Thread {
        OutputStream os;

        InputStream is;

        StreamWriter(InputStream is, OutputStream os) {
            this.is = new BufferedInputStream(is);
            this.os = new BufferedOutputStream(os);
            start();
        }

        @Override
        public void run() {
            byte b[] = new byte[80];
            int rc;
            try {
                while ((rc = is.read(b)) > 0) {
                    os.write(b, 0, rc);
                    os.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
