package moe.tristan.easyfxml.model.styles;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.function.Supplier;

public class CachedFxmlStylesheetTest {

    private static final Supplier<String> GET_DATA = () -> "test";

    @Test
    public void loadStylesheet() {

        final CachedFxmlStylesheet cfs = new CachedFxmlStylesheet() {
            @Override
            public String loadStylesheet() {
                return GET_DATA.get();
            }
        };

        //loads
        Assertions.assertThat(cfs.getStyle()).isEqualToIgnoringCase(GET_DATA.get());

        //loads only once and caches value
        Assertions.assertThat(cfs.getStyle()).isSameAs(cfs.getStyle());

    }
}
