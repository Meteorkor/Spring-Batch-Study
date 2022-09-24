package com.meteor.batch.profile;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Profile;

import com.meteor.batch.profile.component.DevServiceModule;
import com.meteor.batch.profile.component.ExcludedServiceModule;
import com.meteor.batch.profile.component.PrdServiceModule;

@Configuration
@ComponentScan(
        basePackages = "com.meteor.batch.profile",
        excludeFilters =
        @Filter(type = FilterType.ASSIGNABLE_TYPE, classes = ExcludedServiceModule.class))
//@ComponentScan(excludeFilters =
//@Filter(type = FilterType.REGEX, pattern = "com.meteor.batch.profile.component.ExcludedServiceModule"))
//@ComponentScan(excludeFilters =
//@Filter(type = FilterType.REGEX, pattern = "com.meteor.batch.profile.component.ExcludedServiceModule"))
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


    public ExcludedServiceModule excludedServiceModule() {
        return new ExcludedServiceModule();
    }

}
