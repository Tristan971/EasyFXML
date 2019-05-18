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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import javafx.scene.control.DatePicker;
import javafx.scene.layout.Pane;

import moe.tristan.easyfxml.EasyFxml;
import moe.tristan.easyfxml.junit.FxNodeTest;
import moe.tristan.easyfxml.samples.form.user.model.ImmutableUserForm;
import moe.tristan.easyfxml.samples.form.user.model.UserCreationService;
import moe.tristan.easyfxml.samples.form.user.model.UserForm;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UserFormComponentTest extends FxNodeTest {

    @Autowired
    private EasyFxml easyFxml;

    @Autowired
    private UserFormComponent userFormComponent;

    @MockBean
    private UserCreationService userCreationService;

    private Pane userFormPane;

    @Before
    public void setUp() {
        userFormPane = easyFxml.loadNode(userFormComponent).getNodeOrExceptionPane();
    }

    @Test
    public void checkSubmitsOnAllValid() {
        final UserForm expectedUserForm = ImmutableUserForm
            .builder()
            .firstName("Firstname")
            .lastName("Lastname")
            .emailAddress("something@email.com")
            .birthdate(LocalDate.now().minusYears(15))
            .build();

        withNodes(userFormPane)
            .willDo(
                () -> clickOn("#firstNameField").write(expectedUserForm.getFirstName()),
                () -> clickOn("#lastNameField").write(expectedUserForm.getLastName()),
                () -> lookup("#datePicker").queryAs(DatePicker.class).setValue(expectedUserForm.getBirthdate()),
                () -> clickOn("#emailField").write(expectedUserForm.getEmailAddress())
            ).run();

        clickOn("#submitButton");

        final ArgumentCaptor<UserForm> submittedFormCaptor = ArgumentCaptor.forClass(UserForm.class);
        verify(userCreationService).submitUserForm(submittedFormCaptor.capture());

        assertThat(submittedFormCaptor.getValue()).isEqualTo(expectedUserForm);
    }

}
