package com.meteor.batch.basic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.util.CollectionUtils;

import com.google.common.collect.Lists;

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

    @Test
    void constructTest() {
        int SIZE = 10;
        List<String> list = new ArrayList<>(SIZE);
        Assertions.assertEquals(0, list.size());

        IntStream.range(0, SIZE * 2).forEach(n -> {
            list.add(String.valueOf(n));
        });
        Assertions.assertEquals(SIZE * 2, list.size());
    }

    @Test
    void compareTest() {
        List<String> list_1 = Lists.newArrayList("a", "b", "c", "d");
        List<String> list_2 = Lists.newArrayList("a", "b", "c", "d");

        Assertions.assertNotEquals(System.identityHashCode(list_1), System.identityHashCode(list_2));
        Assertions.assertEquals(list_1, list_2);
        Assertions.assertTrue(list_1.equals(list_2));
    }

}
