package com.meteor.batch.writer;

import java.io.File;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.json.JsonFileItemWriter;
import org.springframework.batch.item.json.builder.JsonFileItemWriterBuilder;
import org.springframework.core.io.FileSystemResource;

import com.meteor.batch.reader.vo.FileReaderItem;

public class JsonWriterTest {

    @Test
    void jsonFileItemWriterTest() throws Exception {
        final File tempFile = File.createTempFile("pre", "suf");

        final int ITEM_CNT = 3;

        final JsonFileItemWriter<FileReaderItem> writer = new JsonFileItemWriterBuilder<FileReaderItem>()
                .saveState(false)
                .name("writerName")
                .resource(new FileSystemResource(tempFile))
                .jsonObjectMarshaller(obj -> {
                    return obj.getName();//TODO json 형태로
                })
                .build();
        ExecutionContext executionContext = new ExecutionContext();
        writer.open(executionContext);

        final List<FileReaderItem> collect = IntStream.range(0, ITEM_CNT)
                                                      .mapToObj(n -> {
                                                          return FileReaderItem.builder().name(
                                                                                       "name" + n)
                                                                               .data("data" + n)
                                                                               .build();
                                                      }).collect(Collectors.toList());

        writer.write(collect);
        writer.close();
        final List<String> strings = Files.readAllLines(tempFile.toPath());

        for (String str : strings) {
            System.out.println(str);
        }
        System.out.println("======");

        System.out.println(strings.stream().collect(Collectors.joining("\n")));
        System.out.println("======");

        Assertions.assertEquals(
                strings.stream().collect(Collectors.joining("\n")),
                "[\n"
                + " name0,\n"
                + " name1,\n"
                + " name2\n"
                + "]");

        Assertions.assertEquals(ITEM_CNT + 2, strings.size());
    }

    //TODO, fileWriter인데 대용량 쓰는것도 문제 없는지(아마도 start에 [ 적고 가능한대로 json Writer해서 문제 없을것 같지만..)
}
