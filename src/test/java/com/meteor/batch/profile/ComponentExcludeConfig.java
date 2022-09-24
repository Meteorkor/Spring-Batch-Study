package com.meteor.batch.profile;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;

import com.meteor.batch.profile.component.ExcludedServiceModule;

@ComponentScan(
        basePackages = "com.meteor.batch.profile",
        excludeFilters =
        @Filter(type = FilterType.ASSIGNABLE_TYPE, classes = ExcludedServiceModule.class))
public class ComponentExcludeConfig {
    public final static String PROFILE_DEV = "DEV";
    public final static String PROFILE_PRD = "PRD";
}
