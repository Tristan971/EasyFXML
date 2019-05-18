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

package moe.tristan.easyfxml.samples.form.user.view.userform.fields.birthday;

import java.time.LocalDate;
import java.time.Period;

import org.springframework.stereotype.Component;

import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;

import moe.tristan.easyfxml.fxkit.form.FormFieldController;

@Component
public class BirthdayController extends FormFieldController<LocalDate> {

    static final String ERROR_EMPTY_BIRTHDATE = "You must provide an email address";
    static final String ERROR_LESS_13YO_BIRTHDATE = "You must be at least 13 years old";

    public DatePicker datePicker;
    public Label errorText;

    @Override
    public void initialize() {
        datePicker.valueProperty().addListener((o, prev, cur) -> validate(cur));
    }

    @Override
    public boolean validate(LocalDate fieldValue) {
        if (fieldValue == null) {
            onInvalid(ERROR_EMPTY_BIRTHDATE);
            return false;
        }

        boolean isAtLeast13 = Period.between(fieldValue, LocalDate.now()).getYears() >= 13;
        if (isAtLeast13) {
            onValid();
            return true;
        } else {
            onInvalid(ERROR_LESS_13YO_BIRTHDATE);
            return false;
        }
    }

    @Override
    public void onValid() {
        errorText.setVisible(false);
    }

    @Override
    public void onInvalid(String reason) {
        errorText.setText(reason);
        errorText.setVisible(true);
    }

    @Override
    public String getFieldName() {
        return BirthdayComponent.BIRTHDATE_FIELD_NAME;
    }

    @Override
    public LocalDate getFieldValue() {
        return datePicker.getValue();
    }

}
