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

import static moe.tristan.easyfxml.model.exception.ExceptionHandler.displayExceptionPane;
import static moe.tristan.easyfxml.samples.form.user.view.userform.fields.birthday.BirthdayComponent.BIRTHDATE_FIELD_NAME;
import static moe.tristan.easyfxml.samples.form.user.view.userform.fields.email.EmailComponent.EMAIL_FIELD_NAME;
import static moe.tristan.easyfxml.samples.form.user.view.userform.fields.firstname.FirstnameComponent.FIRST_NAME_FIELD_NAME;
import static moe.tristan.easyfxml.samples.form.user.view.userform.fields.lastname.LastnameComponent.LAST_NAME_FIELD_NAME;
import static moe.tristan.easyfxml.util.Buttons.setOnClick;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import moe.tristan.easyfxml.EasyFxml;
import moe.tristan.easyfxml.fxkit.form.FormController;
import moe.tristan.easyfxml.fxkit.form.FormFieldController;
import moe.tristan.easyfxml.samples.form.user.model.ImmutableUserForm;
import moe.tristan.easyfxml.samples.form.user.model.UserCreationService;
import moe.tristan.easyfxml.samples.form.user.model.UserForm;
import moe.tristan.easyfxml.samples.form.user.view.userform.fields.birthday.BirthdayComponent;
import moe.tristan.easyfxml.samples.form.user.view.userform.fields.email.EmailComponent;
import moe.tristan.easyfxml.samples.form.user.view.userform.fields.firstname.FirstnameComponent;
import moe.tristan.easyfxml.samples.form.user.view.userform.fields.lastname.LastnameComponent;

@Component
public class UserFormController extends FormController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserFormController.class);

    private final EasyFxml easyFxml;
    private final UserCreationService userCreationService;

    private final FirstnameComponent firstnameComponent;
    private final LastnameComponent lastnameComponent;
    private final BirthdayComponent birthdayComponent;
    private final EmailComponent emailComponent;

    public Label titleLabel;
    public VBox fieldsBox;
    public Button submitButton;

    public UserFormController(
        EasyFxml easyFxml,
        UserCreationService userCreationService,
        FirstnameComponent firstnameComponent,
        LastnameComponent lastnameComponent,
        BirthdayComponent birthdayComponent,
        EmailComponent emailComponent
    ) {
        this.easyFxml = easyFxml;
        this.userCreationService = userCreationService;
        this.firstnameComponent = firstnameComponent;
        this.lastnameComponent = lastnameComponent;
        this.birthdayComponent = birthdayComponent;
        this.emailComponent = emailComponent;
    }

    @Override
    public void initialize() {
        setOnClick(submitButton, this::submit);

        Stream.of(firstnameComponent, lastnameComponent, birthdayComponent, emailComponent)
              .map(field -> easyFxml.loadNode(field, VBox.class, FormFieldController.class))
              .forEach(load -> load
                  .afterControllerLoaded(this::subscribeToField)
                  .afterNodeLoaded(fieldsBox.getChildren()::add)
              );
    }

    @Override
    public void submit() {
        final List<FormFieldController> invalidFields = findInvalidFields();
        if (!invalidFields.isEmpty()) {
            List<String> invalidFieldNames = invalidFields.stream().map(FormFieldController::getFieldName).collect(Collectors.toList());

            displayExceptionPane(
                "Invalid fields",
                "Some fields were not valid: " + invalidFieldNames.toString(),
                new IllegalStateException("Some fields were not valid: " + invalidFieldNames.toString())
            );

            return;
        }

        UserForm userForm = ImmutableUserForm
            .builder()
            .firstName(getField(FIRST_NAME_FIELD_NAME))
            .lastName(getField(LAST_NAME_FIELD_NAME))
            .birthdate(getField(BIRTHDATE_FIELD_NAME))
            .emailAddress(getField(EMAIL_FIELD_NAME))
            .build();

        userCreationService.submitUserForm(userForm);
    }

}
