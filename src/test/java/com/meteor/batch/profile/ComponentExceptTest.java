package com.meteor.batch.profile;

import java.util.Arrays;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;

import com.meteor.batch.profile.component.DevServiceModule;
import com.meteor.batch.profile.component.ExcludedServiceModule;
import com.meteor.batch.profile.component.PrdServiceModule;

@SpringBootTest(classes = ComponentExcludeConfig.class)
@ActiveProfiles(ProfileConfig.PROFILE_DEV)
public class ComponentExceptTest {
    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void checkProfile() {
        Assertions.assertTrue(
                Arrays.stream(applicationContext.getEnvironment().getActiveProfiles())
                      .filter(profile -> ProfileConfig.PROFILE_DEV.equals(profile))
                      .findFirst().isPresent()
        );
    }

    @Test
    void prdProfile() {
        Assertions.assertThrows(NoSuchBeanDefinitionException.class, () -> {
            final PrdServiceModule bean = applicationContext.getBean(PrdServiceModule.class);
        });
    }

    @Test
    void devProfile() {
        final DevServiceModule bean = applicationContext.getBean(DevServiceModule.class);
        Assertions.assertNotNull(bean);
    }

    @Test
    void excludedServiceModule() {
        Assertions.assertThrows(NoSuchBeanDefinitionException.class, () -> {
            final ExcludedServiceModule bean = applicationContext.getBean(ExcludedServiceModule.class);
        });
    }

}
