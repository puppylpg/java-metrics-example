package example.concurrency.bbuffer.immature;

/**
 * @author puppylpg on 2018/11/29
 */
public interface BoundedBuffer<V> {

    /**
     * 往循环队列的末尾加入一个值。
     *
     * @param v 需要放入队列的值
     * @throws Exception 数据放入队列时出现异常
     */
    void put(V v) throws Exception;

    /**
     * 从循环队列的队首里取出一个值。
     *
     * @return 队首的值
     * @throws Exception 从队列取数据出现异常
     */
    V take() throws Exception;
}
