package moe.tristan.easyfxml.spring.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import moe.tristan.easyfxml.EasyFxml;
import moe.tristan.easyfxml.api.FxmlNode;
import moe.tristan.easyfxml.model.fxml.BaseEasyFxmlTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.testfx.framework.junit.ApplicationTest;

import javafx.application.Platform;
import javafx.stage.Stage;

@ContextConfiguration(classes = FxSpringContext.class)
@RunWith(SpringRunner.class)
public class FxUiManagerTest extends ApplicationTest {

    private static final String TEST_TITLE = "TEST_TITLE";
    private static final FxmlNode TEST_NODE = BaseEasyFxmlTest.TEST_NODES.PANE;

    @Autowired
    private EasyFxml easyFxml;

    private Stage stage;

    @Override
    public void start(Stage stage) {
        this.stage = stage;
    }

    @Test
    public void startGui() {
        Platform.runLater(() ->
            new TestFxUiManager(easyFxml).startGui(stage)
        );
    }

    private static final class TestFxUiManager extends FxUiManager {

        protected TestFxUiManager(EasyFxml easyFxml) {
            super(easyFxml);
        }

        @Override
        protected String getTitle() {
            return TEST_TITLE;
        }

        @Override
        protected FxmlNode getMainScene() {
            return TEST_NODE;
        }
    }

}
