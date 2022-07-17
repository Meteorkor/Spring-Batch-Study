package com.meteor.batch.reader;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.core.io.ByteArrayResource;

import com.meteor.batch.reader.vo.FileReaderItem;

@SpringBatchTest
public class FileReaderTest {

    @Test
    void flatFileItemReaderTest() throws Exception {
        String text = "kim,data";
        //saveState
//        Indicates whether or not the reader’s state should be saved in the ExecutionContext provided by ItemStream#update(ExecutionContext). The default is true.
        //재처리할때 활용

        final FlatFileItemReader<FileReaderItem> reader = new FlatFileItemReaderBuilder<FileReaderItem>()
                .saveState(false)
                .lineMapper((line, lineNumber) -> {
                    final String[] splitStr = line.split(",");
                    return FileReaderItem.builder()
                                         .name(splitStr[0])
                                         .data(splitStr[1])
                                         .build();
                })
                .build();

        final ByteArrayResource byteArrayResource = new ByteArrayResource(
                text.getBytes(StandardCharsets.UTF_8));

        reader.setResource(byteArrayResource);
        ExecutionContext executionContext = new ExecutionContext();
        reader.open(executionContext);

        final FileReaderItem read = reader.read();
        Assertions.assertEquals("kim", read.getName());
        Assertions.assertEquals("data", read.getData());
        final FileReaderItem secondRead = reader.read();
        Assertions.assertNull(secondRead);
    }

    @Test
    void flatFileItemReaderEmptyLineTest() throws Exception {
        String text = "";

        final FlatFileItemReader<FileReaderItem> reader = new FlatFileItemReaderBuilder<FileReaderItem>()
                .saveState(false)
                .lineMapper((line, lineNumber) -> {
                    final String[] splitStr = line.split(",");
                    return FileReaderItem.builder()
                                         .name(splitStr[0])
                                         .data(splitStr[1])
                                         .build();
                })
                .build();

        final ByteArrayResource byteArrayResource = new ByteArrayResource(
                text.getBytes(StandardCharsets.UTF_8));

        reader.setResource(byteArrayResource);
        ExecutionContext executionContext = new ExecutionContext();
        reader.open(executionContext);

        final FileReaderItem read = reader.read();
        Assertions.assertNull(read);
    }

    @Test
    void flatFileItemReader3LineTest() throws Exception {
        String text = "kim,data\n" +
                      "lee,data2\n" +
                      "park,data3";

        final FlatFileItemReader<FileReaderItem> reader = new FlatFileItemReaderBuilder<FileReaderItem>()
                .saveState(false)
                .lineMapper((line, lineNumber) -> {
                    final String[] splitStr = line.split(",");
                    return FileReaderItem.builder()
                                         .name(splitStr[0])
                                         .data(splitStr[1])
                                         .build();
                })
                .build();

        final ByteArrayResource byteArrayResource = new ByteArrayResource(
                text.getBytes(StandardCharsets.UTF_8));

        reader.setResource(byteArrayResource);
        ExecutionContext executionContext = new ExecutionContext();
        reader.open(executionContext);

        {
            final FileReaderItem read = reader.read();
            Assertions.assertEquals("kim", read.getName());
            Assertions.assertEquals("data", read.getData());
        }

        {
            final FileReaderItem read = reader.read();
            Assertions.assertEquals("lee", read.getName());
            Assertions.assertEquals("data2", read.getData());
        }
        {
            final FileReaderItem read = reader.read();
            Assertions.assertEquals("park", read.getName());
            Assertions.assertEquals("data3", read.getData());
        }
        {
            final FileReaderItem read = reader.read();
            Assertions.assertNull(read);
        }
    }

//    @Test
//    void flatFileItemReaderTest() throws Exception {
//        String text = "kim,data";
//        //saveState
////        Indicates whether or not the reader’s state should be saved in the ExecutionContext provided by ItemStream#update(ExecutionContext). The default is true.
//        //재처리할때 활용
//
//        final FlatFileItemReader<FileReaderItem> reader = new FlatFileItemReaderBuilder<FileReaderItem>()
//                .saveState(false)
//                .lineMapper((line, lineNumber) -> {
//                    final String[] splitStr = line.split(",");
//                    return FileReaderItem.builder()
//                                         .name(splitStr[0])
//                                         .data(splitStr[1])
//                                         .build();
//                })
//                .build();
//
//        final ByteArrayResource byteArrayResource = new ByteArrayResource(
//                text.getBytes(StandardCharsets.UTF_8));
//
//        reader.setResource(byteArrayResource);
//        ExecutionContext executionContext = new ExecutionContext();
//        reader.open(executionContext);
//
//        final FileReaderItem read = reader.read();
//        Assertions.assertEquals("kim", read.getName());
//        Assertions.assertEquals("data", read.getData());
//    }

//    @Test
//    void multiResourceItemReaderBuilderTest() {
//        MultiResourceItemReaderBuilder
//                FlatFileItemReader
//        FlatFileItemReaderBuilder
//    }
}