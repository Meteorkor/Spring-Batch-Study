package com.meteor.batch.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileUtils {

    public static void lineToProcess(Path readPath, Consumer<String> lineProcess) throws IOException {
        Files.lines(readPath)
             .forEach(lineProcess);
    }

}
