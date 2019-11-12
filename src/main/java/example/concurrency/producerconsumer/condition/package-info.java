/**
 * 目的：使用自定义锁对象{@link java.util.concurrent.locks.Lock}代替synchronized对象，
 * 使用Condition代替锁对象上的monitor方法（wait/notify）。这样就能将原来的synchronized锁对象
 * 的monitor方法分解到不同的对象上，产生了多对阻塞队列（条件队列）的效果。
 *
 * 而且，这样通知的时候就可以只通知特定线程了。
 * 比如原有的wait，队列满了，100个生产者线程都在wait。当消费者取出一个元素是，通知生产者
 * 可以继续生产了。一个生产者生产完毕，就继续notify可以取了。这个notify的时候就会通知到生产者。
 * 本质上是因为生产者消费者都在等待“同一个唤醒”，所以一唤都行了，然后才发现不是自己，继续wait。
 *
 * 但是Condition就不一样了。可以搞出来两个Condition，一个叫做“notFull”条件（等待“没满”这一条件的发生），
 * 一个叫做“notEmpty”条件（等待“没空”这一条件的发生）。
 * （起这两个名字，而不起“有空”之类的，是因为满了则生产者睡（代码上进行isFull的判断），
 * 所以他们应该等待的是“没满”，没满的话就可以干活了。消费者等待的是“没空”，所以才是这两个名字。）
 * 当队列满了没空的时候，notFull条件不满足，那就调用await方法，使当前线程等待。
 * 如果有空，那么放一个元素进去，notEmpty条件满足，调用notEmpty条件的signal/signalAll，唤醒等待消费元素的消费者线程。
 * 同理，消费者也一样。这样一来，生产者只唤醒消费者，消费者只唤醒生产者。因为他们是两个条件队列。
 *
 * 所以说，条件队列这名字，起的真妥帖啊！
 *
 * {@link java.util.concurrent.locks.Condition}实例也是普通object，所以本身就可以作为锁对象，
 * 调用他们的wait/notify之类的方法。但是这个和使用Condition相关联的lock没有任何关联（本来就没关联）。
 * 为了避免混淆，使用Condition的时候就别再用它的wait/notify那一套东西了。
 *
 * {@link java.util.concurrent.ArrayBlockingQueue}的put和take就是通过条件队列实现的。（不是用的wait/notify）
 */
package example.concurrency.producerconsumer.condition;