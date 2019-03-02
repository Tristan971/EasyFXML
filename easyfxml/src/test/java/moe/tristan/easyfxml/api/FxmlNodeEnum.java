package moe.tristan.easyfxml.api;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import moe.tristan.easyfxml.EasyFxmlAutoConfiguration;

@ContextConfiguration(classes = EasyFxmlAutoConfiguration.class)
@RunWith(SpringRunner.class)
public class FxmlNodeEnum {

    @Autowired
    private ComponentByOwnClass componentByOwnClass;

    @Test
    public void springConfigSupportsBothByClassComponentAndByEnumComponents() {
        assertThat(componentByOwnClass).isNotNull();
    }

    public static class ComponentByOwnClass implements FxmlNode {

        @Override
        public FxmlFile getFile() {
            return null;
        }

        @Override
        public Class<? extends FxmlController> getControllerClass() {
            return null;
        }

    }

    public enum ComponentsByEnum implements FxmlNode {
        TEST;

        @Override
        public FxmlFile getFile() {
            return null;
        }

        @Override
        public Class<? extends FxmlController> getControllerClass() {
            return null;
        }
    }

}
