package moe.tristan.easyfxml.model.awt.integrations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import moe.tristan.easyfxml.model.system.BrowserSupport;
import moe.tristan.easyfxml.spring.application.FxSpringContext;
import io.vavr.control.Try;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.testfx.framework.junit.ApplicationTest;

import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;

@ContextConfiguration(classes = FxSpringContext.class)
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
        Try<Void> invalidUrlOpening = browserSupport.openUrl("not_a_url");

        // JFX url opening works for anything, it will just basically try to open it in browser and that's it
        assertThat(invalidUrlOpening.isFailure()).isFalse();
    }

    @Test
    public void browse_ioe() {
        Try<Void> nullUrlOpening = browserSupport.openUrl((URL) null);

        assertThat(nullUrlOpening.isFailure()).isTrue();
        assertThat(nullUrlOpening.getCause()).isInstanceOf(NullPointerException.class);
    }

}
