package moe.tristan.easyfxml.model.beanmanagement;

import moe.tristan.easyfxml.spring.SpringContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;

@ContextConfiguration(classes = SpringContext.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class AbstractInstanceManagerTest {

    private AbstractInstanceManager<Object, Object, Object> instanceManager;

    private static final Object PARENT = new Object();
    private static final Object ACTUAL_1 = new Object();
    private static final Object SEL_1 = new Object();
    private static final Object ACTUAL_2 = new Object();
    private static final Object SEL_2 = new Object();
    private static final Supplier<Object> SEL_PROVIDER_1 = () -> SEL_1;
    private static final Supplier<Object> SEL_PROVIDER_2 = () -> SEL_2;

    @Before
    public void setUp() {
        this.instanceManager = new AbstractInstanceManager<Object, Object, Object>() {};
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
    public void selector_provider_has_no_influence() {
        assertThat(SEL_PROVIDER_1.get()).isEqualTo(SEL_1);
        assertThat(SEL_PROVIDER_2.get()).isEqualTo(SEL_2);
    }

    @Test
    public void registerMultiple_withSelectorProvider() {
        this.instanceManager.registerMultiple(PARENT, SEL_PROVIDER_1, ACTUAL_1);
        this.instanceManager.registerMultiple(PARENT, SEL_PROVIDER_2, ACTUAL_2);

        assertThat(this.instanceManager.getMultiple(PARENT, SEL_1).get()).isEqualTo(ACTUAL_1);
        assertThat(this.instanceManager.getMultiple(PARENT, SEL_2).get()).isEqualTo(ACTUAL_2);
    }

    @Test
    public void getAll() {
        this.instanceManager.registerSingle(PARENT, ACTUAL_1);
        this.instanceManager.registerSingle(PARENT, ACTUAL_2);
        this.instanceManager.registerMultiple(PARENT, SEL_1, ACTUAL_1);
        this.instanceManager.registerMultiple(PARENT, SEL_2, ACTUAL_2);

        // Only one exemplary of ACTUAL_1 since the second call to register single overrides the first one
        assertThat(this.instanceManager.getAll(PARENT)).containsExactlyInAnyOrder(
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
