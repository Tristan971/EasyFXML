package moe.tristan.easyfxml.model.awt;

import moe.tristan.easyfxml.spring.SpringContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ContextConfiguration(classes = SpringContext.class)
@RunWith(SpringRunner.class)
public class AwtAccessTest extends HeadlessIncompatibleTest {

    @Test
    public void ensureAwtSupport() {
        assertThatThrownBy(AwtAccess::ensureAwtSupport).isInstanceOf(UnsupportedOperationException.class);
        AwtAccess.enableAwt();
        AwtAccess.ensureAwtSupport();
    }
}
