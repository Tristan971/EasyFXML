package moe.tristan.easyfxml.samples.helloworld.view.hello;

import static java.util.function.Predicate.not;
import static moe.tristan.easyfxml.util.Buttons.setOnClick;

import java.util.Objects;

import org.springframework.stereotype.Component;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import moe.tristan.easyfxml.api.FxmlController;

import io.vavr.control.Option;

@Component
public class HelloController implements FxmlController {

    @FXML
    private TextField userNameTextField;

    @FXML
    private Button helloButton;

    @FXML
    public HBox greetingBox;

    @FXML
    private Label greetingName;

    @Override
    public void initialize() {
        greetingBox.setVisible(false);
        setOnClick(helloButton, this::greet);
    }

    private void greet() {
        final String safeName = Option.of(userNameTextField.getText()).filter(Objects::nonNull).filter(not(String::isBlank)).getOrElse("World");
        greetingName.setText(safeName);
        greetingBox.setVisible(true);
    }

}
