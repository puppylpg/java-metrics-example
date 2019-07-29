package example.exception;

/**
 * @author liuhaibo on 2019/07/29
 */
public class Junk {

    /**
     * example.exception.HighLevelException: Why me...
     * 	at example.exception.Junk.a(Junk.java:36)
     * 	at example.exception.Junk.main(Junk.java:10)
     * Caused by: example.exception.MidLevelException: Someone not me is to blame, and sorry to hurt anyone else...
     * 	at example.exception.Junk.c(Junk.java:48)
     * 	at example.exception.Junk.b(Junk.java:41)
     * 	at example.exception.Junk.a(Junk.java:34)
     * 	... 1 more
     * Caused by: example.exception.LowLevelException: I am the one who is to blame...
     * 	at example.exception.Junk.e(Junk.java:57)
     * 	at example.exception.Junk.d(Junk.java:53)
     * 	at example.exception.Junk.c(Junk.java:46)
     * 	... 3 more
     * ======= cause.toString =======
     * example.exception.MidLevelException: Someone not me is to blame, and sorry to hurt anyone else...
     * ======= cause.message =======
     * Someone not me is to blame, and sorry to hurt anyone else...
     * ======= cause stack =======
     * example.exception.MidLevelException: Someone not me is to blame, and sorry to hurt anyone else...
     * 	at example.exception.Junk.c(Junk.java:48)
     * 	at example.exception.Junk.b(Junk.java:41)
     * 	at example.exception.Junk.a(Junk.java:34)
     * 	at example.exception.Junk.main(Junk.java:10)
     * Caused by: example.exception.LowLevelException: I am the one who is to blame...
     * 	at example.exception.Junk.e(Junk.java:57)
     * 	at example.exception.Junk.d(Junk.java:53)
     * 	at example.exception.Junk.c(Junk.java:46)
     * 	... 3 more
     * ======= cause'cause =======
     * example.exception.LowLevelException: I am the one who is to blame...
     * 	at example.exception.Junk.e(Junk.java:57)
     * 	at example.exception.Junk.d(Junk.java:53)
     * 	at example.exception.Junk.c(Junk.java:46)
     * 	at example.exception.Junk.b(Junk.java:41)
     * 	at example.exception.Junk.a(Junk.java:34)
     * 	at example.exception.Junk.main(Junk.java:10)
     */
    public static void main(String args[]) {
        try {
            a();
        } catch (HighLevelException e) {
            e.printStackTrace();

            System.err.println("======= cause.toString =======");
            // 仅仅是“类名+message”，没啥卵用，不如直接e.printStackTrace
            System.err.println(e.getCause());
            System.err.println("======= cause.message =======");
            System.err.println(e.getCause().getMessage());
            System.err.println("======= cause stack =======");
            e.getCause().printStackTrace();
            System.err.println("======= cause'cause =======");
            e.getCause().getCause().printStackTrace();
            // NPE. 如果最后的cause是自己，调用getCause直接返回null……
//            e.getCause().getCause().getCause().printStackTrace();

//            if (e.getCause() instanceof MidLevelException) {
//                System.out.println("niubility");
//            }
        }
    }

    static void a() throws HighLevelException {
        try {
            b();
        } catch (MidLevelException e) {
            throw new HighLevelException("Why me...", e);
        }
    }

    static void b() throws MidLevelException {
        c();
    }

    static void c() throws MidLevelException {
        try {
            d();
        } catch (LowLevelException e) {
            throw new MidLevelException("Someone not me is to blame, and sorry to hurt anyone else...", e);
        }
    }

    static void d() throws LowLevelException {
        e();
    }

    static void e() throws LowLevelException {
        throw new LowLevelException("I am the one who is to blame...");
    }
}

class HighLevelException extends Exception {
    HighLevelException(String message, Throwable cause) {
        super(message, cause);
    }
}

class MidLevelException extends Exception {
    MidLevelException(String message, Throwable cause) {
        super(message, cause);
    }
}

class LowLevelException extends Exception {
    LowLevelException(String message) {
        super(message);
    }
}
