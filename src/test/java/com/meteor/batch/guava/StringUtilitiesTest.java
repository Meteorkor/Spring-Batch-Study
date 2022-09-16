package com.meteor.batch.guava;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.google.common.base.Joiner;

public class StringUtilitiesTest {

    @Test
    void listNullTest() {
        List<String> list = null;

        Assertions.assertThrows(NullPointerException.class, () -> {
            for (String text : list) {
                Assertions.fail();
            }
        });
    }

    @Test
    void ifTest() {
        if (false && false || true) {

        } else {
            Assertions.fail();
        }
    }

    @Test
    void joinerTest() {
        Assertions.assertEquals("a,b,c", Joiner.on(",").join("a", "b", "c"));
    }

    @Test
    void joinerNullTest() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            Joiner.on(",").join("a", null, "c");
        });

        Assertions.assertEquals("a,c", Joiner.on(",").skipNulls()
                                             .join("a", null, "c"));

        Assertions.assertEquals("a,,c", Joiner.on(",").useForNull("")
                                              .join("a", null, "c"));

        {
            StringBuilder stb = new StringBuilder();
            stb.append("pre");
            Joiner.on(",").appendTo(stb, "a", "b", "c");
            stb.append("suf");
            Assertions.assertEquals("prea,b,csuf", stb.toString());
        }
    }

    @Test
    void mapJoiner() {
        Map<String, String> map = new HashMap<>();
        map.put("key1", "val1");
        map.put("key2", "val2");
        map.put("key3", "val3");

        final String keyValueSeparator = ":";
        final String separator = ",";
        final String joinStr = Joiner.on(separator).withKeyValueSeparator(keyValueSeparator).join(map);

        //"key1:val1,key2:val2,key3:val3"
        Assertions.assertEquals(
                map.entrySet().stream().map(entry -> entry.getKey() + keyValueSeparator + entry.getValue())
                   .collect(Collectors.joining(separator)), joinStr);
    }

}
