package com.meteor.batch.job.retry;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Component
@Slf4j
public class RetryReader implements ItemStreamReader<String> {
    private static final String READ_CNT = "READ_CNT";
    private List<String> dataList;
    private long readCnt;
    private Iterator<String> iter;

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        //select data
        //TODO 데이터 가변
        dataList = IntStream.range(0, 100).mapToObj(String::valueOf).collect(Collectors.toList());

        final long updateCnt = executionContext.getLong(READ_CNT, 0L);
        log.info("updateCnt: {}", updateCnt);

        this.iter = dataList.stream().skip(updateCnt).collect(Collectors.toList()).iterator();
    }

    @Override
    public String read() {
        if (this.iter.hasNext()) {
            readCnt++;
            return this.iter.next();
        }
        return null;
    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
        executionContext.putLong(READ_CNT, readCnt);
    }

    @Override
    public void close() throws ItemStreamException {

    }
}
