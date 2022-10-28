package com.meteor.batch.basic.stream;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.meteor.batch.reader.vo.Emp;

public class NoStreamTest {

    @Test
    void filteredList() {
        // %2==0

        List<Integer> evenList = new ArrayList<>();

        for (Integer number : getNumberList()) {
            if (number % 2 == 0) {
                evenList.add(number);
            }
        }

        assertionEven(evenList);
    }

    @Test
    void convertToMapTest() {
        Map<String, Emp> nameEmpMap = new HashMap<>();

        for (Emp emp : getEmpList()) {
            nameEmpMap.put(emp.getEname(), emp);
        }
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
