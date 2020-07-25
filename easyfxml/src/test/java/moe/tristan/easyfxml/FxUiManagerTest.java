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

package moe.tristan.easyfxml;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javafx.application.Platform;
import javafx.stage.Stage;

import moe.tristan.easyfxml.junit.FxmlComponentTest;

@ContextConfiguration(classes = EasyFxmlAutoConfiguration.class)
@ExtendWith(SpringExtension.class)
public class FxUiManagerTest extends FxmlComponentTest {

    @Autowired
    private EasyFxml easyFxml;

    private Stage stage;

    @Override
    public void start(Stage stage) {
        this.stage = stage;
    }

    @Test
    public void startGui() {
        Platform.runLater(() -> {
            final TestFxUiManager uiManager = new TestFxUiManager();
            uiManager.setEasyFxml(easyFxml);
            uiManager.startGui(stage);
        });
    }

}
