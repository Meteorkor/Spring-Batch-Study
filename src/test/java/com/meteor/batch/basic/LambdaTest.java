package com.meteor.batch.basic;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Function;
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

        final long intStreamSumTime = TestTimeUtils.processTime(() -> {
            Assertions.assertEquals(EXPECT_SUM, sampleList.stream().mapToInt(Integer::intValue).sum());
        });

        System.out.println("forITime: " + forITime);
        System.out.println("forEachTime: " + forEachTime);
        System.out.println("forITimeBoxed: " + forITimeBoxed);
        System.out.println("forEachTimeBoxed: " + forEachTimeBoxed);
        System.out.println("streamSumTime: " + streamSumTime);
        System.out.println("intStreamSumTime: " + intStreamSumTime);

//        forITime: 29
//        forEachTime: 26
//        forITimeBoxed: 79
//        forEachTimeBoxed: 58
//        streamSumTime: 107
//        intStreamSumTime: 64

//        forITime: 35
//        forEachTime: 28
//        forITimeBoxed: 64
//        forEachTimeBoxed: 108
//        streamSumTime: 79
//        intStreamSumTime: 57

    }

    //TODO 각 유형별로 TC 작성
    @Test
    void functionalInterface() {
        //Function
        //Predicate
        //Operator
        //Supplier
        //BiFunction
    }

    @Test
    void consumerTest() {

        Consumer<String> consumer = str -> {
            System.out.println("[Lambda]" + str);
        };

        consumer.accept("str");

        Consumer<String> consumer2 = new Consumer<String>() {
            private int count = 0;

            @Override
            public void accept(String s) {
                System.out.println((count++) + "[anonymousClass]" + s);
            }
        };
        consumer2.accept("str");
        consumer2.accept("str");
        consumer2.accept("str");

//        [Lambda]str
//        0[anonymousClass]str
//        1[anonymousClass]str
//        2[anonymousClass]str
    }

    @Test
    void functionTest() {

        Function<String, String> function1 = str -> "[Lambda]" + str;

        Assertions.assertEquals("[Lambda]str", function1.apply("str"));

        Function<String, String> function2 = new Function<String, String>() {
            private int count = 0;

            @Override
            public String apply(String str) {
                return (count++) + "[anonymousClass]" + str;
            }
        };

        Assertions.assertEquals("0[anonymousClass]str", function2.apply("str"));
        Assertions.assertEquals("1[anonymousClass]str", function2.apply("str"));
        Assertions.assertEquals("2[anonymousClass]str", function2.apply("str"));
        
    }

}
