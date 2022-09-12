package com.meteor.batch.job.parallel;

import java.util.HashMap;
import java.util.Map;

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
    public static final String JOB_NAME = "partitionerStepJob";
    public static final String STEP1_NAME = "partitionerStepJobStep1";
    public static final String STEP1_CALL_CNT = "STEP1_CALL_CNT";

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final PartitionerStepJobConfig.JobParameter jobParameter;

    @Bean
    public Job partitionerStepJob() {
        return jobBuilderFactory.get(JOB_NAME)
                                .start(partitionerStepJobStep1())
                                .build();
    }

    @Bean
    public Step partitionerStepJobStep1() {
        return stepBuilderFactory.get(STEP1_NAME)
                                 .partitioner(STEP1_NAME + "Partitioner", partitioner())
                                 .step(partitionerStepJobStep1Inner())
                                 .build();
    }

    @Bean
    public Step partitionerStepJobStep1Inner() {
        return stepBuilderFactory.get(STEP1_NAME + "_inner")
                                 .tasklet((stepContribution, chunkContext) -> {
                                     final ExecutionContext executionContext =
                                             stepContribution.getStepExecution()
                                                             .getExecutionContext();

                                     //not parallel
                                     //TODO how to parallel context put
                                     executionContext.putInt(STEP1_CALL_CNT,
                                                             executionContext.getInt(STEP1_CALL_CNT, 0) + 1);

                                     return RepeatStatus.FINISHED;
                                 }).build();
    }

    public Partitioner partitioner() {
        return gridSize -> {
            Map<String, ExecutionContext> map = new HashMap<>();
            map.put("test_1", new ExecutionContext());
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
