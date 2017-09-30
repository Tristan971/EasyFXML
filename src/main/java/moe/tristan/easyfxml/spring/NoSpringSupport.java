package moe.tristan.easyfxml.spring;

import lombok.extern.slf4j.Slf4j;
import moe.tristan.easyfxml.EasyFxml;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Arrays;

/**
 * Used for non-Spring projects in replacement of DI system.
 * If you have Spring, simply autowire {@link EasyFxml}.
 */
@Slf4j
public final class NoSpringSupport {

    private static ApplicationContext applicationContext;

    private NoSpringSupport() {
        log.info("Loading non-Spring project dependency injection adapter...");
    }

    public static <T> T getInstance(Class<? extends T> instanceClass) {
        return getApplicationContext().getBean(instanceClass);
    }

    private static ApplicationContext getApplicationContext() {
        if (applicationContext == null) {
            applicationContext = new AnnotationConfigApplicationContext(SpringContext.class);
            log.info("Created ApplicationContext from {}", SpringContext.class.getName());
            log.info("Available beans are : {}", Arrays.toString(applicationContext.getBeanDefinitionNames()));
        }

        return applicationContext;
    }
}
