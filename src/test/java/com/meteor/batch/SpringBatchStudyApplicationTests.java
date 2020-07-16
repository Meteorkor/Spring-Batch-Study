package com.meteor.batch;

import com.meteor.batch.common.AlphabetConst;
import com.meteor.batch.config.job.AlphabetChunkCountingJobConfig;
import com.meteor.batch.config.job.AlphabetChunkParallelCountingJobConfig;
import com.meteor.batch.config.job.AlphabetTaskletCountingJobConfig;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;
import java.util.UUID;

@SpringBootTest
class SpringBatchStudyApplicationTests {
	public static final String INPUT_STRING = "adsasd,asdas123asd,zxcvxcgh,zxcvxcgh,zxcvxcgh,zxcvxcgh,zxcvxcgh,zxcvxcgh,zxcvxcgh,zxcvxcgh,zxcvxcgh,zxcvxcgh";
	public static final String EXPRECTED_VALUE = "{1=1, 2=1, 3=1, a=5, c=20, d=4, g=10, h=10, s=5, v=10, x=20, z=10}";
	@Autowired
	private JobLauncher jobLauncher;
	@Autowired
	private Map<String, Job> jobMap;

	@Test
	void alphabetChunkCountingJobConfigTest() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
		Job job = jobMap.get(AlphabetChunkCountingJobConfig.JOB_ID);

		JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
		jobParametersBuilder.addString("uuid", UUID.randomUUID().toString());
		//"{1=1, a=5, 2=1, c=20, 3=1, s=5, d=4, v=10, g=10, x=20, h=10, z=10}"
//		List<String> lineList = Arrays.asList("adsasd", "asdas123asd", "zxcvxcgh", "zxcvxcgh", "zxcvxcgh", "zxcvxcgh", "zxcvxcgh", "zxcvxcgh", "zxcvxcgh", "zxcvxcgh", "zxcvxcgh", "zxcvxcgh");
//		reader.setStrList(lineList);

		jobParametersBuilder.addString(AlphabetConst.KEY, INPUT_STRING);

		JobParameters jobParameters = jobParametersBuilder.toJobParameters();

		JobExecution jobExecution = jobLauncher.run(job, jobParameters);
		Assert.assertEquals(0, jobExecution.getAllFailureExceptions().size());
		Object o = jobExecution.getExecutionContext().get(AlphabetConst.KEY);
		Assertions.assertEquals(EXPRECTED_VALUE, String.valueOf(o));
	}

	@Test
	void alphabetChunkParallelCountingJobConfigTest() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
		Job job = jobMap.get(AlphabetChunkParallelCountingJobConfig.JOB_ID);

		JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
		jobParametersBuilder.addString("uuid", UUID.randomUUID().toString());
		//"{1=1, a=5, 2=1, c=20, 3=1, s=5, d=4, v=10, g=10, x=20, h=10, z=10}"
//		List<String> lineList = Arrays.asList("adsasd", "asdas123asd", "zxcvxcgh", "zxcvxcgh", "zxcvxcgh", "zxcvxcgh", "zxcvxcgh", "zxcvxcgh", "zxcvxcgh", "zxcvxcgh", "zxcvxcgh", "zxcvxcgh");
//		reader.setStrList(lineList);

		jobParametersBuilder.addString(AlphabetConst.KEY, INPUT_STRING);

		JobParameters jobParameters = jobParametersBuilder.toJobParameters();

		JobExecution jobExecution = jobLauncher.run(job, jobParameters);
		Assert.assertEquals(0, jobExecution.getAllFailureExceptions().size());
		Object o = jobExecution.getExecutionContext().get(AlphabetConst.KEY);
		Assertions.assertEquals(EXPRECTED_VALUE, String.valueOf(o));
	}

	@Test
	void alphabetTaskletCountingJobTest() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {

		Job job = jobMap.get(AlphabetTaskletCountingJobConfig.JOB_ID);

		JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
		jobParametersBuilder.addString("uuid", UUID.randomUUID().toString());
		//"{1=1, a=5, 2=1, c=20, 3=1, s=5, d=4, v=10, g=10, x=20, h=10, z=10}"
//		List<String> lineList = Arrays.asList("adsasd", "asdas123asd", "zxcvxcgh", "zxcvxcgh", "zxcvxcgh", "zxcvxcgh", "zxcvxcgh", "zxcvxcgh", "zxcvxcgh", "zxcvxcgh", "zxcvxcgh", "zxcvxcgh");
//		reader.setStrList(lineList);

		jobParametersBuilder.addString(AlphabetConst.KEY, INPUT_STRING);

		JobParameters jobParameters = jobParametersBuilder.toJobParameters();

		JobExecution jobExecution = jobLauncher.run(job, jobParameters);
		Assert.assertEquals(0, jobExecution.getAllFailureExceptions().size());
		Object o = jobExecution.getExecutionContext().get(AlphabetConst.KEY);
		Assertions.assertEquals(EXPRECTED_VALUE, String.valueOf(o));
	}
}
