package moe.tristan.easyfxml.model.awt.integrations;

import java.awt.MenuItem;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.Map;

/**
 * A SystemTrayIcon is a UI object owned by this application that lies in the operating system's (if supported) tray.
 * <p>
 * On macOS it's the upper-right of the status bar, on Windows the bottom right of the taskbar and on most Linux
 * distributions that are Gnome/KDE-based it's an on-screen system tray.
 */
public interface SystemTrayIcon {

    /**
     * @return The tooltip's text for the icon
     */
    String getLabel();

    /**
     * @return The resource to load as an icon.
     * <p>
     * Must be validly usable with {@link java.awt.Toolkit#getImage(URL)}.
     */
    URL getIcon();

    /**
     * @return The elements that the user sees on click.
     */
    Map<MenuItem, ActionListener> getMenuItems();

}
