package moe.tristan.easyfxml.model.fxml;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import moe.tristan.easyfxml.spring.application.FxSpringContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = FxSpringContext.class)
public class FxmlLoaderTest {

    @Autowired
    private ApplicationContext context;

    private int succ = 0;
    private int fail = 0;
    private FxmlLoader fxmlLoader;

    @Before
    public void setUp() {
        fxmlLoader = new FxmlLoader(context);
        fxmlLoader.setOnSuccess(n -> succ++);
        fxmlLoader.setOnFailure(e -> fail++);
    }

    @Test
    public void onSuccess() {
        new FxmlLoader(context).onSuccess(null);
        assertThat(succ).isEqualTo(0);
        assertThat(fail).isEqualTo(0);

        fxmlLoader.onSuccess(null);
        assertThat(succ).isEqualTo(1);
        assertThat(fail).isEqualTo(0);
    }

    @Test
    public void onFailure() {
        new FxmlLoader(context).onFailure(null);
        assertThat(succ).isEqualTo(0);
        assertThat(fail).isEqualTo(0);

        fxmlLoader.onFailure(null);
        assertThat(succ).isEqualTo(0);
        assertThat(fail).isEqualTo(1);
    }

    @Test
    public void eqHashCode() throws MalformedURLException {
        final FxmlLoader fl1 = new FxmlLoader(context);
        final FxmlLoader fl2 = new FxmlLoader(context);

        final URL testURL = new URL("https://www.example.com");
        final Consumer<Node> testConsumer = System.out::println;

        assertThat(fl1).isEqualTo(fl1);
        assertThat(new Object()).isNotEqualTo(fl1);
        assertThat(fl1).isNotEqualTo(null);

        assertThat(fl1).isEqualTo(fl2);
        assertThat(fl1.hashCode()).isEqualTo(fl2.hashCode());

        fl1.setLocation(testURL);
        assertThat((FXMLLoader) fl1).isNotEqualTo(fl2);
        assertThat(fl1.hashCode()).isNotEqualTo(fl2.hashCode());

        fl2.setLocation(testURL);
        assertThat((FXMLLoader) fl1).isEqualTo(fl2);
        assertThat(fl1.hashCode()).isEqualTo(fl2.hashCode());

        fl1.setOnSuccess(testConsumer);
        assertThat(fl1).isNotEqualTo(fl2);
        assertThat(fl1.hashCode()).isNotEqualTo(fl2.hashCode());

        fl2.setOnSuccess(testConsumer);
        assertThat(fl2).isEqualTo(fl1);
        assertThat(fl1.hashCode()).isEqualTo(fl2.hashCode());
    }

}
