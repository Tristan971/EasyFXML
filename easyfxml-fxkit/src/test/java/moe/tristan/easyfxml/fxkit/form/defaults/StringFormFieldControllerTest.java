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

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;

public class StringFormFieldControllerTest {

    private Property<String> sampleProp;
    private AtomicInteger validationCount;

    private StringFormFieldController sampleFieldController;

    @BeforeEach
    public void setUp() {
        sampleProp = new SimpleStringProperty();
        validationCount = new AtomicInteger(0);
        sampleFieldController = new StringFormFieldController() {
            @Override
            public ObservableValue<String> getObservableValue() {
                return sampleProp;
            }

            @Override
            public String getFieldName() {
                return "Sample string field controller";
            }

            @Override
            public boolean validate(String fieldValue) {
                validationCount.incrementAndGet();
                return true;
            }
        };
    }

    @Test
    public void validatesOnEveryPropertyChangeByDefault() {
        sampleFieldController.initialize(); // simulate JavaFX

        assertThat(validationCount.get()).isEqualTo(0);

        sampleProp.setValue("new value");

        assertThat(validationCount.get()).isEqualTo(1);
    }

    @Test
    public void isNullOrBlankMatches() {
        Stream.of(null, "", "\t \n  ").forEach(nullOrBlank -> {
            sampleProp.setValue(nullOrBlank);
            assertThat(sampleFieldController.isNullOrBlank()).isTrue();
        });

        sampleProp.setValue("Non null nor empty/blank string");
        assertThat(sampleFieldController.isNullOrBlank()).isFalse();
    }

}
