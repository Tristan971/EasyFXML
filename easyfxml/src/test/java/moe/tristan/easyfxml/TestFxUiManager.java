package moe.tristan.easyfxml;

import moe.tristan.easyfxml.api.FxmlNode;
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

}
