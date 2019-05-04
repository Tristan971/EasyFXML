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

/**
 * Base class to implement for your fxml file equivalents.
 * <p>
 * Implementation in an enum is the recommended way to use it.
 */
public interface FxmlFile {

    /**
     * @return the path relative to the classpath root (/target/classes in Maven's default model) as a {@link String}.
     */
    String getPath();

}
