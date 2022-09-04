package com.meteor.batch.job.retry;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
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
    private final RetryJobParameter retryJobParameter;

    public static final String STEP1_CHECK_KEY = "step1_value";
    public static final String STEP2_CHECK_KEY = "step2_value";
    public static final String TEST_STEP1 = "retryStep1";
    public static final String TEST_STEP2 = "retryStep2";

    public static final String FAIL_CNT = "FAIL_CNT";

    @Bean
    public Job retryJob() {
        return jobBuilderFactory.get(RetryJobTestEnvConfig.RETRY_JOB)
                                .start(retryStep1())
//                                .next(retryStep2())
//                                .preventRestart()
                                .build();
    }

//    @Bean
//    public Step retryStep1() {
//        return stepBuilderFactory.get(TEST_STEP1).tasklet(
//                (StepContribution stepContribution, ChunkContext chunkContext) -> {
//                    JobParameters jobParameters = stepContribution.getStepExecution().getJobParameters();
//                    log.info("jobParameters.toString() : {}", jobParameters);
//
//                    chunkContext.getStepContext().getStepExecution().getJobExecution()
//                                .getExecutionContext().put(STEP1_CHECK_KEY, jobParameters.getString("value"));
//                    return RepeatStatus.FINISHED;
//                }).build();
//    }

    @Bean
    public Step retryStep1() {
        AtomicInteger atomicInteger = new AtomicInteger();
        return stepBuilderFactory.get(TEST_STEP1)
                                 .chunk(10)
                                 .reader(retryReader)
                                 .writer(list -> {

                                     atomicInteger.addAndGet(list.size());

                                     if (retryJobParameter.getFailCnt() != -1
                                         && atomicInteger.get() >= retryJobParameter.getFailCnt()) {
                                         throw new IndexOutOfBoundsException("exceedCnt");
                                     }

                                     System.out.println("list : " + list);
                                 }).build();
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

    }

}
