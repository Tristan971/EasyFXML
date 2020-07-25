/*
 * Copyright 2017 - 2020 EasyFXML project and contributors
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

package moe.tristan.easyfxml.samples.panewithbutton;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import moe.tristan.easyfxml.api.FxmlController;

@Scope(SCOPE_PROTOTYPE)
@Component
public class PaneWithButtonController implements FxmlController {

    private static final Logger LOG = LoggerFactory.getLogger(PaneWithButtonController.class);

    @FXML
    private Button button;

    public boolean locatedInstance = false;

    @Override
    public void initialize() {
        this.button.setOnAction(this::onActionHandler);
    }

    @SuppressWarnings("unused")
    private void onActionHandler(final Event event) {
        this.locatedInstance = true;
        LOG.debug("<HANDLED ACTION ON INSTANCE : {}>", this);
    }

}
