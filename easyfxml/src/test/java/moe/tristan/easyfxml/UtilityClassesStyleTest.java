/*
 * Copyright 2017 - 2019 EasyFXML project and contributors
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package moe.tristan.easyfxml;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Objects;

import org.junit.Test;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.annotation.AnnotationUtils;

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
                       .filter(UtilityClassesStyleTest::isNotSpringBean)
                       .filter(clazz -> !clazz.isInterface())
                       .filter(clazz ->
                                   Arrays.stream(clazz.getDeclaredFields())
                                         .allMatch(field -> Modifier.isStatic(field.getModifiers())))
                       .forEach(clazz -> {
                           errorBuilder.append("Expecting class ").append(clazz.getName()).append("to :").append('\n');
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

    private static boolean isNotSpringBean(final Class<?> clazz) {
        final ComponentScan componentScanConfig = AnnotationUtils.findAnnotation(EasyFxmlAutoConfiguration.class, ComponentScan.class);
        final ComponentScan.Filter componentScanFilter = Objects.requireNonNull(componentScanConfig).includeFilters()[0];
        for (Class<?> filteredBeanType : componentScanFilter.classes()) {
            if (filteredBeanType.isAssignableFrom(clazz)) {
                return false;
            }
        }

        final Annotation[] annotations = clazz.getDeclaredAnnotations();
        return Arrays.stream(annotations)
                     .map(Annotation::toString)
                     .noneMatch(name -> name.contains("org.springframework"));
    }

}
