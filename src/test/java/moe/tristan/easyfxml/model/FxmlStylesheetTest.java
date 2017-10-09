package moe.tristan.easyfxml.model;

import io.vavr.control.Option;
import moe.tristan.easyfxml.spring.SpringContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@ContextConfiguration(classes = SpringContext.class)
@RunWith(SpringRunner.class)
public class FxmlStylesheetTest {

    private static final String TEST_STYLE_CONTENT = "root {\n" +
        "    -fx-font-size: 18;\n" +
        "    -fx-font-weight: bold;\n" +
        "}\n";

    private final Option<FxmlStylesheet> stylesheetLoading = FxmlStylesheet.ofResourceFile("fxml/test_style.css");

    @Test
    public void getCssContent() throws IOException {
        final FxmlStylesheet stylesheet = this.stylesheetLoading.getOrElseThrow(IOException::new);
        assertThat(stylesheet.getCssContent()).isEqualTo(TEST_STYLE_CONTENT);
    }

    @Test
    public void ofResourceFile() {
        assertThat(this.stylesheetLoading.isDefined()).isTrue();
    }
}
