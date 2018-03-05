package moe.tristan.easyfxml;

import org.junit.Test;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Asserts the presence of a private constructor on all classes without non-static fields. Partly because I'm getting
 * lowered coverage for this as well...
 */
public class UtilityClassesStyleTest {

    private static final Reflections reflections = new Reflections(
        "moe.tristan.easyfxml",
        new SubTypesScanner(false)
    );

    @Test
    public void assertExistsPrivateCtor() {
        reflections.getSubTypesOf(Object.class).stream()
                   .filter(UtilityClassesStyleTest::isNotTestClass)
                   .filter(UtilityClassesStyleTest::isNotSpringClass)
                   .filter(clazz -> !clazz.isInterface())
                   .filter(clazz ->
                       Arrays.stream(clazz.getDeclaredFields())
                             .allMatch(field -> Modifier.isStatic(field.getModifiers())))
                   .forEach(clazz -> {
                       System.out.println("Expecting class " + clazz.getName() + " to :");
                       System.out.print("\t-> be final ");
                       assertThat(clazz).isFinal();
                       System.out.println("[*]");
                       System.out.print("\t-> have exactly one constructor ");
                       Constructor<?>[] constructors = clazz.getDeclaredConstructors();
                       assertThat(constructors).hasSize(1);
                       System.out.println("[*]");
                       System.out.print("\t-> and that this constructor is private ");
                       assertThat(Modifier.isPrivate(constructors[0].getModifiers())).isTrue();
                       System.out.println("[*]");
                   });
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
