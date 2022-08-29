package com.meteor.batch.guava;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class CacheTest {

    @Test
    void cacheTest() throws ExecutionException {
        AtomicInteger loadCnt = new AtomicInteger();
        LoadingCache<String, String> cache = CacheBuilder.newBuilder()
                                                         .maximumSize(1000)
                                                         .expireAfterWrite(2, TimeUnit.SECONDS)
                                                         .build(new CacheLoader<String, String>() {
                                                             @Override
                                                             public String load(String s) throws Exception {
//                                                                 System.out.println("[LOAD]s : " + s);
                                                                 loadCnt.incrementAndGet();
                                                                 return s + "Value";
                                                             }
                                                         });

        for (int i = 0; i < 10; i++) {
            final String value1 = cache.get("key1");
            final String value2 = cache.get("key2");
        }

        Assertions.assertEquals(2, loadCnt.get());
    }

    @Test
    void cacheReloadTest() throws ExecutionException, InterruptedException {
        AtomicInteger loadCnt = new AtomicInteger();
        LoadingCache<String, String> cache = CacheBuilder.newBuilder()
                                                         .maximumSize(1000)
                                                         .expireAfterWrite(200, TimeUnit.MICROSECONDS)
                                                         .build(new CacheLoader<String, String>() {
                                                             @Override
                                                             public String load(String s) throws Exception {
//                                                                 System.out.println("[LOAD]s : " + s);
                                                                 loadCnt.incrementAndGet();
                                                                 return s + "Value";
                                                             }
                                                         });
        for (int i = 0; i < 10; i++) {
            final String value1 = cache.get("key1");
            Thread.sleep(300);
        }

        Assertions.assertEquals(10, loadCnt.get());
    }

    @Test
    void cacheMaxSizeTest() throws ExecutionException {
        final int MAX_SIZE = 10;
        LoadingCache<String, String> cache = CacheBuilder.newBuilder()
                                                         .maximumSize(MAX_SIZE)
                                                         .expireAfterWrite(100, TimeUnit.SECONDS)
                                                         .build(new CacheLoader<String, String>() {
                                                             @Override
                                                             public String load(String s) throws Exception {
//                                                                 System.out.println("[LOAD]s : " + s);
                                                                 return s + "Value";
                                                             }
                                                         });
        Assertions.assertEquals(0, cache.size());

//        for (int i = 0; i < 200; i++) {
//            cache.put("k" + i, "value" + i);
//        }

        for (int i = 0; i < 200; i++) {
            cache.get("k" + i);
        }

        Assertions.assertEquals(10, cache.size());
    }

}
