package com.meteor.batch.job.parallel;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.boot.task.TaskExecutorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class MultiThreadedStepJobConfig {
    public static final String JOB_NAME = "multiThreadedStepJob";
    public static final String STEP1_NAME = "multiThreadedStepJobStep1";
    public static final int THREAD_CNT = 2;

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final MultiThreadedStepJobConfig.JobParameter jobParameter;

    @Bean
    public Job multiThreadedStepJob() {
        return jobBuilderFactory.get(JOB_NAME)
                                .start(multiThreadedStepJobStep1())
                                .build();
    }

    @Bean
    public Step multiThreadedStepJobStep1() {
        return stepBuilderFactory.get(STEP1_NAME)
                                 .tasklet(((stepContribution, chunkContext) -> {
                                     stepContribution.getStepExecution()
                                                     .getJobExecution()
                                                     .getExecutionContext()
                                                     .put(Thread.currentThread().getName(), Boolean.TRUE);

                                     return RepeatStatus.FINISHED;
                                 }))
                                 .taskExecutor(taskExecutor())

                                 .build();
    }

    public TaskExecutor taskExecutor() {
        final ThreadPoolTaskExecutor build = new TaskExecutorBuilder()
                .corePoolSize(THREAD_CNT)
                .maxPoolSize(THREAD_CNT)
                .build();
        build.afterPropertiesSet();
        return build;
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
