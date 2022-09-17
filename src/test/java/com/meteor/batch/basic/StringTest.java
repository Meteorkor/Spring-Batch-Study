package com.meteor.batch.basic;

import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.springframework.util.StopWatch;

public class StringTest {

    private static final String PREFIX = "prefix::";
    private static final String SUFFIX = "::SUFFIX";
    private static final String FULL_TEXT = PREFIX + "%s" + SUFFIX;

    private static final int REPEAT_TEST_CNT = 1_000_000;

    /**
     * 문자열 중간에 값을 교체해서 처리시 어떤 방법이 빠른지 파악을 위한 TC
     */
    @Test
    void performanceTest() {

        AtomicReference<String> atomicReference = new AtomicReference<>();

        final long splitDataBuilderTime = workTime(() -> {
            IntStream.range(0, REPEAT_TEST_CNT)
                     .forEach(n -> {
                         atomicReference.set(splitDataBuilder(n));
//                         splitDataBuilder(n);
//                         System.out.println(splitDataBuilder(n));
                     });
        });

        final long splitDataStringTime = workTime(() -> {
            IntStream.range(0, REPEAT_TEST_CNT)
                     .forEach(n -> {
                         atomicReference.set(splitDataString(n));
//                         splitDataString(n);
//                         System.out.println(splitDataString(n));
                     });
        });

        final long splitDataReplaceTime = workTime(() -> {
            IntStream.range(0, REPEAT_TEST_CNT)
                     .forEach(n -> {
                         atomicReference.set(splitDataReplace(n));
//                         splitDataReplace(n);
//                         System.out.println(splitDataReplace(n));
                     });
        });

        System.out.println("splitDataBuilderTime : " + splitDataBuilderTime);
        System.out.println("splitDataStringTime : " + splitDataStringTime);
        System.out.println("splitDataReplaceTime : " + splitDataReplaceTime);

        //한가지 args시 StringBuilder, String plus가 String.format 보다 더 빠름
        //String에 대해서는 실제 클래스 파일에는 Builder로 변경되어 성능이 개선되었을듯함
        //TODO args 3개시 비교예정
//        splitDataBuilderTime : 69
//        splitDataStringTime : 79
//        splitDataReplaceTime : 220

//        splitDataBuilderTime : 91
//        splitDataStringTime : 82
//        splitDataReplaceTime : 356

//        splitDataBuilderTime : 78
//        splitDataStringTime : 77
//        splitDataReplaceTime : 292

    }

    long workTime(Runnable runnable) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        runnable.run();
        stopWatch.stop();
        return stopWatch.getTotalTimeMillis();
    }

    String splitDataBuilder(int n) {
        return new StringBuilder().append(PREFIX).append(n).append(SUFFIX).toString();

    }

    String splitDataString(int n) {
        return PREFIX + n + SUFFIX;
    }

    String splitDataReplace(int n) {
        return String.format(FULL_TEXT, n);
    }

}
