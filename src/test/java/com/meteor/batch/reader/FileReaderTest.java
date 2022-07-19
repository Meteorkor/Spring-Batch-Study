package com.meteor.batch.reader;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.batch.item.file.transform.Range;
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
        AtomicInteger skipCount = new AtomicInteger();

        final FlatFileItemReader<FileReaderItem> reader = new FlatFileItemReaderBuilder<FileReaderItem>()
                .saveState(false)
                .skippedLinesCallback(line -> {
                    skipCount.incrementAndGet();
                })
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
        Assertions.assertEquals(0, skipCount.get());
    }

    @Test
    void flatFileItemReader3LineSkipTest() throws Exception {
        String text = "kim,data\n" +
                      "lee,data2\n" +
                      "park,data3";
        AtomicInteger skipCount = new AtomicInteger();

        final FlatFileItemReader<FileReaderItem> reader = new FlatFileItemReaderBuilder<FileReaderItem>()
                .saveState(false)
                .linesToSkip(2)
                .skippedLinesCallback(line -> {
                    skipCount.incrementAndGet();
                })
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

//        {
//            final FileReaderItem read = reader.read();
//            Assertions.assertEquals("kim", read.getName());
//            Assertions.assertEquals("data", read.getData());
//        }
//
//        {
//            final FileReaderItem read = reader.read();
//            Assertions.assertEquals("lee", read.getName());
//            Assertions.assertEquals("data2", read.getData());
//        }
        {
            final FileReaderItem read = reader.read();
            Assertions.assertEquals("park", read.getName());
            Assertions.assertEquals("data3", read.getData());
        }
        {
            final FileReaderItem read = reader.read();
            Assertions.assertNull(read);
        }

        Assertions.assertEquals(2, skipCount.get());
    }

    @Test
    void flatFileItemReader3maxItemCountTest() throws Exception {
        String text = "kim,data\n" +
                      "lee,data2\n" +
                      "park,data3";
        AtomicInteger skipCount = new AtomicInteger();

        final FlatFileItemReader<FileReaderItem> reader = new FlatFileItemReaderBuilder<FileReaderItem>()
                .saveState(false)
                .maxItemCount(2)
                .skippedLinesCallback(line -> {
                    skipCount.incrementAndGet();
                })
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
            Assertions.assertNull(read);//.maxItemCount(2)
        }
        {
            final FileReaderItem read = reader.read();
            Assertions.assertNull(read);
        }

        Assertions.assertEquals(0, skipCount.get());
    }

    @Test
    void flatFileItemReaderDelTest() throws Exception {
        String text = "kim,data\n" +
                      "lee,data2\n" +
                      "park,data3";
        AtomicInteger skipCount = new AtomicInteger();
        final ByteArrayResource byteArrayResource = new ByteArrayResource(
                text.getBytes(StandardCharsets.UTF_8));

        final FlatFileItemReader<FileReaderItem> reader = new FlatFileItemReaderBuilder<FileReaderItem>()
                .delimited()
                .delimiter(",")
                .names("name", "data")
                .saveState(false)
                .maxItemCount(2)
                .skippedLinesCallback(line -> {
                    skipCount.incrementAndGet();
                })
                .resource(byteArrayResource)//Input resource must be set
                .fieldSetMapper((FieldSet fieldSet) -> {
                    return FileReaderItem.builder()
                                         .name(fieldSet.readString("name"))
                                         .data(fieldSet.readString("data"))
                                         .build();
                })
                .build();

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
            Assertions.assertNull(read);//.maxItemCount(2)
        }
        {
            final FileReaderItem read = reader.read();
            Assertions.assertNull(read);
        }

        Assertions.assertEquals(0, skipCount.get());
    }

    @Test
    void flatFileItemReaderFixedTest() throws Exception {
        String text = "kimdat1\n" +
                      "leedat2\n" +
                      "pardat3";
        AtomicInteger skipCount = new AtomicInteger();
        final ByteArrayResource byteArrayResource = new ByteArrayResource(
                text.getBytes(StandardCharsets.UTF_8));

        final FlatFileItemReader<FileReaderItem> reader = new FlatFileItemReaderBuilder<FileReaderItem>()
                .fixedLength()
                .columns(new Range(1, 3), new Range(4, 7))
                .names("name", "data")
                .saveState(false)
                .maxItemCount(2)
                .skippedLinesCallback(line -> {
                    skipCount.incrementAndGet();
                })
                .resource(byteArrayResource)//Input resource must be set
                .fieldSetMapper((FieldSet fieldSet) -> {
                    return FileReaderItem.builder()
                                         .name(fieldSet.readString("name"))
                                         .data(fieldSet.readString("data"))
                                         .build();
                })
                .build();

        ExecutionContext executionContext = new ExecutionContext();
        reader.open(executionContext);

        {
            final FileReaderItem read = reader.read();
            Assertions.assertEquals("kim", read.getName());
            Assertions.assertEquals("dat1", read.getData());
        }

        {
            final FileReaderItem read = reader.read();
            Assertions.assertEquals("lee", read.getName());
            Assertions.assertEquals("dat2", read.getData());
        }
        {
            final FileReaderItem read = reader.read();
            Assertions.assertNull(read);//.maxItemCount(2)
        }
        {
            final FileReaderItem read = reader.read();
            Assertions.assertNull(read);
        }

        Assertions.assertEquals(0, skipCount.get());
    }

}
