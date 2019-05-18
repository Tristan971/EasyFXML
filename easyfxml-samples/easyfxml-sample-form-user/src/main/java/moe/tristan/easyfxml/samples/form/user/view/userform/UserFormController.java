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

import static moe.tristan.easyfxml.util.Buttons.setOnClick;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import moe.tristan.easyfxml.EasyFxml;
import moe.tristan.easyfxml.fxkit.form.FormController;
import moe.tristan.easyfxml.samples.form.user.model.ImmutableUserForm;
import moe.tristan.easyfxml.samples.form.user.model.UserForm;
import moe.tristan.easyfxml.samples.form.user.view.userform.fields.firstname.FirstnameComponent;
import moe.tristan.easyfxml.samples.form.user.view.userform.fields.firstname.FirstnameController;

@Component
public class UserFormController extends FormController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserFormController.class);

    private final EasyFxml easyFxml;

    private final FirstnameComponent firstnameComponent;

    public Label titleLabel;
    public VBox fieldsBox;
    public Button submitButton;

    public UserFormController(EasyFxml easyFxml, FirstnameComponent firstnameComponent) {
        this.easyFxml = easyFxml;
        this.firstnameComponent = firstnameComponent;
    }

    @Override
    public void initialize() {
        setOnClick(submitButton, this::submit);

        easyFxml.loadNode(firstnameComponent, VBox.class, FirstnameController.class)
                .afterControllerLoaded(this::addFormField)
                .afterNodeLoaded(fieldsBox.getChildren()::add);
    }

    @Override
    public void submit() {
        UserForm userForm = ImmutableUserForm
            .builder()
            .firstName(getField("First name"))
            .build();

        LOGGER.info("Submitting user form {}", userForm);
    }

}
