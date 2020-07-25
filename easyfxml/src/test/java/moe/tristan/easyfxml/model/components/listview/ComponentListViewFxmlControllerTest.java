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

package moe.tristan.easyfxml.model.components.listview;

import static java.util.concurrent.TimeUnit.SECONDS;
import static moe.tristan.easyfxml.model.components.listview.ComponentListViewFxmlController.BADLY_SCOPED_BEANS;
import static moe.tristan.easyfxml.model.components.listview.cell.ComponentCellFxmlSampleController.REMOTE_REF;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.testfx.framework.junit5.Start;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import moe.tristan.easyfxml.EasyFxml;
import moe.tristan.easyfxml.junit.SpringBootComponentTest;
import moe.tristan.easyfxml.model.components.listview.view.ComponentListView;
import moe.tristan.easyfxml.model.components.listview.view.ComponentListViewSampleFxmlController;
import moe.tristan.easyfxml.model.fxml.FxmlLoadResult;

@SpringBootTest
@Import({ComponentListView.class, ComponentListViewSampleFxmlController.class})
public class ComponentListViewFxmlControllerTest extends SpringBootComponentTest {

    @Autowired
    private EasyFxml easyFxml;

    @Autowired
    private ComponentListView componentListView;

    private Stage stage;

    @Start
    public void start(Stage stage) {
        this.stage = stage;
    }

    @Test
    public void ensureLoadsAndMapsProperly() throws InterruptedException, TimeoutException, ExecutionException {
        final String TEST_BUTTON_SUCCESS_TEXT = "TEST_SUCCESS";

        final ComponentListViewSampleFxmlController clvsfc = setUpStage();
        IntStream.range(0, 100).forEach(i -> clvsfc.addValue(TEST_BUTTON_SUCCESS_TEXT));

        await().until(() -> REMOTE_REF.get() != null);

        assertThat(BADLY_SCOPED_BEANS).hasSize(1).containsOnly(BadlyScopedController.BADLY_SCOPED_COMPONENT_NAME);

        final Button testButton = REMOTE_REF.get();
        assertThat(testButton).isNotNull();
        assertThat(testButton).isInstanceOf(Button.class);

        await().until(() -> testButton.getText().equals(TEST_BUTTON_SUCCESS_TEXT));
        assertThat(testButton.getText()).isEqualTo(TEST_BUTTON_SUCCESS_TEXT);

        Platform.runLater(() -> {
            assertThat(clvsfc.scrolledToEnd.get()).isFalse();
            clvsfc.listView.scrollTo(1);
            assertThat(clvsfc.scrolledToEnd.get()).isFalse();
            clvsfc.listView.scrollTo(99);
        });

        await().until(clvsfc.scrolledToEnd::get);
    }

    private ComponentListViewSampleFxmlController setUpStage() throws InterruptedException, ExecutionException, TimeoutException {
        final FxmlLoadResult<Pane, ComponentListViewSampleFxmlController> res = easyFxml.load(
            componentListView,
            AnchorPane.class,
            ComponentListViewSampleFxmlController.class
        );

        final Pane listView = res.getNode().getOrElseThrow((Function<? super Throwable, RuntimeException>) RuntimeException::new);

        final ComponentListViewSampleFxmlController clvsfc = res.getController().getOrElseThrow(
            (Function<? super Throwable, RuntimeException>) RuntimeException::new
        );

        return CompletableFuture.supplyAsync(() -> {
            final Scene scene = new Scene(listView);
            stage.setScene(scene);
            stage.show();
            return clvsfc;
        }, Platform::runLater).get(5, SECONDS);
    }

}
