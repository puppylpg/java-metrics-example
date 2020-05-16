/**
 * 生产者-消费者的问题，也可以说是有界队列的问题。
 *
 * 在immature里，使用了自旋等待/休眠分别实现了多线程之间的同步。但无论那种方式，都涉及到响应性和浪费cpu时间片的权衡。
 *
 * 如果有一种方法，在条件不成立时挂起线程，条件成立时又能立刻醒来，就再好不过了。条件队列可以实现这一功能。
 * 条件队列：在队列中的放置的是一个个等待相关条件的线程，而不是普通元素。所以叫条件队列，以示和普通队列的区别。
 *
 * Java中每个对象都是一个锁，同样也是一个条件队列。Object的wait/notify/notifyAll方法构成了条件队列的API。
 * 想调用某个对象的条件队列的任何一个方法，必须持有该对象的锁，否则会抛出{@link java.lang.IllegalMonitorStateException}，
 * 因为“等待由状态构成的条件”和“维护状态的一致性” 必须绑定在一起，这是一个复合操作，所以必须获取锁将其变成原子操作。
 *
 * 在waitnotify里，我们手动操作条件队列实现了更高效的生产者-消费者。但是手动加锁解锁还是挺复杂的。
 *
 * 当然，使用{@link java.util.concurrent.BlockingQueue}才是最好的选择。见advanced.
 *
 * 这下，可以明白为什么wait/notify/notifyAll在{@link java.lang.Object}里了。
 * 同时也能把这三个方法同{@link java.lang.Thread}里的sleep/join等区别开了，他们不是一路的，
 * 且Object里的方法才是跟锁相关的（跟条件队列相关），而Thread里的方法无论怎么操作，**都跟锁无关**。
 * 所以wait会释放锁，而sleep不会，一定不要带着锁去sleep！
 */
package example.concurrency.producerconsumer;

/*

// The standard idiom for calling the wait method in Java

synchronized (sharedObject) {
    while (!condition) {
        // (Releases lock, and reacquires on wakeup)
        sharedObject.wait(); // 获取该对象的锁的线程进入该对象的条件队列
    }
    // do action based upon condition e.g. take or put into queue

    // notify other threads
    notifyAll(); // 通知该对象的条件队列里的所有的线程醒醒，看看条件是否满足了。所以要用while进行判断
}

*/