package moe.tristan.easyfxml.util;

import moe.tristan.easyfxml.api.FxmlStylesheet;
import moe.tristan.easyfxml.spring.InheritedComponent;

@InheritedComponent
public abstract class CachedFxmlStylesheet implements FxmlStylesheet {

    private String cachedInstance = null;

    public abstract String loadStylesheet();

    @Override
    public String getStyle() {
        return cachedInstance == null ? cachedInstance = loadStylesheet() : cachedInstance;
    }
}
