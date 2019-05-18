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

package moe.tristan.easyfxml.samples.form.user.view.userform.fields.firstname;

import static moe.tristan.easyfxml.util.Properties.whenPropertyIsSet;

import java.util.Optional;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import moe.tristan.easyfxml.fxkit.form.FormFieldController;

@Component
public class FirstnameController extends FormFieldController<String> {

    private static final Pattern NO_NUMBERS_PATTERN = Pattern.compile("(\\b[^\\d]+\\b)+");

    public TextField firstNameField;
    public Label invalidLabel;

    @Override
    public void initialize() {
        whenPropertyIsSet(firstNameField.textProperty(), this::validate);
    }

    @Override
    public void validate(String fieldValue) {
        if (NO_NUMBERS_PATTERN.matcher(fieldValue).matches()) {
            onInvalid("Name has digits or trailing spaces");
        }
    }

    @Override
    public void onValid() {
        invalidLabel.setVisible(false);
    }

    @Override
    public void onInvalid(String reason) {
        invalidLabel.setText(reason);
        invalidLabel.setVisible(true);
    }

    @Override
    public String getFieldName() {
        return "First name";
    }

    @Override
    public Class<? extends String> getFieldType() {
        return String.class;
    }

    @Override
    public Optional<String> getFieldValue() {
        return Optional.ofNullable(firstNameField.getText());
    }

}
