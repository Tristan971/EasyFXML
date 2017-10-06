package moe.tristan.easyfxml.model.awt.integrations;

import moe.tristan.easyfxml.model.awt.AwtAccess;
import moe.tristan.easyfxml.spring.SpringContext;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration(classes = SpringContext.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class BrowserSupportTest {

    @Autowired
    private BrowserSupport browserSupport;

    @BeforeClass
    public static void setUpAWT() {
        AwtAccess.enableAwt();
    }

    @Test
    @Ignore("CI Testing doesn't offer browser support, so only manual tests here.")
    public void openUrl() {
        this.browserSupport.openUrl("https://www.google.fr");
    }

}
