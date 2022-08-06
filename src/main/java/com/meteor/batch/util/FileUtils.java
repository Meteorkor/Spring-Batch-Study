package com.meteor.batch.util;

import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Singular;
import lombok.SneakyThrows;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FileUtils {

    @Builder
    public static class FileReadProcess {
        private final Path readPath;
        private final Consumer<String> lineProcess;

        @SneakyThrows
        public void lineToProcess() {
            try (final Stream<String> fileReadLineStream = Files.lines(readPath)) {
                fileReadLineStream.forEach(lineProcess);
            }
        }
    }

    @Builder
    public static class FileWriteProcess {
        private final Path writePath;
        @Singular
        private final List<OpenOption> options;
        private final String itemSeparator;
        private final Supplier<String> itemSupplier;

        @SneakyThrows
        public void itemWriteProcess() {

            try (final BufferedWriter bufferedWriter = Files.newBufferedWriter(writePath,
                                                                               options.toArray(
                                                                                       new OpenOption[0]))) {
                String item;
                boolean firstItem = true;
                while ((item = itemSupplier.get()) != null) {
                    if (!firstItem) {
                        bufferedWriter.write(itemSeparator);
                    } else {
                        firstItem = false;
                    }
                    bufferedWriter.write(item);
                }
            }
        }

    }

}
