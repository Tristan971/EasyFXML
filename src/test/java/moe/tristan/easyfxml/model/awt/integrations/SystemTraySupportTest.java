package moe.tristan.easyfxml.model.awt.integrations;

import io.vavr.control.Try;
import moe.tristan.easyfxml.CIIncompatibleTest;
import moe.tristan.easyfxml.model.awt.AwtAccess;
import moe.tristan.easyfxml.model.awt.interfaces.SystemTrayIcon;
import moe.tristan.easyfxml.spring.SpringContext;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.awt.*;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * This test is wholly incomplete.
 * Consider it absent for now.
 */
@ContextConfiguration(classes = SpringContext.class)
@RunWith(SpringRunner.class)
public class SystemTraySupportTest extends CIIncompatibleTest {

    private static final String TRAY_LABEL = "TEST_LABEL";
    private static final URL TRAY_ICON_URL = getTrayIcon();

    @Autowired
    private SystemTraySupport systemTraySupport;

    @BeforeClass
    public static void enableAwt() {
        AwtAccess.enableAwt();
    }

    @Test
    public void awt_system_tray_supported_on_local_testing() {
        assertThat(this.systemTraySupport.isSupported()).isTrue();
    }

    @Test
    public void register_system_tray_icon() {
        final MenuItem testItem = new MenuItem("DISABLED_ON_CLICK");
        final ActionListener disableOnClick = e -> testItem.setEnabled(false);

        final Map<MenuItem, ActionListener> menuItems = new HashMap<MenuItem, ActionListener>() {{
            this.put(testItem, disableOnClick);
        }};

        final SystemTrayIcon trayIcon = this.systemTrayIconWith(menuItems);
        final Try<TrayIcon> registrationResult = this.systemTraySupport.registerTrayIcon(trayIcon);
        assertThat(registrationResult.isSuccess()).isTrue();
        registrationResult.andThen(loadedIcon -> {
            assertThat(this.systemTraySupport.getTrayIcons()).containsExactly(loadedIcon);
            this.systemTraySupport.removeTrayIcon(loadedIcon);
            assertThat(this.systemTraySupport.getTrayIcons()).isEmpty();
        });
    }

    private SystemTrayIcon systemTrayIconWith(final Map<MenuItem, ActionListener> menuItems) {
        return new SystemTrayIcon() {
            @Override
            public String getLabel() {
                return TRAY_LABEL;
            }

            @Override
            public URL getIcon() {
                return TRAY_ICON_URL;
            }

            @Override
            public Map<MenuItem, ActionListener> getMenuItems() {
                return menuItems;
            }
        };
    }

    private static URL getTrayIcon() {
        return SystemTraySupportTest.class.getClassLoader().getResource("images/duke.jpg");
    }
}
