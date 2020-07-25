/*
 * Copyright 2017 - 2019 EasyFXML project and contributors
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package moe.tristan.easyfxml.model.fxml;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.function.Consumer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

import moe.tristan.easyfxml.EasyFxmlAutoConfiguration;

@ContextConfiguration(classes = EasyFxmlAutoConfiguration.class)
@ExtendWith(SpringExtension.class)
public class FxmlLoaderTest {

    private static final Logger LOG = LoggerFactory.getLogger(FxmlLoaderTest.class);

    @Autowired
    private ApplicationContext context;

    private int succ = 0;
    private int fail = 0;
    private FxmlLoader fxmlLoader;

    @BeforeEach
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
        final Consumer<Node> testConsumer = node -> LOG.debug(node.toString());

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
