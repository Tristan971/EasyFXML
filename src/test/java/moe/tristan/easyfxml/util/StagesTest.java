package moe.tristan.easyfxml.util;

import java.util.concurrent.ExecutionException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.testfx.framework.junit.ApplicationTest;

import javafx.application.Platform;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import moe.tristan.easyfxml.spring.SpringContext;
import static org.assertj.core.api.Assertions.assertThat;

@ContextConfiguration(classes = SpringContext.class)
@RunWith(SpringRunner.class)
public class StagesTest extends ApplicationTest {

    private static String STAGE_TITLE;
    private static String STAGE_TITLE_2;
    private static Pane STAGE_PANE;
    private static Stage TEST_STAGE;

    @Override
    public void start(final Stage stage) {
        Platform.setImplicitExit(false);
        STAGE_TITLE = "STAGE_TITLE";
        STAGE_TITLE_2 = "STAGE_TITLE_2";
        STAGE_PANE = new Pane();
        TEST_STAGE = stage;
        stage.show();
        stage.hide();
    }

    @Test
    public void stageOf() throws ExecutionException, InterruptedException {
        Stages.stageOf(STAGE_TITLE, STAGE_PANE)
              .thenAccept(stage -> {
                  assertThat(stage.getScene().getRoot()).isEqualTo(STAGE_PANE);
                  assertThat(stage.getTitle()).isEqualTo(STAGE_TITLE);
              })
              .toCompletableFuture().get();
    }

    @Test
    public void scheduleDisplaying() throws ExecutionException, InterruptedException {
        Stages.scheduleDisplaying(TEST_STAGE)
              .thenAccept(stage -> assertThat(TEST_STAGE.isShowing()).isTrue())
              .toCompletableFuture().get();
    }

    @Test
    public void scheduleHiding() throws ExecutionException, InterruptedException {
        Stages.scheduleHiding(TEST_STAGE)
              .thenAccept(stage -> assertThat(TEST_STAGE.isShowing()).isFalse())
              .toCompletableFuture().get();
    }

    @Test
    public void asyncStageOperation() throws ExecutionException, InterruptedException {
        FxAsync.doOnFxThread(TEST_STAGE, stage -> stage.setTitle(STAGE_TITLE_2))
               .thenAccept(stage -> assertThat(stage.getTitle()).isEqualTo(STAGE_TITLE_2))
               .toCompletableFuture().get();
    }
}
