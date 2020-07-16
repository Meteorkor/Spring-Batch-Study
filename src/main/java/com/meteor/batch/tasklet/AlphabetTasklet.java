package com.meteor.batch.tasklet;

import com.meteor.batch.common.AlphabetConst;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class AlphabetTasklet implements Tasklet {
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
        String string = contribution.getStepExecution().getJobParameters().getString(AlphabetConst.KEY);

        Map<String, Long> map =
                Stream.of(Objects.requireNonNull(string).split(","))
                        .flatMapToInt(String::chars)
                        .mapToObj(integer -> String.valueOf((char) integer))
                        .collect(Collectors.toMap(s -> s, s -> 1L, Long::sum));

        contribution.getStepExecution().getJobExecution().getExecutionContext().put(AlphabetConst.KEY, new TreeMap<>(map));

        return RepeatStatus.FINISHED;
    }
}