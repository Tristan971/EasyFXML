package moe.tristan.easyfxml.model.beanmanagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import moe.tristan.easyfxml.spring.application.FxSpringContext;
import org.junit.Test;
import org.junit.runner.RunWith;

import static moe.tristan.easyfxml.TestUtils.isSpringSingleton;
import static org.assertj.core.api.Assertions.assertThat;

@ContextConfiguration(classes = FxSpringContext.class)
@RunWith(SpringRunner.class)
public class ControllerManagerTest {

    @Autowired
    private ApplicationContext context;

    @Test
    public void testLinkage() {
        final ControllerManager manager = this.context.getBean(ControllerManager.class);
        assertThat(manager).isNotNull();
    }

    @Test
    public void testSingleton() {
        assertThat(isSpringSingleton(this.context, ControllerManager.class)).isTrue();
    }
}
