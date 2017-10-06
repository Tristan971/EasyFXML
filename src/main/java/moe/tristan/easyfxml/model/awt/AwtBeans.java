package moe.tristan.easyfxml.model.awt;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.awt.Desktop;
import java.awt.SystemTray;
import java.awt.Toolkit;

import static moe.tristan.easyfxml.model.awt.AwtAccess.ensureAwtSupport;

/**
 * AWT-functionnality is a pain to support with JavaFX.
 * We try to provide it here. These components are AWT-only
 * functionnality wrappers that handle most of the
 * insufferable boilerplate for basic system integration.
 */
@Lazy
@Configuration
public class AwtBeans {

    @Bean
    public Desktop awtDesktop() {
        return Desktop.getDesktop();
    }

    @Bean
    public SystemTray awtSystemTray() {
        return SystemTray.getSystemTray();
    }

    @Bean
    public Toolkit awtToolkit() {
        ensureAwtSupport();
        return Toolkit.getDefaultToolkit();
    }

}
