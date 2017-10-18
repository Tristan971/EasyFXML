package moe.tristan.easyfxml.model.awt.integrations;

import java.awt.MenuItem;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.Map;

public interface SystemTrayIcon {

    String getLabel();

    URL getIcon();

    Map<MenuItem, ActionListener> getMenuItems();
}
