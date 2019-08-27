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

package moe.tristan.easyfxml.samples.helloworld.view;

import javafx.stage.Stage;

import moe.tristan.easyfxml.FxUiManager;
import moe.tristan.easyfxml.api.FxmlComponent;
import moe.tristan.easyfxml.samples.helloworld.view.hello.HelloComponent;

public class HelloWorldUiManager extends FxUiManager {

    private final HelloComponent helloComponent;

    protected HelloWorldUiManager(HelloComponent helloComponent) {
        this.helloComponent = helloComponent;
    }

    /**
     * @return the main {@link Stage}'s title you want
     */
    @Override
    protected String title() {
        return "Hello, World!";
    }

    /**
     * @return the component to load as the root view in your main {@link Stage}.
     */
    @Override
    protected FxmlComponent mainComponent() {
        return helloComponent;
    }

}
