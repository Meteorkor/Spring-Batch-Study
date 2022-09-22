package com.meteor.batch.profile;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.meteor.batch.profile.component.DevServiceModule;
import com.meteor.batch.profile.component.PrdServiceModule;

@Configuration
public class ProfileConfig {
    public final static String PROFILE_DEV = "DEV";
    public final static String PROFILE_PRD = "PRD";

    @Profile(PROFILE_DEV)
    @Bean
    public DevServiceModule devServiceModule() {
        return new DevServiceModule();
    }

    @Profile(PROFILE_PRD)
    @Bean
    public PrdServiceModule prdServiceModule() {
        return new PrdServiceModule();
    }

}
