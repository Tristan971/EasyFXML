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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

import org.junit.Before;
import org.junit.Test;

public class FormControllerTest {

    private static final String INVALID_FIELD_NAME = "invalid_field";
    private static final Object INVALID_FIELD_VALUE = null;
    private static final FormFieldController INVALID_FIELD = formFieldWith(
        INVALID_FIELD_NAME,
        INVALID_FIELD_VALUE,
        __ -> false
    );

    private static final String VALID_FIELD_NAME = "valid_field";
    private static final Object VALID_FIELD_VALUE = new Object();
    private static final FormFieldController VALID_FIELD = formFieldWith(
        VALID_FIELD_NAME,
        VALID_FIELD_VALUE,
        __ -> true
    );


    private AtomicBoolean initialized;
    private AtomicInteger submissions;
    private FormController formController;

    @Before
    public void setUp() {
        initialized = new AtomicBoolean(false);
        submissions = new AtomicInteger(0);
        formController = new FormController() {
            @Override
            public void submit() {
                submissions.incrementAndGet();
            }

            @Override
            public void initialize() {
                initialized.set(true);
            }
        };

        formController.subscribeToField(VALID_FIELD);
        formController.subscribeToField(INVALID_FIELD);
    }

    @Test
    public void findingInvalidFieldsReturnsInvalidOnes() {
        assertThat(formController.findInvalidFields()).containsExactly(INVALID_FIELD);
    }

    @Test
    public void onlyTracksValuesOfSubscribedFields() {
        formController.unsubscribeToField(INVALID_FIELD);

        assertThat((Object) formController.getField(VALID_FIELD_NAME)).isSameAs(VALID_FIELD_VALUE);

        assertThatThrownBy(() -> formController.getField(INVALID_FIELD_NAME))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining(INVALID_FIELD_NAME) // include invalid field name looked up
            .hasMessageContaining(VALID_FIELD_NAME);  // include valid field names that were available
    }

    private static FormFieldController formFieldWith(String name, Object value, Predicate valid) {
        return new FormFieldController() {
            @Override
            public String getFieldName() {
                return name;
            }

            @Override
            public Object getFieldValue() {
                return value;
            }

            @Override
            public void initialize() {
            }

            @Override
            public boolean validate(Object fieldValue) {
                return valid.test(fieldValue);
            }
        };
    }

}
