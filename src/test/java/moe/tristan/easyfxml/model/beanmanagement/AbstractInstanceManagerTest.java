package moe.tristan.easyfxml.model.beanmanagement;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import moe.tristan.easyfxml.spring.application.FxSpringContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ContextConfiguration(classes = FxSpringContext.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class AbstractInstanceManagerTest {

    private AbstractInstanceManager<Object, Object, Object> instanceManager;

    private static final Object PARENT = new Object();
    private static final Object ACTUAL_1 = new Object();
    private static final Object SEL_1 = new Object();
    private static final Object ACTUAL_2 = new Object();
    private static final Object SEL_2 = new Object();

    @Before
    public void setUp() {
        this.instanceManager = new AbstractInstanceManager<Object, Object, Object>() {
        };
    }

    @Test
    public void registerSingle() {
        this.instanceManager.registerSingle(PARENT, ACTUAL_1);
        assertThat(this.instanceManager.getSingle(PARENT).get()).isEqualTo(ACTUAL_1);
    }

    @Test
    public void registerMultiple() {
        this.instanceManager.registerMultiple(PARENT, SEL_1, ACTUAL_1);
        this.instanceManager.registerMultiple(PARENT, SEL_2, ACTUAL_2);

        assertThat(this.instanceManager.getMultiple(PARENT, SEL_1).get()).isEqualTo(ACTUAL_1);
        assertThat(this.instanceManager.getMultiple(PARENT, SEL_2).get()).isEqualTo(ACTUAL_2);
    }

    @Test
    public void getAll() {
        this.instanceManager.registerSingle(PARENT, ACTUAL_1);
        this.instanceManager.registerSingle(PARENT, ACTUAL_2);
        this.instanceManager.registerMultiple(PARENT, SEL_1, ACTUAL_1);
        this.instanceManager.registerMultiple(PARENT, SEL_2, ACTUAL_2);

        final List<Object> all = this.instanceManager.getAll(PARENT);
        // Only one exemplary of ACTUAL_1 since the second call to register single overrides the first one
        assertThat(all).containsExactlyInAnyOrder(
            ACTUAL_1, ACTUAL_2, ACTUAL_2
        );
    }

    @Test
    public void getMultiples() {
        this.instanceManager.registerMultiple(PARENT, SEL_1, ACTUAL_1);
        this.instanceManager.registerMultiple(PARENT, SEL_2, ACTUAL_2);

        assertThat(this.instanceManager.getMultiples(PARENT)).containsExactlyInAnyOrder(ACTUAL_1, ACTUAL_2);
    }
}
