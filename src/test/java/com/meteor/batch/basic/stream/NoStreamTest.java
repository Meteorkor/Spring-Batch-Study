package com.meteor.batch.basic.stream;

import java.util.ArrayList;
import java.util.Collections;
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

    @Test
    void firstTest() {
        //empty list
        final List<Object> objects = Collections.emptyList();
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> {
            final Object o = objects.get(0);
        });

        final List<Emp> empList = getEmpList();//db 조회라는 가정으로, 변수 선언 필요

        if (empList.isEmpty()) {
            final Emp emp = empList.get(0);
            Assertions.assertNotNull(emp);
        }
    }

    @Test
    void filterAndFirstTest() {
        // %2==0
        List<Integer> evenList = new ArrayList<>();

        for (Integer number : getNumberList()) {//filter, all List
            if (number % 2 == 0) {
                evenList.add(number);
            }
        }

//        for (Integer number : getNumberList()) {//filter, one
//            if (number % 2 == 0) {
//                evenList.add(number);
//                break;
//            }
//        }

        if (!evenList.isEmpty()) {
            final Integer integer = evenList.get(0);//doing
        } else {
            //throw Exception
            throw new RuntimeException();
        }
    }

    @Test
    void filterAndFirstAndConvert() {
        // %2==0
        List<Integer> evenList = new ArrayList<>();

        for (Integer number : getNumberList()) {
            if (number % 2 == 0) {
                evenList.add(number);
            }
        }

        assertionEven(evenList);

        Emp emp = null;

        if (!evenList.isEmpty()) {
            final Integer empno = evenList.get(0);
            emp = Emp.builder()
                     .empno(empno)
                     .ename("name:" + empno)
                     .build();

            //doing
        } else {
            //Not working throw Exception
        }

        //emp; //doing
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
