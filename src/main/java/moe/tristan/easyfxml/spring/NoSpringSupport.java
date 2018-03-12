package moe.tristan.easyfxml.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import moe.tristan.easyfxml.EasyFxml;
import moe.tristan.easyfxml.spring.application.FxSpringContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * Used for non-Spring projects in replacement of DI system. If you have Spring, simply autowire {@link EasyFxml}.
 */
public final class NoSpringSupport {

    private static final Logger LOG = LoggerFactory.getLogger(NoSpringSupport.class);

    private static ApplicationContext applicationContext;

    private NoSpringSupport() {
    }

    /**
     * Use this method to get instances of the services.
     *
     * @param instanceClass The class of which we want an instance
     * @param <T>           The type expected
     *
     * @return The instance requested
     */
    public static <T> T getInstance(final Class<? extends T> instanceClass) {
        return getApplicationContext().getBean(instanceClass);
    }

    private static ApplicationContext getApplicationContext() {
        if (applicationContext == null) {
            applicationContext = new AnnotationConfigApplicationContext(FxSpringContext.class);
            LOG.info("Created ApplicationContext from {}", FxSpringContext.class.getName());
            LOG.info("Available beans are : {}", Arrays.toString(applicationContext.getBeanDefinitionNames()));
        }

        return applicationContext;
    }
}
