package moe.tristan.easyfxml;

import static org.junit.Assume.assumeFalse;
import static org.junit.Assume.assumeTrue;

import java.awt.Desktop;
import java.awt.SystemTray;
import org.junit.Before;

/**
 * This class skips all extending test classes tests if we are in a CI
 * environment. Many things are reasons for this including no AWT
 * support for some of the functionnalities in headless environments.
 * I'll get a non-headless test env sometimes.
 */
@SuppressWarnings("AccessOfSystemProperties")
public class CIIncompatibleTest {
    @Before
    public void ensureNotCi() {
        final String envProperty = System.getProperty("env");
        if (envProperty != null) {
            assumeFalse("ci".equals(envProperty));
        } else {
            assumeTrue(SystemTray.isSupported());
            assumeTrue(Desktop.isDesktopSupported());
        }
        System.out.println("WAS NOT CI ENVIRONMENT. EXECUTING TEST.");
    }
}
