package com.meteor.batch.job;

import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

//@ExtendWith(SpringExtension.class)
@SpringBatchTest
@SpringBootTest
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

}
