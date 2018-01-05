package moe.tristan.easyfxml.model.styles;

import moe.tristan.easyfxml.api.FxmlStylesheet;

public abstract class CachedFxmlStylesheet implements FxmlStylesheet {

    private String cachedInstance = null;

    protected abstract String loadStylesheet();

    @Override
    public String getStyle() {
        return cachedInstance == null ? cachedInstance = loadStylesheet() : cachedInstance;
    }

}
