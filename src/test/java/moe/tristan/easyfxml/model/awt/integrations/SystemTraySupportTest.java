package moe.tristan.easyfxml.model.awt.integrations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import moe.tristan.easyfxml.model.awt.objects.OnMouseClickListener;
import moe.tristan.easyfxml.spring.application.FxSpringContext;
import io.vavr.control.Try;
import org.junit.Test;
import org.junit.runner.RunWith;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.awt.MenuItem;
import java.awt.TrayIcon;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * This test is wholly incomplete. Consider it absent for now.
 */
@ContextConfiguration(classes = FxSpringContext.class)
@RunWith(SpringRunner.class)
public class SystemTraySupportTest {

    private static final String TRAY_LABEL = "TEST_LABEL";
    private static final URL TRAY_ICON_URL = getTrayIcon();

    private BooleanProperty clickRegistered = new SimpleBooleanProperty(false);

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    public void register_system_tray_icon() throws ExecutionException, InterruptedException, TimeoutException {
        final SystemTraySupport systemTraySupport = this.applicationContext.getBean(SystemTraySupport.class);

        final MenuItem testItem = new MenuItem("DISABLED_ON_CLICK");
        final ActionListener disableOnClick = e -> testItem.setEnabled(false);

        final Map<MenuItem, ActionListener> menuItems = new HashMap<>();
        menuItems.put(testItem, disableOnClick);

        final SystemTrayIcon sysTrayIcon = this.systemTrayIconWith(menuItems);
        final CompletionStage<Try<TrayIcon>> asyncRegistrationRes = systemTraySupport.registerTrayIcon(sysTrayIcon);
        final Try<TrayIcon> registrationRes = asyncRegistrationRes.toCompletableFuture().get(5, SECONDS);

        assertThat(registrationRes.isSuccess()).isTrue();
        assertThat(systemTraySupport.getTrayIcons()).containsExactly(registrationRes.get());

        registrationRes.map(trayIcon -> {
            assertThat(trayIcon.getMouseListeners()).hasSize(1);
            return trayIcon.getMouseListeners()[0];
        }).andThenTry(mouseListener -> {
            assertThat(mouseListener).isSameAs(sysTrayIcon.onMouseClickListener());
            mouseListener.mousePressed(null);
            assertThat(clickRegistered.getValue()).isTrue();
        });

        final CompletionStage<Void> asyncRemove = systemTraySupport.removeTrayIcon(registrationRes.get());
        asyncRemove.toCompletableFuture().get(5, SECONDS);

        assertThat(systemTraySupport.getTrayIcons()).doesNotContain(registrationRes.get());
    }

    private SystemTrayIcon systemTrayIconWith(final Map<MenuItem, ActionListener> menuItems) {

        final MouseListener onMouseClickListener = new OnMouseClickListener(e -> clickRegistered.setValue(true));

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

            @Override
            public MouseListener onMouseClickListener() {
                return onMouseClickListener;
            }
        };
    }

    private static URL getTrayIcon() {
        return SystemTraySupportTest.class.getClassLoader().getResource("images/duke.jpg");
    }
}
