package moe.tristan.easyfxml.model.awt.integrations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import moe.tristan.easyfxml.spring.SpringContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.testfx.framework.junit.ApplicationTest;

import java.net.URL;

@ContextConfiguration(classes = SpringContext.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class BrowserSupportTest extends ApplicationTest {

    @Autowired
    private BrowserSupport browserSupport;

    @Test
    public void openUrl_good_url() {
        browserSupport.openUrl("https://www.google.fr");
    }

    @Test
    public void openUrl_bad_url() {
        browserSupport.openUrl("not_a_url");
    }

    @Test
    public void browse_ioe() {
        browserSupport.openUrl((URL) null);
    }

}
