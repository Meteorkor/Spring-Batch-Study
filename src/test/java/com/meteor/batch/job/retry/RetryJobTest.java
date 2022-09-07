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
        final long END_EXCLUSIVE = 1000L;
        final long FAIL_THROW_CNT = 20L;
        final JobParameters jobParameters = new JobParametersBuilder().addLong(RetryJobConfig.FAIL_CNT,
                                                                               FAIL_THROW_CNT)
                                                                      .addLong(RetryJobConfig.END_EXCLUSIVE,
                                                                               END_EXCLUSIVE)
                                                                      .toJobParameters();
        final JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);
        Assertions.assertEquals(BatchStatus.FAILED, jobExecution.getStatus());
        Assertions.assertEquals(ExitStatus.FAILED.getExitCode(), jobExecution.getExitStatus().getExitCode());

        final Object o = jobExecution.getStepExecutions()
                                     .stream()
                                     .filter(step -> RetryJobConfig.TEST_STEP1.equals(step.getStepName()))
                                     .findFirst()
                                     .get()
                                     .getExecutionContext()
                                     .get(RetryJobConfig.SUM);
        Assertions.assertEquals((FAIL_THROW_CNT - 1) * FAIL_THROW_CNT / 2, o);

        final Object failCnt1 = jobExecution.getStepExecutions()
                                            .stream()
                                            .filter(step -> RetryJobConfig.PRE_STEP.equals(step.getStepName()))
                                            .findFirst()
                                            .get()
                                            .getExecutionContext()
                                            .get(RetryJobConfig.FAIL_CNT);
        Assertions.assertEquals(0L, failCnt1);

        //org.springframework.batch.core.repository.JobRestartException: JobInstance already exists and is not restartable
        //.preventRestart() 설정되어있을경우 위와같은 에러 발생
        final JobExecution jobExecution2 = jobLauncherTestUtils.launchJob(jobParameters);
        final Object o2 = jobExecution2.getStepExecutions()
                                       .stream()
                                       .filter(step -> RetryJobConfig.TEST_STEP1.equals(step.getStepName()))
                                       .findFirst()
                                       .get()
                                       .getExecutionContext()
                                       .get(RetryJobConfig.SUM);
        Assertions.assertEquals((END_EXCLUSIVE - 1) * END_EXCLUSIVE / 2, o2);

        //Executing step: [preStep] 는 성공한 Step이기 때문에 skip 됨
        Assertions.assertFalse(jobExecution2.getStepExecutions()
                                            .stream()
                                            .filter(step -> RetryJobConfig.PRE_STEP.equals(step.getStepName()))
                                            .findFirst().isPresent());
    }

}
