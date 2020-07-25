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

import java.util.Optional;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javafx.scene.Scene;
import javafx.stage.Stage;

import moe.tristan.easyfxml.api.FxmlComponent;
import moe.tristan.easyfxml.api.FxmlStylesheet;
import moe.tristan.easyfxml.model.exception.ExceptionHandler;
import moe.tristan.easyfxml.model.fxml.FxmlStylesheets;
import moe.tristan.easyfxml.util.Stages;

import io.vavr.control.Option;
import io.vavr.control.Try;

/**
 * The {@link FxUiManager} is one take on a bootstrapping class for the JavaFX UI.
 * <p>
 * The goal being both to decouple the GUI code from the main class and also to offer a somewhat declarative entry point for the JavaFX side of your project.
 * <p>
 * Every method's overriding possibilities is detailed on it.
 * <p>
 * The minimal overriding is that of :
 * <p>
 * - {@link #title()} for the main stage's title.
 * <p>
 * - {@link #mainComponent()} for the main scene info as a {@link FxmlComponent}.
 * <p>
 * - {@link #getStylesheet()} that is optionally overriden if you use a custom stylesheet. If you don't need one, ignore it.
 */
public abstract class FxUiManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(FxUiManager.class);

    private EasyFxml easyFxml;

    /**
     * Called by {@link FxApplication} after Spring and JavaFX are started. This is the equivalent of {@link javafx.application.Application#start(Stage)} in
     * traditional JavaFX apps.
     *
     * @param mainStage A reference to the main stage of the application, received from JavaFX via {@link javafx.application.Application#start(Stage)}.
     */
    public void startGui(final Stage mainStage) {
        try {
            onStageCreated(mainStage);
            final Scene mainScene = getScene(mainComponent());
            onSceneCreated(mainScene);
            mainStage.setScene(mainScene);
            mainStage.setTitle(title());

            Option.ofOptional(getStylesheet())
                  .filter(style -> !FxmlStylesheets.DEFAULT_JAVAFX_STYLE.equals(style))
                  .peek(stylesheet -> this.setTheme(stylesheet, mainStage));

            mainStage.show();
        } catch (Throwable t) {
            throw new RuntimeException("There was a failure during start-up of the application.", t);
        }
    }

    /**
     * @return The title to give the main stage
     */
    protected abstract String title();

    /**
     * @return The component to load in the main stage upon startup
     */
    protected abstract FxmlComponent mainComponent();

    /**
     * Called right after the main {@link Scene} was created if you want to edit it.
     *
     * @param mainScene The main scene of the application
     */
    @SuppressWarnings("unused")
    protected void onSceneCreated(final Scene mainScene) {
        //do nothing by default
    }

    /**
     * Called as we enter the {@link #startGui(Stage)} method.
     *
     * @param mainStage The main stage, supplied by JavaFX's {@link javafx.application.Application#start(Stage)} method.
     */
    @SuppressWarnings("unused")
    protected void onStageCreated(final Stage mainStage) {
        //do nothing by default
    }

    /**
     * @return If overriden, this function returns the {@link FxmlStylesheet} to apply to the main window.
     */
    protected Optional<FxmlStylesheet> getStylesheet() {
        return Optional.empty();
    }

    /**
     * Simple utility class to load an {@link FxmlComponent} as a {@link Scene} for use with {@link #mainComponent()}
     *
     * @param component the component to load in the {@link Scene}
     *
     * @return The ready-to-use {@link Scene}
     *
     * @throws RuntimeException if the scene could not be loaded properly
     */
    protected Scene getScene(final FxmlComponent component) {
        return easyFxml
            .load(component)
            .getNode()
            .map(Scene::new)
            .getOrElseThrow((Function<? super Throwable, RuntimeException>) RuntimeException::new);
    }

    private void setTheme(final FxmlStylesheet stylesheet, Stage mainStage) {
        Try.of(() -> stylesheet)
           .mapTry(FxmlStylesheet::getExternalForm)
           .mapTry(stylesheetUri -> Stages.setStylesheet(mainStage, stylesheetUri))
           .orElseRun(err -> ExceptionHandler.displayExceptionPane(
               "Could not load theme!",
               "Could not load theme file with description : " + stylesheet,
               err
           ));
    }

    @Autowired
    public void setEasyFxml(EasyFxml easyFxml) {
        this.easyFxml = easyFxml;
    }

}
