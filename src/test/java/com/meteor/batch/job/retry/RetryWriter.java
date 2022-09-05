package com.meteor.batch.job.retry;

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@StepScope
public class RetryWriter implements ItemStreamWriter<String> {
    @Value("#{jobParameters['" + RetryJobConfig.FAIL_CNT + "'] ?: " + -1 + '}')
    private Long failCnt;
    private StepExecution stepExecution;
    private Long sum = 0L;
    private boolean isRestart = false;

    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        this.stepExecution = stepExecution;
    }

    @Override
    public void write(List<? extends String> items) throws Exception {
        final long aLong = stepExecution.getExecutionContext().getLong(RetryJobConfig.READ_CNT, 0L);


        this.sum += items.stream().map(Long::valueOf).reduce(0L, (n1, n2) -> n1 + n2);

//        System.out.println("aLong : " + aLong + ", failCnt : " + failCnt);
        if (!isRestart && failCnt != -1L && aLong == failCnt) {
            System.out.println("loss sum : " + sum);
            throw new IndexOutOfBoundsException("exceedCnt");
        }
    }

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        this.isRestart = executionContext.getLong(RetryJobConfig.READ_CNT, 0L) != 0L;
        this.sum = executionContext.getLong(RetryJobConfig.SUM, 0L);

    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
        executionContext.put(RetryJobConfig.SUM, sum);
    }

    @Override
    public void close() throws ItemStreamException {
    }

}
