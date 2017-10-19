package moe.tristan.easyfxml.model.awt;

import javafx.stage.Stage;
import org.junit.Before;
import org.testfx.framework.junit.ApplicationTest;

import java.awt.Desktop;
import java.awt.SystemTray;

import static org.junit.Assume.assumeTrue;

/**
 * This class skips all extending test classes tests if we are in a CI
 * environment. Many things are reasons for this including no AWT
 * support for some of the functionnalities in headless environments.
 * I'll get a non-headless test env sometimes.
 */
@SuppressWarnings("AccessOfSystemProperties")
public class HeadlessIncompatibleTest extends ApplicationTest {
    @Before
    public void ensureNotCi() {
        assumeTrue(SystemTray.isSupported());
        assumeTrue(Desktop.isDesktopSupported());
        System.out.println("Headed env, executing AWT tests.");
    }

    @Override
    public void start(Stage stage) {
        // since AWT is mostly called in conjunction with JavaFX it's better to make sure it's initialized
    }
}
