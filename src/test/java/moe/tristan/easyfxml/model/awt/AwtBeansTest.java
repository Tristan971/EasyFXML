package moe.tristan.easyfxml.model.awt;

import moe.tristan.easyfxml.CIIncompatibleTest;
import moe.tristan.easyfxml.spring.SpringContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.awt.*;

@ContextConfiguration(classes = SpringContext.class)
@RunWith(SpringRunner.class)
public class AwtBeansTest extends CIIncompatibleTest {

    @Autowired
    private ApplicationContext context;

    @Test
    public void awtDesktop() {
        final Desktop desktop = context.getBean(Desktop.class);
    }

    @Test
    public void awtSystemTray() {
    }

    @Test
    public void awtToolkit() {
    }
}
