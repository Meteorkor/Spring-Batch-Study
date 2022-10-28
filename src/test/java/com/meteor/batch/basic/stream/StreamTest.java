package com.meteor.batch.basic.stream;

import java.util.List;
import java.util.Map;
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
