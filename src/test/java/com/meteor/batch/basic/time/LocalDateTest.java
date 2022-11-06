package com.meteor.batch.basic.time;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LocalDateTest {

    @Test
    void localDate_millSec() {
        final LocalDate now = LocalDate.now();

        final long i = now.atStartOfDay()
                          .toEpochSecond(ZoneOffset.ofHours(9)) * 1000L;

        final LocalDate fromNow = Instant.ofEpochMilli(i)
                                         .atZone(ZoneId.systemDefault())
                                         .toLocalDate();

        Assertions.assertEquals(now, fromNow);
    }

    @Test
    void localDate_epochSec() {
        final LocalDate now = LocalDate.now();

        final long l = now.toEpochDay();
        final LocalDate fromNow = LocalDate.ofEpochDay(l);

        Assertions.assertEquals(now, fromNow);
    }

    @Test
    void localDateTime_millSec() {
        final LocalDateTime now = LocalDateTime.now();

        // sec 하위시간은 사라짐
        final long i = now.toEpochSecond(ZoneOffset.ofHours(9)) * 1000L;

        final LocalDateTime fromNow = Instant.ofEpochMilli(i)
                                             .atZone(ZoneId.systemDefault())
                                             .toLocalDateTime();

        Assertions.assertNotEquals(now, fromNow);
        Assertions.assertEquals(now.getSecond(), fromNow.getSecond());
    }

    @Test
    void localDateTime_epochSec() {
        final LocalDateTime now = LocalDateTime.now();

        final long l = now.toEpochSecond(ZoneOffset.ofHours(9));
        final LocalDateTime fromNow = LocalDateTime.ofEpochSecond(l, 0, ZoneOffset.ofHours(9));

        Assertions.assertNotEquals(now, fromNow);
        Assertions.assertEquals(now.getSecond(), fromNow.getSecond());
    }

}
