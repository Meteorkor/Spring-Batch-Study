package com.meteor.batch.guava;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.google.common.base.Joiner;

public class StringUtilitiesTest {

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

    //TODO
//    @Test
//    void mapJoiner(){
//
//    }

}
