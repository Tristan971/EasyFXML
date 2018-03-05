package moe.tristan.easyfxml;

import org.springframework.context.ApplicationContext;

public final class TestUtils {

    private TestUtils() {
    }

    public static boolean isSpringSingleton(final ApplicationContext context, final Class<?> beanClazz) {
        final Object bean1 = context.getBean(beanClazz);
        final Object bean2 = context.getBean(beanClazz);

        return bean1.equals(bean2);
    }
}
