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

package moe.tristan.easyfxml.samples.form.user.view.userform.fields.birthday;

import static moe.tristan.easyfxml.samples.form.user.view.userform.fields.birthday.BirthdayController.ERROR_EMPTY_BIRTHDATE;
import static moe.tristan.easyfxml.samples.form.user.view.userform.fields.birthday.BirthdayController.ERROR_LESS_13YO_BIRTHDATE;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import moe.tristan.easyfxml.EasyFxml;
import moe.tristan.easyfxml.junit.FxNodeTest;
import moe.tristan.easyfxml.model.fxml.FxmlLoadResult;

@SpringBootTest
@RunWith(SpringRunner.class)
public class BirthdayComponentTest extends FxNodeTest {

    @Autowired
    private EasyFxml easyFxml;

    @Autowired
    private BirthdayComponent BirthdayComponent;

    private Pane birthdayPane;
    private BirthdayController birthdayController;

    @Before
    public void setUp() {
        final FxmlLoadResult<Pane, BirthdayController> load = easyFxml.loadNode(BirthdayComponent, Pane.class, BirthdayController.class);
        birthdayPane = load.getNodeOrExceptionPane();
        birthdayController = load.getController().get();
    }

    @Test
    public void checkValidBirthday() {
        final LocalDate validBirthday = LocalDate.of(1996, 1, 10);

        withNodes(birthdayPane)
            .willDo(() -> lookup("#datePicker").queryAs(DatePicker.class).setValue(validBirthday))
            .run();

        assertThat(!lookup("#errorText").queryAs(Label.class).isVisible())
            .withFailMessage("Error label is expected to not be visible on valid date!")
            .isTrue();
    }

    @Test
    public void checkEmptyBirthday() {
        withNodes(birthdayPane)
            .andAwaitFor(() -> lookup("#datePicker").queryAs(DatePicker.class).isVisible());

        assertThat(birthdayController.isValid())
            .withFailMessage("Expected empty date to be invalid.")
            .isFalse();

        assertThat(lookup("#errorText").queryAs(Label.class).isVisible())
            .withFailMessage("Expected invalid date to make the invalid label visible.")
            .isTrue();

        assertThat(lookup("#errorText").queryAs(Label.class).getText())
            .isEqualTo(ERROR_EMPTY_BIRTHDATE);
    }

    @Test
    public void checkInvalidBirthday() {
        final LocalDate invalidBirthday = LocalDate.now().minusYears(1);

        withNodes(birthdayPane)
            .willDo(() -> lookup("#datePicker").queryAs(DatePicker.class).setValue(invalidBirthday))
            .run();

        assertThat(birthdayController.isValid())
            .withFailMessage("Expected date that does not follow pattern to be invalid.")
            .isFalse();

        assertThat(lookup("#errorText").queryAs(Label.class).isVisible())
            .withFailMessage("Expected date that does not follow pattern to make the invalid label visible.")
            .isTrue();

        assertThat(lookup("#errorText").queryAs(Label.class).getText())
            .isEqualTo(ERROR_LESS_13YO_BIRTHDATE);
    }

}
