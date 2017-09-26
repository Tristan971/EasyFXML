package moe.tristan.easyfxml.spring;

import moe.tristan.easyfxml.EasyFxml;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Used for non-Spring projects in replacement of DI system.
 * If you have Spring, simply autowire {@link EasyFxml}.
 */
public final class EasyFxmlSupport {

    private static ApplicationContext applicationContext;

    private EasyFxmlSupport() {}

    public static EasyFxml getInstance() {
        return getApplicationContext().getBean(EasyFxml.class);
    }

    private static ApplicationContext getApplicationContext() {
        if (applicationContext == null) {
            applicationContext = new AnnotationConfigApplicationContext(SpringContext.class);
        }

        return applicationContext;
    }
}
