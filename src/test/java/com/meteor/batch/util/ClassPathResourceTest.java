package com.meteor.batch.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.logging.log4j.util.Strings;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

public class ClassPathResourceTest {

    @Test
    void classResourceReadFile() {
        final ClassPathResource classPathResource = new ClassPathResource("emp1.json");

        try (final Stream<String> lines = new BufferedReader(
                new InputStreamReader(classPathResource.getInputStream())).lines()) {
            final String json = lines.collect(Collectors.joining(Strings.LINE_SEPARATOR));
            Assertions.assertEquals("{\n"
                                    + "  \"empno\": 1,\n"
                                    + "  \"ename\": \"kim\"\n"
                                    + "}", json);

        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    @Test
    void classResourceGetFileFailTest() throws IOException {
        final ClassPathResource classPathResource = new ClassPathResource("emp1.json");
        //ClassPathResource에 대해서는 getFile을 사용하지 말것(test의 resources에서는 정상동작함)
        //java.io.FileNotFoundException: class path resource [emp1.json] cannot be resolved to URL because it does not exist
        Assertions.assertThrows(FileNotFoundException.class, () -> {
            classPathResource.getFile();
        });
    }

}
