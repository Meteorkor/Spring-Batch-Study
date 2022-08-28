package com.meteor.batch.guava;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Streams;

public class CollectionUtilitiesTest {

    @Test
    void createTest() {
        final List<String> newListFromGuava = Lists.newArrayList();
        final List<String> newListFromJdk = new ArrayList<>();

        final List<String> newFixedList = Lists.newArrayList("a", "b", "c");
    }

    @Test
    void iterables() {
        {
            final Iterable<String> concat = Iterables.concat(Lists.newArrayList("a", "b", "c"),
                                                             Lists.newArrayList("1", "2", "3"));
            final List<String> collect = Streams.stream(concat).collect(Collectors.toList());
            Assertions.assertEquals("a", collect.get(0));
            Assertions.assertEquals("b", collect.get(1));
            Assertions.assertEquals("c", collect.get(2));
            Assertions.assertEquals("1", collect.get(3));
            Assertions.assertEquals("2", collect.get(4));
            Assertions.assertEquals("3", collect.get(5));
        }
        {
            final ArrayList<String> iterable = Lists.newArrayList("a", "b", "c", "1", "a");
            Assertions.assertEquals(2, Iterables.frequency(iterable, "a"));
            Assertions.assertEquals(1, Iterables.frequency(iterable, "1"));
            Assertions.assertEquals(0, Iterables.frequency(iterable, "2"));
        }
        {
            final Iterable<String> concat1 = Iterables.concat(Lists.newArrayList("a", "b", "c"),
                                                              Lists.newArrayList("1", "2", "3"));
            final Iterable<String> concat2 = Iterables.concat(Lists.newArrayList("a", "b", "c"),
                                                              Lists.newArrayList("1", "2", "3"));

            Assertions.assertNotEquals(concat1, concat2);
            //****
            Assertions.assertTrue(Iterables.elementsEqual(concat1, concat2));
        }
    }

    @Test
    void collectionLike() {
        {
            final ArrayList<String> list = Lists.newArrayList();
            Collections.addAll(list, "a", "b", "c");
            Assertions.assertEquals("a", list.get(0));
            Assertions.assertEquals("b", list.get(1));
            Assertions.assertEquals("c", list.get(2));

            Assertions.assertEquals("c", list.get(2));
        }
        {
            final ArrayList<String> list = Lists.newArrayList("a", "b", "c", "d", "e");
            final List<List<String>> partition = Lists.partition(list, 2);

            Assertions.assertTrue(Iterables.elementsEqual(Lists.newArrayList("a", "b"), partition.get(0)));
            Assertions.assertTrue(Iterables.elementsEqual(Lists.newArrayList("c", "d"), partition.get(1)));
            Assertions.assertTrue(Iterables.elementsEqual(Lists.newArrayList("e"), partition.get(2)));

            Assertions.assertTrue(
                    Iterables.elementsEqual(Lists.newArrayList("e", "d", "c", "b", "a"), Lists.reverse(list)));
        }
    }

}
