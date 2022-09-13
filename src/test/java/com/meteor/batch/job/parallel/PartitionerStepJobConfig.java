package com.meteor.batch.job.parallel;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class PartitionerStepJobConfig {
    public static final String CALL_CNT_KEY = "CALL_CNT_KEY";

    public static final String JOB_NAME = "partitionerStepJob";

    public static final String STEP1_NAME = "partitionerStepJobStep1";
    public static final String STEP1_INNER = STEP1_NAME + "_inner";
    public static final String STEP1_CONTEXT_1_NAME = "context1Name";

    public static final String STEP2_NAME = "partitionerStepJobStep2";
    public static final String STEP2_INNER = STEP2_NAME + "_inner";
    public static final int STEP2_CNT = 10;

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final PartitionerStepJobConfig.JobParameter jobParameter;

    @Bean
    public Job partitionerStepJob() {
        return jobBuilderFactory.get(JOB_NAME)
                                .start(partitionerStepJobStep1())
                                .next(partitionerStepJobStep2())
                                .build();
    }

    @Bean
    public Step partitionerStepJobStep1() {
        return stepBuilderFactory.get(STEP1_NAME)
                                 .partitioner(STEP1_NAME + "Partitioner", step1Partitioner())
                                 .step(partitionerStepJobStep1Inner())
                                 .build();
    }

    @Bean
    public Step partitionerStepJobStep1Inner() {
        return stepBuilderFactory.get(STEP1_INNER)
                                 .tasklet((stepContribution, chunkContext) -> {
                                     final ExecutionContext executionContext =
                                             stepContribution.getStepExecution()
                                                             .getExecutionContext();

                                     executionContext.putInt(CALL_CNT_KEY,
                                                             executionContext.getInt(CALL_CNT_KEY, 0) + 1);

                                     return RepeatStatus.FINISHED;
                                 }).build();
    }

    public Partitioner step1Partitioner() {
        return gridSize -> {
            Map<String, ExecutionContext> map = new HashMap<>();
            map.put(STEP1_CONTEXT_1_NAME, new ExecutionContext());
            return map;
        };
    }

    @Bean
    public Step partitionerStepJobStep2() {
        return stepBuilderFactory.get(STEP2_NAME)
                                 .partitioner(STEP2_NAME + "Partitioner", step2Partitioner())
                                 .step(partitionerStepJobStep2Inner())
                                 .build();
    }

    @Bean
    public Step partitionerStepJobStep2Inner() {
        return stepBuilderFactory.get(STEP2_INNER)
                                 .tasklet((stepContribution, chunkContext) -> {
                                     final ExecutionContext executionContext =
                                             stepContribution.getStepExecution()
                                                             .getExecutionContext();

                                     executionContext.putInt(CALL_CNT_KEY,
                                                             executionContext.getInt(CALL_CNT_KEY, 0) + 1);

                                     return RepeatStatus.FINISHED;
                                 }).build();
    }

    public Partitioner step2Partitioner() {
        return gridSize -> {
            Map<String, ExecutionContext> map = new HashMap<>();
            IntStream.range(0, STEP2_CNT).forEach(n ->
                                                          map.put("context_" + n, new ExecutionContext())
            );
            return map;
        };
    }

    @Component
    @Getter
    @JobScope
    @NoArgsConstructor
    private static class JobParameter {

//        @Value("#{jobParameters['" + FAIL_CNT + "'] ?: " + -1 + '}')
//        private Long failCnt;
//
//        @Value("#{jobParameters['" + END_EXCLUSIVE + "'] ?: " + 1000 + '}')
//        private Long endExclusive;

    }
}
