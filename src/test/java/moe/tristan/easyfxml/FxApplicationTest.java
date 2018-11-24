package moe.tristan.easyfxml;

import org.junit.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.testfx.framework.junit.ApplicationTest;

import javafx.stage.Stage;

import moe.tristan.easyfxml.util.NoSpringBean;

public class FxApplicationTest {

    @Test
    public void start() throws Exception {
        FxApplicationTest.TestFxApplication.main();
    }

    @SpringBootApplication
    @NoSpringBean
    public static class TestFxApplication extends FxApplication {
        public static void main(String... args) throws Exception {
            ApplicationTest.launch(TestFxApplication.class, args);
        }

        @Override
        public void start(Stage primaryStage) {
            super.start(primaryStage);
            stop();
        }
    }

}
