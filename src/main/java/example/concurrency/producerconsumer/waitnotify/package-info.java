/**
 * wait/notify/notifyAll是属于Object的方法，在Object上构成阻塞队列。
 * wait()：释放锁，并等待被唤醒；
 *      当前线程必须拥有此对象的monitor（即锁），才能调用某个对象的wait()方法能让当前线程阻塞，
 *      这种阻塞是通过提前释放synchronized锁，重新去请求锁导致的阻塞，这种请求必须有其他线程通过notify()或者notifyAll（）唤醒重新竞争获得锁
 *      即：爸爸歇会儿，锁给你们，搞完了叫我，但是我醒了（且抢到锁之后）就从这里继续执行。
 *
 * notify()/notifyAll()：唤醒那些等待的线程；
 *      notify()或者notifyAll()方法并不释放锁，必须等到synchronized方法或者语法块执行完才真正释放锁
 *      即：爸爸把你们叫醒了，临界区的条件满足了，你们可以用了（只要你能竞争到锁），但是爸爸还没释放锁，等我释放了大家（包括爸爸我）再一起抢。
 *
 * 1. 搞清临界资源是什么，wait和notify是在等临界资源和通知临界资源；
 * 2. 使用临界资源要加锁，锁的就是临界资源；
 * 3. 判断临界资源是否可用，使用while而非if：
 *      先判断一次是否可用，不可用就wait，但是再醒过来的时候未必就是可用的；
 *      比如消费者压根没消费，却恶意唤醒生产者，生产者不再次检查是否满足条件就直接生产，gg；
 * 4. 优先使用notifyAll()而非notify()，多唤醒几个，不满足的话让他们再等就行了；
 *      虽然如果大家等待的条件相同，notify()随机唤醒一个等待的线程，是正确且高效的，但是
 *      使用notifyAll可以唤醒某些可能被恶意等待的线程，所以优先选用。
 *
 *
 * 以下是Thread的方法，不是Object的，和阻塞队列无关，和锁无关：
 * 多说一句，Thread.sleep(xxx) vs. yield()：
 *      Thread.sleep(xxx)只交出cpu，不交出锁，自己歇了，cpu有可能被低级线程抢到；
 *      yield()只交出cpu，不交出锁，不可能把cpu交给更低级的线程，因为它交出cpu之后，立刻就竞争了；
 *
 * 他们之间的相似性，大概就是wait的时候是无法继续执行的，sleep的时候也是无法执行的。都休眠了。yield是不休眠的。
 * wait方法，调用了native的wait(0)方法，代表永久休眠，除非被唤醒。但是wait(n)在休眠方面有点儿像sleep(n)
 */
package example.concurrency.producerconsumer.waitnotify;
/*

// The standard idiom for calling the wait method in Java

synchronized (sharedObject) {
    while (condition) {
        // (Releases lock, and reacquires on wakeup)
        sharedObject.wait(); // 获取该对象的锁的线程进入该对象的条件队列
    }
    // do action based upon condition e.g. take or put into queue

    // notify other threads
    notifyAll(); // 通知该对象的条件队列里的所有的线程醒醒，看看条件是否满足了。所以要用while进行判断
}

*/