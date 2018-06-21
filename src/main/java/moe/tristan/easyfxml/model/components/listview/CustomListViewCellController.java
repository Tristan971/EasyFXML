package moe.tristan.easyfxml.model.components.listview;

import moe.tristan.easyfxml.api.FxmlController;

import javafx.beans.value.ObservableValue;

public interface CustomListViewCellController<T> extends FxmlController {

    void updateWithValue(final T newValue);
    <U extends ObservableValue<Boolean>> void selectedProperty(final U selected);

}
