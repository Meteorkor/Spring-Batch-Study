package com.meteor.batch.writer;

import com.meteor.batch.common.AlphabetConst;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Component
@StepScope
@RequiredArgsConstructor
public class AlphabetChunkWriter implements ItemWriter<Map<String, Long>> {
    private JobExecution jobExecution;

    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        this.jobExecution = stepExecution.getJobExecution();
    }

    @Override
    public void write(List<? extends Map<String, Long>> list) {
        ExecutionContext jobExecutionContext = jobExecution.getExecutionContext();
        Map<String, Long> collect = list.stream().flatMap(map -> map.entrySet().stream()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, Long::sum));

        Object old = jobExecutionContext.get(AlphabetConst.KEY);
        if (old != null) {
            if (old instanceof Map) {
                Map<String, Long> oldMap = (Map<String, Long>) old;
                oldMap.forEach((k, v) -> collect.merge(k, v, Long::sum));
            }
        }

        jobExecutionContext.put(AlphabetConst.KEY, new TreeMap<>(collect));
    }
}