package com.meteor.batch.basic;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;

import com.meteor.batch.TestTimeUtils;

class ConstTest {
    private static final Charset CH = Charset.forName(StandardCharsets.UTF_8.name());
    private static final int REPEAT_CNT = 1_000_000;
    private static final String TEXT = UUID.randomUUID().toString();

    @Test
    void subStringCompare() {
        String txHHMMSS = "150303";

        final long perCall = TestTimeUtils.processTime(() -> {
            IntStream.range(0, REPEAT_CNT).forEach(n -> {
                String fileName = "TYPE" + txHHMMSS.substring(0, 2) + ".txt";
            });
        });

        final long oneCall = TestTimeUtils.processTime(() -> {
            final String substring = txHHMMSS.substring(0, 2);
            IntStream.range(0, REPEAT_CNT).forEach(n -> {
                String fileName = "TYPE" + substring + ".txt";
            });
        });

        final long oneCall2 = TestTimeUtils.processTime(() -> {
            final String substring = txHHMMSS.substring(0, 2);
            IntStream.range(0, REPEAT_CNT).forEach(n -> {
                String fileName = new StringBuilder("TYPE").append(substring).append(".txt").toString();
            });
        });

        System.out.println("perCall: " + perCall);
        System.out.println("oneCall: " + oneCall);
        System.out.println("oneCall2: " + oneCall2);

        //        perCall: 105
        //        perCall: 85
        //        perCall: 93

        //        oneCall: 36
        //        oneCall: 39
        //        oneCall: 39

        //        oneCall2: 54
        //        oneCall2: 56

        //이슈가 될정도로 시간이 소요되지는 않지만, 별도 선언 했을때와, 하지 않았을때는 3배정도 차이
    }

    @Test
    void charSetCompare() {
        String txHHMMSS = "150303";

        final long perCall = TestTimeUtils.processTime(() -> {
            IntStream.range(0, REPEAT_CNT).forEach(n -> {
                final Charset charset = Charset.forName(StandardCharsets.UTF_8.name());
                byte[] bytes = TEXT.getBytes(charset);
            });
        });

        final long oneCall = TestTimeUtils.processTime(() -> {
            final Charset charset = Charset.forName(StandardCharsets.UTF_8.name());
            IntStream.range(0, REPEAT_CNT).forEach(n -> {
                byte[] bytes = TEXT.getBytes(charset);
            });
        });

        System.out.println("perCall: " + perCall);
        System.out.println("oneCall: " + oneCall);

//        perCall: 28
//        perCall: 29
//        perCall: 26
//        oneCall: 11
//        oneCall: 11
//        oneCall: 12
        //subStr보다 더 미미한 수준, 하지만 상대적으로 2배차

    }

    @Test
    void concatCompare() {

        final long perCall = TestTimeUtils.processTime(() -> {
            IntStream.range(0, REPEAT_CNT).forEach(n -> {
                String result = TEXT.concat("URL2").concat("URL3");
            });
        });

        final long oneCall = TestTimeUtils.processTime(() -> {
            String result = TEXT.concat("URL2").concat("URL3");
            IntStream.range(0, REPEAT_CNT).forEach(n -> {
            });
        });

        final long plusPerCall = TestTimeUtils.processTime(() -> {
            IntStream.range(0, REPEAT_CNT).forEach(n -> {
                String result = TEXT + "URL2" + "URL3";
            });
        });
        final long plusOneCall = TestTimeUtils.processTime(() -> {
            String result = TEXT + "URL2" + "URL3";
            IntStream.range(0, REPEAT_CNT).forEach(n -> {

            });
        });

        System.out.println("perCall: " + perCall);
        System.out.println("oneCall: " + oneCall);
        System.out.println("plusPerCall: " + plusPerCall);
        System.out.println("plusOneCall: " + plusOneCall);

//        perCall: 65
//        oneCall: 7
//        plusPerCall: 47
//        plusOneCall: 3
//        perCall: 280
//        oneCall: 9
//        plusPerCall: 177
//        plusOneCall: 5

        //기본적으로 Plus가 더 빠르고, 한번 호출하는것이 10배 넘게 차이가 발생
    }

}
