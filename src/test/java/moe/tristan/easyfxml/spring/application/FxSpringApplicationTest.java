package moe.tristan.easyfxml.spring.application;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;
import moe.tristan.easyfxml.EasyFxml;
import moe.tristan.easyfxml.api.FxmlNode;
import moe.tristan.easyfxml.model.fxml.BaseEasyFxmlTest;
import org.junit.Test;

import javafx.stage.Stage;

public class FxSpringApplicationTest {

    private static final String TEST_TITLE = "TEST_TITLE";
    private static final FxmlNode TEST_NODE = BaseEasyFxmlTest.TEST_NODES.PANE;

    @Test
    public void start() {
        TestFxSpringApplication.main();
    }

    @SpringBootApplication
    @Import(TestFxUiManager.class)
    public static class TestFxSpringApplication extends FxSpringApplication {
        public static void main(String ... args) {
            launch(args);
        }

        @Override
        public void start(Stage primaryStage) {
            super.start(primaryStage);
            primaryStage.hide();
        }
    }

    @Component
    public static class TestFxUiManager extends FxUiManager {

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
