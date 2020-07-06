package example.time;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * @author puppylpg on 2020/07/06
 */
public class ZonedDateTimeDemo {

    public static void main(String... args) {
        LocalDateTime localDateTime = LocalDateTime.now();
        ZoneId myZone = ZoneId.of("Asia/Shanghai");
        // to zoned date time
        ZonedDateTime myZonedDateTime = localDateTime.atZone(myZone);
        ZonedDateTime myZonedDateTime2 = ZonedDateTime.now();
        System.out.printf("My local date time: %s, My zoned date time: %s, %s%n", localDateTime, myZonedDateTime, myZonedDateTime2);

        Set<String> allZones = ZoneId.getAvailableZoneIds();
        List<String> zoneList = new ArrayList<>(allZones);
        Collections.sort(zoneList);

        for (String s : zoneList) {
            ZoneId anotherZone = ZoneId.of(s);
            // my zone to another zone
            ZonedDateTime zonedDateTime = myZonedDateTime.withZoneSameInstant(anotherZone);
            ZoneOffset offset = zonedDateTime.getOffset();
            System.out.printf("%s, %s, %s%n", anotherZone, offset, zonedDateTime);
        }
    }
}
