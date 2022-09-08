package com.meteor.batch.job.parallel;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

import com.meteor.batch.job.retry.RetryJobTestEnvConfig;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class MultiThreadedStepJobConfig {
    public static final String JOB_NAME = "multiThreadedStepJob";
    public static final String STEP1_NAME = "multiThreadedStepJobStep1";

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final MultiThreadedStepJobConfig.JobParameter jobParameter;

    @Bean
    public Job multiThreadedStepJob() {
        return jobBuilderFactory.get(RetryJobTestEnvConfig.RETRY_JOB)
                                .start(multiThreadedStepJobStep1())
                                .build();
    }

    @Bean
    public Step multiThreadedStepJobStep1() {
        return stepBuilderFactory.get(STEP1_NAME)
                                 .tasklet(((stepContribution, chunkContext) -> {
                                     return RepeatStatus.FINISHED;
                                 }))
                                 .build();
    }

    public TaskExecutor taskExecutor() {
        return new SimpleAsyncTaskExecutor();
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
