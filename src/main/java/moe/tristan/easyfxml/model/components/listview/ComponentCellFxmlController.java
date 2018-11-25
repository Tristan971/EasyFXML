package moe.tristan.easyfxml.model.components.listview;

import javafx.beans.binding.BooleanExpression;

import moe.tristan.easyfxml.api.FxmlController;

public interface ComponentCellFxmlController<T> extends FxmlController {

    void updateWithValue(final T newValue);

    /**
     * @param selected a property watching whether this list view cell is currently selected. You can listen on it and react accordingly.
     */
    default void selectedProperty(final BooleanExpression selected) {
    }

}
