package com.meteor.batch.basic;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.RepeatedTest;
import org.springframework.util.StopWatch;

public class EnumFindTest {
    final int TEST_CNT = 1_000_000;

    /**
     * 6배 ~ 30배
     * findFromMap : 16
     * findFromValues : 479
     *
     * findFromMap : 4
     * findFromValues : 362
     *
     * findFromMap : 5
     * findFromValues : 339
     */
    @RepeatedTest(10)
    void performanceTest_firstElement() {
        final TypeEnum type = TypeEnum.Z;
        {
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            IntStream.range(0, TEST_CNT)
                     .forEach(n -> {
                         TypeEnum.findFromMap(type.name());
                     });
            stopWatch.stop();
            System.out.println("findFromMap : " + stopWatch.getTotalTimeMillis());
        }
        {
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            IntStream.range(0, TEST_CNT)
                     .forEach(n -> {
                         TypeEnum.findFromValues(type.name());
                     });
            stopWatch.stop();
            System.out.println("findFromValues : " + stopWatch.getTotalTimeMillis());
        }
    }

    /**
     * 30배 ~ 60배
     * findFromMap : 13
     * findFromValues : 339
     *
     * findFromMap : 5
     * findFromValues : 315
     *
     * findFromMap : 5
     * findFromValues : 356
     */
    @RepeatedTest(10)
    void performanceTest_lastElement() {
        final TypeEnum type = TypeEnum.Z;
//        final int TEST_CNT = 1_000_000;
        {
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            IntStream.range(0, TEST_CNT)
                     .forEach(n -> {
                         TypeEnum.findFromMap(type.name());
                     });
            stopWatch.stop();
            System.out.println("findFromMap : " + stopWatch.getTotalTimeMillis());
        }
        {
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            IntStream.range(0, TEST_CNT)
                     .forEach(n -> {
                         TypeEnum.findFromValues(type.name());
                     });
            stopWatch.stop();
            System.out.println("findFromValues : " + stopWatch.getTotalTimeMillis());
        }
    }

    /**
     * 30배~50배
     * findFromMap : 111
     * findFromValues : 5709
     *
     * findFromMap : 240
     * findFromValues : 6356
     *
     * findFromMap : 147
     * findFromValues : 5731
     */
    @RepeatedTest(10)
    void performanceTest_allElement() {
        {
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            Arrays.stream(TypeEnum.values())
                  .forEach(type -> {
                      IntStream.range(0, TEST_CNT)
                               .forEach(n -> {
                                   TypeEnum.findFromMap(type.name());
                               });
                  });

            stopWatch.stop();
            System.out.println("findFromMap : " + stopWatch.getTotalTimeMillis());
        }
        {
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            Arrays.stream(TypeEnum.values())
                  .forEach(type -> {
                      IntStream.range(0, TEST_CNT)
                               .forEach(n -> {
                                   TypeEnum.findFromValues(type.name());
                               });
                  });
            stopWatch.stop();
            System.out.println("findFromValues : " + stopWatch.getTotalTimeMillis());
        }
    }

    enum TypeEnum {
        A, B, C, D, E, F, G,
        H, I, J, K, L, M, N, O, P,
        Q, R, S, T, U, V, W, X, Y, Z;

        private static final Map<String, TypeEnum> typeMap = Arrays.stream(values()).collect(
                Collectors.toMap(Enum::name, Function.identity()));

        public static TypeEnum findFromMap(String type) {
            return typeMap.get(type);
        }

        public static TypeEnum findFromValues(String type) {
            return Arrays.stream(values())
                         .filter(it -> it.name().equals(type))
                         .findFirst().get();
        }

    }
}
