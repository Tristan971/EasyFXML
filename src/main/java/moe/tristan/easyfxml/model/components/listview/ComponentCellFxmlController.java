package moe.tristan.easyfxml.model.components.listview;

import moe.tristan.easyfxml.api.FxmlController;

import javafx.beans.binding.BooleanExpression;

public interface ComponentCellFxmlController<T> extends FxmlController {

    void updateWithValue(final T newValue);
    default void selectedProperty(final BooleanExpression selected) {}

}
