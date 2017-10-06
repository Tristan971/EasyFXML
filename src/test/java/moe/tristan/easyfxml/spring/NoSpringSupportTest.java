package moe.tristan.easyfxml.spring;

import moe.tristan.easyfxml.EasyFxml;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Java6Assertions.assertThat;

/**
 * Do not add {@link SpringBootTest} annotation
 * or use {@link RunWith} with {@link SpringJUnit4ClassRunner}
 * as this test checks whether the non-Spring projects
 * can work normally with the library...
 */
@SuppressWarnings("SpringJavaAutowiredMembersInspection")
public class NoSpringSupportTest {
    @Autowired
    private Environment environment;

    @Test
    public void test_doesnt_load_spring() {
        assertThat(this.environment).isNull();
    }

    @Test
    public void test_or_prod_class_using_spring() {
        final Set<String> illegalAnnotationsFoundOnClasses =
            Stream.of(NoSpringSupportTest.class, NoSpringSupport.class)
                .map(Class::getAnnotations)
                .flatMap(Arrays::stream)
                .map(Annotation::toString)
                .filter(clazz -> clazz.contains("springframework"))
                .collect(Collectors.toSet());

        assertThat(illegalAnnotationsFoundOnClasses).isEmpty();
    }

    @Test
    public void getInstance() {
        final EasyFxml instance = NoSpringSupport.getInstance(EasyFxml.class);
        assertThat(instance).isNotNull();
    }
}
