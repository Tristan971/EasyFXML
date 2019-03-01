package moe.tristan.easyfxml.model.fxml;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import moe.tristan.easyfxml.api.FxmlController;

@Scope(SCOPE_PROTOTYPE)
public class SAMPLE_CONTROL_CLASS implements FxmlController {

    private static final Logger LOG = LoggerFactory.getLogger(SAMPLE_CONTROL_CLASS.class);

    @FXML
    private Button button;

    public boolean locatedInstance = false;

    @Override
    public void initialize() {
        this.button.setOnAction(this::onActionHandler);
    }

    @SuppressWarnings("unused")
    private void onActionHandler(final Event event) {
        this.locatedInstance = true;
        LOG.debug("<HANDLED ACTION ON INSTANCE : {}>", this);
    }

}
