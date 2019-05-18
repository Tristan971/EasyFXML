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

package moe.tristan.easyfxml.fxkit.form;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import moe.tristan.easyfxml.api.FxmlController;

public abstract class FormController implements FxmlController {

    private Map<String, FormFieldController> formFieldControllers = new ConcurrentHashMap<>();

    public void addFormField(FormFieldController formField) {
        formFieldControllers.put(formField.getFieldName(), formField);
    }

    public String getField(String fieldName) {
        return getFieldAs(fieldName, String.class);
    }

    public <T> T getFieldAs(String formFieldName, Class<T> fieldValueType) {
        //noinspection unchecked
        return (T) formFieldControllers.get(formFieldName).getFieldValue();
    }

    public abstract void submit();

}