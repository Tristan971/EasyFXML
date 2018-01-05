package moe.tristan.easyfxml.util;

import org.assertj.core.api.Assertions;
import org.junit.Test;

public class CachedFxmlStylesheetTest {

    @Test
    public void loadStylesheet() {
        final CachedFxmlStylesheet cfs = new CachedFxmlStylesheet() {
            @Override
            public String loadStylesheet() {
                return "test";
            }
        };

        Assertions.assertThat(cfs.getStyle()).isEqualToIgnoringCase("test");
    }
}
