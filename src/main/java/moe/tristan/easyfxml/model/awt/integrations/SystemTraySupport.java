package moe.tristan.easyfxml.model.awt.integrations;

import io.vavr.control.Try;
import moe.tristan.easyfxml.model.awt.AwtRequired;
import moe.tristan.easyfxml.model.awt.interfaces.SystemTrayIcon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static io.vavr.API.unchecked;

@Component
public class SystemTraySupport implements AwtRequired {

    private final AtomicReference<SystemTray> awtSystemTray;
    private final Toolkit awtToolkit;

    @Autowired
    public SystemTraySupport(final SystemTray awtSystemTray, final Toolkit awtToolkit) {
        this.awtSystemTray = new AtomicReference<>(awtSystemTray);
        this.awtToolkit = awtToolkit;
    }

    public Try<TrayIcon> registerTrayIcon(final SystemTrayIcon systemTrayIcon) {
        return Try.of(() -> this.mapTrayIcon(systemTrayIcon))
            .map(unchecked(trayIcon -> {
                this.awtSystemTray.get().add(trayIcon);
                return trayIcon;
            }));
    }

    public void removeTrayIcon(final TrayIcon trayIcon) {
        this.awtSystemTray.get().remove(trayIcon);
    }

    public List<TrayIcon> getTrayIcons() {
        return Arrays.stream(this.awtSystemTray.get().getTrayIcons()).collect(Collectors.toList());
    }

    @Override
    public boolean isSupported() {
        return SystemTray.isSupported();
    }

    private TrayIcon mapTrayIcon(final SystemTrayIcon systemTrayIcon) {
        final PopupMenu popupMenu = new PopupMenu();
        systemTrayIcon.getMenuItems().entrySet().stream()
            .map(entry -> {
                entry.getKey().addActionListener(entry.getValue());
                return entry.getKey();
            })
            .forEach(popupMenu::add);

        final Image iconImage = this.awtToolkit.getImage(systemTrayIcon.getIcon());

        return new TrayIcon(iconImage, systemTrayIcon.getLabel(), popupMenu);
    }
}
