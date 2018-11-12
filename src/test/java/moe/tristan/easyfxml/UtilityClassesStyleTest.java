package moe.tristan.easyfxml;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Arrays;

import org.junit.Test;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Asserts the presence of a private constructor on all classes without non-static fields. Partly because I'm getting
 * lowered coverage for this as well...
 */
public class UtilityClassesStyleTest {

    private static final Logger LOG = LoggerFactory.getLogger(UtilityClassesStyleTest.class);

    private static final Reflections reflections = new Reflections(
        "moe.tristan.easyfxml",
        new SubTypesScanner(false)
    );

    @Test
    public void assertExistsPrivateCtor() {
        final StringBuilder errorBuilder = new StringBuilder();
        try {
            reflections.getSubTypesOf(Object.class).stream()
                       .filter(UtilityClassesStyleTest::isNotTestClass)
                       .filter(UtilityClassesStyleTest::isNotSpringClass)
                       .filter(clazz -> !clazz.isInterface())
                       .filter(clazz ->
                                   Arrays.stream(clazz.getDeclaredFields())
                                         .allMatch(field -> Modifier.isStatic(field.getModifiers())))
                       .forEach(clazz -> {
                           errorBuilder.append("Expecting class").append(clazz.getName()).append("to :").append('\n');
                           errorBuilder.append("\t-> be final ").append('\n');
                           assertThat(clazz).isFinal();
                           errorBuilder.append("\t-> have exactly one constructor ").append('\n');
                           Constructor<?>[] constructors = clazz.getDeclaredConstructors();
                           assertThat(constructors).hasSize(1);
                           errorBuilder.append("\t-> and that this constructor is private ").append('\n');
                           assertThat(Modifier.isPrivate(constructors[0].getModifiers())).isTrue();
                       });
        } catch (Throwable t) {
            LOG.error(errorBuilder.toString());
            throw t;
        }
    }

    private static boolean isNotTestClass(final Class<?> clazz) {
        if (clazz.getName().endsWith("Test")) {
            return false;
        }
        return !clazz.getName().split("\\$")[0].endsWith("Test");
    }

    private static boolean isNotSpringClass(final Class<?> clazz) {
        final Annotation[] annotations = clazz.getDeclaredAnnotations();
        return Arrays.stream(annotations)
                     .map(Annotation::toString)
                     .noneMatch(name -> name.contains("org.springframework"));
    }

}
