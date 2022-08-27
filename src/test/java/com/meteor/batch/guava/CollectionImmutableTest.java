package com.meteor.batch.guava;

import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.ImmutableSortedSet;

/**
 * Immutable objects have many advantages,
 * including:
 * Safe for use by untrusted libraries.
 * Thread-safe: can be used by many threads with no risk of race conditions.
 * Doesn't need to support mutation, and can make time and space savings with that assumption. All immutable collection implementations are more memory-efficient than their mutable siblings. (analysis)
 * Can be used as a constant, with the expectation that it will remain fixed.
 */
public class CollectionImmutableTest {

    @Test
    void immutableTest() {
        {
            final Set<String> guavaImmutableSet = ImmutableSet.of(
                    "red",
                    "orange",
                    "yellow",
                    "green",
                    "blue",
                    "purple");

            Assertions.assertThrows(UnsupportedOperationException.class, () -> {
                guavaImmutableSet.add("A");
            });
        }
        {
            final ImmutableList<String> of = ImmutableList.of("red",
                                                              "orange",
                                                              "yellow",
                                                              "green",
                                                              "blue",
                                                              "purple");
            Assertions.assertThrows(UnsupportedOperationException.class, () -> {
                of.add("A");
            });

            Assertions.assertEquals("red", of.get(0));
        }
        {
            final ImmutableSortedSet<Integer> of1 = ImmutableSortedSet.of(3, 1, 2);
            Assertions.assertThrows(UnsupportedOperationException.class, () -> {
                of1.add(4);
            });

            Assertions.assertEquals(1, of1.first());
            Assertions.assertEquals(3, of1.last());
        }
        {
            final ImmutableMap<String, String> of2 = ImmutableMap.of("key", "value");
            Assertions.assertThrows(UnsupportedOperationException.class, () -> {
                of2.put("key2", "value2");
            });

            Assertions.assertEquals("value", of2.get("key"));
        }
        {

            final ImmutableSortedMap<String, String> of3 = ImmutableSortedMap.of("a", "aValuie", "c", "cValue",
                                                                                 "b",
                                                                                 "bValue");
            Assertions.assertThrows(UnsupportedOperationException.class, () -> {
                of3.put("d", "dValue");
            });

            Assertions.assertEquals("a", of3.firstEntry().getKey());
            Assertions.assertEquals("c", of3.lastEntry().getKey());
        }
    }

}
