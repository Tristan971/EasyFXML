package moe.tristan.easyfxml.model.beanmanagement;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import moe.tristan.easyfxml.EasyFxmlAutoConfiguration;

@ContextConfiguration(classes = EasyFxmlAutoConfiguration.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class AbstractInstanceManagerTest {

    private AbstractInstanceManager<String, String> instanceManager;

    private static final String PARENT = "PARENT";
    private static final String ACTUAL_1 = "ACTUAL1";
    private static final String ACTUAL_2 = "ACTUAL2";
    private static final Selector SEL_1 = new Selector("SEL1");
    private static final Selector SEL_2 = new Selector("SEL2");

    @Before
    public void setUp() {
        this.instanceManager = new AbstractInstanceManager<>() {
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

        final List<String> all = this.instanceManager.getAll(PARENT);
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
