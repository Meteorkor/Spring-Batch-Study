package com.meteor.batch.job.retry;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Component
@Slf4j
@StepScope
public class RetryReader implements ItemStreamReader<String> {

    @Value("#{jobParameters['" + RetryJobConfig.END_EXCLUSIVE + "'] ?: " + 1000 + '}')
    private Long endExclusive;

    private List<String> dataList;
    private long readCnt;
    private Iterator<String> iter;


    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        //select data
        dataList = IntStream.range(0, endExclusive.intValue()).mapToObj(String::valueOf).collect(Collectors.toList());

        final long loadSum = executionContext.getLong(RetryJobConfig.SUM, 0L);
        log.info("loadSum: {}", loadSum);

        final long updateCnt = executionContext.getLong(RetryJobConfig.READ_CNT, 0L);
        this.readCnt = updateCnt;
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
        executionContext.putLong(RetryJobConfig.READ_CNT, this.readCnt);
    }

    @Override
    public void close() throws ItemStreamException {

    }
}
