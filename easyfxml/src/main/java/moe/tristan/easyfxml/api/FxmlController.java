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

import javafx.application.Platform;

/**
 * This is a base interface for all controllers.
 * <p>
 * The {@link #initialize()} method is called by the JavaFX {@link Platform} after all the component's subcomponents
 * have been loaded and are ready for usage.
 * <p>
 * Never use the constructor to do any more than dependency injection as the components are not guaranteed to have been
 * loaded yet and generally result in the infamous {@link NullPointerException}.
 */
public interface FxmlController {

    /**
     * This method is automatically called by the JavaFX {@link Platform} as soon as all the components are loaded (not
     * necessarily rendered). This is where initial UX/UI setup should be done (enabling, disabling hiding etc...)
     * <p>
     * Calling it from the constructor is a hazard and will generally cause failures.
     */
    @SuppressWarnings("unused")
    void initialize();

}
