package moe.tristan.easyfxml;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.testfx.framework.junit.ApplicationTest;

import javafx.stage.Stage;

@SpringBootApplication
public class TestFxApplication extends FxApplication {

    public static void main(String... args) throws Exception {
        ApplicationTest.launch(TestFxApplication.class, args);
    }

    @Override
    public void start(Stage primaryStage) {
        stop();
    }

}
