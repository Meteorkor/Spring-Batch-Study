package com.meteor.batch.writer;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.core.io.FileSystemResource;

import com.meteor.batch.reader.vo.FileReaderItem;

public class FileWriterTest {

    @Test
    void flatFileItemWriterTest() throws Exception {

        final File tempFile = File.createTempFile("pre", "suf");

        final FlatFileItemWriter<FileReaderItem> writer = new FlatFileItemWriterBuilder<FileReaderItem>()
                .saveState(false)
                .name("writerName")
                .lineAggregator(item -> {
                    return new StringBuilder()
                            .append(item.getName())
                            .append(",")
                            .append(item.getData()).toString();
                })
                .resource(new FileSystemResource(tempFile))
                .build();
//        writer.setResource(new FileSystemResource(tempFile));
        ExecutionContext executionContext = new ExecutionContext();
        writer.open(executionContext);

        final List<FileReaderItem> itemList = Lists.newArrayList(FileReaderItem.builder().name("kim")
                                                                               .data("data1")
                                                                               .build());

        writer.write(itemList);

        final List<String> strings = Files.readAllLines(tempFile.toPath());
        Assertions.assertEquals(1, strings.size());
        Assertions.assertEquals("kim,data1", strings.get(0));
    }

    @Test
    void flatFileItemWriterHeaderFooterTest() throws Exception {
        String HEADER_STR = "HEADER";
        String FOOT_STR = "FOOTER";

        final File tempFile = File.createTempFile("pre", "suf");

        final FlatFileItemWriter<FileReaderItem> writer = new FlatFileItemWriterBuilder<FileReaderItem>()
                .saveState(false)
                .name("writerName")
                .headerCallback(headerWriter -> headerWriter.write(HEADER_STR))
                .footerCallback(footerWriter -> footerWriter.write(FOOT_STR))
                .lineAggregator(item -> {
                    return new StringBuilder()
                            .append(item.getName())
                            .append(",")
                            .append(item.getData()).toString();
                })
                .resource(new FileSystemResource(tempFile))
                .build();
//        writer.setResource(new FileSystemResource(tempFile));
        ExecutionContext executionContext = new ExecutionContext();
        writer.open(executionContext);

        final List<FileReaderItem> itemList = Lists.newArrayList(FileReaderItem.builder().name("kim")
                                                                               .data("data1")
                                                                               .build());

        writer.write(itemList);

        writer.close();//footer

        final List<String> strings = Files.readAllLines(tempFile.toPath());

        Assertions.assertEquals(1 + 1 + 1, strings.size());
        Assertions.assertEquals(HEADER_STR, strings.get(0));
        Assertions.assertEquals("kim,data1", strings.get(1));
        Assertions.assertEquals(FOOT_STR, strings.get(2));
    }

    @Test
    void flatFileItemWriterFormattedTest() throws Exception {
        String HEADER_STR = "HEADER";
        String FOOT_STR = "FOOTER";

        final File tempFile = File.createTempFile("pre", "suf");

        final FlatFileItemWriter<FileReaderItem> writer = new FlatFileItemWriterBuilder<FileReaderItem>()
                .saveState(false)
                .name("writerName")
                .formatted()
                .format("%s,%s|||||")
                .names("name", "data")
                .headerCallback(headerWriter -> headerWriter.write(HEADER_STR))
                .footerCallback(footerWriter -> footerWriter.write(FOOT_STR))
//                .lineAggregator(item -> {
//                    return new StringBuilder()
//                            .append(item.getName())
//                            .append(",")
//                            .append(item.getData()).toString();
//                })
                .resource(new FileSystemResource(tempFile))
                .build();
//        writer.setResource(new FileSystemResource(tempFile));
        ExecutionContext executionContext = new ExecutionContext();
        writer.open(executionContext);

        final List<FileReaderItem> itemList = Lists.newArrayList(FileReaderItem.builder().name("kim")
                                                                               .data("data1")
                                                                               .build());

        writer.write(itemList);

        writer.close();//footer

        final List<String> strings = Files.readAllLines(tempFile.toPath());
        System.out.println("str : " + strings);

        Assertions.assertEquals(1 + 1 + 1, strings.size());
        Assertions.assertEquals(HEADER_STR, strings.get(0));
        Assertions.assertEquals("kim,data1|||||", strings.get(1));
        Assertions.assertEquals(FOOT_STR, strings.get(2));
    }

}
