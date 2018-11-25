package moe.tristan.easyfxml;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.testfx.framework.junit.ApplicationTest;

import javafx.application.Platform;
import javafx.stage.Stage;

@SpringBootTest
@RunWith(SpringRunner.class)
public class FxUiManagerTest extends ApplicationTest {

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

}
