package com.meteor.batch.basic;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.meteor.batch.TestTimeUtils;

public class LambdaTest {

    final int END_EXCLUSIVE_NUM = 10_000_000;

    @Test
    void anonymousClassTest() {
//        int number = 100;
        new Runnable() {
            @Override
            public void run() {
//                number++;
                System.out.println("process!!");
            }
        }.run();
    }

    @Test
    void anonymousFunctionTest() {
//        int number = 100;
        final Runnable runnable = () -> {
//            number++;
            System.out.println("process!!");
        };
        runnable.run();
    }
    //같은 동작

    @Test
    void anonymousClassTestMemberVairable() {
//        int number = 100;
        new Runnable() {
            private int variable;

            @Override
            public void run() {
                variable++;
//                number++;
                System.out.println("process!!");
            }
        }.run();
    }

    @Test
    void lambdaLocalVariableRef() {
        AtomicInteger atomicInteger = new AtomicInteger();
        final Runnable runnable = () -> {
            atomicInteger.incrementAndGet();
        };

        runnable.run();
        runnable.run();
        Assertions.assertEquals(2, atomicInteger.get());
    }

    @Test
    void performanceForEach() {

        final List<Integer> sampleList = IntStream.range(0, END_EXCLUSIVE_NUM).boxed().collect(
                Collectors.toList());
        final int EXPECT_SUM = sampleList.stream().reduce(Integer::sum).get().intValue();

        final long forITime = TestTimeUtils.processTime(() -> {
            int sum = 0;
            for (int i = 0; i < sampleList.size(); i++) {
                sum += sampleList.get(i);
            }
            Assertions.assertEquals(EXPECT_SUM, sum);
        });

        final long forEachTime = TestTimeUtils.processTime(() -> {
            int sum = 0;
            for (Integer num : sampleList) {
                sum += num;
            }
            Assertions.assertEquals(EXPECT_SUM, sum);
        });

        final long forITimeBoxed = TestTimeUtils.processTime(() -> {
            Integer sum = 0;
            for (int i = 0; i < sampleList.size(); i++) {
                sum += sampleList.get(i);
            }
            Assertions.assertEquals(EXPECT_SUM, sum);
        });

        final long forEachTimeBoxed = TestTimeUtils.processTime(() -> {
            Integer sum = 0;
            for (Integer num : sampleList) {
                sum += num;
            }
            Assertions.assertEquals(EXPECT_SUM, sum);
        });

        final long streamSumTime = TestTimeUtils.processTime(() -> {
            Assertions.assertEquals(EXPECT_SUM, sampleList.stream().reduce(Integer::sum).get());
        });

        System.out.println("forITime: " + forITime);
        System.out.println("forEachTime: " + forEachTime);
        System.out.println("forITimeBoxed: " + forITimeBoxed);
        System.out.println("forEachTimeBoxed: " + forEachTimeBoxed);
        System.out.println("streamSumTime: " + streamSumTime);

//        forITime: 32
//        forEachTime: 26
//        forITimeBoxed: 128
//        forEachTimeBoxed: 93
//        streamSumTime: 239

//        forITime: 39
//        forEachTime: 36
//        forITimeBoxed: 68
//        forEachTimeBoxed: 226
//        streamSumTime: 112

//        forITime: 27
//        forEachTime: 27
//        forITimeBoxed: 79
//        forEachTimeBoxed: 117
//        streamSumTime: 88


    }

    //TODO 각 유형별로 TC 작성
    @Test
    void functionalInterface() {
        //Consumer
        //Function
        //Predicate
        //Operator
        //Supplier
        //BiFunction
    }

}
