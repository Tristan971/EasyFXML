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

package moe.tristan.easyfxml;

import static moe.tristan.easyfxml.model.fxml.FxmlFileResolutionStrategy.RELATIVE;

import java.util.Objects;

import org.springframework.boot.context.properties.ConfigurationProperties;

import moe.tristan.easyfxml.model.fxml.FxmlFileResolutionStrategy;

@ConfigurationProperties(prefix = "moe.tristan.easyfxml")
public class EasyFxmlProperties {

    /**
     * Base classpath location in which to look for fxml files
     */
    private FxmlFileResolutionStrategy fxmlFileResolutionStrategy = RELATIVE;

    public FxmlFileResolutionStrategy getFxmlFileResolutionStrategy() {
        return fxmlFileResolutionStrategy;
    }

    public void setFxmlFileResolutionStrategy(FxmlFileResolutionStrategy fxmlFileResolutionStrategy) {
        this.fxmlFileResolutionStrategy = Objects.requireNonNull(fxmlFileResolutionStrategy);
    }

}
