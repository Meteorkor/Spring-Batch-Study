package com.meteor.batch.guava;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

/**
 * Basic utilities: Make using the Java language more pleasant.
 * Using and avoiding null: null can be ambiguous, can cause confusing errors, and is sometimes just plain unpleasant. Many Guava utilities reject and fail fast on nulls, rather than accepting them blindly.
 * https://github.com/google/guava/wiki/UsingAndAvoidingNullExplained
 */
public class BasicUtilsNullSpecificCaseTest {

    /**
     * If you're trying to use null values in a Set or as a key in a Map -- don't;
     * it's clearer (less surprising) if you explicitly special-case null during lookup operations.
     */
    @Test
    void dontSetValueMapKeyNull() {
        {
            Set<String> set = new HashSet<>();
            set.add(null);
            Assertions.assertEquals(1, set.size());
        }
        {
            Map<String, String> map = new HashMap<>();
            final String value = "str";
            map.put(null, value);
            Assertions.assertEquals(1, map.size());
            Assertions.assertEquals(value, map.get(null));
        }
        //it's clearer (less surprising) if you explicitly special-case null during lookup operations.
    }

    /**
     * If you want to use null as a value in a Map -- leave out that entry; keep a separate Set of non-null keys (or null keys).
     * It's very easy to mix up the cases where a Map contains an entry for a key, with value null, and the case where the Map has no entry for a key.
     * It's much better just to keep such keys separate, and to think about what it means to your application when the value associated with a key is null.
     */
    @Test
    void notMapValueNull() {
        //value에 null을 사용하면, 값이 없는것인지, 있는데 null인것인지 구분하기 어렵다.
        //value가 null인것에 대한 관리가 필요하면, 별도의 keySet으로 관리하여 사용하라
        final String KEY = "key";
        final String NO_VALUE_KEY = "noValueKey";

        Map<String, String> map = new HashMap<>();
        map.put(KEY, null);
        Assertions.assertEquals(1, map.size());

        Assertions.assertEquals(null, map.get(KEY));
        Assertions.assertEquals(null, map.get(NO_VALUE_KEY));
    }

    /**
     * If you're using nulls in a List -- if the list is sparse, might you rather use a Map<Integer, E>?
     * This might actually be more efficient, and could potentially actually match your application's needs more accurately.
     */
    @Test
    void userMapIntKeyNullValueThanList() {
        final String value = "value";

        {
            List<String> list = new ArrayList<>();
            list.add(value);
            list.add(null);

            Assertions.assertEquals(2, list.size());
            Assertions.assertEquals(value, list.get(0));
            Assertions.assertEquals(null, list.get(1));
        }
        {
            Map<Integer, String> map = new HashMap<>();
            map.put(0, value);
            map.put(1, null);
            Assertions.assertEquals(2, map.size());
            Assertions.assertEquals(value, map.get(0));
            Assertions.assertEquals(null, map.get(1));
        }
    }

    /**
     * Consider if there is a natural "null object" that can be used.
     * There isn't always. But sometimes. For example, if it's an enum, add a constant to mean whatever you're expecting null to mean here.
     * For example, java.math.RoundingMode has an UNNECESSARY value to indicate "do no rounding, and throw an exception if rounding would be necessary."
     */
    @Test
    void nullReplaceToNullObject() {
        final List<AddRemoveWorkingEnum> userActionHistory = new ArrayList<>();

        //put
        userActionHistory.add(AddRemoveWorkingEnum.ADD);

        //remove
        userActionHistory.add(AddRemoveWorkingEnum.REMOVE);

        //cancel or Null Case
        userActionHistory.add(AddRemoveWorkingEnum.NONE);
    }

    /**
     * For NullObject
     */
    enum AddRemoveWorkingEnum {
        ADD, REMOVE,
        //null mean, default value
        NONE
    }

    /**
     * If you really need null values, and you're having problems with a null-hostile collection implementations, use a different implementation.
     * For example, use Collections.unmodifiableList(Lists.newArrayList()) instead of ImmutableList.
     */
    @Test
    void nullValueImmutableListToUnmodifiableListArrayList() {
        //null이 반드시 필요한데, 클래스에서 null 지원하지 않는경우(list)
        final String value = "value";

        //java.lang.NullPointerException: at index 1
        Assertions.assertThrows(NullPointerException.class, () -> {
            List<String> list = ImmutableList.of(value, null);
        });

        {
            final List<String> list = Collections.unmodifiableList(Lists.newArrayList(value, null));

            Assertions.assertEquals(2, list.size());
            Assertions.assertEquals(value, list.get(0));
            Assertions.assertEquals(null, list.get(1));
        }
    }
}
