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

import org.junit.Test;

public class FormFieldControllerTest {

    @Test
    public void defaultValidationIsNoop() {
        final FormFieldController sampleFieldController = new FormFieldController() {

            @Override
            public void initialize() {

            }

            @Override
            public String getFieldName() {
                return "Sample";
            }

            @Override
            public Object getFieldValue() {
                return null;
            }

            @Override
            public void onValid() {
                throw new RuntimeException("Should not be called by default!");
            }

            @Override
            public void onInvalid(String reason) {
                throw new RuntimeException("Should not be called by default!");
            }
        };

        assertThat(sampleFieldController.validate(null)).isTrue();
    }

}
