package com.meteor.batch.util;

import java.time.LocalDateTime;
import java.time.temporal.ChronoField;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LocalDateTimeProcessTest {

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

}
