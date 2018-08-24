package moe.tristan.easyfxml.model.awt.integrations;

import org.springframework.stereotype.Component;
import moe.tristan.easyfxml.model.awt.AwtUtils;
import io.vavr.control.Try;

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

/**
 * Allows creation/management of a custom system tray icon.
 *
 * @deprecated Check out Dorkbox's SystemTray library for that matter. It is much more resilient.
 */
@Deprecated
@Component
public class SystemTraySupport {

    /**
     * Loads the tray icon.
     *
     * @param systemTrayIcon The icon to load
     *
     * @return A {@link CompletionStage} that returns the awt-typed reference to the icon. This is to be kept if you
     * wish to call {@link #removeTrayIcon(TrayIcon)} later.
     */
    public CompletionStage<Try<TrayIcon>> registerTrayIcon(final SystemTrayIcon systemTrayIcon) {
        return this.mapTrayIcon(systemTrayIcon)
                   .thenCompose(trayIcon -> AwtUtils.asyncAwtCallbackWithRequirement(
                           SystemTray::getSystemTray,
                           systemTray -> Try.of(() -> {
                               systemTray.add(trayIcon);
                               return trayIcon;
                           })
                   )).thenApplyAsync(trayIconRes -> trayIconRes.map(icon -> {
                    icon.setImageAutoSize(true);
                    icon.addMouseListener(systemTrayIcon.onMouseClickListener());
                    return icon;
                }));
    }

    /**
     * Removes the tray icon previously added.
     *
     * @param trayIcon The previously added TrayIcon (see {@link #registerTrayIcon(SystemTrayIcon)}
     *
     * @return A {@link CompletionStage} that finished upon effective removal.
     */
    public CompletionStage<Void> removeTrayIcon(final TrayIcon trayIcon) {
        return AwtUtils.asyncAwtRunnableWithRequirement(
                SystemTray::getSystemTray,
                systemTray -> systemTray.remove(trayIcon)
        );
    }

    /**
     * @return the currently owned and registered tray icons.
     */
    public List<TrayIcon> getTrayIcons() {
        final CompletionStage<TrayIcon[]> trayIconsAwait = AwtUtils.asyncAwtCallbackWithRequirement(
                SystemTray::getSystemTray,
                SystemTray::getTrayIcons
        );

        TrayIcon[] icons = new TrayIcon[0];
        try {
            icons = trayIconsAwait.toCompletableFuture().get(2, TimeUnit.SECONDS);
        } catch (Exception ignored) {
            // system issue
        }

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
