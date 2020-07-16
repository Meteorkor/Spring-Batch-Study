package com.meteor.batch.config.job;

import com.meteor.batch.processor.LineToAlphabetProcessor;
import com.meteor.batch.reader.AlphabetChunkReader;
import com.meteor.batch.writer.AlphabetChunkWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class AlphabetChunkCountingJobConfig {
    private final JobBuilderFactory jobBuilder;
    private final StepBuilderFactory stepBuilder;
    private final ApplicationContext applicationContext;
    public final static String JOB_NAME = "AlphabetChunkCounting";
    public final static String JOB_ID = JOB_NAME + "JOB";
    public final static String STEP_ID = JOB_NAME + "STEP";


    @Bean(JOB_ID)
    public Job job() {
        return jobBuilder.get(JOB_ID).start(step()).build();
    }

    @Bean(STEP_ID)
    public Step step() {

        AlphabetChunkReader reader = applicationContext.getBean(AlphabetChunkReader.class);
        return
                stepBuilder.get(STEP_ID)
                        .<String, Map<String, Long>>chunk(10)
                        .reader(reader)
                        .processor(applicationContext.getBean(LineToAlphabetProcessor.class))
                        .writer(applicationContext.getBean(AlphabetChunkWriter.class))
                        .build();
    }
}