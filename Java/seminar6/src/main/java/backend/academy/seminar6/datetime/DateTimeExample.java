package backend.academy.seminar6.datetime;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DateTimeExample {

    private static final Logger logger = LoggerFactory.getLogger(DateTimeExample.class);

    public static void main(String[] args) throws InterruptedException {
        dateExample();
        calendarExample();
        clockExample();
    }

    public static void dateExample() {
        Date currentDate = new Date(); // create Date from now
        logger.info("Current date: {}", currentDate); // 2024-10-01T13:47:58.000+0300
        logger.info("Millis since epoch: {}", currentDate.getTime()); // 1727779678000

        Date somewhereInTheFuture = new Date(1827883248123L); // create Date from epoch millis
        logger.info("Somewhere in the future: {}", somewhereInTheFuture); // 2027-12-04T04:20:48.123+0300
    }

    public static void calendarExample() {
        Calendar calendar = Calendar.getInstance();
        logger.info("Current date: {}", calendar); // too cumbersome...
        logger.info("Current day: {}", calendar.get(Calendar.DAY_OF_MONTH)); // 1
        logger.info("Current month: {}", calendar.get(Calendar.MONTH)); // October is 9, wtf?
        logger.info("Current year: {}", calendar.get(Calendar.YEAR)); // 2024

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.add(Calendar.DAY_OF_MONTH, 30);

        calendar.setTimeZone(TimeZone.getTimeZone(ZoneId.of("UTC")));

        var date = calendar.getTime();
        logger.info("Modified human-readable date: {}", date); // 2024-10-31T00:00:11.181+0300
    }

    public static void modernDateTimeClasses() {
        Instant instant = Instant.now(); // now, in UTC
        logger.info("Current date: {}", instant); // 2024-10-01T21:32:24.506747Z

        LocalDateTime localDateTime = LocalDateTime.now(); // now, TZ is not stored
        logger.info("Current dateTime: {}", localDateTime); // 2024-10-02T00:32:24.517454

        LocalDateTime anotherLocalDateTime = LocalDateTime.of(
            2049, 12, 13, 15, 30
        );
        logger.info("Another dateTime: {}", anotherLocalDateTime); // 2049-12-13T15:30

        OffsetDateTime offsetDateTime = OffsetDateTime.now(); // now, with TZ offset from UTC stored
        logger.info("Current offsetDateTime: {}", offsetDateTime); // 2024-10-02T00:32:24.517949+03:00

        OffsetDateTime anotherOffsetDateTime = OffsetDateTime.of(
            anotherLocalDateTime, ZoneOffset.of("+9")
        );
        logger.info("Another offsetDateTime: {}", anotherOffsetDateTime); // 2049-12-13T15:30+09:00

        ZonedDateTime zonedDateTime = ZonedDateTime.now(); // now, with TZ stored
        logger.info("Current zonedDateTime: {}", zonedDateTime); // 2024-10-02T00:32:24.518467+03:00[Europe/Moscow]

        ZonedDateTime anotherZonedDateTime = ZonedDateTime.now(ZoneId.of("America/Los_Angeles"));
        // 2024-10-01T14:32:24.519109-07:00[America/Los_Angeles]
        logger.info("Another zonedDateTime: {}", anotherZonedDateTime);
    }

    public static void modernDateTimeExample() {
        LocalDate localDate = LocalDate.parse("2023-07-23");
        LocalTime localTime = LocalTime.of(12, 24, 34);
        LocalDateTime localDateTime = LocalDateTime.of(localDate, localTime);

        LocalDateTime now = LocalDateTime.now();
        logger.info("Now: {}", now); // 2024-10-02T01:11:50.110143
        logger.info("Is now after localDateTime: {}", now.isAfter(localDateTime)); // true

        LocalDateTime alteredNow = now.plus(3, ChronoUnit.HOURS)
            .plusSeconds(153)
            .withMinute(0);
        logger.info("Altered now: {}", alteredNow); // 2024-10-02T04:00:23.110143
        logger.info("Is now after alteredNow: {}", now.isAfter(alteredNow)); // false

        LocalDateTime truncatedAlteredNow = alteredNow.truncatedTo(ChronoUnit.DAYS);
        logger.info("Truncated now: {}", truncatedAlteredNow); // 2024-10-02T00:00
    }

    public static void formattingExample() {
        OffsetDateTime now = OffsetDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        logger.info("Current date: {}", now); // 2024-10-02T01:19:14.047583+03:00
        logger.info("Formatted date: {}", formatter.format(now)); // 2024-10-02 01:19:14

        formatter = DateTimeFormatter.ISO_LOCAL_DATE;
        logger.info("Date-only format: {}", formatter.format(now)); // 2024-10-02

        formatter = DateTimeFormatter.RFC_1123_DATE_TIME;
        logger.info("RFC format: {}", formatter.format(now)); // Wed, 2 Oct 2024 01:19:14 +0300

        formatter = DateTimeFormatter.ISO_INSTANT;
        logger.info("Instant format: {}", formatter.format(now)); // 2024-10-01T22:19:14.047583Z
    }

    public static void clockExample() throws InterruptedException {
        Clock clock = Clock.systemDefaultZone();

        LocalDateTime now = LocalDateTime.now(clock);
        logger.info("Now: {}", now); // 2024-10-02T01:26:39.587350

        clock = Clock.systemUTC();
        now = LocalDateTime.now(clock);
        logger.info("Now: {}", now); // 2024-10-01T22:26:39.593166

        clock = Clock.fixed(Instant.parse("2007-12-03T10:15:30.00Z"), ZoneOffset.UTC);
        now = LocalDateTime.now(clock);
        logger.info("Now: {}", now); // 2007-12-03T10:15:30

        clock = Clock.tick(Clock.systemDefaultZone(), Duration.ofSeconds(1));
        now = LocalDateTime.now(clock);
        logger.info("Now: {}", now); // 2024-10-02T01:26:39
        Thread.sleep(250);
        logger.info("Now: {}", now); // 2024-10-02T01:26:39
    }

}
