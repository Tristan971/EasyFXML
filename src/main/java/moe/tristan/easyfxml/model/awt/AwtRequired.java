package moe.tristan.easyfxml.model.awt;

import org.springframework.beans.factory.InitializingBean;

public interface AwtRequired extends InitializingBean {
    @Override
    default void afterPropertiesSet() {
        AWTSupport.ensureAwtSupport();
    }
}
