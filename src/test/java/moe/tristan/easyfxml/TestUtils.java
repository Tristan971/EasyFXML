package moe.tristan.easyfxml;

import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

public class TestUtils {
    public static void ensureSpringSingleton(final ApplicationContext context, final Class<?> beanClazz) {
        final Object bean1 = context.getBean(beanClazz);
        final Object bean2 = context.getBean(beanClazz);

        assertThat(bean1).isEqualTo(bean2);
    }

    public static void ensureSpringPrototype(final ApplicationContext context, final Class<?> beanClazz) {
        final Object bean1 = context.getBean(beanClazz);
        final Object bean2 = context.getBean(beanClazz);

        assertThat(bean1).isNotEqualTo(bean2);
    }
}
