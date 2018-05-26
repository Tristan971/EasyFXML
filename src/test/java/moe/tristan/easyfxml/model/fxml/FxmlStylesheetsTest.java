package moe.tristan.easyfxml.model.fxml;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class FxmlStylesheetsTest {

    @Test
    public void ensureConstantDoesntChange() {
        assertThat(FxmlStylesheets.DEFAULT_JAVAFX_STYLE.getPath()).isNull();
    }

}
