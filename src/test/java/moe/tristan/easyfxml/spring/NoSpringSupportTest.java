package moe.tristan.easyfxml.spring;

import javafx.fxml.FXMLLoader;
import moe.tristan.easyfxml.EasyFxml;
import moe.tristan.easyfxml.model.awt.AwtAccess;
import moe.tristan.easyfxml.model.beanmanagement.ControllerManager;
import moe.tristan.easyfxml.model.beanmanagement.StageManager;
import moe.tristan.easyfxml.model.fxml.BaseEasyFxml;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Do not add {@link SpringBootTest} annotation
 * or use {@link RunWith} with {@link SpringJUnit4ClassRunner}
 * as this test checks whether the non-Spring projects
 * can work normally with the library...
 */
@SuppressWarnings({"SpringJavaAutowiredMembersInspection", "CodeBlock2Expr"})
public class NoSpringSupportTest {

    @Autowired
    private ApplicationContext ctx;

    @Test
    public void test_doesnt_load_spring() {
        assertThat(this.ctx).isNull();
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
        AwtAccess.enableAwt();
        Stream.of(
            EasyFxml.class,
            BaseEasyFxml.class,
            FXMLLoader.class,

            ControllerManager.class,
            StageManager.class
        ).map(NoSpringSupport::getInstance).forEach(instance -> {
            assertThat(instance).isNotNull();
        });
    }
}
