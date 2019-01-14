package moe.tristan.easyfxml.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.testfx.framework.junit.ApplicationTest;

import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

import moe.tristan.easyfxml.EasyFxml;
import moe.tristan.easyfxml.EasyFxmlAutoConfiguration;
import moe.tristan.easyfxml.api.FxmlController;
import moe.tristan.easyfxml.api.FxmlFile;
import moe.tristan.easyfxml.api.FxmlNode;
import moe.tristan.easyfxml.model.fxml.NoControllerClass;

@ContextConfiguration(classes = EasyFxmlAutoConfiguration.class)
@RunWith(SpringRunner.class)
public class PanesTest extends ApplicationTest {

    @Autowired
    private EasyFxml easyFxml;

    private final FxmlNode testPaneInfo = new FxmlNode() {
        @Override
        public FxmlFile getFile() {
            return () -> "fxml/test_pane.fxml";
        }

        @Override
        public Class<? extends FxmlController> getControllerClass() {
            return NoControllerClass.class;
        }
    };

    private Pane container;
    private Pane embedded;

    @Before
    public void setUp() {
        container = easyFxml.loadNode(testPaneInfo).getNode().get();
    }

    @Test
    public void setContent() {
        assertThat(container.getChildren())
            .hasSize(1)
            .hasOnlyElementsOfType(Button.class);

        embedded = new Pane();

        Panes.setContent(container, embedded)
             .whenCompleteAsync((res, err) -> {
                 assertThat(err).isNull();
                 assertThat(res).isNotNull();
                 assertThat(res).isEqualTo(container);
                 assertThat(container.getChildren())
                     .hasSize(1)
                     .hasOnlyElementsOfType(embedded.getClass());
             });

    }
}
