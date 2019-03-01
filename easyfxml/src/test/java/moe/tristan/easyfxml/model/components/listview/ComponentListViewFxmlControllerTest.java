package moe.tristan.easyfxml.model.components.listview;

import static java.util.concurrent.TimeUnit.SECONDS;
import static moe.tristan.easyfxml.model.components.listview.ComponentListViewFxmlController.BADLY_SCOPED_BEANS;
import static moe.tristan.easyfxml.model.components.listview.CustomListViewTestComponents.VIEW;
import static moe.tristan.easyfxml.model.components.listview.cell.ComponentCellFxmlSampleController.REMOTE_REF;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;
import java.util.stream.IntStream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.testfx.framework.junit.ApplicationTest;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import moe.tristan.easyfxml.EasyFxml;
import moe.tristan.easyfxml.EasyFxmlAutoConfiguration;
import moe.tristan.easyfxml.model.components.listview.view.ComponentListViewSampleFxmlController;
import moe.tristan.easyfxml.model.fxml.FxmlLoadResult;

@ContextConfiguration(classes = EasyFxmlAutoConfiguration.class)
@RunWith(SpringRunner.class)
public class ComponentListViewFxmlControllerTest extends ApplicationTest {

    @Autowired
    private EasyFxml easyFxml;

    private Stage stage;
    private ComponentListViewSampleFxmlController clvsfc;

    @Override
    public void start(Stage stage) {
        this.stage = stage;
    }

    @Test
    public void ensureLoadsAndMapsProperly() throws InterruptedException, TimeoutException, ExecutionException {
        final String TEST_BUTTON_SUCCESS_TEXT = "TEST_SUCCESS";

        final ComponentListViewSampleFxmlController ctrl = setUpStage();
        IntStream.range(0, 100).forEach(i -> ctrl.addValue(TEST_BUTTON_SUCCESS_TEXT));

        await().until(() -> REMOTE_REF.get() != null);

        assertThat(BADLY_SCOPED_BEANS).hasSize(1).containsOnly("componentListViewFxmlControllerTest.BadlyScopedController");

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

            await().until(() -> clvsfc.scrolledToEnd.get());
        });
    }

    private ComponentListViewSampleFxmlController setUpStage() throws InterruptedException, ExecutionException, TimeoutException {
        final CompletableFuture<ComponentListViewSampleFxmlController> setUpAsyncWait = new CompletableFuture<>();

        final FxmlLoadResult<Pane, ComponentListViewSampleFxmlController> res = easyFxml.loadNode(
            VIEW,
            AnchorPane.class,
            ComponentListViewSampleFxmlController.class
        );

        final Pane listView = res.getNode().getOrElseThrow((Function<? super Throwable, RuntimeException>) RuntimeException::new);

        final ComponentListViewSampleFxmlController clvsfc = res.getController().getOrElseThrow(
            (Function<? super Throwable, RuntimeException>) RuntimeException::new
        );

        this.clvsfc = clvsfc;

        Platform.runLater(() -> {
            final Scene scene = new Scene(listView);
            stage.setScene(scene);
            stage.show();
            setUpAsyncWait.complete(clvsfc);
        });

        return setUpAsyncWait.get(5, SECONDS);
    }

    public static class BadlyScopedController implements ComponentCellFxmlController<String> {

        @Override
        public void updateWithValue(String newValue) {
        }

        @Override
        public void initialize() {
        }

    }

}
