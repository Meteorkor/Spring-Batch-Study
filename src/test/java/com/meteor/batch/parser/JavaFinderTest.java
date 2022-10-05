package com.meteor.batch.parser;

import java.util.List;

import org.junit.jupiter.api.Test;

public class JavaFinderTest {

    @Test
    void simpleFindTest() {
        final JavaFinder javaFinder = JavaFinder.INSTANCE;
        final List<String> java = javaFinder.findJava(
                "/Users/user/dev/git/study/Spring-Batch-Study/src/test/java/com/meteor/batch/parser");

        java.forEach(file -> {
            System.out.println("=========");
            System.out.println(file);
            System.out.println("=========");
        });
    }

    @Test
    void parentFolderFindTest() {
        final JavaFinder javaFinder = JavaFinder.INSTANCE;
        final List<String> java = javaFinder.findJava(
                "path"
//                "path"
        );

        java.forEach(file -> {
            System.out.println("=========");
            System.out.println(file);
            System.out.println("=========");
        });
    }

}
