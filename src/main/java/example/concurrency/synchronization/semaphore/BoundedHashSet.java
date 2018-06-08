package example.concurrency.synchronization.semaphore;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Semaphore;

/**
 * 用了信号量，其实相当于mutex。
 * @author liuhaibo on 2018/06/08
 */
public class BoundedHashSet<T> {
    private final Set<T> set = Collections.synchronizedSet(new HashSet<>());
    private final Semaphore semaphore;

    public BoundedHashSet(int bound) {
        this.semaphore = new Semaphore(bound);
    }

    public boolean add(T o) throws InterruptedException {
        semaphore.acquire();
        boolean added = false;
        try {
            added = set.add(o);
            return added;
        }
        finally {
            if (!added) {
                semaphore.release();
            }
        }
    }

    public boolean remove(T o) {
        boolean removed = set.remove(o);
        if (removed) {
            semaphore.release();
        }
        return removed;
    }

    public String toString() {
        return "set: " + set.toString() + "semaphore: " + semaphore;
    }

    public static void main(String... args) throws InterruptedException {
        BoundedHashSet<String> bSet = new BoundedHashSet<>(3);
        Runnable runnable = () -> {
            final String[] list = {"Hello", "World"};
            for (String s : list) {
                boolean result = false;
                try {
                    result = bSet.add(s);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(result + " => " + bSet);
            }

            System.out.println(bSet.remove("Hello") + " => " + bSet);
        };

        new Thread(runnable).start();
        new Thread(runnable).start();
    }

    /*
    true => set: [Hello]semaphore: java.util.concurrent.Semaphore@4663ba49[Permits = 2]
    false => set: [Hello]semaphore: java.util.concurrent.Semaphore@4663ba49[Permits = 2]
    true => set: [Hello, World]semaphore: java.util.concurrent.Semaphore@4663ba49[Permits = 1]
    false => set: [Hello, World]semaphore: java.util.concurrent.Semaphore@4663ba49[Permits = 1]
    true => set: [World]semaphore: java.util.concurrent.Semaphore@4663ba49[Permits = 2]
    false => set: [World]semaphore: java.util.concurrent.Semaphore@4663ba49[Permits = 2]
     */
}
