package com.meteor.batch.job.retry;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Configuration
public class RetryJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final RetryReader retryReader;
    private final RetryWriter retryWriter;
    private final RetryJobParameter retryJobParameter;

    public static final String STEP1_CHECK_KEY = "step1_value";
    public static final String STEP2_CHECK_KEY = "step2_value";
    public static final String TEST_STEP1 = "retryStep1";
    public static final String PRE_STEP = "preStep";

    public static final String END_EXCLUSIVE = "END_EXCLUSIVE";
    public static final String FAIL_CNT = "FAIL_CNT";
    public static final String SUM = "SUM";
    public static final String READ_CNT = "READ_CNT";

    @Bean
    public Job retryJob() {
        return jobBuilderFactory.get(RetryJobTestEnvConfig.RETRY_JOB)
                                .start(preStep())
                                .next(retryStep1())
//                                .next(retryStep2())
//                                .preventRestart()
                                .build();
    }

    @Bean
    public Step retryStep1() {
        return stepBuilderFactory.get(TEST_STEP1)
                                 .<String, String>chunk(10)
                                 .reader(retryReader)
                                 .writer(retryWriter)
                                 .build();
    }

    @Bean
    public Step preStep() {
        return stepBuilderFactory.get(PRE_STEP)
                                 .tasklet((stepContribution, chunkContext) -> {
                                     stepContribution.getStepExecution()
                                                     .getExecutionContext()
                                                     .putLong(FAIL_CNT, 0L);
                                     return RepeatStatus.FINISHED;
                                 })
                                 .build();
    }

    //TODO step1 완료 후 재수행시 step2 부터 시작되는 시나리오도 추가
//    @Bean
//    public Step retryStep2() {
//        return stepBuilderFactory.get(TEST_STEP2).tasklet(
//                (StepContribution stepContribution, ChunkContext chunkContext) -> {
//                    JobParameters jobParameters = stepContribution.getStepExecution().getJobParameters();
//                    log.info("jobParameters.toString() : {}", jobParameters);
//
//                    chunkContext.getStepContext().getStepExecution().getJobExecution()
//                                .getExecutionContext().put(STEP2_CHECK_KEY, jobParameters.getString("value"));
//
//                    return RepeatStatus.FINISHED;
//                }).build();
//    }

    @Component
    @Getter
    @JobScope
    @NoArgsConstructor
    private static class RetryJobParameter {

        @Value("#{jobParameters['" + FAIL_CNT + "'] ?: " + -1 + '}')
        private Long failCnt;

        @Value("#{jobParameters['" + END_EXCLUSIVE + "'] ?: " + 1000 + '}')
        private Long endExclusive;

    }

}
