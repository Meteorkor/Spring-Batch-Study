package com.meteor.batch.guava;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.google.common.base.Optional;

/**
 * Many of the cases where programmers use null is to indicate some sort of absence: perhaps where there might have been a value, there is none, or one could not be found.
 * For example, Map.get returns null when no value is found for a key.
 */
public class BasicUtilsNullOptionalTest {
    //java.util.Optional
    //com.google.common.base.Optional

    @Test
    void guavaOptional() {
        {
            final Optional<Object> absentOptional = Optional.absent();
            Assertions.assertFalse(absentOptional.isPresent());
            Assertions.assertEquals("DUMMY", absentOptional.or("DUMMY"));
            Assertions.assertEquals("DUMMY", absentOptional.transform(val -> "TRANS").or("DUMMY"));

            //java.lang.IllegalStateException: Optional.get() cannot be called on an absent value
            Assertions.assertThrows(IllegalStateException.class, () -> {
                absentOptional.get();
            });
            Assertions.assertNull(absentOptional.orNull());
        }
    }

}
