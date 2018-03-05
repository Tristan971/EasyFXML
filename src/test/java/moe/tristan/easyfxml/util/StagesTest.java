package moe.tristan.easyfxml.util;

import moe.tristan.easyfxml.api.FxmlStylesheet;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

public class StagesTest extends ApplicationTest {

    private static final FxmlStylesheet TEST_STYLE = new FxmlStylesheet() {
        @Override
        public String getExternalForm() {
            return Resources.getResourceURL("fxml/test_style.css")
                            .getOrElseThrow((Function<? super Throwable, RuntimeException>) RuntimeException::new)
                            .toExternalForm();
        }
    };

    private Stage testStage;
    private String stageTitle;
    private String stageTitle2;

    @Override
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
    public void scheduleHiding() throws ExecutionException, InterruptedException {
        Stages.scheduleHiding(testStage)
              .thenAccept(stage -> assertThat(testStage.isShowing()).isFalse())
              .toCompletableFuture().get();
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
}
