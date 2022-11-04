package com.meteor.batch.basic;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.util.StopWatch;

import com.google.common.base.Strings;
import com.meteor.batch.TestTimeUtils;

public class StringTest {

    private static final String PREFIX = "prefix::";
    private static final String SUFFIX = "::SUFFIX";
    private static final String FULL_TEXT = PREFIX + "%s" + SUFFIX;

    private static final int REPEAT_TEST_CNT = 1_000_000;

    @Test
    void splitPerfTest() {
        final String text = IntStream.range(0, 100).mapToObj(n -> "A" + n).collect(
                Collectors.joining(","));
        final String EXPECT_VAL = "A0";
        final String LAST_VAL = "A99";

        final long split2 = TestTimeUtils.processTime(() -> {
            IntStream.range(0, REPEAT_TEST_CNT).forEach(n -> {
                final String[] split = text.split(",", 2);
                Assertions.assertEquals(EXPECT_VAL, split[0]);
            });
        });

        final long splitN = TestTimeUtils.processTime(() -> {
            IntStream.range(0, REPEAT_TEST_CNT).forEach(n -> {
                final String[] split = text.split(",");
                Assertions.assertEquals(EXPECT_VAL, split[0]);
            });
        });

        final long indexOf = TestTimeUtils.processTime(() -> {
            IntStream.range(0, REPEAT_TEST_CNT).forEach(n -> {
                final String substring = text.substring(0, text.indexOf(","));
                Assertions.assertEquals(EXPECT_VAL, substring);
            });
        });

        final long lastIndexOf = TestTimeUtils.processTime(() -> {
            IntStream.range(0, REPEAT_TEST_CNT).forEach(n -> {
                final String substring = text.substring(text.lastIndexOf(",") + 1, text.length());
                Assertions.assertEquals(LAST_VAL, substring);
            });
        });

        System.out.println("split2: " + split2);
        System.out.println("splitN: " + splitN);
        System.out.println("indexOf: " + indexOf);
        System.out.println("lastIndexOf: " + lastIndexOf);
//        split2: 203
//        splitN: 2521
//        indexOf: 25
//        split2: 231
//        splitN: 2692
//        indexOf: 23
//        split2: 258
//        splitN: 2946
//        indexOf: 32
//        lastIndexOf: 42

        //split시에는 limit을 활용하는편이 빠르긴하고
        //앞 혹은 뒤에서 자를때는 indexOf를 활용하는편이 10배 가량 빠름

    }

    @Test
    void splitTest() {
        String text = "a,b,c,d";
        final String[] tokens = text.split(",");
        Assertions.assertEquals(4, tokens.length);
        Assertions.assertEquals("a", tokens[0]);
        Assertions.assertEquals("d", tokens[3]);

        final String[] tokens2 = text.split(",", 2);
        Assertions.assertEquals(2, tokens2.length);
        Assertions.assertEquals("a", tokens2[0]);
        Assertions.assertEquals("b,c,d", tokens2[1]);
    }

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

    @Test
    void markSupported() {
        String str = "text";
        InputStream is = new ByteArrayInputStream(str.getBytes());
        Assertions.assertTrue(is.markSupported());
    }

    @Test
    void subStringTest() {
        String str = "0123456789";

        Assertions.assertEquals(str, str.substring(0));
        Assertions.assertEquals("89", str.substring(8));
        Assertions.assertEquals(str, str.substring(0, str.length()));
        Assertions.assertEquals("89", str.substring(8, str.length()));
    }

    @Test
    void replaceTest_basic() {
        {
            String first = "A" + Strings.repeat("_", 1_000_000);

            final String replace = first.replace("A", "B");
            final String replaceAll = first.replaceAll("A", "B");
            final String replaceFirst = first.replaceFirst("A", "B");

            Assertions.assertEquals(replace, replaceAll);
            Assertions.assertEquals(replace, replaceFirst);
        }
        {
            String first = "A" + Strings.repeat("_", 1_000_000) + "A";

            final String replace = first.replace("A", "B");
            final String replaceAll = first.replaceAll("A", "B");
            final String replaceFirst = first.replaceFirst("A", "B");

            Assertions.assertEquals(replace, replaceAll);
            Assertions.assertNotEquals(replace, replaceFirst);
            Assertions.assertEquals(replace, replaceFirst.replaceFirst("A", "B"));
        }
        {
            String str = "a,b,";
            final String replace = str.replace(",", "B");
            final String replaceAll = str.replaceAll(",", "B");
            final String replaceFirst = str.replaceFirst(",", "B");
            System.out.println("replace: " + replace);
            System.out.println("replaceAll: " + replaceAll);
            System.out.println("replaceFirst: " + replaceFirst);
            Assertions.assertEquals("aBbB", replace);
            Assertions.assertEquals("aBbB", replaceAll);
            Assertions.assertEquals("aBb,", replaceFirst);
        }
    }

    //perf replace, replaceAll

    @Test
    void replaceVerseReplaceAll() {
        int REPEAT_CNT = 100;
        String first = "A" + Strings.repeat("_", 1_000_000) + "A";
        String expect = "B" + Strings.repeat("_", 1_000_000) + "B";

        final long replaceTime = TestTimeUtils.processTime(() -> {
            IntStream.range(0, REPEAT_CNT).forEach(n -> {
                final String replace1 = first.replace("A", "B");
                Assertions.assertEquals(expect, replace1);
            });
        });

        final long replaceAllTime = TestTimeUtils.processTime(() -> {
            IntStream.range(0, REPEAT_CNT).forEach(n -> {
                final String replace1 = first.replaceAll("A", "B");
                Assertions.assertEquals(expect, replace1);
            });
        });

        System.out.println("replaceTime : " + replaceTime);
        System.out.println("replaceAllTime : " + replaceAllTime);
//        replaceTime : 84
//        replaceAllTime : 355
//        replaceTime : 93
//        replaceAllTime : 481
    }

    //TODO
    @Test
    void replaceTest_pef_one_first() {

    }

    //TODO
    @Test
    void replaceTest_pef_one_start_end() {

    }

    //replace, replaceAll, replaceFirst 각 성능 및 동작 확인

}
