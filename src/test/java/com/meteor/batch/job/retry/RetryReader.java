package com.meteor.batch.job.retry;

import java.util.Iterator;
import java.util.List;

import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamReader;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RetryReader implements ItemStreamReader<String> {
    private final List<String> dataList;
    private long readCnt;
    private Iterator<String> iter;

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        this.iter = dataList.iterator();
    }

    @Override
    public String read() {
        if (this.iter.hasNext()) {
            return this.iter.next();
        }
        return null;
    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
        executionContext.putLong("READ_CNT", readCnt);
    }

    @Override
    public void close() throws ItemStreamException {

    }
}
