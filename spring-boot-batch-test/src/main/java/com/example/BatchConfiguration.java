package com.example;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
@EnableAutoConfiguration
public class BatchConfiguration {

	/**
	 * <br> JobBuilderFactory(JobRepository)
	 * <br> JobBuilder
	 * <br> job{
	 * <br>  Step, JobParameter Set(incrementer...)
	 * <br> }
	 * 
	 */
	
  @Autowired
  private JobBuilderFactory jobBuilderFactory;

  /**
   * <br> StepBuilderFactory(JobRepository, PlatformTransactionManager)
   * <br> StepBuilder
   * <br> Step{
   * <br> 	Tasklet{
   * <br>		return RepeatStatus(FINISHED, CONTINUABLE)
   * <br>	}
   * <br>
   * <br> } 
   */
  @Autowired
  private StepBuilderFactory stepBuilderFactory;

  /**
   * <br> Context는 AttributeAccessorSupport를 상속받아 활용..
   * <br> TaskletStep.class innerClass ChunkTransactionCallback.class
   * <br> doInTransaction() call "tasklet.execute()"
   * @return
   */
  @Bean
  public Step step1() {
    return stepBuilderFactory.get("step1")
        .tasklet(new Tasklet() {
          public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
        	  chunkContext.getAttribute("aaa");
        	  System.out.println("aaaaaaaaaaaaaa");
        	  return null;
          }
        })
        .build();
  }

  @Bean
  public Job job(Step step1) throws Exception {
    return jobBuilderFactory.get("job1")
        .incrementer(new RunIdIncrementer())
        .start(step1)
        .build();
  }
}