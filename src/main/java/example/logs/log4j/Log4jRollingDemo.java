package example.logs.log4j;

import org.apache.log4j.Logger;

/**
 * @author liuhaibo on 2017/12/02
 */
public class Log4jRollingDemo {

    private final static Logger log = Logger.getLogger(Log4jRollingDemo.class);

    public static void main(String[] args) throws InterruptedException {
        for(int i = 0; i < 2000; i++){
            log.info("This is the " + i + " time I say 'Hello World'.");
            Thread.sleep(10);
        }
    }
}