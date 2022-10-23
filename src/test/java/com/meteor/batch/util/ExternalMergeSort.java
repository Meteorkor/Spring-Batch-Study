package com.meteor.batch.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;

import lombok.Builder;
import lombok.SneakyThrows;

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

    @SneakyThrows
    public File sort() throws FileNotFoundException {

        //elemetLoadMaxCnt 만큼 로드해서 정렬 후 File에 임시 파일에 Write
        //모든 파일에 각각 다 쓴 후 Merge 진행

        final List<FileIterator> fileIterators = divideSortedTempFile().stream().map(
                file -> new FileIterator(file, lineToObjMapper)).collect(Collectors.toList()
        );

        final File tempFile = File.createTempFile("sorted", "suffix");

        TreeSet<FileIterator> treeSet = new TreeSet<>((fileIterator1, fileIterator2) -> {
            return comparator.compare(fileIterator1.peek(), fileIterator2.peek());
        });
        treeSet.addAll(fileIterators);

        try (final BufferedWriter bufferedWriter = Files.newBufferedWriter(tempFile.toPath())) {
            while (!fileIterators.isEmpty()) {
                final FileIterator tFileIterator = treeSet.pollFirst();

                final T next = tFileIterator.next();
                treeSet.add(tFileIterator);
//                treeSet.addAll(fileIterators);

                if (next == null) {
                    fileIterators.remove(tFileIterator);
                    treeSet.remove(tFileIterator);
                } else {
                    bufferedWriter.write(objToLineMapper.apply(next));
                    bufferedWriter.newLine();
                }
            }
        }

        return tempFile;
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
                    sortAndWrite(tempFile, lines);
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
                sortAndWrite(tempFile, lines);
                fileList.add(tempFile);
            }

        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        return fileList;
    }

    private void sortAndWrite(File tempFile, List<T> lines) throws IOException {
        Files.write(tempFile.toPath(),
                    lines.stream().sorted(comparator)
                         .map(objToLineMapper::apply)
                         .collect(Collectors.toList()),
                    StandardOpenOption.APPEND);
    }

    class FileIterator implements Iterator<T> {
        private final File targetFile;
        private final BufferedReader bufferedReader;
        private final Function<String, T> lineToObjMapper;
        private boolean isClosed = false;

        private T value;

        @SneakyThrows
        public FileIterator(File targetFile, Function<String, T> lineToObjMapper) {
            this.targetFile = targetFile;
            this.bufferedReader = Files.newBufferedReader(targetFile.toPath());
            this.lineToObjMapper = lineToObjMapper;
        }

        public T peek() {
            if (hasNext()) {return this.value;}

            return null;
        }

        @SneakyThrows
        @Override
        public boolean hasNext() {
            if (this.value == null && !isClosed) {
                String tempStr = bufferedReader.readLine();
                if (tempStr != null) {
                    this.value = lineToObjMapper.apply(tempStr);
                } else {
                    isClosed = true;
                    bufferedReader.close();
                }
            }

            return this.value != null;
        }

        @Override
        public T next() {
            T result = peek();
            this.value = null;
            return result;
        }
    }

}
