package com.meteor.config;

import com.meteor.partition.RangePartitioner;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.jsr.item.ItemReaderAdapter;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class PartitionJobConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job rangeTestJob() throws Exception {
//		return jobBuilderFactory.get("11").start(rangePartitionerStep()).on("*").to(npeStep()).next(npeStep()).build().build();

        return jobBuilderFactory.get("11").start(rangePartitionerStep()).next(npeStep()).build();

    }
    @Bean
    public Step rangePartitionerStep() {

        ThreadPoolTaskExecutor asyncExecutor = new ThreadPoolTaskExecutor();
        asyncExecutor.setCorePoolSize(2);
        asyncExecutor.setDaemon(true);
        asyncExecutor.initialize();

        return stepBuilderFactory.get("rangePartitionerStep")
                .partitioner("normalStep", rangePartitioner())
                .gridSize(10)
                .taskExecutor(asyncExecutor)
//				.step(normalStep())
                .step(remoteStep())
                .build();

    }


    @Bean
    public Partitioner rangePartitioner() {

        return new RangePartitioner();
    }

    private Step npeStep() {
        /*
         * JobExecution jobExecution = null; ExecutionContext jobContext =
         * jobExecution.getExecutionContext(); jobContext.get("");
         */

        System.out.println("System.identityHashCode( Thread.currentThread() ) : "
                + System.identityHashCode(Thread.currentThread()));



        // BatchRuntime.getJobOperator().getParameters(1);
        return stepBuilderFactory.get("npeStep").tasklet(new Tasklet() {
            public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {

                Object obj = chunkContext.getAttribute("fromId");

                System.out.println( "npeStep : " + obj );
                System.out.println("System.identityHashCode( Thread.currentThread() ) : "
                        + System.identityHashCode(Thread.currentThread()));
		/*
				if(true){
					throw new NullPointerException();
				}
				*/
/*
				obj = chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext()
						.get("key");
*/
                return null;
//				return RepeatStatus.CONTINUABLE;
            }
        }).build();
    }

    private Step remoteStep() {

        return stepBuilderFactory.get("step44").tasklet(new Tasklet() {
            public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {

//				ExecutionContext context = chunkContext.getStepContext().
//						getStepExecution().getJobExecution().getExecutionContext();
                Map<String, Object> context = chunkContext.getStepContext().getStepExecutionContext();

                String name = String.valueOf(context.get("name"));
                String fromId = String.valueOf(context.get("fromId"));
                String toId = String.valueOf(context.get("toId"));

                System.out.println( String.format("name : %s, fromId : %s, toId : %s", name, fromId, toId) );

                return null;
            }
        }).build();
    }
}
