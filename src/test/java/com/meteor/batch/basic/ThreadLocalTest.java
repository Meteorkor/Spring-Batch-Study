package com.meteor.batch.basic;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ThreadLocalTest {

    private ThreadLocal<String> threadLocalStr = new ThreadLocal<>();
    private InheritableThreadLocal<String> inheritableThreadLocal = new InheritableThreadLocal<>();
    private InheritableThreadLocal<String> otherInheritableThreadLocal = new InheritableThreadLocal<>();

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
    void otherThreadLeakTest() throws ExecutionException, InterruptedException {
        final String str = "str";
        final String child = "child";
        threadLocalStr.set(str);

        final ExecutorService executorService = Executors.newSingleThreadExecutor();
        final Future<?> submit = executorService.submit(() -> {
            Assertions.assertNull(threadLocalStr.get());
            threadLocalStr.set(child);
            Assertions.assertEquals(child, threadLocalStr.get());
        });

        submit.get();

        final Future<?> submit2 = executorService.submit(() -> {
            //Pool 이라서 전에 설정한 ThreadLocal이 남아있음
            Assertions.assertEquals(child, threadLocalStr.get());
        });
        submit2.get();

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
    void inheritableThreadLocalOtherThreadChildRemoveTest() throws ExecutionException, InterruptedException {
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

    @Test
    void inheritableThreadLocalOtherThreadRemoveTest() throws ExecutionException, InterruptedException {
        final String str = "str";
        inheritableThreadLocal.set(str);
        CountDownLatch latch = new CountDownLatch(1);

        final ExecutorService executorService = Executors.newFixedThreadPool(3);
        final Future<?> submit = executorService.submit(() -> {
            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Assertions.assertEquals(str, inheritableThreadLocal.get());
            inheritableThreadLocal.remove();
            Assertions.assertNotEquals(str, inheritableThreadLocal.get());
        });

        inheritableThreadLocal.remove();
        latch.countDown();
        submit.get();

        Assertions.assertNull(inheritableThreadLocal.get());
        //Thread 생성시 카피가 되는듯함
    }

    @Test
    void inheritableThreadLocalOtherThreadTimingTest() throws ExecutionException, InterruptedException {
        final String str = "str";

        CountDownLatch latch = new CountDownLatch(1);

        final ExecutorService executorService = Executors.newFixedThreadPool(3);
        final Future<?> submit = executorService.submit(() -> {
            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Assertions.assertNull(inheritableThreadLocal.get());
        });

        inheritableThreadLocal.set(str);
        latch.countDown();
        submit.get();

        Assertions.assertEquals(str, inheritableThreadLocal.get());
        //Thread 생성시 카피가 되는듯함->  확인해보니 Thread 생성자 arg에 inheritThreadLocals 가 기본적으로 true이고, parent항목을 카피하는부분이 있음

//        private Thread(ThreadGroup g, Runnable target, String name, long stackSize, AccessControlContext acc,
//        boolean inheritThreadLocals)
//        if (inheritThreadLocals && parent.inheritableThreadLocals != null) {
//            this.inheritableThreadLocals =
//                    ThreadLocal.createInheritedMap(parent.inheritableThreadLocals);
//        }

    }

}
