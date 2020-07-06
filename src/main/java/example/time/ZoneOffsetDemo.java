package example.time;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

/**
 * @author puppylpg on 2020/07/06
 */
public class ZoneOffsetDemo {

    public static void main(String... args) {
//        System.out.println(ZoneOffset.of("Asia/Shanghai"));
        ZoneId zoneId = ZoneId.of("Asia/Shanghai");
        ZoneOffset zoneOffset = ZoneOffset.ofHours(8);
        LocalDateTime localDateTime = LocalDateTime.now();

        System.out.printf("%s, %s%n", zoneId, zoneOffset);
        System.out.printf("%s, %s%n", localDateTime.atZone(zoneId), localDateTime.atOffset(zoneOffset));
    }
}
