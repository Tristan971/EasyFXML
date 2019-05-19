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

package moe.tristan.easyfxml.fxkit.form.defaults;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.beans.value.ObservableValue;

import moe.tristan.easyfxml.fxkit.form.FormFieldController;

public abstract class StringFormFieldController extends FormFieldController<String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(StringFormFieldController.class);

    @Override
    public void initialize() {
        // by default, every change is made to run through validation
        validateValueOnChange();
    }

    public abstract ObservableValue<String> getObservableValue();

    @Override
    public String getFieldValue() {
        return getObservableValue().getValue();
    }

    protected boolean isNullOrBlank() {
        final String fieldValue = getFieldValue();
        return fieldValue == null || fieldValue.isBlank();
    }

    private void validateValueOnChange() {
        getObservableValue().addListener((o, prev, cur) -> {
            LOGGER.debug("Validating field [{}] -> \"{}\"", getFieldName(), getFieldValue());
            validate(cur);
        });
    }

}
