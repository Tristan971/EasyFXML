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

package moe.tristan.easyfxml.api;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.Objects;

import org.junit.Before;
import org.junit.Test;

import moe.tristan.easyfxml.util.Resources;

public class FxmlStylesheetTest {

    private static final String RES_REL_PATH = "css/test_style.css";

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
