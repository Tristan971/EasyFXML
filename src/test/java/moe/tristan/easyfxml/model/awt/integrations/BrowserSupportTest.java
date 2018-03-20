package moe.tristan.easyfxml.model.awt.integrations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import moe.tristan.easyfxml.spring.application.FxSpringContext;
import io.vavr.control.Try;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.testfx.framework.junit.ApplicationTest;

import java.net.MalformedURLException;
import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;

@ContextConfiguration(classes = FxSpringContext.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class BrowserSupportTest extends ApplicationTest {

    @Autowired
    private BrowserSupport browserSupport;

    @Test
    public void openUrl_good_url() throws Throwable {
        Try<Void> validUrlOpen = browserSupport.openUrl("https://www.google.fr");

        if (validUrlOpen.isFailure()) {
            throw validUrlOpen.getCause();
        }
        assertThat(validUrlOpen.isSuccess()).isTrue();
    }

    @Test
    public void openUrl_bad_url() {
        Try<Void> invalidUrlOpening = browserSupport.openUrl("not_a_url");

        assertThat(invalidUrlOpening.isFailure()).isTrue();
        assertThat(invalidUrlOpening.getCause()).isInstanceOf(MalformedURLException.class);
    }

    @Test
    public void browse_ioe() {
        Try<Void> nullUrlOpening = browserSupport.openUrl((URL) null);

        assertThat(nullUrlOpening.isFailure()).isTrue();
        assertThat(nullUrlOpening.getCause()).isInstanceOf(NullPointerException.class);
    }

}
