package moe.tristan.easyfxml.model.awt.integrations;

import io.vavr.control.Try;
import moe.tristan.easyfxml.model.awt.AwtRequired;
import moe.tristan.easyfxml.model.awt.interfaces.SystemTrayElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.concurrent.atomic.AtomicReference;

import static io.vavr.API.unchecked;

@Component
public class SystemTraySupport implements AwtRequired {

    private final AtomicReference<SystemTray> awtSystemTray;
    private final Toolkit awtToolkit;

    @Autowired
    public SystemTraySupport(SystemTray awtSystemTray, Toolkit awtToolkit) {
        this.awtSystemTray = new AtomicReference<>(awtSystemTray);
        this.awtToolkit = awtToolkit;
    }

    public Try<TrayIcon> registerTrayMenu(final SystemTrayElement sysTrayElem) {
        return Try.of(() -> this.mapTrayIcon(sysTrayElem))
            .map(unchecked(trayIcon -> {
                this.awtSystemTray.get().add(trayIcon);
                return trayIcon;
            }));
    }

    @Override
    public boolean isSupported() {
        return SystemTray.isSupported();
    }

    private TrayIcon mapTrayIcon(final SystemTrayElement systemTrayElement) {
        final PopupMenu popupMenu = new PopupMenu();
        systemTrayElement.getMenuItems().entrySet().stream()
            .map(entry -> {
                entry.getKey().addActionListener(entry.getValue());
                return entry.getKey();
            })
            .forEach(popupMenu::add);

        final Image iconImage = this.awtToolkit.getImage(systemTrayElement.getIcon());

        return new TrayIcon(iconImage, systemTrayElement.getLabel(), popupMenu);
    }
}
