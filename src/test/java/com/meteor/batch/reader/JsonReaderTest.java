package com.meteor.batch.reader;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.JsonItemReader;
import org.springframework.batch.item.json.builder.JsonItemReaderBuilder;
import org.springframework.core.io.ByteArrayResource;

import com.meteor.batch.reader.vo.Emp;

public class JsonReaderTest {

    @Test
    void mustJsonArray() throws Exception {
        final String text = "{\"empno\": 0, \"ename\": \"kim\"}";

        final JsonItemReader<Emp> jsonItemReader = new JsonItemReaderBuilder<Emp>()
                .name("jsonItemReader")
                .jsonObjectReader(new JacksonJsonObjectReader<>(Emp.class))
                .resource(new ByteArrayResource(text.getBytes(StandardCharsets.UTF_8)))
                .build();

        Assertions.assertThrows(IllegalStateException.class, () -> {
            jsonItemReader.open(new ExecutionContext());
        });
        final Emp read = jsonItemReader.read();
    }

    @Test
    void jsonItemReaderRead() throws Exception {
        final String text = "[{\"empno\": 0, \"ename\": \"kim\"}, {\"empno\": 1, \"ename\": \"lee\"}]";

        final JsonItemReader<Emp> jsonItemReader = new JsonItemReaderBuilder<Emp>()
                .name("jsonItemReader")
                .jsonObjectReader(new JacksonJsonObjectReader<>(Emp.class))
                .resource(new ByteArrayResource(text.getBytes(StandardCharsets.UTF_8)))
                .build();

        jsonItemReader.open(new ExecutionContext());
        {
            //need to no args constructor
            final Emp read = jsonItemReader.read();
            Assertions.assertEquals(0, read.getEmpno());
            Assertions.assertEquals("kim", read.getEname());
        }
        {
            final Emp read = jsonItemReader.read();
            Assertions.assertEquals(1, read.getEmpno());
            Assertions.assertEquals("lee", read.getEname());
        }
        {
            final Emp read = jsonItemReader.read();
            Assertions.assertNull(read);
        }
    }

}
