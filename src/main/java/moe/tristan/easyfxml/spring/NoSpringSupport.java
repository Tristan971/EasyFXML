package moe.tristan.easyfxml.spring;

import moe.tristan.easyfxml.EasyFxml;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Arrays;

/**
 * Used for non-Spring projects in replacement of DI system.
 * If you have Spring, simply autowire {@link EasyFxml}.
 */
public final class NoSpringSupport {
    private static final Logger LOG = LoggerFactory.getLogger(NoSpringSupport.class);

    private static ApplicationContext applicationContext;

    private NoSpringSupport() {
    }

    public static <T> T getInstance(final Class<? extends T> instanceClass) {
        return getApplicationContext().getBean(instanceClass);
    }

    private static ApplicationContext getApplicationContext() {
        if (applicationContext == null) {
            applicationContext = new AnnotationConfigApplicationContext(SpringContext.class);
            LOG.info("Created ApplicationContext from {}", SpringContext.class.getName());
            LOG.info("Available beans are : {}", Arrays.toString(applicationContext.getBeanDefinitionNames()));
        }

        return applicationContext;
    }
}
