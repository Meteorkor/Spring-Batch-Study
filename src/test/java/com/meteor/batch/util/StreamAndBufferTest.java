package com.meteor.batch.util;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
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

    //small string repeat
    @Test
    void writePerformanceTest_smallString() throws IOException {
        File temp = File.createTempFile("prefix", "suffix");

        String output = "text";
//        int repeatCnt = 10_000_000;
        int repeatCnt = 1_000_000;
//        int repeatCnt = 1000;

        final long fileWriterTime = TestTimeUtils.processTime(() -> {
            try (final FileWriter fileWriter = new FileWriter(temp);) {
                IntStream.range(0, repeatCnt).forEach(n -> {
                    try {
                        fileWriter.write(output);
                    } catch (IOException ignore) {
                    }
                });

            } catch (IOException ignore) {
            }
        });

        //fileWriterTime : 196
//        fileWriterTime : 164
        System.out.println("fileWriterTime : " + fileWriterTime);
        Assertions.assertEquals(Strings.repeat(output, repeatCnt), Files.readAllLines(temp.toPath())
                                                                        .stream()
                                                                        .collect(Collectors.joining()));
        temp.delete();

        final long bufferedWriterTime = TestTimeUtils.processTime(() -> {
            try (final BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(temp));) {
                IntStream.range(0, repeatCnt).forEach(n -> {
                    try {
                        bufferedWriter.write(output);
                    } catch (IOException ignore) {
                    }
                });

            } catch (IOException ignore) {
            }
        });

        //bufferedWriterTime : 42
        //bufferedWriterTime : 46
        System.out.println("bufferedWriterTime : " + bufferedWriterTime);
        Assertions.assertEquals(Strings.repeat(output, repeatCnt), Files.readAllLines(temp.toPath())
                                                                        .stream()
                                                                        .collect(Collectors.joining()));
        temp.delete();

        final long fileBufferedWriterTime = TestTimeUtils.processTime(() -> {
            try (final BufferedWriter fileBufferedWriter = Files.newBufferedWriter(temp.toPath());) {
                IntStream.range(0, repeatCnt).forEach(n -> {
                    try {
                        fileBufferedWriter.write(output);
                    } catch (IOException ignore) {
                    }
                });

            } catch (IOException ignore) {
            }
        });

        //fileBufferedWriterTime : 57
//        fileBufferedWriterTime : 42
        //확실히 buffered가 빠름, string 저장시에는 bytes 를 넘겨야하는 outpustream 보다는 string 그대로 넘길수 있는 writer 활용
        System.out.println("fileBufferedWriterTime : " + fileBufferedWriterTime);
        Assertions.assertEquals(Strings.repeat(output, repeatCnt), Files.readAllLines(temp.toPath())
                                                                        .stream()
                                                                        .collect(Collectors.joining()));
        temp.delete();

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
        temp.delete();

        final long filesWriteTime = TestTimeUtils.processTime(() -> {

            IntStream.range(0, repeatCnt).forEach(n -> {
                try {
                    Files.write(temp.toPath(), output.getBytes(), StandardOpenOption.CREATE,
                                StandardOpenOption.APPEND);
                } catch (IOException ignore) {
                    ignore.printStackTrace();
                }
            });

        });

        //아주아주 오래 시간이 소요됨..
        //같은 파일에 여러번쓸때는 Files.write는 사용하지 않도록.. 계속 열고 닫고 등등의 체크가 있으니 상당히 성능이 안나오는것으로 보임
        System.out.println("filesWriteTime : " + filesWriteTime);
        Assertions.assertEquals(Strings.repeat(output, repeatCnt), Files.readAllLines(temp.toPath())
                                                                        .stream()
                                                                        .collect(Collectors.joining()));
        temp.delete();

        //확연히 bufferedOutputStream가 빠름, TODO Channel에서 어떻게 Buffered 적용하는지 확인
//        fileOutputStreamTime : 3219
//        bufferedOutputStreamTime : 62
//        channelWriteTime : 3622

    }

    @Test
    void writePerformanceTest_BigString() throws IOException {
        File temp = File.createTempFile("prefix", "suffix");

//        int repeatCnt = 10;
        int repeatCnt = 10_000_000;//300MB
        String output = Strings.repeat("text", repeatCnt);

        final long fileWriterTime = TestTimeUtils.processTime(() -> {
            try (final FileWriter fileWriter = new FileWriter(temp);) {
                fileWriter.write(output);
            } catch (IOException ignore) {
            }
        });

        //fileWriterTime : 209
        System.out.println("fileWriterTime : " + fileWriterTime);
        Assertions.assertEquals(output, Files.readAllLines(temp.toPath())
                                             .stream()
                                             .collect(Collectors.joining()));
        temp.delete();

        final long bufferedWriterTime = TestTimeUtils.processTime(() -> {
            try (final BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(temp));) {
                bufferedWriter.write(output);
            } catch (IOException ignore) {
            }
        });

        //bufferedWriterTime : 146
        System.out.println("bufferedWriterTime : " + bufferedWriterTime);
        Assertions.assertEquals(output, Files.readAllLines(temp.toPath())
                                             .stream()
                                             .collect(Collectors.joining()));
        temp.delete();

        final long fileBufferedWriterTime = TestTimeUtils.processTime(() -> {
            try (final BufferedWriter fileBufferedWriter = Files.newBufferedWriter(temp.toPath());) {
                fileBufferedWriter.write(output);
            } catch (IOException ignore) {
            }
        });

        //fileBufferedWriterTime : 109
        System.out.println("fileBufferedWriterTime : " + fileBufferedWriterTime);
        Assertions.assertEquals(output, Files.readAllLines(temp.toPath())
                                             .stream()
                                             .collect(Collectors.joining()));
        temp.delete();

        final long fileOutputStreamTime = TestTimeUtils.processTime(() -> {
            try (final FileOutputStream fileOutputStream = new FileOutputStream(temp)) {
                fileOutputStream.write(output.getBytes());
            } catch (IOException ignore) {
            }
        });

        System.out.println("fileOutputStreamTime : " + fileOutputStreamTime);
        Assertions.assertEquals(output, Files.readAllLines(temp.toPath())
                                             .stream()
                                             .collect(Collectors.joining()));
        temp.delete();

        final long bufferedOutputStreamTime = TestTimeUtils.processTime(() -> {
            try (final BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(
                    new FileOutputStream(temp));) {
                bufferedOutputStream.write(output.getBytes());
            } catch (IOException ignore) {
            }
        });

        //bufferedOutputStreamTime : 49
        System.out.println("bufferedOutputStreamTime : " + bufferedOutputStreamTime);

        Assertions.assertEquals(output, Files.readAllLines(temp.toPath())
                                             .stream()
                                             .collect(Collectors.joining()));
        temp.delete();

        final long channelWriteTime = TestTimeUtils.processTime(() -> {
            try (final FileChannel fileChannel = new FileOutputStream(temp).getChannel()) {

                try {
                    final ByteBuffer allocate = ByteBuffer.allocate(output.length());
                    allocate.put(output.getBytes());
                    allocate.flip();
                    fileChannel.write(allocate);
//                    final ByteBuffer allocate = ByteBuffer.allocate(1024);
//                    allocate.put(output.getBytes());
//                    allocate.flip();
//                    fileChannel.write(allocate);
                } catch (IOException ignore) {
                }

            } catch (IOException ignore) {
            }
        });

        //channelWriteTime : 59
        System.out.println("channelWriteTime : " + channelWriteTime);
        Assertions.assertEquals(output, Files.readAllLines(temp.toPath())
                                             .stream()
                                             .collect(Collectors.joining()));
        temp.delete();

        final long channelDirectWriteTime = TestTimeUtils.processTime(() -> {
            try (final FileChannel fileChannel = new FileOutputStream(temp).getChannel()) {

                try {
                    final ByteBuffer allocate = ByteBuffer.allocateDirect(output.length());
                    allocate.put(output.getBytes());
                    allocate.flip();
                    fileChannel.write(allocate);
                } catch (IOException ignore) {
                }

            } catch (IOException ignore) {
            }
        });

        System.out.println("channelDirectWriteTime : " + channelDirectWriteTime);
        Assertions.assertEquals(output, Files.readAllLines(temp.toPath())
                                             .stream()
                                             .collect(Collectors.joining()));
        temp.delete();

        final long filesWriteTime = TestTimeUtils.processTime(() -> {

            try {
                Files.write(temp.toPath(), output.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        //filesWriteTime : 116
        System.out.println("filesWriteTime : " + filesWriteTime);
        Assertions.assertEquals(output, Files.readAllLines(temp.toPath())
                                             .stream()
                                             .collect(Collectors.joining()));
        temp.delete();

//        fileWriterTime : 174
//        bufferedWriterTime : 172
//        fileBufferedWriterTime : 124
//        fileOutputStreamTime : 66
//        bufferedOutputStreamTime : 53
//        channelWriteTime : 66
//        channelDirectWriteTime : 87

        //큰파일을 바로 쓸때는 buffered는 조금 비효율적인듯..
        //하지만, 보통 파일에 쓸때는 메모리에 데이터를 많이 올리지 않고 나눠서 올리기 때문에, Buffered는 충분히 활용해야함

    }

    //TODO Writer로 쓰는것과 stream에 쓰는것 성능 비교(String일때 기준)

    //NIO ByteBuffer

    //DataBuffer
    //https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#databuffers-factory
}
