package moe.tristan.easyfxml.spring;

import moe.tristan.easyfxml.EasyFxml;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Java6Assertions.assertThat;

/**
 * Do not add {@link SpringBootTest} annotation
 * as this test checks whether the non-Spring projects
 * can work normally with the library.
 */
public class EasyFxmlDependencyInjectionTest {

    @Test
    public void getInstance() {
        final EasyFxml instance = EasyFxmlDependencyInjection.getInstance();
        assertThat(instance).isNotNull();
    }
}