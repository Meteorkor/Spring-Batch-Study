package com.meteor.batch.util;

import java.util.stream.IntStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.util.StopWatch;

public class ExceptionReturnTest {

    private StopWatch sw = new StopWatch();
    private int CNT = 100_000;

    @BeforeEach
    void setUp() {
        sw.start();
    }

    @AfterEach
    void tearDown() {
        sw.stop();
        System.out.println("sw.getLastTaskTimeMillis() : " + sw.getLastTaskTimeMillis());
        //exception: 735
        //response: 8
        //API 디자인은 내부에서 exception 던지는것이 깔끔하기는한데, exception 생성시 trace 때문에 성능으 확 떨어짐..
    }

    @Test
    void exception() {
        final ResponseExceptionClient client = new ResponseExceptionClient();
        IntStream.range(0, CNT).forEach(n -> {
            try {
                client.request();
            } catch (Throwable ignore) {
            }
        });
    }

    @Test
    void response() {
        final ResponseBooleanClient client = new ResponseBooleanClient();
        IntStream.range(0, CNT).forEach(n -> {
            try {
                client.request();
            } catch (Throwable ignore) {
            }
        });
    }

    class ResponseExceptionClient {
        public void request() {
            throw new IllegalStateException();
        }
    }

    class ResponseBooleanClient {
        public boolean request() {
            return false;
        }
    }

}
