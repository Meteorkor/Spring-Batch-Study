package com.meteor.batch.job.parallel;

import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MultiThreadedStepJobEnvConfig {
    private final Map<String, Job> jobMap;

    @Bean
    @Primary
    public Job primaryTestJob() {
        return jobMap.get(MultiThreadedStepJobConfig.JOB_NAME);
    }
}
