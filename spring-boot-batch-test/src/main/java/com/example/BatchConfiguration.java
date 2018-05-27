package com.example;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.builder.JobFlowBuilder;
import org.springframework.batch.core.job.builder.FlowBuilder.SplitBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
//import org.springframework.core.task.AsyncTaskExecutor;
//import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.SyncTaskExecutor;
import org.springframework.integration.http.inbound.HttpRequestHandlingMessagingGateway;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.example.model.Emp;
import com.example.processor.EmpItemProcessor;
import com.example.reader.EmpReader;
import com.example.writer.EmpWriter;

@Configuration
@EnableBatchProcessing
@EnableAutoConfiguration
public class BatchConfiguration {

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

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
	
	@Bean
	public HttpRequestHandlingMessagingGateway requestGateway(){
		
		return new HttpRequestHandlingMessagingGateway();
	}
	
	@Autowired
	private HttpRequestHandlingMessagingGateway requestGateway;
	
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

		return stepBuilderFactory.get("step4").<String, Emp> chunk(20).reader(new EmpReader())
				.processor(transformer())
				.writer(writer())
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
	 * @param step3
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
	
	@Bean
	public Partitioner rangePartitioner() {

		return new RangePartitioner();
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
	
	/**
	 * Partitioner Job
	 * 
	 * @since  2017. 1. 16.
	 * @return
	 * @throws Exception
	 */
	@Bean
	public Job rangeTestJob() throws Exception {
//		return jobBuilderFactory.get("11").start(rangePartitionerStep()).on("*").to(npeStep()).next(npeStep()).build().build();
		
		return jobBuilderFactory.get("11").start(rangePartitionerStep()).next(npeStep()).build();
		
	}
	
	
}