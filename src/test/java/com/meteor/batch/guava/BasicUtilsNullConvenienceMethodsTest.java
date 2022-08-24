package com.meteor.batch.guava;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.google.common.base.MoreObjects;
import com.google.common.base.Strings;
import com.meteor.batch.reader.vo.Emp;

public class BasicUtilsNullConvenienceMethodsTest {

    @Test
    void stringsNull() {
        {//nullStr
            String nullStr = null;
            Assertions.assertTrue(Strings.isNullOrEmpty(nullStr));
            Assertions.assertNull(Strings.emptyToNull(nullStr));
            Assertions.assertEquals("", Strings.nullToEmpty(nullStr));
        }
        {//emptyStr
            String emptyStr = "";
            Assertions.assertTrue(Strings.isNullOrEmpty(emptyStr));
            Assertions.assertNull(Strings.emptyToNull(emptyStr));
            Assertions.assertEquals("", Strings.nullToEmpty(emptyStr));
        }
        {//notNullStr
            String notNullStr = "DATA";
            Assertions.assertFalse(Strings.isNullOrEmpty(notNullStr));
            Assertions.assertEquals(notNullStr, Strings.emptyToNull(notNullStr));
            Assertions.assertEquals(notNullStr, Strings.nullToEmpty(notNullStr));
        }
    }

    @Test
    void stringsEtc() {
        {
            String textA = "B1003DD";
            String textB = "B1004DD";
            Assertions.assertTrue(Strings.commonPrefix(textA, textB).startsWith("B"));
            Assertions.assertEquals("B100", Strings.commonPrefix(textA, textB));
            Assertions.assertEquals("DD", Strings.commonSuffix(textA, textB));
        }
        {
            String textA = "B1003DD";
            String textB = "1004DD";
            Assertions.assertFalse(Strings.commonPrefix(textA, textB).startsWith("B"));
            Assertions.assertEquals("", Strings.commonPrefix(textA, textB));
            Assertions.assertEquals("DD", Strings.commonSuffix(textA, textB));
        }
        {
            String textA = "B1003DD";
            String textB = "B1004DD";
            Assertions.assertEquals(textA + "-" + textB, Strings.lenientFormat("%s-%s", textA, textB));
        }
    }

}
