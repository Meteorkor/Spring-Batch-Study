package com.meteor.config;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.DuplicateJobException;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.support.ReferenceJobFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.StoppableTasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.stream.IntStream;

@Configuration
@RequiredArgsConstructor
public class LongJobConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private Logger logger = LoggerFactory.getLogger(LongJobConfig.class);

    @Bean
    public Job longJob() throws DuplicateJobException {
//        return jobBuilderFactory.get("longJob").start(longStep()).build();
        return jobBuilderFactory.get("longJob").start(stopStep()).build();

    }

    public Step longStep() {

        return stepBuilderFactory.get("longStep").tasklet((contri, chunkContext) -> {
            IntStream.range(0, 99).forEach(s -> {

//            IntStream.range(0, 10).forEach(s -> {
                try {

                    logger.info("isComplete {} , contristatus : {}, step.status : {}", chunkContext.isComplete(), contri.getExitStatus(), chunkContext.getStepContext().getStepExecution().getStatus());

                    logger.info("contri.getStepExecution().getJobExecution().getId() : {}", contri.getStepExecution().getJobExecution().getId());

                    contri.getStepExecution().getJobExecution().getExecutionContext().entrySet().stream().forEach((entry) -> {
                        logger.info("key : {} , value : {}", entry.getKey(), entry.getValue());
                    });

                    Thread.sleep(1000);
                    logger.info("[{}] process", s);
                } catch (InterruptedException ignore) {
                }
            });
            return RepeatStatus.FINISHED;
        }).build();
    }

    public Step stopStep() {
        return stepBuilderFactory.get("longStep").tasklet(new StoppableTasklet() {
            @Override
            public void stop() {
                System.out.println("stop!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            }

            @Override
            public RepeatStatus execute(StepContribution contri, ChunkContext chunkContext) throws Exception {
                IntStream.range(0, 99).forEach(s -> {

//            IntStream.range(0, 10).forEach(s -> {
                    try {

                        logger.info("isComplete {} , contristatus : {}, step.status : {}",
                                chunkContext.isComplete(), contri.getExitStatus(), chunkContext.getStepContext().getStepExecution().getStatus());

                        logger.info("contri.getStepExecution().getJobExecution().getId() : {}",
                                contri.getStepExecution().getJobExecution().getId());

                        contri.getStepExecution().getJobExecution().getExecutionContext().entrySet().stream().forEach((entry) -> {
                            logger.info("key : {} , value : {}", entry.getKey(), entry.getValue());
                        });

                        Thread.sleep(1000);
                        logger.info("[{}] process", s);
                    } catch (InterruptedException ignore) {
                    }
                });
                return RepeatStatus.FINISHED;
            }
        }).build();
    }
}
