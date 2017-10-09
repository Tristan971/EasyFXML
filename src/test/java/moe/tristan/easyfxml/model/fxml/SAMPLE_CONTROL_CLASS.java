package moe.tristan.easyfxml.model.fxml;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import moe.tristan.easyfxml.model.FxmlController;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

@Component
@Scope(SCOPE_PROTOTYPE)
public class SAMPLE_CONTROL_CLASS implements FxmlController {

    @FXML
    private Button button;

    public boolean hasBeenClicked = false;

    @Override
    public void initialize() {
        this.button.setOnMouseClicked(this::hasClickedHandler);
    }

    private void hasClickedHandler(final MouseEvent event) {
        System.out.println("<CLICK RECORDED on instance : "+this.toString()+" >");
        this.hasBeenClicked = true;
    }
}
