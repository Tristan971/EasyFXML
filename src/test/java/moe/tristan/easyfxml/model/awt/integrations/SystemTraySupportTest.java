package moe.tristan.easyfxml.model.awt.integrations;

import java.awt.MenuItem;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import io.vavr.control.Try;
import moe.tristan.easyfxml.model.awt.HeadlessIncompatibleTest;
import moe.tristan.easyfxml.spring.SpringContext;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * This test is wholly incomplete. Consider it absent for now.
 */
@ContextConfiguration(classes = SpringContext.class)
@RunWith(SpringRunner.class)
public class SystemTraySupportTest extends HeadlessIncompatibleTest {

    private static final String TRAY_LABEL = "TEST_LABEL";
    private static final URL TRAY_ICON_URL = getTrayIcon();

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    public void register_system_tray_icon() throws ExecutionException, InterruptedException {
        final SystemTraySupport systemTraySupport = this.applicationContext.getBean(SystemTraySupport.class);

        final MenuItem testItem = new MenuItem("DISABLED_ON_CLICK");
        final ActionListener disableOnClick = e -> testItem.setEnabled(false);

        final Map<MenuItem, ActionListener> menuItems = new HashMap<MenuItem, ActionListener>() {{
            this.put(testItem, disableOnClick);
        }};

        final SystemTrayIcon trayIcon = this.systemTrayIconWith(menuItems);
        systemTraySupport.registerTrayIcon(trayIcon)
                         .whenCompleteAsync((res, err) -> assertThat(res.isSuccess()).isTrue())
                         .thenApply(Try::get)
                         .whenCompleteAsync((res, err) -> assertThat(systemTraySupport.getTrayIcons()).containsExactly(
                             res))
                         .whenCompleteAsync((res, err) -> systemTraySupport.removeTrayIcon(res))
                         .thenAccept(icon -> assertThat(systemTraySupport.getTrayIcons()).doesNotContain(icon))
                         .toCompletableFuture().get();
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
