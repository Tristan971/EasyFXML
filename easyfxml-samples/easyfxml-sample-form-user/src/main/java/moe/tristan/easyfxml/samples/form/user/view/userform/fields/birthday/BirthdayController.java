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

    public DatePicker datePicker;
    public Label errorText;

    @Override
    public void initialize() {
        datePicker.valueProperty().addListener((o, prev, cur) -> validate(cur));
    }

    @Override
    public void validate(LocalDate fieldValue) {
        boolean isAtLeast13 = Period.between(fieldValue, LocalDate.now()).getYears() >= 13;
        if (isAtLeast13) {
            onValid();
        } else {
            onInvalid("You must be at least 13 years old.");
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
        return "Birthdate";
    }

    @Override
    public Class<? extends LocalDate> getFieldType() {
        return LocalDate.class;
    }

    @Override
    public LocalDate getFieldValue() {
        return datePicker.getValue();
    }

}
