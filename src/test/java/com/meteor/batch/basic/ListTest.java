package com.meteor.batch.basic;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.util.CollectionUtils;

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

    @Test
    void collectionUtilsTest() {
        {
            List<String> nullList = null;
            Assertions.assertTrue(CollectionUtils.isEmpty(nullList));
        }
        {
            List<String> emptyList = Collections.emptyList();
            Assertions.assertTrue(CollectionUtils.isEmpty(emptyList));
        }
    }

    @Test
    void iteratorTest() {
        List<String> emptyList = Collections.emptyList();
        final Iterator<String> emptyIter = emptyList.iterator();

        Assertions.assertFalse(emptyIter.hasNext());
        Assertions.assertThrows(NoSuchElementException.class, () -> {
            emptyIter.next();
        });
    }

}
