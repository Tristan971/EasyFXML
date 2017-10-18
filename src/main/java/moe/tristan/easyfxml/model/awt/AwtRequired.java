package moe.tristan.easyfxml.model.awt;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;

/**
 * Any {@link Bean} implementing this class will auto-ensure that
 * AWT is enabled through {@link AwtAccess#ensureAwtSupport()} before
 * making itself valid to the spring container.
 * <p>
 * That means that for any subclass, through {@link InitializingBean#afterPropertiesSet()},
 * the bean will fail its own instanciation if AWT is not available.
 */
public interface AwtRequired extends InitializingBean {
    @Override
    default void afterPropertiesSet() {
        AwtAccess.ensureAwtSupport();
    }

    boolean isSupported();
}
