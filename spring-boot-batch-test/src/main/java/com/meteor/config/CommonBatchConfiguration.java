package com.meteor.config;

import com.meteor.model.Emp;
import com.meteor.partition.RangePartitioner;
import com.meteor.processor.EmpItemProcessor;
import com.meteor.reader.EmpReader;
import com.meteor.reader.FlatFileReaderTest1;
import com.meteor.writer.EmpWriter;
import com.meteor.writer.FlatFileWriterTest1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.adapter.ItemReaderAdapter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.mapping.PassThroughLineMapper;
import org.springframework.batch.item.file.transform.PassThroughLineAggregator;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.task.SyncTaskExecutor;
import org.springframework.integration.http.inbound.HttpRequestHandlingMessagingGateway;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

//import org.springframework.core.task.AsyncTaskExecutor;
//import org.springframework.core.task.SimpleAsyncTaskExecutor;

@Configuration
@EnableBatchProcessing
//@EnableAutoConfiguration
public class CommonBatchConfiguration {
    private Logger logger = LoggerFactory.getLogger("BatchConfiguration");

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
	 * 테스트
	 * @param jobRegistry
	 * @return
	 */
	@Bean
	public JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor(JobRegistry jobRegistry) {
		JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor = new JobRegistryBeanPostProcessor();
		jobRegistryBeanPostProcessor.setJobRegistry(jobRegistry);
		return jobRegistryBeanPostProcessor;
	}

	@Bean
	public ItemReader<String> reader() {

		return new EmpReader();
	}

	// private Flow flow1 = new FlowBuilder<Flow>("test1").from(fStep1()).end();

	
	
	@Bean
	public ItemProcessor<String, Emp> transformer() {

		return new EmpItemProcessor();
	}

	private Step fStep1() {

		return stepBuilderFactory.get("step1").tasklet(new Tasklet() {
			public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {

				System.out.println("**********step1************");
				chunkContext.getStepContext().setAttribute("key", "key");

				chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext().put("key",
						"test");

				chunkContext.getStepContext().setAttribute("key", "test");
				chunkContext.setAttribute("key", "test");
				System.out.println("chunkContext.attributeNames(1).length : " + chunkContext.attributeNames().length);
				System.out.println("aaaaaaaaaaaafStep11aa");
				/*
				 * if(true) throw new NullPointerException("aaa");
				 */
				return null;
			}
		}).build();
	}

	
	@Bean
	public HttpRequestHandlingMessagingGateway requestGateway(){
		
		return new HttpRequestHandlingMessagingGateway();
	}
	
	@Autowired
	private HttpRequestHandlingMessagingGateway requestGateway;
	

	
	private Step normalStep() {
		/*
		 * JobExecution jobExecution = null; ExecutionContext jobContext =
		 * jobExecution.getExecutionContext(); jobContext.get("");
		 */

		System.out.println("System.identityHashCode( Thread.currentThread() ) : "
				+ System.identityHashCode(Thread.currentThread()));

		// BatchRuntime.getJobOperator().getParameters(1);
		return stepBuilderFactory.get("step44").tasklet(new Tasklet() {
			public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
				
//				if(true){
//					throw new NullPointerException();
//				}
				System.out.println("StepEx : " + chunkContext.getStepContext().getStepExecutionContext().get("fromId"));
				
				System.out.println("chunkContext.attributeNames(2).length : " + chunkContext.attributeNames().length);
				Object obj = chunkContext.getAttribute("key");
				System.out.println("obj : " + obj);
				obj = chunkContext.getStepContext().getAttribute("key");
				System.out.println("obj : " + obj);
				System.out.println("aaaaaaaaaaaafStep22aa");
				System.out.println("System.identityHashCode( Thread.currentThread() ) : "
						+ System.identityHashCode(Thread.currentThread()));

/*
				for (int idx = 0; idx < 10; idx++) {
					try {
						System.out.println("idx : " + idx);
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
*/
				// obj =
				// chunkContext.getStepContext().getStepExecution().getJobExecution().

				obj = chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext()
						.get("key");

				// obj =
				// chunkContext.getStepContext().getJobExecutionContext().get("key");
				System.out.println("obj 3 : " + obj);
				return null;
			}
		}).build();
	}

	private Step fFinalStep() {

		return stepBuilderFactory.get("fFinalStep").tasklet(new Tasklet() {
			public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {

				System.out.println("**********fFinalStep************");
				/*
				 * if(true) throw new NullPointerException("aaa");
				 */
				return null;
			}
		}).build();
	}

	@Bean
	public ItemWriter<Emp> writer() {

		return new EmpWriter();
	}

	@Bean
	public Step etlStep3() {

		// stepBuilderFactory.get("aa").

		return stepBuilderFactory.get("step3").<String, Emp> chunk(20).reader(new EmpReader())
				.processor(transformer())
				.writer(writer())
				.build();
	}
	@Bean
	public Step etlStep4() {

		// stepBuilderFactory.get("aa").
	    logger.info("etlStep4");
		return stepBuilderFactory.get("step4").<String, Emp> chunk(20).reader(new EmpReader())
				.processor(transformer())
				.writer(writer())
				.build();
	}
	
	
	
//	@Bean
    public Step flatFileStep1() {
	    Resource inputResource1 = getInputResource("testLine1\ntestLine2\ntestLine3\ntestLine4\ntestLine5\ntestLine6");

	    FlatFileItemReader reader = new FlatFileItemReader();
	    reader.setResource(inputResource1);
        reader.setLineMapper(new PassThroughLineMapper());

//        FlatFileWriterTest1 writer = new FlatFileWriterTest1();
        byte[] bytes = new byte[1024];
       
        File outputFile;
        	String path = "D:\\tmp\\";
//            outputFile = File.createTempFile("flatfile-test-output-", ".tmp");
			outputFile = new File(path + "test.data");


		FlatFileItemWriterBuilder flatFileItemWriterBuilder = new FlatFileItemWriterBuilder();
		flatFileItemWriterBuilder.


		FlatFileItemWriter writer = new FlatFileItemWriter();
		writer.setResource(new FileSystemResource(outputFile));
		writer.setLineSeparator("\n");
        writer.setEncoding("UTF-8");
        
        
        logger.info("flatFileStep1");
        return stepBuilderFactory.get("flatFileStep1")
				.<String, String> chunk(20).reader( reader )
//                .processor(transformer())
//                .writer(writer())
				.writer(writer)
                .build();
    }
	
	
	public Step partitionStep1() {
		Step sstep = stepBuilderFactory.get("partitionStep1").partitioner(etlStep4()) .build();
		return sstep;
	}
	
	@Bean
	public Step step1() {
		final AtomicInteger aInt = new AtomicInteger(0);
		return stepBuilderFactory.get("step1").tasklet(new Tasklet() {
			public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {

				System.out.println("aaaaaaaaaaaaaa");
				
				
//				if(true)return null;
				
				if(aInt.get()>1){
					System.out.println("Fin");
//					if(true)
//					throw new NullPointerException();
					return RepeatStatus.FINISHED;
				}else{
					System.out.println("con");
					aInt.incrementAndGet();
					return RepeatStatus.CONTINUABLE;
				}
				
//				return RepeatStatus.FINISHED;
			}
		}).build();
	}

	/**
	 * Step이 두개의 경우 파라미터 명을 통해 구분
	 * 
	 * @param step3
	 * @return
	 * @throws Exception
 	
	@Bean
	public Job jobSplit(Step step3) throws Exception {
		Flow flow1 = new FlowBuilder<Flow>("flow1").from(fStep1()).end();
		Flow flow2 = new FlowBuilder<Flow>("flow2").from(fStep1()).end();
		Flow flow3 = new FlowBuilder<Flow>("flow").from(fStep1()).end();
		Flow finalFlow = new FlowBuilder<Flow>("test2").from(fFinalStep()).end();

		SyncTaskExecutor syncExecutor = new SyncTaskExecutor();
		AsyncTaskExecutor asyncExecutor = new SimpleAsyncTaskExecutor();

//		JobBuilder jobBuilder = jobBuilderFactory.get("splitJob");
//		JobFlowBuilder jobFlowBuilder = jobBuilder.start(flow1);
//		SplitBuilder splitBuilder = jobFlowBuilder.split(asyncExecutor);
//		FlowBuilder<Job> flowBuilder =  splitBuilder.add( flow2,flow3 );
//		flowBuilder.next(finalFlow);
//		return flowBuilder.build();

		
		return jobBuilderFactory.get("splitJob").start(flow1)
				.split(asyncExecutor).add(flow2, flow3).next(finalFlow).end().build();
		
	}
	*/
	/**
	 * Step이 두개의 경우 파라미터 명을 통해 구분
	 * 
	 * @param
	 * @return
	 * @throws Exception
	 */
//	@Bean
	public Job etlJob222() throws Exception {
		SyncTaskExecutor syncExecutor = new SyncTaskExecutor();
//		AsyncTaskExecutor asyncExecutor = new SimpleAsyncTaskExecutor();
		
		ThreadPoolTaskExecutor asyncExecutor = new ThreadPoolTaskExecutor();
		asyncExecutor.setCorePoolSize(10);
		asyncExecutor.initialize();
		
		
		Flow simpleETL1 = new FlowBuilder<Flow>("simpleETL1").from(etlStep3()).end();
		Flow simpleETL2 = new FlowBuilder<Flow>("simpleETL2").from(etlStep4()).end();
		
		Flow flow1 = new FlowBuilder<Flow>("test1").start(step1())
//		Flow flow1 = new FlowBuilder<Flow>("test1").start(etlStep3())
//		Flow flow1 = new FlowBuilder<Flow>("test1").next(step1())
//		Flow flow1 = new FlowBuilder<Flow>("test1").from(step1())//성공해야
				.split(asyncExecutor)
//				.split(syncExecutor)
				.add(simpleETL1, simpleETL2, simpleETL2)
				.build();
		//from 은 Finish, null 이면 더이상 진행 안함..
		Flow flow2 = new FlowBuilder<Flow>("test2").from(normalStep()).end();
		Flow flow3 = new FlowBuilder<Flow>("test2").from(normalStep()).end();
		Flow finalFlow = new FlowBuilder<Flow>("test2").from(fFinalStep()).end();
		// return jobBuilderFactory.get("11").start(flow1).end().build();

		
		// AsyncTaskExecutor asyncExecutor = new ConcurrentTaskExecutor();

		
		 //return jobBuilderFactory.get("11").start(flow1).end().build();//main
		return jobBuilderFactory.get("11").start(flow1).end().build();
//		return jobBuilderFactory.get("11").start(flow1)
//				.split(asyncExecutor).add(flow2, flow3).next(finalFlow).end().build();
		
	}
	

	


//	@Bean
	public Job etlTestJob() throws Exception {
//      return jobBuilderFactory.get("11").start(rangePartitionerStep()).on("*").to(npeStep()).next(npeStep()).build().build();
        
        return jobBuilderFactory.get("etl").start(etlStep4()).build();
        
    }
	@Bean
	public Job flatTestJob() throws Exception {
//      return jobBuilderFactory.get("11").start(rangePartitionerStep()).on("*").to(npeStep()).next(npeStep()).build().build();
        
        return jobBuilderFactory.get("etl")
				.start(flatFileStep1()).build();
        
    }
	
	private Resource getInputResource(String input) {
        return new ByteArrayResource(input.getBytes());
    }
	
}