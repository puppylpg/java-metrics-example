/**
 * Atomic和VolatileSynchronized用起来很简单，但是只能用于单个field的同步。
 * 如果需要多个field一起同步，比如Point(x, y)，就要使用synchronized或者lock了。
 *
 * @author liuhaibo on 2019/11/11
 */
package example.concurrency.synchronization.lock.counters.counter;