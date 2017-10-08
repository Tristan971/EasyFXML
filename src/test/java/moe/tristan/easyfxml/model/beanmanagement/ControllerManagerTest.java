package moe.tristan.easyfxml.model.beanmanagement;

import moe.tristan.easyfxml.TestUtils;
import moe.tristan.easyfxml.spring.SpringContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@ContextConfiguration(classes = SpringContext.class)
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
        TestUtils.ensureSpringSingleton(this.context, ControllerManager.class);
    }
}