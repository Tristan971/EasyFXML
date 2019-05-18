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

package moe.tristan.easyfxml.samples.form.user.view.userform;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import moe.tristan.easyfxml.fxkit.form.FormController;
import moe.tristan.easyfxml.samples.form.user.model.UserForm;

public class UserFormController extends FormController<UserForm> {

    public Label titleLabel;
    public VBox fieldsBox;
    public Button submitButton;

    @Override
    public void initialize() {

    }

    @Override
    public void submit(UserForm form) {

    }

}
