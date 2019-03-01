package moe.tristan.easyfxml.model.fxml;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class FxmlStylesheetsTest {

    @Test
    public void ensureConstantDoesntChange() {
        assertThat(FxmlStylesheets.DEFAULT_JAVAFX_STYLE.getPath()).isNull();
    }

}
