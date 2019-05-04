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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import moe.tristan.easyfxml.EasyFxmlAutoConfiguration;

@ContextConfiguration(classes = EasyFxmlAutoConfiguration.class)
@RunWith(SpringRunner.class)
public class FxmlNodeEnum {

    @Autowired
    private ComponentByOwnClass componentByOwnClass;

    @Test
    public void springConfigSupportsBothByClassComponentAndByEnumComponents() {
        assertThat(componentByOwnClass).isNotNull();
    }

    public static class ComponentByOwnClass implements FxmlNode {

        @Override
        public FxmlFile getFile() {
            return null;
        }

        @Override
        public Class<? extends FxmlController> getControllerClass() {
            return null;
        }

    }

    public enum ComponentsByEnum implements FxmlNode {
        TEST;

        @Override
        public FxmlFile getFile() {
            return null;
        }

        @Override
        public Class<? extends FxmlController> getControllerClass() {
            return null;
        }
    }

}
