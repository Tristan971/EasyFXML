package moe.tristan.easyfxml.model.components.listview.cell;

import java.util.concurrent.atomic.AtomicReference;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import javafx.application.Platform;
import javafx.beans.binding.BooleanExpression;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import moe.tristan.easyfxml.model.components.listview.ComponentCellFxmlController;

@Scope(scopeName = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ComponentCellFxmlSampleController implements ComponentCellFxmlController<String> {

    public static final AtomicReference<Button> REMOTE_REF = new AtomicReference<>(null);

    @FXML
    private Button testButton;

    @Override
    public void updateWithValue(final String newValue) {
        if (newValue == null) {
            return;
        }

        REMOTE_REF.set(testButton);
        Platform.runLater(() -> testButton.setText(newValue));
    }

    @Override
    public void selectedProperty(final BooleanExpression selected) {
        Platform.runLater(() -> testButton.disableProperty().bind(selected.not()));
    }

    @Override
    public void initialize() {
    }

}
