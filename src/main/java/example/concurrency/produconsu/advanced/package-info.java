/**
 * 1. 不需要考虑对临界资源加锁解锁；
 * 2. 不需要考虑临界条件是否满足；
 * 3. 不需要考虑wait()/notifyAll()；
 *
 * 使用{@link java.util.concurrent}包里的并发组件，就是这么简单┑(￣Д ￣)┍
 *
 * 这次是将{@link java.util.concurrent.BlockingQueue}作为临界资源，作为生产者-消费者的缓冲区。
 */
package example.concurrency.produconsu.advanced;