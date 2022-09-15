package com.meteor.batch.stream;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.util.StopWatch;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import lombok.SneakyThrows;

public class ParallelStreamTest {

    @Test
    void parallelSortTest() {
        final List<Integer> collect = IntStream.range(0, 1_000_000).parallel()
                                               .boxed().collect(Collectors.toList());

        for (int i = 0; i < collect.size(); i++) {
            Assertions.assertEquals(i, collect.get(i));
        }
    }

    @Test
    void perfCompare() {
        {
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            IntStream.range(0, 10_000)
                     .boxed().map(n -> {
                         work();
                         return n;
                     }).collect(Collectors.toList());
            stopWatch.stop();
            System.out.println("stopWatch.getTotalTimeMillis(1) : " + stopWatch.getTotalTimeMillis());
        }
        {
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            IntStream.range(0, 10_000)
                     .parallel()
                     .boxed().map(n -> {
                         work();
                         return n;
                     }).collect(Collectors.toList());
            stopWatch.stop();
            System.out.println("stopWatch.getTotalTimeMillis(2) : " + stopWatch.getTotalTimeMillis());
        }

        //stopWatch.getTotalTimeMillis(1) : 12062
        //stopWatch.getTotalTimeMillis(2) : 1177
    }

    @SneakyThrows
    void work() {
        Thread.sleep(1);
    }

    @Test
    void parallelSortSleepTest() {
        {
            List<Integer> list = Collections.synchronizedList(Lists.newArrayList());
            final List<Integer> collect = IntStream.range(0, 1_000_000).parallel()
                                                   .boxed()
                                                   .map(n -> {
                                                       list.add(n);
                                                       return n;
                                                   })
                                                   .collect(Collectors.toList());

            for (int i = 0; i < collect.size(); i++) {
                Assertions.assertEquals(i, collect.get(i));
            }
            Assertions.assertFalse(Iterables.elementsEqual(list, collect));
        }
        {
            List<Integer> list = Collections.synchronizedList(Lists.newArrayList());
            final List<Integer> collect = IntStream.range(0, 1_000_000)
                                                   .boxed()
                                                   .map(n -> {
                                                       list.add(n);
                                                       return n;
                                                   })
                                                   .collect(Collectors.toList());

            for (int i = 0; i < collect.size(); i++) {
                Assertions.assertEquals(i, collect.get(i));
            }
            Assertions.assertTrue(Iterables.elementsEqual(list, collect));
        }
    }

}
