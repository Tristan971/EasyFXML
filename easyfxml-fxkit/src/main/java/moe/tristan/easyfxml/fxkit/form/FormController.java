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

import static java.util.function.Predicate.not;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import moe.tristan.easyfxml.api.FxmlController;

public abstract class FormController implements FxmlController {

    private Map<String, FormFieldController> subscribedFields = new ConcurrentHashMap<>();

    public void subscribeToField(FormFieldController formField) {
        subscribedFields.put(formField.getFieldName(), formField);
    }

    public void unsubscribeToField(FormFieldController formField) {
        final Set<String> toRemove = subscribedFields
            .entrySet()
            .stream()
            .filter(entry -> formField.equals(entry.getValue()))
            .map(Entry::getKey)
            .collect(Collectors.toSet());
        toRemove.forEach(subscribedFields::remove);
    }

    public <T> T getField(String formFieldName) {
        if (!subscribedFields.containsKey(formFieldName)) {
            throw new IllegalArgumentException(
                "Form was not subscribed to any field with name [\"" + formFieldName + "\"].\n"
                + "Subscribed fields: " + subscribedFields.keySet()
            );
        }

        //noinspection unchecked
        return (T) subscribedFields.get(formFieldName).getFieldValue();
    }

    protected List<FormFieldController> findInvalidFields() {
        return subscribedFields.values().stream().filter(not(FormFieldController::isValid)).collect(Collectors.toList());
    }

    public abstract void submit();

}
