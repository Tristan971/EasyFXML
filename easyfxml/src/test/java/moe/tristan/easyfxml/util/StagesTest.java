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

import static moe.tristan.easyfxml.util.Resources.getResourcePath;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.Start;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import moe.tristan.easyfxml.api.FxmlStylesheet;
import moe.tristan.easyfxml.junit.SpringBootComponentTest;

import io.vavr.control.Try;

public class StagesTest extends SpringBootComponentTest {

    private static final String RES_REL_PATH_TEST_STYLE = "css/test_style.css";
    private static final FxmlStylesheet TEST_STYLE = () -> getResourcePath(RES_REL_PATH_TEST_STYLE).get();

    private Stage testStage;
    private String stageTitle;
    private String stageTitle2;

    @Start
    public void start(final Stage stage) {
        Platform.setImplicitExit(false);
        stageTitle = "stageTitle";
        stageTitle2 = "stageTitle2";
        Pane stagePane = new Pane();
        testStage = stage;
        testStage.setScene(new Scene(stagePane));
        stage.show();
        stage.hide();
    }

    @Test
    public void stageOf() throws ExecutionException, InterruptedException {
        final Pane newPane = new Pane();
        Stages.stageOf(stageTitle, newPane)
              .thenAccept(stage -> {
                  assertThat(stage.getScene().getRoot()).isEqualTo(newPane);
                  assertThat(stage.getTitle()).isEqualTo(stageTitle);
              })
              .toCompletableFuture().get();
    }

    @Test
    public void scheduleDisplaying() throws ExecutionException, InterruptedException {
        Stages.scheduleDisplaying(testStage)
              .thenAccept(stage -> assertThat(testStage.isShowing()).isTrue())
              .toCompletableFuture().get();
    }

    @Test
    public void scheduleDisplayingPostAsync() throws ExecutionException, InterruptedException {
        Try.of(() -> testStage)
           .map(Stages::scheduleHiding)
           .map(Stages::scheduleDisplaying)
           .get()
           .toCompletableFuture()
           .get();

        await().until(testStage::isShowing);
        assertThat(testStage.isShowing()).isTrue();
    }

    @Test
    public void scheduleHiding() throws ExecutionException, InterruptedException {
        Stages.scheduleHiding(testStage)
              .thenAccept(stage -> assertThat(testStage.isShowing()).isFalse())
              .toCompletableFuture().get();
    }

    @Test
    public void scheduleHidingPostAsync() throws ExecutionException, InterruptedException {
        Try.of(() -> testStage)
           .map(Stages::scheduleDisplaying)
           .map(Stages::scheduleHiding)
           .get()
           .toCompletableFuture()
           .get();

        await().until(() -> !testStage.isShowing());
        assertThat(testStage.isShowing()).isFalse();
    }

    @Test
    public void asyncStageOperation() throws ExecutionException, InterruptedException {
        FxAsync.doOnFxThread(testStage, stage -> stage.setTitle(stageTitle2))
               .thenAccept(stage -> assertThat(stage.getTitle()).isEqualTo(stageTitle2))
               .toCompletableFuture().get();
    }

    @Test
    public void setStylesheet() throws ExecutionException, InterruptedException {
        final CompletionStage<Stage> setStyleAsyncOp = Stages.setStylesheet(testStage, TEST_STYLE);
        final Stage stage = setStyleAsyncOp.toCompletableFuture().get();
        final ObservableList<String> stylesheets = stage.getScene().getStylesheets();
        assertThat(stylesheets).hasSize(1);
        assertThat(stylesheets).containsExactly(TEST_STYLE.getExternalForm());
    }

    @Test
    public void setStylesheetPostAsync() throws ExecutionException, InterruptedException {
        Try.of(() -> testStage)
           .map(Stages::scheduleDisplaying)
           .map(cs -> Stages.setStylesheet(cs, TEST_STYLE))
           .get()
           .toCompletableFuture()
           .get();

        await().until(() -> testStage.getScene().getStylesheets().contains(TEST_STYLE.getExternalForm()));
        assertThat(testStage.getScene().getStylesheets()).containsExactly(TEST_STYLE.getExternalForm());
    }

}
