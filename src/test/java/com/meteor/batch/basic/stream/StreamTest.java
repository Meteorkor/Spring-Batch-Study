package com.meteor.batch.basic.stream;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.meteor.batch.reader.vo.Emp;

public class StreamTest {

    @Test
    void filteredList() {
        // %2==0
        assertionEven(
                getNumberList().stream()
                               .filter(n -> n % 2 == 0)
                               .collect(Collectors.toList())
        );
    }

    @Test
    void convertToMapTest() {
        Map<String, Emp> nameEmpMap = getEmpList()
                .stream()
                .collect(Collectors.toMap(Emp::getEname, Function.identity()));
        //nameEmpMap
    }

    @Test
    void firstTest() {
        //empty list
        final List<Object> objects = Collections.emptyList();
        final Optional<Object> first = objects.stream().findFirst();
        Assertions.assertFalse(first.isPresent());

        final Optional<Emp> first1 = getEmpList().stream().findFirst();
        Assertions.assertNotNull(first1.get());
    }

    @Test
    void filterAndFirstTest() {
        final Integer num = getNumberList().stream()
                                       .filter(n -> n % 2 == 0)//filter
                                       .findFirst()
                                       .orElseThrow(RuntimeException::new);
    }

    @Test
    void filterAndFirstAndConvert() {
        final Emp emp = getNumberList().stream()
                                       .filter(n -> n % 2 == 0)//filter
                                       .findFirst()
                                       .map(n -> Emp.builder()//First
                                                    .empno(n)
                                                    .ename("name:" + n)
                                                    .build())
                                       .orElseThrow(RuntimeException::new);

    }

    //mock
    private void assertionEven(List<Integer> evenList) {
        evenList.forEach(n -> Assertions.assertTrue(n % 2 == 0));
    }

    private List<Integer> getNumberList() {
        return IntStream.range(0, 1000)
                        .boxed()
                        .collect(Collectors.toList());
    }

    private List<Emp> getEmpList() {
        return IntStream.range(0, 1000)
                        .mapToObj(n -> Emp.builder()
                                          .empno(n)
                                          .ename("cus_" + n)
                                          .build())
                        .collect(Collectors.toList());
    }

}
