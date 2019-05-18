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

package moe.tristan.easyfxml.samples.form.user.view.userform.fields.lastname;

import org.springframework.stereotype.Component;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import moe.tristan.easyfxml.fxkit.form.sample.StringFormFieldController;

@Component
public class LastnameController extends StringFormFieldController {

    static final String ERROR_EMPTY_PROVIDED = "You must provide a last name";

    public TextField lastNameField;
    public Label invalidLabel;

    @Override
    public boolean validate(String fieldValue) {
        if (!fieldValueIsNotBlank(fieldValue)) {
            onInvalid(ERROR_EMPTY_PROVIDED);
            return false;
        } else {
            onValid();
            return true;
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
    public ObservableValue<String> getObservableValue() {
        return lastNameField.textProperty();
    }

    @Override
    public String getFieldName() {
        return LastnameComponent.LAST_NAME_FIELD_NAME;
    }

}
