package com.meteor.batch.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.google.common.base.Strings;

public class StreamAndBufferTest {

    @Test
    void streamTest() throws IOException {
        String output = "text";
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            byteArrayOutputStream.write(output.getBytes());
            final byte[] bytes = byteArrayOutputStream.toByteArray();
            Assertions.assertEquals(output, new String(bytes));
        }
    }

    @Test
    void outStreamToWriter() throws IOException {
        String output = "text";
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            final OutputStreamWriter outputStreamWriter = new OutputStreamWriter(byteArrayOutputStream);
            outputStreamWriter.write(output);
            outputStreamWriter.close();
            final byte[] bytes = byteArrayOutputStream.toByteArray();
            Assertions.assertEquals(output, new String(bytes));
        }

    }

    @Test
    void outStreamToWriterRepeatTest() throws IOException {
        String output = "text";
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            final OutputStreamWriter outputStreamWriter = new OutputStreamWriter(byteArrayOutputStream);
            IntStream.range(0, 10).forEach(n -> {
                try {
                    outputStreamWriter.write(output);
                } catch (IOException ignore) {
                }
            });

            outputStreamWriter.close();
            final byte[] bytes = byteArrayOutputStream.toByteArray();
            Assertions.assertEquals(Strings.repeat(output, 10), new String(bytes));
        }
    }

    //TODO Writer로 쓰는것과 stream에 쓰는것 성능 비교(String일때 기준)

    //NIO ByteBuffer

    //DataBuffer
}
