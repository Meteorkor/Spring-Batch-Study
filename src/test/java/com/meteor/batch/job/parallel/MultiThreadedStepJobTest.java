package com.meteor.batch.job.parallel;

import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import com.meteor.batch.job.retry.RetryJobConfig;
import com.meteor.batch.job.retry.RetryJobTestEnvConfig;

@SpringBatchTest
@SpringBootTest
@Import(MultiThreadedStepJobEnvConfig.class)
public class MultiThreadedStepJobTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private JobRepositoryTestUtils jobRepositoryTestUtils;

    @Before
    public void clearJobExecutions() {
        this.jobRepositoryTestUtils.removeJobExecutions();
    }

    @Test
    void jobLauncherTestUtilsLaunchJobParametersFailTest() throws Exception {
        final JobParameters jobParameters = new JobParametersBuilder().addLong(RetryJobConfig.FAIL_CNT, 10L)
                                                                      .toJobParameters();
        final JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);
        Assertions.assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus());
        Assertions.assertEquals(ExitStatus.COMPLETED.getExitCode(), jobExecution.getExitStatus().getExitCode());
        Assertions.assertEquals(MultiThreadedStepJobConfig.THREAD_CNT, jobExecution.getExecutionContext().entrySet().size());
    }

}
