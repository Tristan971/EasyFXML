package moe.tristan.easyfxml.model.fxml;

import moe.tristan.easyfxml.spring.SpringContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@ContextConfiguration(classes = SpringContext.class)
@RunWith(SpringRunner.class)
public class NoControllerClassTest {

    @Test
    public void initialize() {
        final NoControllerClass noControllerClass = new NoControllerClass();
        noControllerClass.initialize();
        assertThat(NoControllerClass.class.getDeclaredMethods()).hasSize(1);
    }
}
