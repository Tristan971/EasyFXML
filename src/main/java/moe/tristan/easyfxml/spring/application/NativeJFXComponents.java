package moe.tristan.easyfxml.spring.application;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javafx.application.Application;
import javafx.application.HostServices;

@Configuration
public class NativeJFXComponents {

    @Bean
    public HostServices hostServices(final Application application) {
        return application.getHostServices();
    }

}
