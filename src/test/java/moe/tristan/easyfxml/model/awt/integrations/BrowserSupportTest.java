package moe.tristan.easyfxml.model.awt.integrations;

import moe.tristan.easyfxml.CIIncompatibleTest;
import moe.tristan.easyfxml.model.awt.AwtAccess;
import moe.tristan.easyfxml.spring.SpringContext;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration(classes = SpringContext.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class BrowserSupportTest extends CIIncompatibleTest {

    @Autowired
    private BrowserSupport browserSupport;

    @BeforeClass
    public static void setUpAWT() {
        AwtAccess.enableAwt();
    }

    @Test
    public void openUrl() {
        this.browserSupport.openUrl("https://www.google.fr");
    }

}
