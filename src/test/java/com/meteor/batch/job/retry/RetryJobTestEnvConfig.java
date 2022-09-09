package com.meteor.batch.job.retry;

import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RetryJobTestEnvConfig {

    public static final String RETRY_JOB = "retryJob";
    private final Map<String, Job> jobMap;

    @Bean
    @Primary
    public Job primaryTestJob() {
        return jobMap.get(RETRY_JOB);
    }
}
