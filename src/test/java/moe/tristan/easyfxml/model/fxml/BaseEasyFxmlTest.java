package moe.tristan.easyfxml.model.fxml;

import moe.tristan.easyfxml.spring.SpringContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.Assertions.assertThat;

@ContextConfiguration(classes = SpringContext.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class BaseEasyFxmlTest {

    @Autowired
    private BaseEasyFxml baseEasyFxml;

    @Before
    public void setUp() {
        assertThat(this.baseEasyFxml).isNotNull();
    }

    @Test
    public void getPaneForView() {
    }

    @Test
    public void getPaneForFile() {
    }
}