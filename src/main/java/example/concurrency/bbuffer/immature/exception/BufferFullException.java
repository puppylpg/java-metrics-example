package example.concurrency.bbuffer.immature.exception;

/**
 * @author puppylpg on 2018/11/29
 */
public class BufferFullException extends Exception {

    public BufferFullException() {}

    public BufferFullException(String message) {
        super(message);
    }
}
