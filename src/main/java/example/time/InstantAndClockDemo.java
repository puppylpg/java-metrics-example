package example.time;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * @author puppylpg on 2020/07/06
 */
public class InstantAndClockDemo {

    public static void main(String... args) {
        Instant instant = Instant.now();
        System.out.println("UTC time: " + Instant.now());
        System.out.println(Instant.now().toEpochMilli());
        System.out.println(System.currentTimeMillis());
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(instant, ZoneId.systemDefault());
        System.out.println("+08:00 time: " + zonedDateTime);
    }
}
