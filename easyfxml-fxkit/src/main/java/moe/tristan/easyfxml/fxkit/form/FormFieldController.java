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

import moe.tristan.easyfxml.api.FxmlController;

public abstract class FormFieldController<F> implements FxmlController {

    public abstract String getFieldName();

    public abstract F getFieldValue();

    public void validate(F fieldValue) {
        // noop by default
    }

    public void onValid() {
        // noop by default
    }

    public void onInvalid(String reason) {
        // noop by default
    }

}
