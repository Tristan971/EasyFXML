package moe.tristan.easyfxml.model.components.listview;

import org.springframework.context.ApplicationContext;
import moe.tristan.easyfxml.api.FxmlController;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.util.function.Supplier;

@SuppressWarnings("WeakerAccess")
public abstract class CustomListViewBase<T> implements FxmlController {

    @FXML
    protected ListView<T> listView;

    protected final ApplicationContext applicationContext;
    protected final Supplier<CustomListViewCellBase<T>> cellSupplier;

    public CustomListViewBase(
        ApplicationContext applicationContext,
        Class<? extends CustomListViewCellBase<T>> customCellClass
    ) {
        this.applicationContext = applicationContext;
        this.cellSupplier = () -> applicationContext.getBean(customCellClass);
    }

    @Override
    public void initialize() {
        setCustomCellFactory();
    }

    private void setCustomCellFactory() {
        listView.setCellFactory(list -> cellSupplier.get());
    }

}
