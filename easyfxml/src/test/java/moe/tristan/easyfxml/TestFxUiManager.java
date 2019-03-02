package moe.tristan.easyfxml;

import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Optional;

import moe.tristan.easyfxml.api.FxmlNode;
import moe.tristan.easyfxml.api.FxmlStylesheet;
import moe.tristan.easyfxml.model.fxml.BaseEasyFxmlTest;

public class TestFxUiManager extends FxUiManager {

    private static final String TEST_TITLE = "TEST_TITLE";
    private static final FxmlNode TEST_NODE = BaseEasyFxmlTest.TEST_NODES.PANE;

    @Override
    protected String title() {
        return TEST_TITLE;
    }

    @Override
    protected FxmlNode mainComponent() {
        return TEST_NODE;
    }

    @Override
    protected Optional<FxmlStylesheet> getStylesheet() {
        return Optional.of(() -> {
            try {
                return Paths.get(Objects.requireNonNull(getClass().getClassLoader().getResource("css/sample.css")).toURI());
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        });
    }

}
