/*
 * Copyright 2017 - 2019 EasyFXML project and contributors
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package moe.tristan.easyfxml.samples.helloworld.view.hello;

import static java.util.function.Predicate.not;
import static moe.tristan.easyfxml.util.Buttons.setOnClick;

import java.util.Optional;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import moe.tristan.easyfxml.api.FxmlController;

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
        final String safeName = Optional.of(userNameTextField.getText()).filter(not(String::isBlank)).orElse("World");
        greetingName.setText(safeName);
        greetingBox.setVisible(true);
    }

}
