package moe.tristan.easyfxml.spring.application;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;
import moe.tristan.easyfxml.EasyFxml;
import moe.tristan.easyfxml.api.FxmlNode;
import moe.tristan.easyfxml.model.fxml.BaseEasyFxmlTest;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import javafx.stage.Stage;

public class FxSpringApplicationTest {

    private static final String TEST_TITLE = "TEST_TITLE";
    private static final FxmlNode TEST_NODE = BaseEasyFxmlTest.TEST_NODES.PANE;

    @Test
    public void start() throws Exception {
        TestFxSpringApplication.main();
    }

    @SpringBootApplication
    @Import(TestFxUiManager.class)
    public static class TestFxSpringApplication extends FxSpringApplication {
        public static void main(String ... args) throws Exception {
            ApplicationTest.launch(TestFxSpringApplication.class, args);
        }

        @Override
        public void start(Stage primaryStage) {
            super.start(primaryStage);
            stop();
        }
    }

    @Component
    public static class TestFxUiManager extends FxUiManager {

        protected TestFxUiManager(EasyFxml easyFxml) {
            super(easyFxml);
        }

        @Override
        protected String title() {
            return TEST_TITLE;
        }

        @Override
        protected FxmlNode mainComponent() {
            return TEST_NODE;
        }
    }
}
