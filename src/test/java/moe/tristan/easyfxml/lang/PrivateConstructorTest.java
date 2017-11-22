package moe.tristan.easyfxml.lang;

import moe.tristan.easyfxml.spring.SpringContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Asserts the presence of a private constructor on all classes
 * without non-static fields.
 * Partly because I'm getting lowered coverage for this as well...
 */
@ContextConfiguration(classes = SpringContext.class)
@RunWith(SpringRunner.class)
public class PrivateConstructorTest {

    private static final Reflections reflections = new Reflections(
        "moe.tristan.easyfxml",
        new SubTypesScanner(false)
    );

    @Test
    public void assertExistsPrivateCtor() {
        reflections.getSubTypesOf(Object.class).stream()
            .filter(clazz -> !clazz.isAnnotationPresent(SpringBootTest.class))
            .filter(clazz -> !clazz.isAnnotationPresent(Configuration.class))
            .filter(clazz -> !clazz.isAnnotationPresent(Component.class))
            .filter(clazz -> !clazz.isAnnotationPresent(ContextConfiguration.class))
            .filter(clazz -> !clazz.isAnnotationPresent(SpringBootApplication.class))
            .filter(clazz -> !clazz.isInterface())
            .filter(clazz ->
                Arrays.stream(clazz.getDeclaredFields())
                    .allMatch(field -> Modifier.isStatic(field.getModifiers())))
            .forEach(clazz -> {
                System.out.println("Expecting class "+clazz.getName()+" to :");
                System.out.print("\t-> be final ");
                assertThat(clazz).isFinal();
                System.out.println("[*]");
                System.out.print("\t-> have exactly one constructor ");
                Constructor<?>[] constructors = clazz.getDeclaredConstructors();
                assertThat(constructors).hasSize(1);
                System.out.println("[*]");
                System.out.print("\t-> and that this constructor is private ");
                assertThat(Modifier.isPrivate(constructors[0].getModifiers()));
                System.out.println("[*]");
            });
    }

}
