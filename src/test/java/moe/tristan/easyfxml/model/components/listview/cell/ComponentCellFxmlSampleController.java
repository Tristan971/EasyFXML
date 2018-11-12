package moe.tristan.easyfxml.model.components.listview.cell;

import java.util.concurrent.atomic.AtomicReference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javafx.application.Platform;
import javafx.beans.binding.BooleanExpression;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import moe.tristan.easyfxml.model.components.listview.ComponentCellFxmlController;

@Component
@Scope(scopeName = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ComponentCellFxmlSampleController implements ComponentCellFxmlController<String> {

    private static final Logger LOG = LoggerFactory.getLogger(ComponentCellFxmlSampleController.class);

    public static final AtomicReference<Button> LAST_UPD_ITS_UGLY = new AtomicReference<>(null);

    @FXML
    private Button testButton;

    @Override
    public void updateWithValue(final String newValue) {
        if (newValue == null) return;

        LAST_UPD_ITS_UGLY.set(testButton);
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
