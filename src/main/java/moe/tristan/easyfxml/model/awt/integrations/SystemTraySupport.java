package moe.tristan.easyfxml.model.awt.integrations;

import io.vavr.control.Try;
import moe.tristan.easyfxml.model.awt.AwtUtils;
import org.springframework.stereotype.Component;

import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class SystemTraySupport {

    public CompletionStage<Try<TrayIcon>> registerTrayIcon(final SystemTrayIcon systemTrayIcon) {
        return this.mapTrayIcon(systemTrayIcon).thenCompose(trayIcon ->
            AwtUtils.asyncAwtCallbackWithRequirement(
                SystemTray::getSystemTray,
                systemTray -> Try.of(() -> {
                    systemTray.add(trayIcon);
                    return trayIcon;
                })
        ));
    }

    public CompletionStage<Void> removeTrayIcon(final TrayIcon trayIcon) {
        return AwtUtils.asyncAwtRunnableWithRequirement(
            SystemTray::getSystemTray,
            systemTray -> systemTray.remove(trayIcon)
        );
    }

    public List<TrayIcon> getTrayIcons() {
        final CompletionStage<TrayIcon[]> trayIconsAwait = AwtUtils.asyncAwtCallbackWithRequirement(
            SystemTray::getSystemTray,
            SystemTray::getTrayIcons
        );

        TrayIcon[] icons = new TrayIcon[0];
        try {
            icons = trayIconsAwait.toCompletableFuture().get(2, TimeUnit.SECONDS);
        } catch (Exception ignored) {}

        return Arrays.stream(icons).collect(Collectors.toList());
    }

    private CompletionStage<TrayIcon> mapTrayIcon(final SystemTrayIcon systemTrayIcon) {
        return AwtUtils.asyncAwtCallbackWithRequirement(
            java.awt.Toolkit::getDefaultToolkit,
            toolkit -> {
                final PopupMenu popupMenu = registerPopUpMenuListeners(systemTrayIcon.getMenuItems());
                final Image iconImage = toolkit.getImage(systemTrayIcon.getIcon());
                return new TrayIcon(iconImage, systemTrayIcon.getLabel(), popupMenu);
            }
        );
    }

    private PopupMenu registerPopUpMenuListeners(final Map<MenuItem, ActionListener> items) {
        final PopupMenu popupMenu = new PopupMenu();
        items.forEach((menuItem, actionListener) -> {
            menuItem.addActionListener(actionListener);
            popupMenu.add(menuItem);
        });
        return popupMenu;
    }
}
