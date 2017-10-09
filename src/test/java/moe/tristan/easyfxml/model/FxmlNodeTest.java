package moe.tristan.easyfxml.model;

import io.vavr.control.Option;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class FxmlNodeTest {

    private final FxmlNode testNode = new FxmlNode() {
        @Override
        public FxmlFile getFxmlFile() {
            return null;
        }

        @Override
        public Option<Class<? extends FxmlController>> getControllerClass() {
            return null;
        }
    };

    @Test
    public void non_overriden_css() {
        assertThat(this.testNode.getStylesheet().isEmpty()).isTrue();
    }
}
