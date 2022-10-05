package com.meteor.batch.parser;

import org.junit.jupiter.api.Test;

public class JavaFinderTest {
    @Test
    void find() {
        final JavaFinder javaFinder = JavaFinder.INSTANCE;
        javaFinder.findJava("/Users/user/dev/git/study/Spring-Batch-Study/src/test/java/com/meteor/batch/parser");
    }
}
