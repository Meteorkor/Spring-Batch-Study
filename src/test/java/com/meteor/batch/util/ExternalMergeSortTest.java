package com.meteor.batch.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

public class ExternalMergeSortTest {

    @Test
    void divideSortedTempFile() throws IOException {
        File tempFile = File.createTempFile("temp", "sufff");
        tempFile.deleteOnExit();

        Files.write(tempFile.toPath(),
                    ThreadLocalRandom.current().longs(100)
                                     .boxed().map(String::valueOf)
                                     .collect(Collectors.toList()),
                    StandardOpenOption.APPEND);

        final List<String> sortedList = Files.readAllLines(tempFile.toPath()).stream().sorted().collect(
                Collectors.toList());

        final ExternalMergeSort fileSort = ExternalMergeSort.builder()
                                                            .elementLoadMaxCnt(1000)
                                                            .targetFile(tempFile)
                                                            .objToLineMapper(obj -> obj.toString())
                                                            .lineToObjMapper(line -> line)
                                                            .comparator(Comparator.comparing(Object::toString))
                                                            .build();

        final Object divideSortedTempFile = ReflectionTestUtils.invokeMethod(fileSort, "divideSortedTempFile");

        if (divideSortedTempFile instanceof List) {

            for (Object fileObj : (List) divideSortedTempFile) {
                if (fileObj instanceof File) {
                    try (final BufferedReader bufferedReader = Files.newBufferedReader(
                            ((File) fileObj).toPath())) {
                        String readLine;
                        while ((readLine = bufferedReader.readLine()) != null) {
                            System.out.println("readLine : " + readLine);
                        }
                    }
                }
            }
        }
    }

}
