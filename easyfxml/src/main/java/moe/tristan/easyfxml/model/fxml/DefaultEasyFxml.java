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

import java.io.IOException;
import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javafx.scene.Node;
import javafx.scene.layout.Pane;

import moe.tristan.easyfxml.EasyFxml;
import moe.tristan.easyfxml.EasyFxmlProperties;
import moe.tristan.easyfxml.api.FxmlComponent;
import moe.tristan.easyfxml.api.FxmlController;
import moe.tristan.easyfxml.model.beanmanagement.ControllerManager;
import moe.tristan.easyfxml.model.beanmanagement.Selector;

import io.vavr.control.Try;

/**
 * This is the standard implementation of {@link EasyFxml}.
 */
@Component
public class DefaultEasyFxml implements EasyFxml {

    private final ApplicationContext context;
    private final ControllerManager controllerManager;
    private final EasyFxmlProperties easyFxmlProperties;

    @Autowired
    protected DefaultEasyFxml(
        final ApplicationContext context,
        final ControllerManager controllerManager,
        EasyFxmlProperties easyFxmlProperties
    ) {
        this.context = context;
        this.controllerManager = controllerManager;
        this.easyFxmlProperties = easyFxmlProperties;
    }

    private Try<FxmlController> makeControllerForComponent(final FxmlComponent component) {
        return Try.of(component::getControllerClass).map(this.context::getBean);
    }

    @Override
    public FxmlLoadResult<Pane, FxmlController> load(final FxmlComponent component) {
        return this.load(component, Pane.class, FxmlController.class);
    }

    @Override
    public FxmlLoadResult<Pane, FxmlController> load(final FxmlComponent component, final Selector selector) {
        return this.load(component, Pane.class, FxmlController.class, selector);
    }

    @Override
    public <NODE extends Node, CONTROLLER extends FxmlController>
    FxmlLoadResult<NODE, CONTROLLER>
    load(
        final FxmlComponent component,
        final Class<? extends NODE> componentClass,
        final Class<? extends CONTROLLER> controllerClass
    ) {
        return this.loadNodeImpl(
            this.getSingleStageFxmlLoader(component),
            component,
            componentClass,
            controllerClass
        );
    }

    @Override
    public <NODE extends Node, CONTROLLER extends FxmlController> FxmlLoadResult<NODE, CONTROLLER> load(
        final FxmlComponent component,
        final Class<? extends NODE> nodeClass,
        final Class<? extends CONTROLLER> controllerClass,
        final Selector selector
    ) {
        return this.loadNodeImpl(
            this.getMultiStageFxmlLoader(component, selector),
            component,
            nodeClass,
            controllerClass
        );
    }

    /**
     * This method acts just like {@link #load(FxmlComponent)} but with no autoconfiguration of controller binding and stylesheet application.
     *
     * @param fxmlLoader      The loader to use. See {@link FxmlLoader} for why this matters.
     * @param component       The node to load as declared in some enum most likely
     * @param nodeClass       The class to try to cast the node to
     * @param controllerClass The class to try to cast the controller to
     * @param <NODE>          The type of the node to load
     * @param <CONTROLLER>    The type of the controller to bind to the node
     *
     * @return The {@link FxmlLoadResult} related to this node attempt. Containing a load result for both the node and the controller loaded, exposed through
     * two {@link Try} instances.
     */
    private <NODE extends Node, CONTROLLER extends FxmlController>
    FxmlLoadResult<NODE, CONTROLLER>
    loadNodeImpl(
        final FxmlLoader fxmlLoader,
        final FxmlComponent component,
        final Class<? extends NODE> nodeClass,
        final Class<? extends CONTROLLER> controllerClass
    ) {
        final URL componentViewFileUrl = determineComponentViewFileLocation(component);

        fxmlLoader.setLocation(componentViewFileUrl);
        final Try<NODE> nodeLoad = Try.of(fxmlLoader::load).map(nodeClass::cast);
        final Try<CONTROLLER> controllerLoad = Try.of(fxmlLoader::getController).map(controllerClass::cast);

        nodeLoad
            .onSuccess(fxmlLoader::onSuccess)
            .onFailure(fxmlLoader::onFailure);

        return new FxmlLoadResult<>(nodeLoad, controllerLoad);
    }

    private FxmlLoader getSingleStageFxmlLoader(final FxmlComponent component) {
        final FxmlLoader loader = this.context.getBean(FxmlLoader.class);
        final Try<FxmlController> instanceLoadingResult = this.makeControllerForComponent(component);
        instanceLoadingResult.peek(instance -> {
            loader.setControllerFactory(clazz -> instance);
            loader.setOnSuccess(elem -> this.controllerManager.registerSingle(component, instance));
            loader.setOnFailure(cause -> {
                throw new FxmlComponentLoadException(component, cause);
            });
        });
        return loader;
    }

    private FxmlLoader getMultiStageFxmlLoader(final FxmlComponent component, final Selector selector) {
        final FxmlLoader loader = this.context.getBean(FxmlLoader.class);
        final Try<FxmlController> instanceLoadingResult = this.makeControllerForComponent(component);
        instanceLoadingResult.peek(instance -> {
            loader.setControllerFactory(clazz -> instance);
            loader.setOnSuccess(elem -> this.controllerManager.registerMultiple(component, selector, instance));
            loader.setOnFailure(cause -> {
                throw new FxmlComponentLoadException(component, cause);
            });
        });
        return loader;
    }

    /**
     * @param component the component whose FXML file we want a sanitized URL for
     *
     * @return a validated file URL for the given component's FXML file.
     */
    private URL determineComponentViewFileLocation(final FxmlComponent component) {
        String path = component.getFile().getPath();

        ClassPathResource resource = switch (easyFxmlProperties.getFxmlFileResolutionStrategy()) {
            case RELATIVE -> new ClassPathResource(path, component.getClass());
            case ABSOLUTE -> new ClassPathResource(path);
        };

        try {
            return resource.getURL();
        } catch (IOException e) {
            throw new IllegalStateException(String.format(
                "Cannot load file [%s] for component [%s]",
                path,
                component
            ));
        }
    }

}
