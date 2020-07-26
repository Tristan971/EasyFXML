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

package moe.tristan.easyfxml.model.beanmanagement;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import moe.tristan.easyfxml.EasyFxmlAutoConfiguration;

@ContextConfiguration(classes = EasyFxmlAutoConfiguration.class)
@ExtendWith(SpringExtension.class)
public class AbstractInstanceManagerTest {

    private AbstractInstanceManager<String, String> instanceManager;

    private static final String PARENT = "PARENT";
    private static final String ACTUAL_1 = "ACTUAL1";
    private static final String ACTUAL_2 = "ACTUAL2";
    private static final Selector SEL_1 = new Selector("SEL1");
    private static final Selector SEL_2 = new Selector("SEL2");

    @BeforeEach
    public void setUp() {
        this.instanceManager = new AbstractInstanceManager<>() {
        };
    }

    @Test
    public void registerSingle() {
        this.instanceManager.registerSingle(PARENT, ACTUAL_1);
        assertThat(this.instanceManager.getSingle(PARENT).get()).isEqualTo(ACTUAL_1);
    }

    @Test
    public void registerMultiple() {
        this.instanceManager.registerMultiple(PARENT, SEL_1, ACTUAL_1);
        this.instanceManager.registerMultiple(PARENT, SEL_2, ACTUAL_2);

        assertThat(this.instanceManager.getMultiple(PARENT, SEL_1).get()).isEqualTo(ACTUAL_1);
        assertThat(this.instanceManager.getMultiple(PARENT, SEL_2).get()).isEqualTo(ACTUAL_2);
    }

    @Test
    public void getAll() {
        this.instanceManager.registerSingle(PARENT, ACTUAL_1);
        this.instanceManager.registerSingle(PARENT, ACTUAL_2);
        this.instanceManager.registerMultiple(PARENT, SEL_1, ACTUAL_1);
        this.instanceManager.registerMultiple(PARENT, SEL_2, ACTUAL_2);

        final List<String> all = this.instanceManager.getAll(PARENT);
        // Only one exemplary of ACTUAL_1 since the second call to register single overrides the first one
        assertThat(all).containsExactlyInAnyOrder(
            ACTUAL_1, ACTUAL_2, ACTUAL_2
        );
    }

    @Test
    public void getMultiples() {
        this.instanceManager.registerMultiple(PARENT, SEL_1, ACTUAL_1);
        this.instanceManager.registerMultiple(PARENT, SEL_2, ACTUAL_2);

        assertThat(this.instanceManager.getMultiples(PARENT)).containsExactlyInAnyOrder(ACTUAL_1, ACTUAL_2);
    }

}
