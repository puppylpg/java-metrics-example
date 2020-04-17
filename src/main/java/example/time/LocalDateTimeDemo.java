package example.time;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

/**
 * @author liuhaibo on 2020/04/13
 */
public class LocalDateTimeDemo {

    private static DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy/MM/dd/HH");

    public static void main(String... args) {
        System.out.println(getProcessHours("2020/04/11/00", "2020/04/11/05"));
        System.out.println(getProcessHours("2019/04/11/00", "2020/04/11/15"));
    }

    private static List<String> getProcessHours(String startTime, String endTime) {
        LocalDateTime startDateTime = LocalDateTime.parse(startTime, DATE_TIME_FORMAT);
        LocalDateTime endDateTime = LocalDateTime.parse(endTime, DATE_TIME_FORMAT);
        long hours = ChronoUnit.HOURS.between(startDateTime, endDateTime) + 1;

        return LongStream.range(0, hours).mapToObj(hour -> startDateTime.plus(hour, ChronoUnit.HOURS).format(DATE_TIME_FORMAT)).collect(Collectors.toList());
    }
}
