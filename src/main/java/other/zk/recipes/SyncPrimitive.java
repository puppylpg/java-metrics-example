package other.zk.recipes;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;

public class SyncPrimitive implements Watcher {

    static ZooKeeper zk = null;
    static final Integer MUTEX = -1;

    String root;

    SyncPrimitive(String address) {
        if (zk == null) {
            try {
                System.out.println("Starting ZK:");
                zk = new ZooKeeper(address, 3000, this);
                System.out.println("Finished starting ZK: " + zk);
            } catch (IOException e) {
                e.printStackTrace();
                zk = null;
            }
        }
    }

    synchronized public void process(WatchedEvent event) {
        synchronized (MUTEX) {
            //System.out.println("Process: " + event.getType());
            MUTEX.notify();
        }
    }

}