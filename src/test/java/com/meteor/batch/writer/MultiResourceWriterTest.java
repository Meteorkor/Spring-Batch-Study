package com.meteor.batch.writer;

import java.io.File;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.MultiResourceItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.builder.MultiResourceItemWriterBuilder;
import org.springframework.core.io.FileSystemResource;

import com.meteor.batch.reader.vo.FileReaderItem;

public class MultiResourceWriterTest {

    @Test
    void multiResourceItemWriterTest() throws Exception {
        final String SUFFIX = "suffix.";
        final FlatFileItemWriter<FileReaderItem> writer = new FlatFileItemWriterBuilder<FileReaderItem>()
                .saveState(false)
                .name("writerName")
                .lineAggregator(item -> {
                    return new StringBuilder()
                            .append(item.getName())
                            .append(",")
                            .append(item.getData()).toString();
                })
                .build();

        final int itemCountLimitPerResource = 100;

        final List<FileReaderItem> collect = IntStream.range(0, itemCountLimitPerResource * 3).mapToObj(n -> {
            return FileReaderItem.builder().name("name" + n).data(String.valueOf(n)).build();
        }).collect(Collectors.toList());

        final File tempFile = File.createTempFile("pre", "suf");

        final MultiResourceItemWriter<FileReaderItem> multiResourceItemWriter =
                new MultiResourceItemWriterBuilder<FileReaderItem>().resourceSuffixCreator(
                                                                            index -> SUFFIX + index)
                                                                    .saveState(false)
                                                                    .resource(new FileSystemResource(tempFile))
//                                                                    .itemCountLimitPerResource(
//                                                                            itemCountLimitPerResource)
                                                                    .delegate(writer)
                                                                    .build();

        multiResourceItemWriter.write(collect);
        multiResourceItemWriter.write(collect);
        multiResourceItemWriter.write(collect);
        multiResourceItemWriter.close();

        File writtenFile = new File(tempFile.getAbsolutePath() + SUFFIX + 1);

        final int size = Files.readAllLines(writtenFile.toPath()).size();
        Assertions.assertEquals(size, collect.size() * 3);
    }

    @Test
    void multiResourceItemWriterItemCountLimitPerResourceTest() throws Exception {
        final String SUFFIX = "suffix.";
        final FlatFileItemWriter<FileReaderItem> writer = new FlatFileItemWriterBuilder<FileReaderItem>()
                .saveState(false)
                .name("writerName")
                .lineAggregator(item -> {
                    return new StringBuilder()
                            .append(item.getName())
                            .append(",")
                            .append(item.getData()).toString();
                })
                .build();

        final int itemCountLimitPerResource = 100;

        final List<FileReaderItem> collect = IntStream.range(0, itemCountLimitPerResource * 3).mapToObj(n -> {
            return FileReaderItem.builder().name("name" + n).data(String.valueOf(n)).build();
        }).collect(Collectors.toList());

        final File tempFile = File.createTempFile("pre", "suf");

        final MultiResourceItemWriter<FileReaderItem> multiResourceItemWriter =
                new MultiResourceItemWriterBuilder<FileReaderItem>().resourceSuffixCreator(
                                                                            index -> SUFFIX + index)
                                                                    .saveState(false)
                                                                    .resource(new FileSystemResource(tempFile))
                                                                    .itemCountLimitPerResource(
                                                                            itemCountLimitPerResource)
                                                                    .delegate(writer)
                                                                    .build();

        multiResourceItemWriter.write(collect);
        multiResourceItemWriter.write(collect);
        multiResourceItemWriter.write(collect.subList(0, collect.size() / 2));
        multiResourceItemWriter.close();

        File writtenFile1 = new File(tempFile.getAbsolutePath() + SUFFIX + 1);
        File writtenFile2 = new File(tempFile.getAbsolutePath() + SUFFIX + 2);
        File writtenFile3 = new File(tempFile.getAbsolutePath() + SUFFIX + 3);
        File writtenFile4 = new File(tempFile.getAbsolutePath() + SUFFIX + 4);

        Assertions.assertEquals(Files.readAllLines(writtenFile1.toPath()).size(), collect.size());
        Assertions.assertEquals(Files.readAllLines(writtenFile2.toPath()).size(), collect.size());
        Assertions.assertEquals(Files.readAllLines(writtenFile3.toPath()).size(), collect.size() / 2);
        Assertions.assertFalse(writtenFile4.exists());
    }

}
