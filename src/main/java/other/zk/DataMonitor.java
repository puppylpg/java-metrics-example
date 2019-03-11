package other.zk;

import java.util.Arrays;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.AsyncCallback.StatCallback;
import org.apache.zookeeper.KeeperException.Code;
import org.apache.zookeeper.data.Stat;

/**
 * A simple class that monitors the data and existence of a ZooKeeper
 * node. It uses asynchronous ZooKeeper APIs.
 *
 * zk的回调one-time trigger！！
 *
 * 流程：
 * 完成回调：DataMonitor检查znode的exists，设置“完成回调”，当检查完毕时，触发“完成回调”processResult，
 * （同时也给该znode设置了“watch回调”）
 * 如果zk状态正常，啥也不做；
 * 若发现zk超时、无法认证，结束程序；
 * 否则尝试再用exists检查（再设置新的完成回调和watch回调）
 *
 * watch回调：如果znode变了，会触发{@link ZkCommander}的“watch回调”，委托给{@link DataMonitor}：
 * 如果是session超时，结束进程；
 * 如果是znode数据变了（getData，并重新创建watch），重启linux command；
 * 如果是znode删了/新增了，打log，重新创建watch（必须重新创建，要不然下次再变就收不到通知了）；
 * 如果是znode的其他事件，重新检查znode。
 *
 * 所以其实就是，每当znode变了，将{@link WatchedEvent}通知watcher，
 * 然后watcher根据{@link WatchedEvent}，做出进一步判断。
 */
public class DataMonitor implements Watcher, StatCallback {

    private ZooKeeper zk;

    private String znode;

    /**
     * 链式watcher，就是在正常watcher后面，又加了一个watcher，
     * 起到了链式调用watcher的作用。见使用的地方
     */
    private Watcher chainedWatcher;

    boolean dead;

    private DataMonitorListener listener;

    DataMonitor(ZooKeeper zk, String znode, Watcher chainedWatcher, DataMonitorListener listener) {
        this.zk = zk;
        this.znode = znode;
//        this.chainedWatcher = this;
        this.chainedWatcher = chainedWatcher;
        this.listener = listener;
        // Get things started by checking if the node exists. We are going
        // to be completely event driven
        // 把自己注册为“确认znode状态”的完成回调，一旦确认完毕（这是一个完成回调，不是watch回调），就会调用该回调。
        // 见AsyncCallback.StatCallback#processResult
        // 顺带给该znode添加watch回调（这个回调是创建zk的时候的指定的那个回调，对于该程序，就是ZkCommander）
        zk.exists(znode, true, this, null);
    }

    @Override
    public void process(WatchedEvent event) {
        String path = event.getPath();
        switch (event.getType()) {
            case None:
                // We are being told that the state of the
                // connection has changed
                switch (event.getState()) {
                    case SyncConnected:
                        // In this particular example we don't need to do anything
                        // here - watches are automatically re-registered with
                        // server and any watches triggered while the client was
                        // disconnected will be delivered (in order of course)
                        System.out.println("Every thing is ok for whole zk or node: " + path);
                        break;
                    case Expired:
                        // It's all over
                        dead = true;
                        System.out.println("Session expired, quit.");
                        listener.closing(KeeperException.Code.SessionExpired);
                        break;
                }
                break;
            case NodeDataChanged:
                System.out.println("Node data changed: " + path);
                byte[] b = null;
                try {
                    b = zk.getData(znode, true, null);
                } catch (KeeperException | InterruptedException e) {
                    e.printStackTrace();
                }
                if (b != null) {
                    listener.nodeChange(b);
                }
                break;
            case NodeDeleted:
                System.out.println("Woops, node deleted: " + path);
                zk.exists(znode, true, this, null);
                break;
            case NodeCreated:
                System.out.println("Wow, new node created: " + path);
                zk.exists(znode, true, this, null);
                break;
            case NodeChildrenChanged:
            default:
                System.out.println("Others things which I don't care happened for the node: " + path);
                System.out.println("Don't know, let me find out..");
                zk.exists(znode, true, this, null);
        }

        // 如果我们把DataMonitor也注册为chainedWatcher，就无限递归stackoverflow了
        if (chainedWatcher != null) {
            chainedWatcher.process(event);
        }
    }

    @Override
    public void processResult(int rc, String path, Object ctx, Stat stat) {
        switch (rc) {
            case Code.Ok:
                System.out.println("Node exists: " + path);
                break;
            case Code.NoNode:
                System.out.println("No node: " + path);
                break;
            case Code.SessionExpired:
            case Code.NoAuth:
                System.out.println("No Auth or SessionExpired, quit.");
                dead = true;
                listener.closing(rc);
                return;
            default:
                System.out.println("Unknown completion callback status. retry.");
                // Retry errors：可恢复性错误，尝试恢复一发
                zk.exists(znode, true, this, null);
        }
    }

    /**
     * Other classes use the DataMonitor by implementing this method
     */
    public interface DataMonitorListener {
        /**
         * The node has changed.
         */
        void nodeChange(byte data[]);

        /**
         * The ZooKeeper session is no longer valid.
         *
         * @param rc the ZooKeeper reason code
         */
        void closing(int rc);
    }

}
