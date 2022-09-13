package com.meteor.batch.job.parallel;

import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import com.meteor.batch.job.retry.RetryJobConfig;

@SpringBatchTest
@SpringBootTest
@Import(PartitionerStepJobEnvConfig.class)
public class PartitionerStepJobTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private JobRepositoryTestUtils jobRepositoryTestUtils;

    @Before
    public void clearJobExecutions() {
        this.jobRepositoryTestUtils.removeJobExecutions();
    }

    @DisplayName("단건 executionContext")
    @Test
    void jobLauncherPartitionerStep1Test() throws Exception {
        final JobParameters jobParameters = new JobParametersBuilder().addLong(RetryJobConfig.FAIL_CNT, 10L)
                                                                      .toJobParameters();
        final JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

        String partitionerContext = PartitionerStepJobConfig.STEP1_INNER
                                    + ":" + PartitionerStepJobConfig.STEP1_CONTEXT_1_NAME;

        final StepExecution stepExecution1 = jobExecution.getStepExecutions().stream().filter(
                stepExecution -> partitionerContext.equals(stepExecution.getStepName())
        ).findFirst().get();

        Assertions.assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus());
        Assertions.assertEquals(ExitStatus.COMPLETED.getExitCode(), jobExecution.getExitStatus().getExitCode());
        Assertions.assertEquals(1, stepExecution1.getExecutionContext()
                                                 .getInt(PartitionerStepJobConfig.CALL_CNT_KEY));
    }

    @DisplayName("다건 executionContext")
    @Test
    void jobLauncherPartitionerStep2Test() throws Exception {
        final JobParameters jobParameters = new JobParametersBuilder().addLong(RetryJobConfig.FAIL_CNT, 10L)
                                                                      .toJobParameters();
        final JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

        final long count = jobExecution.getStepExecutions().stream().filter(
                stepExecution -> stepExecution.getStepName().startsWith(PartitionerStepJobConfig.STEP2_INNER)
        ).count();

        Assertions.assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus());
        Assertions.assertEquals(ExitStatus.COMPLETED.getExitCode(), jobExecution.getExitStatus().getExitCode());
        Assertions.assertEquals(PartitionerStepJobConfig.STEP2_CNT, count);
    }

    //TODO
    @DisplayName("다건 chunked")
    @Test
    void jobLauncherPartitionerStep3Test() throws Exception {

    }

}
