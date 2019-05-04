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

package moe.tristan.easyfxml.util;

import static org.testfx.assertions.api.Assertions.assertThat;

import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class NodesTest extends ApplicationTest {

    private static final double MARGIN = 5d;

    private Button testButton;

    @Override
    public void start(final Stage stage) {
        this.testButton = new Button();
        Pane container = new AnchorPane(this.testButton);
        stage.setScene(new Scene(container));
        stage.show();
    }

    @Test
    public void centerNode() {
        Nodes.centerNode(this.testButton, MARGIN);
    }

    @Test
    public void testAutosizeHelpers() {
        final BooleanProperty shouldDisplay = new SimpleBooleanProperty(true);
        final Button testButton = new Button();
        Nodes.hideAndResizeParentIf(testButton, shouldDisplay);

        assertThat(testButton.isManaged()).isTrue();
        assertThat(testButton.isVisible()).isTrue();

        shouldDisplay.setValue(false);
        assertThat(testButton.isManaged()).isFalse();
        assertThat(testButton.isVisible()).isFalse();
    }

}
