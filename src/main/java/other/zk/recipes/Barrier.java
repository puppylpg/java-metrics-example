package other.zk.recipes;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.Stat;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Random;

/**
 * Barrier
 */
public class Barrier extends SyncPrimitive {
    private int size;
    private String name;

    /**
     * Barrier constructor
     */
    private Barrier(String address, String root, int size) {
        super(address);
        this.root = root;
        this.size = size;

        // Create barrier node
        if (zk != null) {
            try {
                Stat s = zk.exists(root, false);
                if (s == null) {
                    zk.create(root, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                }
            } catch (KeeperException e) {
                System.out.println("Keeper exception when instantiating queue: " + e.toString());
            } catch (InterruptedException e) {
                System.out.println("Interrupted exception");
            }
        }

        // My node name
        try {
            name = InetAddress.getLocalHost().getCanonicalHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

    }

    /**
     * Join barrier
     */
    private boolean enter() throws KeeperException, InterruptedException {
        zk.create(root + "/" + name, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        while (true) {
            synchronized (MUTEX) {
                List<String> list = zk.getChildren(root, true);

                if (list.size() < size) {
                    MUTEX.wait();
                } else {
                    return true;
                }
            }
        }
    }

    /**
     * Wait until all reach barrier
     */
    private void leave() throws KeeperException, InterruptedException {
        zk.delete(root + "/" + name, 0);
        while (true) {
            synchronized (MUTEX) {
                List<String> list = zk.getChildren(root, true);
                if (list.size() > 0) {
                    MUTEX.wait();
                } else {
                    return;
                }
            }
        }
    }

    public static void main(String... args) {
        Barrier b = new Barrier(args[0], "/b1", new Integer(args[1]));
        try {
            boolean flag = b.enter();
            System.out.println("Entered barrier: " + args[1]);
            if (!flag) {
                System.out.println("Error when entering the barrier");
            }
        } catch (KeeperException | InterruptedException e) {
            // do nothing...
        }

        // Generate random integer
        Random rand = new Random();
        int r = rand.nextInt(100);
        // Loop for rand iterations
        for (int i = 0; i < r; i++) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                // do nothing...
            }
        }
        try {
            b.leave();
        } catch (KeeperException | InterruptedException e) {
            // do nothing...
        }
        System.out.println("Left barrier");
    }

}