package com.example;

import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringBootBatchTestApplication {

	public static void main(String[] args) throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {
		SpringApplication.run(SpringBootBatchTestApplication.class, args);
//		SpringApplication.run(Object.class, args);
		/*
		SimpleJobLauncher launcher = new SimpleJobLauncher();
		
		Job job = new SimpleJob();
		Map<String,JobParameter> parameters = new HashMap<>();
		JobParameters param = new JobParameters(parameters)
		
		JobExecution jobExecution = launcher.run(job, param);
		*/
	}
}
