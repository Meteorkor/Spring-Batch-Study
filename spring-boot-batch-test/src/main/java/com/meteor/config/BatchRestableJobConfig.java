package com.meteor.config;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class BatchRestableJobConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;


    @Bean
    public Job restableJob(){
        return
                jobBuilderFactory.get("restableJob")
                        .start(firstStep()).next(secondFailStep())
                        .incrementer(new RunIdIncrementer())
                        .build();
    }

    public Step firstStep(){
        return
                stepBuilderFactory.get("firstStep").tasklet((contri,con)->{
                    Logger logger = LoggerFactory.getLogger(this.getClass());
                    logger.info("con.getStepContext() : {}",con.getStepContext());
                    logger.info("getJobExecution().getExecutionContext() : {}", con.getStepContext().getStepExecution().getJobExecution().getExecutionContext());
                    logger.info("getStepExecution().getExecutionContext() : {}", con.getStepContext().getStepExecution().getExecutionContext());


//                    con.getStepContext().getStepExecution().getExecutionContext().putString("value","ss");

                    logger.info("===========put job Value : ");
                    con.getStepContext().getStepExecution().getJobExecution().getExecutionContext().putString("value","jobValue1");
                    {
                        Object obj = con.getStepContext().getStepExecution().getExecutionContext().get("value");
                        logger.info("Step obj : {}", obj);
                        con.getStepContext().getStepExecution().getExecutionContext().putString("value","stepValue1");//이전데이터가 남아있네..
                    }

                    return RepeatStatus.FINISHED;
                }).build();
    }
    public Step secondFailStep(){
        return
                stepBuilderFactory.get("secondStep").tasklet((contri,con)->{
                    Logger logger = LoggerFactory.getLogger(this.getClass());

                    logger.info("con.getStepContext() : {}",con.getStepContext());
                    logger.info("getJobExecution().getExecutionContext() : {}", con.getStepContext().getStepExecution().getJobExecution().getExecutionContext());
                    logger.info("getStepExecution().getExecutionContext() : {}", con.getStepContext().getStepExecution().getExecutionContext());

                    logger.info("con.getStepContext() : {}",con.getStepContext());
                    {
                        Object obj = con.getStepContext().getStepExecution().getExecutionContext().get("value");
                        logger.info("Step obj : {}", obj);
                        con.getStepContext().getStepExecution().getExecutionContext().putString("value","stepValue2");//이전데이터가 남아있네..
                    }

                    {
                        Object obj = con.getStepContext().getStepExecution().getJobExecution().getExecutionContext().get("value");
                        logger.info("Job obj : {}", obj);
                        con.getStepContext().getStepExecution().getJobExecution().getExecutionContext().putString("value","jobValue2");//이전데이터가 남아있네..

                    }


                    return RepeatStatus.FINISHED;
                }).build();

    }
}
