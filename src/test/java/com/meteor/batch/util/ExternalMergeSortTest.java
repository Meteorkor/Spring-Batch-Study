package com.meteor.batch.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
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
        int elementLoadMaxCnt = 100_000;

//        final int endExclusive = 1000;
        final int endExclusive = 100_000_000;//750MB
//        final int endExclusive = 10_000_000;75MB


        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(tempFile.toPath(),
                                                                     StandardOpenOption.APPEND)) {
            List<String> numList = new ArrayList<>(elementLoadMaxCnt);
            for (int i = 0; i < endExclusive; i++) {
                if (numList.size() == elementLoadMaxCnt) {
                    Collections.shuffle(numList);
                    bufferedWriter.write(
                            numList.stream().collect(Collectors.joining("\n")));
                    bufferedWriter.newLine();
                    numList = new ArrayList<>(elementLoadMaxCnt);
                }
                numList.add(String.valueOf(i));
            }

            if (!numList.isEmpty()) {
                bufferedWriter.write(
                        numList.stream().collect(Collectors.joining("\n")));
                bufferedWriter.newLine();
            }
        }

        final ExternalMergeSort fileSort = ExternalMergeSort.builder()
                                                            .elementLoadMaxCnt(elementLoadMaxCnt)
                                                            .targetFile(tempFile)
                                                            .objToLineMapper(obj -> obj.toString())
                                                            .lineToObjMapper(line -> line)
                                                            .comparator(Comparator.comparing(n -> {
                                                                if (n == null) {return Integer.MIN_VALUE;}
                                                                return Integer.valueOf(String.valueOf(n));
                                                            }))
                                                            .build();

        final File sortedFile = fileSort.sort();

        System.out.println("sortedFile.getAbsolutePath(): " + sortedFile.getAbsolutePath());

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
