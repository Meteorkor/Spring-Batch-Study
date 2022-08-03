package com.meteor.batch.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class TestJobConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    public static final String STEP1_CHECK_KEY = "step1_value";
    public static final String STEP2_CHECK_KEY = "step2_value";
    public static final String TEST_STEP1 = "testStep1";
    public static final String TEST_STEP2 = "testStep2";

    @Bean
    public Job testJob() {
        return jobBuilderFactory.get("testJob")
                                .start(testStep1())
                                .next(testStep2())
                                .preventRestart()
                                .build();
    }

    @Bean
    public Step testStep1() {

        return stepBuilderFactory.get(TEST_STEP1).tasklet(
                (StepContribution stepContribution, ChunkContext chunkContext) -> {
                    JobParameters jobParameters = stepContribution.getStepExecution().getJobParameters();
                    log.info("jobParameters.toString() : {}", jobParameters.toString());

                    chunkContext.getStepContext().getStepExecution().getJobExecution()
                                .getExecutionContext().put(STEP1_CHECK_KEY, jobParameters.getString("value"));
                    return RepeatStatus.FINISHED;
                }).build();
    }

    @Bean
    public Step testStep2() {
        return stepBuilderFactory.get(TEST_STEP2).tasklet(
                (StepContribution stepContribution, ChunkContext chunkContext) -> {
                    JobParameters jobParameters = stepContribution.getStepExecution().getJobParameters();
                    log.info("jobParameters.toString() : {}", jobParameters.toString());

                    chunkContext.getStepContext().getStepExecution().getJobExecution()
                                .getExecutionContext().put(STEP2_CHECK_KEY, jobParameters.getString("value"));

                    return RepeatStatus.FINISHED;
                }).build();
    }

}
