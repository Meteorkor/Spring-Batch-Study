package com.meteor.batch.job.parallel;

import org.junit.Before;
import org.junit.jupiter.api.Assertions;
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

    @Test
    void jobLauncherTestUtilsLaunchJobParametersFailTest() throws Exception {
        final JobParameters jobParameters = new JobParametersBuilder().addLong(RetryJobConfig.FAIL_CNT, 10L)
                                                                      .toJobParameters();
        final JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

        //TODO context 체크
        String partitionerContext = PartitionerStepJobConfig.STEP1_NAME + "_inner:test_1";

        final StepExecution stepExecution1 = jobExecution.getStepExecutions().stream().filter(
                stepExecution -> partitionerContext.equals(stepExecution.getStepName())
        ).findFirst().get();

        Assertions.assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus());
        Assertions.assertEquals(ExitStatus.COMPLETED.getExitCode(), jobExecution.getExitStatus().getExitCode());
        Assertions.assertEquals(1, stepExecution1.getExecutionContext()
                                                 .getInt(PartitionerStepJobConfig.STEP1_CALL_CNT));
    }

}
