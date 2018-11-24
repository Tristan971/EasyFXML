package moe.tristan.easyfxml.model.components.listview.view;

import org.springframework.context.ConfigurableApplicationContext;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import moe.tristan.easyfxml.model.components.listview.ComponentListViewFxmlController;
import moe.tristan.easyfxml.model.components.listview.cell.ComponentListCellSample;

public class ComponentListViewSampleFxmlController extends ComponentListViewFxmlController<String> {

    private final ObservableList<String> values = FXCollections.observableArrayList();
    public final BooleanProperty scrolledToEnd = new SimpleBooleanProperty(false);

    public ComponentListViewSampleFxmlController(ConfigurableApplicationContext applicationContext) {
        super(applicationContext, ComponentListCellSample.class);
    }

    @Override
    public void initialize() {
        super.initialize();
        listView.itemsProperty().bind(new ReadOnlyListWrapper<>(values));
    }

    public void addValue(final String value) {
        values.add(value);
    }

    @Override
    protected void onScrolledToEndOfListView() {
        scrolledToEnd.setValue(true);
    }
}
