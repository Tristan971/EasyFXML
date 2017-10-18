package moe.tristan.easyfxml.model.beanmanagement;

import static moe.tristan.easyfxml.TestUtils.isSpringSingleton;
import static org.assertj.core.api.Assertions.assertThat;

import moe.tristan.easyfxml.spring.SpringContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@ContextConfiguration(classes = SpringContext.class)
@RunWith(SpringRunner.class)
public class StageManagerTest {

    @Autowired
    private ApplicationContext context;

    @Test
    public void testLinkage() {
        final StageManager manager = this.context.getBean(StageManager.class);
        assertThat(manager).isNotNull();
    }

    @Test
    public void testSingleton() {
        assertThat(isSpringSingleton(this.context, StageManager.class)).isTrue();
    }
}
