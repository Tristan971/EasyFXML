package moe.tristan.easyfxml.model.fxml;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class FxmlLoaderTest {

    private int succ = 0;
    private int fail = 0;
    private FxmlLoader fxmlLoader;

    @Before
    public void setUp() {
        fxmlLoader = new FxmlLoader();
        fxmlLoader.setOnSuccess(n -> succ++);
        fxmlLoader.setOnFailure(e -> fail++);
    }

    @Test
    public void onSuccess() {
        fxmlLoader.onSuccess(null);
        assertThat(succ).isEqualTo(1);
        assertThat(fail).isEqualTo(0);
    }

    @Test
    public void onFailure() {
        fxmlLoader.onFailure(null);
        assertThat(succ).isEqualTo(0);
        assertThat(fail).isEqualTo(1);
    }
}
