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

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * This class is a convenience boilerplate set-up for you to mix JavaFX and Spring together. It is absolutely not needed
 * but I believe one could find peace in not having to deal with the configuration of two frameworks.
 * <p>
 * The usage is examplified in this project's documentation over at
 * <a href="https://tristan971.github.io/EasyFXML/">Tristan971/EasyFXML</a>.
 * <p>
 * The gist of it is that your "main" class is expected to extend this class instead of {@link Application} (which this
 * one does extend anyway) and that you just declare your main function with a call to {@link #launch(String...)}. Then
 * it is expected that your main class is annotated with {@link SpringBootApplication}.
 * <p>
 * This class initializes the application with a call to {@link #beforeSpringInit()}, then initializing Spring, then
 * calling {@link #afterSpringInit()}. These tho methods can be overriden and you can do calls to {@link #springContext}
 * in {@link #afterSpringInit()}, should you need to do things that require Spring before the UI loads (or {@link
 * #beforeSpringInit()} in the case of things to do before Spring is even started.
 * <p>
 * When initialization is done, this class calls your implementation of {@link FxUiManager}. You can of course bypass
 * that, should you not like the layout of {@link FxUiManager}, by overriding {@link #start(Stage)}.
 *
 * @see FxUiManager
 */
@Import(EasyFxmlAutoConfiguration.class)
public abstract class FxApplication extends Application {

    protected ConfigurableApplicationContext springContext;

    @Override
    public void init() {
        beforeSpringInit();
        this.springContext = getSab().run();
        afterSpringInit();
    }

    protected SpringApplicationBuilder getSab() {
        return new SpringApplicationBuilder(getClass())
            .main(getClass())
            .headless(false)
            .web(WebApplicationType.NONE);
    }

    protected void beforeSpringInit() {
        // do nothing by default
    }

    protected void afterSpringInit() {
        // do nothing by default
    }

    @Override
    public void start(final Stage primaryStage) {
        this.springContext.getBean(FxUiManager.class).startGui(primaryStage);
    }

    @Override
    public void stop() {
        this.springContext.stop();
    }

}
