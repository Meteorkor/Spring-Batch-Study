package com.meteor.batch.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.ValueRange;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class LocalDateTimeProcessTest {

    @Test
    void getTest() {
        final int year = 2022;
        final int month = 1;
        final int dayOfMonth = 1;
        final int hour = 11;
        final int minute = 12;
        final int second = 13;
        final LocalDateTime localDateTime = LocalDateTime.of(year, month, dayOfMonth, hour, minute, second);

        Assertions.assertEquals(year, localDateTime.get(ChronoField.YEAR));
        Assertions.assertEquals(month, localDateTime.get(ChronoField.MONTH_OF_YEAR));
        Assertions.assertEquals(dayOfMonth, localDateTime.get(ChronoField.DAY_OF_MONTH));
        Assertions.assertEquals(hour, localDateTime.get(ChronoField.HOUR_OF_DAY));
        Assertions.assertEquals(minute, localDateTime.get(ChronoField.MINUTE_OF_HOUR));
        Assertions.assertEquals(second, localDateTime.get(ChronoField.SECOND_OF_MINUTE));

    }

    @Test
    void isCompareTest() {
        final int year = 2022;
        final int month = 1;
        final int dayOfMonth = 1;
        final int hour = 11;
        final int minute = 12;
        final int second = 13;
        final LocalDateTime localDateTime = LocalDateTime.of(year, month, dayOfMonth, hour, minute, second);

        final LocalDateTime nextDateTime = localDateTime.plusDays(1);

        Assertions.assertTrue(localDateTime.isBefore(nextDateTime));
        Assertions.assertFalse(localDateTime.isAfter(nextDateTime));
        Assertions.assertTrue(
                localDateTime.isEqual(LocalDateTime.of(year, month, dayOfMonth, hour, minute, second)));
    }

    @Test
    void formatTest() {
        final int year = 2022;
        final int month = 1;
        final int dayOfMonth = 1;
        final int hour = 14;
        final int minute = 12;
        final int second = 13;
        final LocalDateTime localDateTime = LocalDateTime.of(year, month, dayOfMonth, hour, minute, second);

        Assertions.assertEquals("2022-01-01T14:12:13",
                                localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        Assertions.assertEquals("20220101 02:12:13",
                                localDateTime.format(DateTimeFormatter.ofPattern("yyyyMMdd hh:mm:ss")));
        Assertions.assertEquals("20220101 14:12:13",
                                localDateTime.format(DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss")));
    }

    @Test
    void rangeTest() {
        final int year = 2022;
        final int month = 1;
        final int dayOfMonth = 1;
        final int hour = 14;
        final int minute = 12;
        final int second = 13;
        final LocalDateTime localDateTime = LocalDateTime.of(year, month, dayOfMonth, hour, minute, second);

        {
            final ValueRange range = localDateTime.range(ChronoField.YEAR);
            Assertions.assertEquals(999999999, range.getMaximum());
            Assertions.assertEquals(-999999999, range.getMinimum());
        }
        {
            final ValueRange range = localDateTime.range(ChronoField.MONTH_OF_YEAR);
            Assertions.assertEquals(12, range.getMaximum());
            Assertions.assertEquals(1, range.getMinimum());
        }
        {
            final ValueRange range = localDateTime.range(ChronoField.DAY_OF_MONTH);
            Assertions.assertEquals(31, range.getMaximum());
            Assertions.assertEquals(1, range.getMinimum());
        }
        {
            final ValueRange range = localDateTime.plusMonths(1).range(ChronoField.DAY_OF_MONTH);
            Assertions.assertEquals(28, range.getMaximum());
            Assertions.assertEquals(1, range.getMinimum());
        }
    }

}
