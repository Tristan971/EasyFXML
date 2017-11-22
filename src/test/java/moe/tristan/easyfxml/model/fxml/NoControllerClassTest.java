package moe.tristan.easyfxml.model.fxml;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class NoControllerClassTest {

    @Test
    public void initialize() {
        final NoControllerClass noControllerClass = new NoControllerClass();
        noControllerClass.initialize();
        assertThat(NoControllerClass.class.getDeclaredMethods()).hasSize(1);
    }
}
