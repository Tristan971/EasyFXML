package moe.tristan.easyfxml.spring.application;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * {@link Configuration} class expected to be imported by all library-using applications.
 */
@Configuration
@ComponentScan(basePackages = "moe.tristan.easyfxml", lazyInit = true)
public class FxSpringContext {

}
