package moe.tristan.easyfxml.spring;

import moe.lyrebird.view.util.AbstractEasyFxml;
import moe.tristan.easyfxml.EasyFxml;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_SINGLETON;

@Configuration
@ComponentScan(basePackages = "moe.tristan.easyfxml")
public class SpringContext {

}
