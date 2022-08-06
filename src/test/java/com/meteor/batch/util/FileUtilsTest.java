package com.meteor.batch.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Iterator;
import java.util.List;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class FileUtilsTest {

    @Test
    void lineToProcess() throws IOException {
        File file = File.createTempFile("pre", "suf");
        file.deleteOnExit();

        final List<String> of = Lists.list("a", "b", "c");
        Files.write(file.toPath(), of, StandardOpenOption.CREATE);

        final Iterator<String> iterator = of.iterator();

        final FileUtils.FileReadProcess fileReadProcess = FileUtils.FileReadProcess.builder()
                                                                                   .readPath(file.toPath())
                                                                                   .lineProcess(
                                                                                           line ->
                                                                                                   Assertions.assertEquals(
                                                                                                           iterator.next(),
                                                                                                           line
                                                                                                   )
                                                                                   ).build();

        fileReadProcess.lineToProcess();
    }

    @Test
    void itemWriteProcess() throws IOException {
        File file = File.createTempFile("pre", "suf");
        file.deleteOnExit();

        final List<String> of = Lists.list("a", "b", "c");
        final Iterator<String> itemIter = of.iterator();

        final FileUtils.FileWriteProcess fileWriteProcess = FileUtils.FileWriteProcess.builder()
                                                                                      .writePath(file.toPath())
                                                                                      .itemSupplier(
                                                                                              () -> itemIter.hasNext() ?
                                                                                                    itemIter.next() :
                                                                                                    null
                                                                                      )
                                                                                      .itemSeparator(
                                                                                              System.lineSeparator())
                                                                                      .option(StandardOpenOption.CREATE)
                                                                                      .build();

        fileWriteProcess.itemWriteProcess();

        final Iterator<String> iterator = of.iterator();
        final FileUtils.FileReadProcess fileReadProcess = FileUtils.FileReadProcess.builder().readPath(
                                                                           file.toPath())
                                                                                   .lineProcess(line -> {
                                                                                       Assertions.assertEquals(
                                                                                               iterator.next(),
                                                                                               line);
                                                                                   }).build();

        fileReadProcess.lineToProcess();
    }

}