package moe.tristan.easyfxml.model.styles;

import moe.tristan.easyfxml.spring.SpringContext;
import moe.tristan.easyfxml.util.Paths;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.nio.file.Files;

@ContextConfiguration(classes = SpringContext.class)
@RunWith(SpringRunner.class)
public class CachedFileBasedFxmlStylesheetTest {

    private static final String EXPECTED_CSS_PATH = "fxml/test_style.css";

    private String expectedResult = null;

    @Before
    public void setUp() throws IOException {
        final byte[] loaded = Files.readAllBytes(
            Paths.getPathForResource(EXPECTED_CSS_PATH).get()
        );
        expectedResult = new String(loaded);
    }

    @Test
    public void loadStylesheet() {
        final CachedFileBasedFxmlStylesheet cfbs = new CachedFileBasedFxmlStylesheet(EXPECTED_CSS_PATH);
        Assertions.assertThat(cfbs.getStyle()).isEqualToIgnoringWhitespace(expectedResult);
    }

}
