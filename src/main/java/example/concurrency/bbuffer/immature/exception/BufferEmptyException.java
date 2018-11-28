package example.concurrency.bbuffer.immature.exception;

/**
 * @author puppylpg on 2018/11/29
 */
public class BufferEmptyException extends Exception {

    public BufferEmptyException() {}

    public BufferEmptyException(String message) {
        super(message);
    }
}
