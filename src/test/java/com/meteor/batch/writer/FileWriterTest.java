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

}
