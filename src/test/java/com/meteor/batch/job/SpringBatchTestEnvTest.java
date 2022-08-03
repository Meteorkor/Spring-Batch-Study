package com.meteor.batch.job;

import java.util.UUID;

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
@Import(SpringBatchTestEnvConfig.class)
public class SpringBatchTestEnvTest {

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
    void jobLauncherTestUtilsLaunchJobParametersTest() throws Exception {
        final String testVal = "testVal";
        final JobParameters jobParameters = new JobParametersBuilder().addString("value", testVal)
                                                                      .addString("RAN",
                                                                                 UUID.randomUUID().toString())
                                                                      .toJobParameters();
        final JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);
        Assertions.assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus());
        Assertions.assertEquals(ExitStatus.COMPLETED, jobExecution.getExitStatus());

        Assertions.assertEquals(jobExecution.getExecutionContext().getString(TestJobConfig.STEP1_CHECK_KEY),
                                testVal);
        Assertions.assertEquals(jobExecution.getExecutionContext().getString(TestJobConfig.STEP2_CHECK_KEY),
                                testVal);
    }

    @Test
    void jobLauncherTestUtilsLaunchStepTest() throws Exception {
        final String testVal = "testVal";

        final JobExecution jobExecution = jobLauncherTestUtils.launchStep(TestJobConfig.TEST_STEP1);
        Assertions.assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus());
        Assertions.assertEquals(ExitStatus.COMPLETED, jobExecution.getExitStatus());
    }

    @Test
    void jobLauncherTestUtilsLaunchStep1JobParameterTest() throws Exception {
        final String testVal = "testVal";
        final JobParameters jobParameters = new JobParametersBuilder().addString("value", testVal)
                                                                      .addString("RAN",
                                                                                 UUID.randomUUID().toString())
                                                                      .toJobParameters();

        final JobExecution jobExecution = jobLauncherTestUtils.launchStep(TestJobConfig.TEST_STEP1,
                                                                          jobParameters);
        Assertions.assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus());
        Assertions.assertEquals(ExitStatus.COMPLETED, jobExecution.getExitStatus());

        Assertions.assertEquals(jobExecution.getExecutionContext().getString(TestJobConfig.STEP1_CHECK_KEY),
                                testVal);

        Assertions.assertNull(
                jobExecution.getExecutionContext().getString(TestJobConfig.STEP2_CHECK_KEY, null));

    }

    @Test
    void jobLauncherTestUtilsLaunchStep2JobParameterTest() throws Exception {
        final String testVal = "testVal";
        final JobParameters jobParameters = new JobParametersBuilder().addString("value", testVal)
                                                                      .addString("RAN",
                                                                                 UUID.randomUUID().toString())
                                                                      .toJobParameters();

        final JobExecution jobExecution = jobLauncherTestUtils.launchStep(TestJobConfig.TEST_STEP2,
                                                                          jobParameters);
        Assertions.assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus());
        Assertions.assertEquals(ExitStatus.COMPLETED, jobExecution.getExitStatus());

        Assertions.assertEquals(jobExecution.getExecutionContext().getString(TestJobConfig.STEP2_CHECK_KEY),
                                testVal);
        Assertions.assertNull(
                jobExecution.getExecutionContext().getString(TestJobConfig.STEP1_CHECK_KEY, null));
    }

}
