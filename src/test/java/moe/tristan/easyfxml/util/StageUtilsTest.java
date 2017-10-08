package moe.tristan.easyfxml.util;

import javafx.application.Platform;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import moe.tristan.easyfxml.spring.SpringContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.testfx.framework.junit.ApplicationTest;

import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;

@ContextConfiguration(classes = SpringContext.class)
@RunWith(SpringRunner.class)
public class StageUtilsTest extends ApplicationTest {

    private static String STAGE_TITLE;
    private static String STAGE_TITLE_2;
    private static Pane STAGE_PANE;
    private static Stage TEST_STAGE;

    @Override
    public void start(Stage stage) {
        Platform.setImplicitExit(false);
        STAGE_TITLE = "STAGE_TITLE";
        STAGE_TITLE_2 = "STAGE_TITLE_2";
        STAGE_PANE = new Pane();
        TEST_STAGE = stage;
        stage.show();
        stage.hide();
    }

    @Test
    public void stageOf() {
        final CompletableFuture<Stage> stageReq = StageUtils.stageOf(STAGE_TITLE, STAGE_PANE);
        stageReq.thenAccept(stage -> {
            assertThat(stage.getScene().getRoot()).isEqualTo(STAGE_PANE);
            assertThat(stage.getTitle()).isEqualTo(STAGE_TITLE);
        });
    }

    @Test
    public void scheduleDisplaying() {
        StageUtils.scheduleDisplaying(TEST_STAGE).thenAccept(
            stage -> assertThat(TEST_STAGE.isShowing()).isTrue()
        );
    }

    @Test
    public void scheduleHiding() {
        StageUtils.scheduleHiding(TEST_STAGE).thenAccept(
            stage -> assertThat(TEST_STAGE.isShowing()).isTrue()
        );
    }

    @Test
    public void asyncStageOperation() {
        StageUtils.asyncStageOperation(
            TEST_STAGE,
            stage -> stage.setTitle(STAGE_TITLE_2)
        ).thenAccept(stage -> assertThat(stage.getTitle()).isEqualTo(STAGE_TITLE_2));
    }
}
