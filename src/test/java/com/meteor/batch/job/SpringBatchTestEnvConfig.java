package com.meteor.batch.job;

import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SpringBatchTestEnvConfig {

    private final Map<String, Job> jobMap;

    @Bean
    @Primary
    public Job primaryTestJob() {
        return jobMap.get("testJob");
    }

}
