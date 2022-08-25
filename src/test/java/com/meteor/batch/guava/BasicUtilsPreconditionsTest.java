package com.meteor.batch.guava;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.google.common.base.Preconditions;

public class BasicUtilsPreconditionsTest {

    @Test
    void checkArgumentTest() {
        int value = 10;
        Preconditions.checkArgument(value > 1);//true
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Preconditions.checkArgument(value < 1);//false
        });

        Preconditions.checkArgument(value > 1, "value > 1");//true

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Preconditions.checkArgument(value < 1, "value < 1");//false
        }, "value < 1");

        Preconditions.checkArgument(value > 1, "%s > 1", value);

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Preconditions.checkArgument(value < 1, "%s < 1", value);
        }, "10 < 1");
    }

    @Test
    void checkNotNullTest() {
        String strValue = "str";
        String nullValue = null;
        Assertions.assertEquals(strValue, Preconditions.checkNotNull(strValue));
        Assertions.assertThrows(NullPointerException.class, () -> {
            Preconditions.checkNotNull(nullValue);
        });

        Assertions.assertEquals(strValue, Preconditions.checkNotNull(strValue, "value is null"));
        Assertions.assertThrows(NullPointerException.class, () -> {
            Preconditions.checkNotNull(nullValue, "value is null");
        }, "value is null");

        Assertions.assertEquals(strValue, Preconditions.checkNotNull(strValue, "%s is null", strValue));
        Assertions.assertThrows(NullPointerException.class, () -> {
            Preconditions.checkNotNull(nullValue, "%s is null", nullValue);
        }, "null is null");
    }

}
