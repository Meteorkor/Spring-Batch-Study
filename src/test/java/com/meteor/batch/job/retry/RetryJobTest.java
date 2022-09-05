package com.meteor.batch.job.retry;

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

@SpringBatchTest
@SpringBootTest
@Import(RetryJobTestEnvConfig.class)
public class RetryJobTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private JobRepositoryTestUtils jobRepositoryTestUtils;

    @Before
    public void clearJobExecutions() {
        this.jobRepositoryTestUtils.removeJobExecutions();
    }

    @Test
    void autowireCheck() {
        Assertions.assertNotNull(jobLauncherTestUtils);
        Assertions.assertNotNull(jobRepositoryTestUtils);
    }

    @Test
    void jobLauncherTestUtilsGetTest() {
        Assertions.assertNotNull(jobLauncherTestUtils.getJob());
        Assertions.assertNotNull(jobLauncherTestUtils.getJobLauncher());
        Assertions.assertNotNull(jobLauncherTestUtils.getJobRepository());
        Assertions.assertNotNull(jobLauncherTestUtils.getUniqueJobParameters());
    }

    @Test
    void jobLauncherTestUtilsLaunchJobTest() throws Exception {
        final JobExecution jobExecution = jobLauncherTestUtils.launchJob();
        Assertions.assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus());
        Assertions.assertEquals(ExitStatus.COMPLETED, jobExecution.getExitStatus());
    }

    @Test
    void jobLauncherTestUtilsLaunchJobParametersFailTest() throws Exception {
        final JobParameters jobParameters = new JobParametersBuilder().addLong(RetryJobConfig.FAIL_CNT, 10L)
                                                                      .toJobParameters();
        final JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);
        Assertions.assertEquals(BatchStatus.FAILED, jobExecution.getStatus());
        Assertions.assertEquals(ExitStatus.FAILED.getExitCode(), jobExecution.getExitStatus().getExitCode());
    }

    @Test
    void jobLauncherTestUtilsLaunchJobParametersFailAndRetryTest() throws Exception {
        final JobParameters jobParameters = new JobParametersBuilder().addLong(RetryJobConfig.FAIL_CNT, 20L)
                                                                      .addLong(RetryJobConfig.END_EXCLUSIVE,
                                                                               1000L)
                                                                      .toJobParameters();
        final JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);
        Assertions.assertEquals(BatchStatus.FAILED, jobExecution.getStatus());
        Assertions.assertEquals(ExitStatus.FAILED.getExitCode(), jobExecution.getExitStatus().getExitCode());

        final Object o = jobExecution.getExecutionContext().get(RetryJobConfig.SUM);
        System.out.println("o : " + o);

        //org.springframework.batch.core.repository.JobRestartException: JobInstance already exists and is not restartable
        //.preventRestart() 설정되어있을경우 위와같은 에러 발생
        final JobExecution jobExecution2 = jobLauncherTestUtils.launchJob(jobParameters);
        final Object o2 = jobExecution2.getExecutionContext().get(RetryJobConfig.SUM);
        System.out.println("o2 : " + o2);
        //TODO assert 추가 및 reader측에서 에러 내도록 변경, writer에서 context에 데이터 남기는것 assert 추가
    }

}
