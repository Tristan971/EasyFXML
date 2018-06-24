package moe.tristan.easyfxml.model.components.listview.view;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;
import moe.tristan.easyfxml.model.components.listview.ComponentListViewFxmlController;
import moe.tristan.easyfxml.model.components.listview.cell.ComponentListCellSample;

import javafx.beans.property.ReadOnlyListWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

@Component
public class ComponentListViewSampleFxmlController extends ComponentListViewFxmlController<String> {

    private final ObservableList<String> values = FXCollections.observableArrayList();

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

}
