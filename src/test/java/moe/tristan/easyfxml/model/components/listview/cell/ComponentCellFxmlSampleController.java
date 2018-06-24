package moe.tristan.easyfxml.model.components.listview.cell;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import moe.tristan.easyfxml.model.components.listview.ComponentCellFxmlController;

import javafx.application.Platform;
import javafx.beans.binding.BooleanExpression;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

@Component
@Scope(scopeName = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ComponentCellFxmlSampleController implements ComponentCellFxmlController<String> {

    @FXML
    private Button testButton;

    @Override
    public void updateWithValue(final String newValue) {
        Platform.runLater(() -> testButton.setText(newValue));
    }

    @Override
    public void selectedProperty(final BooleanExpression selected) {
        Platform.runLater(() -> testButton.disableProperty().bind(selected.not()));
    }

    @Override
    public void initialize() {
        System.out.println("Initialized cell " + this.toString());
    }

}
