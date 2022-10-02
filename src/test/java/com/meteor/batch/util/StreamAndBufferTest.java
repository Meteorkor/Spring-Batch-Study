package com.meteor.batch.util;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.google.common.base.Strings;
import com.meteor.batch.TestTimeUtils;

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

    @Test
    void outStreamVsOutWriter() {
        String output = "text";
//        int repeatCnt = 10000;
        int repeatCnt = 10_000_000;

        final long outputStreamTime = TestTimeUtils.processTime(() -> {
            try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
                IntStream.range(0, repeatCnt).forEach(n -> {
                    try {
                        byteArrayOutputStream.write(output.getBytes());
                    } catch (IOException ignore) {
                    }
                });

                final byte[] bytes = byteArrayOutputStream.toByteArray();
                Assertions.assertEquals(Strings.repeat(output, repeatCnt), new String(bytes));
            } catch (IOException ignore) {

            }
        });
//        outputStreamTime : 866
        System.out.println("outputStreamTime : " + outputStreamTime);

        final long outputStreamWriterTime = TestTimeUtils.processTime(() -> {
            try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
                final OutputStreamWriter outputStreamWriter = new OutputStreamWriter(byteArrayOutputStream);
                IntStream.range(0, repeatCnt).forEach(n -> {
                    try {
                        outputStreamWriter.write(output);
                    } catch (IOException ignore) {
                    }
                });
                ////outputStreamWriterTime : 321
                //합쳐서 처리시 훨씬 빨라짐, 물론 메모리가 허용가능해야..
//                outputStreamWriter.write(Strings.repeat(output, repeatCnt));

                outputStreamWriter.close();
                final byte[] bytes = byteArrayOutputStream.toByteArray();
                Assertions.assertEquals(Strings.repeat(output, repeatCnt), new String(bytes));
            } catch (IOException ignore) {

            }
        });
        //outputStreamWriterTime : 931
        System.out.println("outputStreamWriterTime : " + outputStreamWriterTime);
    }

    @Test
    void writePerformanceTest() throws IOException {
        File temp = File.createTempFile("prefix", "suffix");

        String output = "text";
//        int repeatCnt = 10_000_000;
        int repeatCnt = 1_000_000;

        final long fileOutputStreamTime = TestTimeUtils.processTime(() -> {
            try (final FileOutputStream fileOutputStream = new FileOutputStream(temp)) {
                IntStream.range(0, repeatCnt).forEach(n -> {
                    try {
                        fileOutputStream.write(output.getBytes());
                    } catch (IOException ignore) {
                    }
                });

            } catch (IOException ignore) {
            }
        });

        System.out.println("fileOutputStreamTime : " + fileOutputStreamTime);
        Assertions.assertEquals(Strings.repeat(output, repeatCnt), Files.readAllLines(temp.toPath())
                                                                        .stream()
                                                                        .collect(Collectors.joining()));
        temp.delete();

        final long bufferedOutputStreamTime = TestTimeUtils.processTime(() -> {
            try (final BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(
                    new FileOutputStream(temp));) {
                IntStream.range(0, repeatCnt).forEach(n -> {
                    try {
                        bufferedOutputStream.write(output.getBytes());
                    } catch (IOException ignore) {
                    }
                });

            } catch (IOException ignore) {
            }
        });

        //outputStreamTime : 106
//        outputStreamTime : 69
        System.out.println("bufferedOutputStreamTime : " + bufferedOutputStreamTime);

        Assertions.assertEquals(Strings.repeat(output, repeatCnt), Files.readAllLines(temp.toPath())
                                                                        .stream()
                                                                        .collect(Collectors.joining()));
        temp.delete();

//TODO        Files.newBufferedWriter()

//        final ByteBuffer allocate = ByteBuffer.allocate(1024);
        final long channelWriteTime = TestTimeUtils.processTime(() -> {
            try (final FileChannel fileChannel = new FileOutputStream(temp).getChannel()) {
                IntStream.range(0, repeatCnt).forEach(n -> {
                    try {
                        final ByteBuffer allocate = ByteBuffer.allocate(1024);
                        allocate.put(output.getBytes());
                        allocate.flip();
                        fileChannel.write(allocate);
//                        allocate.clear();
//                        allocate.
                    } catch (IOException ignore) {
                    }
                });

            } catch (IOException ignore) {
            }
        });
//        channelWriteTime : 3414, allocate.clear();
        System.out.println("channelWriteTime : " + channelWriteTime);
        Assertions.assertEquals(Strings.repeat(output, repeatCnt), Files.readAllLines(temp.toPath())
                                                                        .stream()
                                                                        .collect(Collectors.joining()));
        //확연히 bufferedOutputStream가 빠름, TODO Channel에서 어떻게 Buffered 적용하는지 확인
//        fileOutputStreamTime : 3219
//        bufferedOutputStreamTime : 62
//        channelWriteTime : 3622


    }

    //TODO Writer로 쓰는것과 stream에 쓰는것 성능 비교(String일때 기준)

    //NIO ByteBuffer

    //DataBuffer
    //https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#databuffers-factory
}
