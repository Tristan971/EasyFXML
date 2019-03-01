package moe.tristan.easyfxml.api;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.Objects;

import org.junit.Before;
import org.junit.Test;

import moe.tristan.easyfxml.util.Resources;

public class FxmlStylesheetTest {

    private static final String RES_REL_PATH = "fxml/test_style.css";

    private String cssFile;

    @Before
    public void setUp() throws URISyntaxException, MalformedURLException {
        cssFile = Objects.requireNonNull(getClass().getClassLoader().getResource(RES_REL_PATH))
                         .toURI()
                         .toURL()
                         .toExternalForm();
    }

    @Test
    public void getExternalForm() {
        final FxmlStylesheet fss = () -> Resources.getResourcePath(RES_REL_PATH).get();
        assertThat(fss.getExternalForm()).isEqualTo(cssFile);
    }

}
