package com.meteor.batch.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import lombok.Builder;

@Builder
public class ExternalMergeSort<T> {
    /**
     * elementLoadMaxCnt
     */
    private final long elementLoadMaxCnt;
    private final File targetFile;
    private final Function<String, T> lineToObjMapper;
    private final Function<T, String> objToLineMapper;
    private final Comparator<T> comparator;

    public File sort() throws FileNotFoundException {

        //elemetLoadMaxCnt 만큼 로드해서 정렬 후 File에 임시 파일에 Write
        //모든 파일에 각각 다 쓴 후 Merge 진행

        //TODO 작업예정
        return null;
    }

    private List<File> divideSortedTempFile() {
        List<File> fileList = new ArrayList<>();

        try (final BufferedReader bufferedReader = Files.newBufferedReader(targetFile.toPath())) {
            long readLineCnt = 0L;
            File tempFile = File.createTempFile("externalMerge", "suffix");
            tempFile.deleteOnExit();
            String readLineStr;
            List<T> lines = new ArrayList<>();

            while ((readLineStr = bufferedReader.readLine()) != null) {
                if (readLineCnt >= elementLoadMaxCnt) {
                    flush(tempFile, lines);
                    fileList.add(tempFile);

                    //init
                    readLineCnt = 0L;
                    lines = new ArrayList<>();

                    tempFile = File.createTempFile("externalMerge", "suffix");
                    tempFile.deleteOnExit();
                }

                lines.add(lineToObjMapper.apply(readLineStr));
                readLineCnt++;
            }

            if (!lines.isEmpty()) {
                flush(tempFile, lines);
                fileList.add(tempFile);
            }

        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        return fileList;
    }

    private void flush(File tempFile, List<T> lines) throws IOException {
        Files.write(tempFile.toPath(),
                    lines.stream().sorted(comparator)
                         .map(objToLineMapper::apply)
                         .collect(Collectors.toList()),
                    StandardOpenOption.APPEND);
    }

}
