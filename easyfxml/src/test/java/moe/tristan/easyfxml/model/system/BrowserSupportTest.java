package moe.tristan.easyfxml.model.system;

import java.net.URL;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.testfx.framework.junit.ApplicationTest;

import moe.tristan.easyfxml.EasyFxmlAutoConfiguration;

import io.vavr.control.Try;

@ContextConfiguration(classes = EasyFxmlAutoConfiguration.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class BrowserSupportTest extends ApplicationTest {

    @Autowired
    private BrowserSupport browserSupport;

    @Test
    public void openUrl_good_url() {
        workaroundOpenJfxHavingAwfulBrowserSupport(() -> browserSupport.openUrl("https://www.google.fr"));
    }

    @Test
    public void openUrl_bad_url() {
        workaroundOpenJfxHavingAwfulBrowserSupport(() -> {
            Try<Void> invalidUrlOpening = browserSupport.openUrl("not_a_url");

            // JFX url opening works for anything, it will just basically try to open it in browser and that's it
            assertThat(invalidUrlOpening.isFailure()).isFalse();
        });
    }

    @Test
    public void browse_ioe() {
        workaroundOpenJfxHavingAwfulBrowserSupport(() -> {
            Try<Void> nullUrlOpening = browserSupport.openUrl((URL) null);

            assertThat(nullUrlOpening.isFailure()).isTrue();
            assertThat(nullUrlOpening.getCause()).isInstanceOf(NullPointerException.class);
        });
    }

    private void workaroundOpenJfxHavingAwfulBrowserSupport(Runnable exec) {
        try {
            exec.run();
        } catch (Exception e) {
            if (e.getClass().equals(Exception.class) && e.getMessage().equals("No web browser found")) {
                throw new IllegalStateException("Tests require the installation of at least one valid web browser. See HostServicesDelegate#browsers");
            } else {
                throw e;
            }
        }
    }

}
