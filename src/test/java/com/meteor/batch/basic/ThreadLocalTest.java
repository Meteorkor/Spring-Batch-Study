package com.meteor.batch.basic;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ThreadLocalTest {
    
    private ThreadLocal<String> threadLocalStr = new ThreadLocal<>();

    @Test
    void simpleTest() {
        final String str = "str";
        Assertions.assertNull(threadLocalStr.get());

        threadLocalStr.set(str);
        Assertions.assertEquals(str, threadLocalStr.get());

        threadLocalStr.remove();
        Assertions.assertNull(threadLocalStr.get());
    }

    @Test
    void otherThreadTest() throws ExecutionException, InterruptedException {
        final String str = "str";
        threadLocalStr.set(str);

        final ExecutorService executorService = Executors.newFixedThreadPool(3);
        final Future<?> submit = executorService.submit(() -> {
            Assertions.assertNull(threadLocalStr.get());
        });

        submit.get();
        Assertions.assertEquals(str, threadLocalStr.get());
    }

    //TODO InheritableThreadLocal Test

}
