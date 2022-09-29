package com.meteor.batch.basic;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ThreadLocalTest {

    private ThreadLocal<String> threadLocalStr = new ThreadLocal<>();
    private InheritableThreadLocal<String> inheritableThreadLocal = new InheritableThreadLocal<>();

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

    @Test
    void inheritableThreadLocalSimpleTest() {
        final String str = "str";
        Assertions.assertNull(inheritableThreadLocal.get());

        inheritableThreadLocal.set(str);
        Assertions.assertEquals(str, inheritableThreadLocal.get());

        inheritableThreadLocal.remove();
        Assertions.assertNull(inheritableThreadLocal.get());
    }

    @Test
    void inheritableThreadLocalOtherThreadTest() throws ExecutionException, InterruptedException {
        final String str = "str";
        inheritableThreadLocal.set(str);

        final ExecutorService executorService = Executors.newFixedThreadPool(3);
        final Future<?> submit = executorService.submit(() -> {
//            Assertions.assertNull(inheritableThreadLocal.get());
            Assertions.assertEquals(str, inheritableThreadLocal.get());
        });

        submit.get();

        Assertions.assertEquals(str, inheritableThreadLocal.get());
    }

    @Test
    void inheritableThreadLocalOtherThreadRemoveTest() throws ExecutionException, InterruptedException {
        final String str = "str";
        inheritableThreadLocal.set(str);

        final ExecutorService executorService = Executors.newFixedThreadPool(3);
        final Future<?> submit = executorService.submit(() -> {
//            Assertions.assertNull(inheritableThreadLocal.get());
            Assertions.assertEquals(str, inheritableThreadLocal.get());
            inheritableThreadLocal.remove();
            Assertions.assertNotEquals(str, inheritableThreadLocal.get());
        });

        submit.get();

        Assertions.assertEquals(str, inheritableThreadLocal.get());
        //Thread 생성시 카피가 되는듯함
    }

}
