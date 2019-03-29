package moe.tristan.easyfxml.samples.helloworld.view.hello;

import static org.testfx.assertions.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import moe.tristan.easyfxml.EasyFxml;
import moe.tristan.easyfxml.model.exception.ExceptionHandler;
import moe.tristan.easyfxml.junit.FxNodeTest;

@SpringBootTest
@RunWith(SpringRunner.class)
public class HelloComponentTest extends FxNodeTest {

    @Autowired
    private EasyFxml easyFxml;

    @Autowired
    private HelloComponent helloComponent;

    private Pane helloPane;

    @Before
    public void setUp() {
        this.helloPane = easyFxml.loadNode(helloComponent).getNode().getOrElseGet(ExceptionHandler::fromThrowable);
    }

    @Test
    public void shouldGreetWithUserEnteredName() {
        final String expectedUserName = "Tristan Deloche";

        withNodes(helloPane).willDo(() -> {
            clickOn("#userNameTextField").write(expectedUserName);
            clickOn("#helloButton");
        }).andAwaitFor(() -> lookup("#greetingBox").queryAs(HBox.class).isVisible());

        assertThat(lookup("#greetingName").queryAs(Label.class)).hasText(expectedUserName);
    }

    @Test
    public void shouldGreetWithHelloWorldWhenDidNotEnterName() {
        final String defaultGreetingName = "World";

        withNodes(helloPane)
            .willDo(() -> clickOn("#helloButton"))
            .andAwaitFor(() -> lookup("#greetingBox").query().isVisible());

        assertThat(lookup("#greetingName").queryAs(Label.class)).hasText(defaultGreetingName);
    }

}
