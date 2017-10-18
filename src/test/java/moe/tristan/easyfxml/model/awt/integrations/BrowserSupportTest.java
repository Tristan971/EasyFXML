package moe.tristan.easyfxml.model.awt.integrations;

import java.net.URL;

import javafx.embed.swing.JFXPanel;
import moe.tristan.easyfxml.model.awt.AwtAccess;
import moe.tristan.easyfxml.model.awt.HeadlessIncompatibleTest;
import moe.tristan.easyfxml.spring.SpringContext;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.Assertions.assertThat;

@ContextConfiguration(classes = SpringContext.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class BrowserSupportTest extends HeadlessIncompatibleTest {

    @Autowired
    private BrowserSupport browserSupport;

    @BeforeClass
    public static void setUp() {
        AwtAccess.enableAwt();
        new JFXPanel();
    }

    @Test
    public void openUrl_good_url() {
        browserSupport.openUrl("https://www.google.fr");
    }

    @Test
    public void openUrl_bad_url() {
        browserSupport.openUrl("not_a_url");
    }

    @Test
    public void is_supported() {
        // if it is not supported this test should not even run
        assertThat(browserSupport.isSupported()).isTrue();
    }

    @Test
    public void browse_ioe() {
        browserSupport.openUrl((URL) null);
    }

}
