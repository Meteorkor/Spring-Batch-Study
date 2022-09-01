package com.meteor.batch.basic;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ListTest {

    @Test
    void listNullForTest() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            List<String> nullList = null;
            for (int i = 0; i < nullList.size(); i++) {
                System.out.println(nullList.get(i));
            }
            Assertions.fail();
        });
    }

    @Test
    void listNullForEachTest() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            List<String> nullList = null;
            for (String text : nullList) {
                System.out.println(text);
            }
            Assertions.fail();
        });
    }

    @Test
    void emptyListForTest() {
        List<String> emptyList = Collections.emptyList();
        for (int i = 0; i < emptyList.size(); i++) {
            System.out.println(emptyList.get(i));
        }
    }

    @Test
    void emptyListForEachTest() {
        List<String> emptyList = Collections.emptyList();
        for (String text : emptyList) {
            System.out.println(text);
        }
    }
    
}
