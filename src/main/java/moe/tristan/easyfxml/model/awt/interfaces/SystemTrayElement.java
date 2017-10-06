package moe.tristan.easyfxml.model.awt.interfaces;

import java.awt.MenuItem;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.Map;

public interface SystemTrayElement {

    String getLabel();

    URL getIcon();

    Map<MenuItem, ActionListener> getMenuItems();
}
