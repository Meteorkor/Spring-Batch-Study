package com.meteor.batch;

import java.util.Objects;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ObjectsTest {

    @Test
    void strEqualsTest() {
        String val1 = "";
        String val2 = "";

        Assertions.assertTrue(Objects.equals(val1, val2));
    }

    @Test
    void requireNonNullTest() {
        String val1 = null;
        Assertions.assertThrows(NullPointerException.class, () -> {
            Objects.requireNonNull(val1);
        });
    }

}
