package example.time;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author liuhaibo on 2019/12/26
 */
public class TimeDemo {

    public static void main(String... args) {
        String concreteTime = new SimpleDateFormat("yyyyMMdd:HHmmss:SSS", Locale.CHINA).format(new Date());
        System.out.println(concreteTime);

        Date now = new Date();
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(now);
        System.out.println(date);
        String time = new SimpleDateFormat("HH:mm:ss.SSS", Locale.CHINA).format(now);
        System.out.println(time);
    }
}
