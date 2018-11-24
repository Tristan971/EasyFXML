package moe.tristan.easyfxml.model.components.listview;

import static java.util.concurrent.TimeUnit.SECONDS;
import static moe.tristan.easyfxml.model.components.listview.CustomListViewTestComponents.VIEW;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;
import java.util.stream.IntStream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringRunner;
import org.testfx.framework.junit.ApplicationTest;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import moe.tristan.easyfxml.EasyFxml;
import moe.tristan.easyfxml.model.components.listview.cell.ComponentCellFxmlSampleController;
import moe.tristan.easyfxml.model.components.listview.view.ComponentListViewSampleFxmlController;
import moe.tristan.easyfxml.model.fxml.FxmlLoadResult;

@SpringBootTest
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

        Thread.sleep(3000);

        final Set<String> badlyScopedBeans = ComponentListViewFxmlController.BADLY_SCOPED_BEANS;
        assertThat(badlyScopedBeans)
                .hasSize(1)
                .containsOnly("componentListViewFxmlControllerTest.BadlyScopedController");

        final Button testButton = ComponentCellFxmlSampleController.LAST_UPD_ITS_UGLY.get();
        assertThat(testButton).isNotNull();
        assertThat(testButton).isInstanceOf(Button.class);
        assertThat(testButton.getText()).isEqualTo(TEST_BUTTON_SUCCESS_TEXT);

        Platform.runLater(() -> {
            assertThat(clvsfc.scrolledToEnd.get()).isFalse();
            clvsfc.listView.scrollTo(1);
            assertThat(clvsfc.scrolledToEnd.get()).isFalse();
            clvsfc.listView.scrollTo(99);

            await().atMost(1, SECONDS).until(() -> clvsfc.scrolledToEnd.get());
        });
    }

    private ComponentListViewSampleFxmlController setUpStage() throws InterruptedException, ExecutionException, TimeoutException {
        final CompletableFuture<ComponentListViewSampleFxmlController> setUpAsyncWait = new CompletableFuture<>();

        final FxmlLoadResult<Pane, ComponentListViewSampleFxmlController> res = easyFxml.loadNode(
                VIEW,
                AnchorPane.class,
                ComponentListViewSampleFxmlController.class
        );

        final Pane listView =
                res.getNode()
                   .getOrElseThrow((Function<? super Throwable, RuntimeException>) RuntimeException::new);

        final ComponentListViewSampleFxmlController clvsfc =
                res.getController()
                   .getOrElseThrow((Function<? super Throwable, RuntimeException>) RuntimeException::new);
        this.clvsfc = clvsfc;

        Platform.runLater(() -> {
            final Scene scene = new Scene(listView);
            stage.setScene(scene);
            stage.show();
            setUpAsyncWait.complete(clvsfc);
        });

        return setUpAsyncWait.get(5, SECONDS);
    }

    @Component
    public static class BadlyScopedController implements ComponentCellFxmlController<String> {
        @Override
        public void updateWithValue(String newValue) {
        }

        @Override
        public void initialize() {
        }
    }

}
