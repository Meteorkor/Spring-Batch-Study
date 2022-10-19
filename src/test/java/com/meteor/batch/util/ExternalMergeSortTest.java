package com.meteor.batch.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

public class ExternalMergeSortTest {

    @Test
    void sort() throws IOException {
        File tempFile = File.createTempFile("temp", "sufff");
        tempFile.deleteOnExit();

        final int endExclusive = 1000;

        final List<Integer> collect = IntStream.range(0, endExclusive).boxed().collect(Collectors.toList());
        Collections.shuffle(collect);
        Files.write(tempFile.toPath(),
                    collect.stream().map(String::valueOf).collect(Collectors.toList()),
                    StandardOpenOption.APPEND);

        final ExternalMergeSort fileSort = ExternalMergeSort.builder()
                                                            .elementLoadMaxCnt(endExclusive / 10)
                                                            .targetFile(tempFile)
                                                            .objToLineMapper(obj -> obj.toString())
                                                            .lineToObjMapper(line -> line)
                                                            .comparator(Comparator.comparing(n -> {
                                                                return Integer.valueOf(String.valueOf(n));
                                                            }))
                                                            .build();

        final File sortedFile = fileSort.sort();

        try (final BufferedReader bufferedReader = Files.newBufferedReader(sortedFile.toPath())) {
            String readLine;
            Integer beforeNum = null;
            while ((readLine = bufferedReader.readLine()) != null) {
                final int readNum = Integer.parseInt(readLine);
                if (beforeNum == null) {
                    beforeNum = readNum;
                } else {
                    Assertions.assertTrue(readNum > beforeNum,
                                          String.format("NOT %s > %s", readNum, beforeNum));
                }
            }
        }

    }

    @RepeatedTest(100)
    void divideSortedTempFile() throws IOException {
        File tempFile = File.createTempFile("temp", "sufff");
        tempFile.deleteOnExit();

        final int endExclusive = 1000;

        final List<Integer> collect = IntStream.range(0, endExclusive).boxed().collect(Collectors.toList());
        Collections.shuffle(collect);
        Files.write(tempFile.toPath(),
                    collect.stream().map(String::valueOf).collect(Collectors.toList()),
                    StandardOpenOption.APPEND);

        final ExternalMergeSort fileSort = ExternalMergeSort.builder()
                                                            .elementLoadMaxCnt(endExclusive / 10)
                                                            .targetFile(tempFile)
                                                            .objToLineMapper(obj -> obj.toString())
                                                            .lineToObjMapper(line -> line)
                                                            .comparator(Comparator.comparing(n -> {
                                                                return Integer.valueOf(String.valueOf(n));
                                                            }))
                                                            .build();

        final Object divideSortedTempFile = ReflectionTestUtils.invokeMethod(fileSort, "divideSortedTempFile");

        if (divideSortedTempFile instanceof List) {

            Assertions.assertEquals(10, ((List<?>) divideSortedTempFile).size());

            for (Object fileObj : (List) divideSortedTempFile) {
                if (fileObj instanceof File) {
                    try (final BufferedReader bufferedReader = Files.newBufferedReader(
                            ((File) fileObj).toPath())) {
                        String readLine;
                        Integer beforeNum = null;
                        while ((readLine = bufferedReader.readLine()) != null) {
                            final int readNum = Integer.parseInt(readLine);
                            if (beforeNum == null) {
                                beforeNum = readNum;
                            } else {
                                Assertions.assertTrue(readNum > beforeNum,
                                                      String.format("NOT %s > %s", readNum, beforeNum));
                            }
                        }
                    }
                }
            }
        }
    }

    @Test
    void divideSortedTempFile_zero() throws IOException {
        File tempFile = File.createTempFile("temp", "sufff");
        tempFile.deleteOnExit();

        final int endExclusive = 0;

        final List<Integer> collect = IntStream.range(0, endExclusive).boxed().collect(Collectors.toList());
        Collections.shuffle(collect);
        Files.write(tempFile.toPath(),
                    collect.stream().map(String::valueOf).collect(Collectors.toList()),
                    StandardOpenOption.APPEND);

        final ExternalMergeSort fileSort = ExternalMergeSort.builder()
                                                            .elementLoadMaxCnt(endExclusive / 10)
                                                            .targetFile(tempFile)
                                                            .objToLineMapper(obj -> obj.toString())
                                                            .lineToObjMapper(line -> line)
                                                            .comparator(Comparator.comparing(n -> {
                                                                return Integer.valueOf(String.valueOf(n));
                                                            }))
                                                            .build();

        final Object divideSortedTempFile = ReflectionTestUtils.invokeMethod(fileSort, "divideSortedTempFile");

        if (divideSortedTempFile instanceof List) {

            Assertions.assertEquals(0, ((List<?>) divideSortedTempFile).size());

            for (Object fileObj : (List) divideSortedTempFile) {
                if (fileObj instanceof File) {
                    try (final BufferedReader bufferedReader = Files.newBufferedReader(
                            ((File) fileObj).toPath())) {
                        String readLine;
                        Integer beforeNum = null;
                        while ((readLine = bufferedReader.readLine()) != null) {
                            final int readNum = Integer.parseInt(readLine);
                            if (beforeNum == null) {
                                beforeNum = readNum;
                            } else {
                                Assertions.assertTrue(readNum > beforeNum,
                                                      String.format("NOT %s > %s", readNum, beforeNum));
                            }
                        }
                    }
                }
            }
        }
    }

    @Test
    void divideSortedTempFile_one() throws IOException {
        File tempFile = File.createTempFile("temp", "sufff");
        tempFile.deleteOnExit();

        final int endExclusive = 1;

        final List<Integer> collect = IntStream.range(0, endExclusive).boxed().collect(Collectors.toList());
        Collections.shuffle(collect);
        Files.write(tempFile.toPath(),
                    collect.stream().map(String::valueOf).collect(Collectors.toList()),
                    StandardOpenOption.APPEND);

        final ExternalMergeSort fileSort = ExternalMergeSort.builder()
                                                            .elementLoadMaxCnt(endExclusive)
                                                            .targetFile(tempFile)
                                                            .objToLineMapper(obj -> obj.toString())
                                                            .lineToObjMapper(line -> line)
                                                            .comparator(Comparator.comparing(n -> {
                                                                return Integer.valueOf(String.valueOf(n));
                                                            }))
                                                            .build();

        final Object divideSortedTempFile = ReflectionTestUtils.invokeMethod(fileSort, "divideSortedTempFile");

        if (divideSortedTempFile instanceof List) {

            Assertions.assertEquals(1, ((List<?>) divideSortedTempFile).size());

            for (Object fileObj : (List) divideSortedTempFile) {
                if (fileObj instanceof File) {
                    try (final BufferedReader bufferedReader = Files.newBufferedReader(
                            ((File) fileObj).toPath())) {
                        String readLine;
                        Integer beforeNum = null;
                        while ((readLine = bufferedReader.readLine()) != null) {
                            final int readNum = Integer.parseInt(readLine);
                            if (beforeNum == null) {
                                beforeNum = readNum;
                            } else {
                                Assertions.assertTrue(readNum > beforeNum,
                                                      String.format("NOT %s > %s", readNum, beforeNum));
                            }
                        }
                    }
                }
            }
        }
    }

}
