package moe.tristan.easyfxml.model;

import moe.tristan.easyfxml.api.FxmlController;
import moe.tristan.easyfxml.api.FxmlFile;
import moe.tristan.easyfxml.api.FxmlNode;
import moe.tristan.easyfxml.api.FxmlStylesheet;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class FxmlNodeTest {

    private final FxmlNode testNode = new FxmlNode() {
        @Override
        public FxmlFile getFile() {
            return null;
        }

        @Override
        public Class<? extends FxmlController> getControllerClass() {
            return null;
        }
    };

    @Test
    public void non_overriden_css() {
        assertThat(this.testNode.getStylesheet()).isEqualTo(FxmlStylesheet.INHERIT);
    }
}
