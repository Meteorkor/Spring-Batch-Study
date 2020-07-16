package com.meteor.batch.config.job;

import com.meteor.batch.tasklet.AlphabetTasklet;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class AlphabetTaskletCountingJobConfig {
    private final JobBuilderFactory jobBuilder;
    private final StepBuilderFactory stepBuilder;
    private final ApplicationContext applicationContext;
    public final static String JOB_NAME = "AlphabetTaskletCounting";
    public final static String JOB_ID = JOB_NAME + "JOB";
    public final static String STEP_ID = JOB_NAME + "STEP";


    @Bean(JOB_ID)
    public Job job() {

        return jobBuilder.get(JOB_ID).start(step()).build();
    }

    @Bean(STEP_ID)
    public Step step() {
        AlphabetTasklet tasklet = applicationContext.getBean(AlphabetTasklet.class);
        return
                stepBuilder.get(STEP_ID)
                        .tasklet(tasklet)
                        .build();
    }
}