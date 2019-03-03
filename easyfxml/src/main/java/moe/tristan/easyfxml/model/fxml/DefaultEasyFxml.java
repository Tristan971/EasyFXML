package moe.tristan.easyfxml.model.fxml;

import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

import javafx.scene.Node;
import javafx.scene.layout.Pane;

import moe.tristan.easyfxml.EasyFxml;
import moe.tristan.easyfxml.api.FxmlController;
import moe.tristan.easyfxml.api.FxmlNode;
import moe.tristan.easyfxml.model.beanmanagement.ControllerManager;
import moe.tristan.easyfxml.model.beanmanagement.Selector;

import io.vavr.control.Try;

/**
 * This is the standard implementation of {@link EasyFxml}.
 */
public class DefaultEasyFxml implements EasyFxml {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultEasyFxml.class);

    private final ApplicationContext context;
    private final Environment environment;
    private final ControllerManager controllerManager;

    @Autowired
    protected DefaultEasyFxml(
        final ApplicationContext context,
        final Environment environment,
        final ControllerManager controllerManager
    ) {
        this.context = context;
        this.environment = environment;
        this.controllerManager = controllerManager;
    }

    private Try<FxmlController> makeControllerForNode(final FxmlNode node) {
        return Try.of(node::getControllerClass).map(this.context::getBean);
    }

    @Override
    public FxmlLoadResult<Pane, FxmlController> loadNode(final FxmlNode node) {
        return this.loadNode(node, Pane.class, FxmlController.class);
    }

    @Override
    public FxmlLoadResult<Pane, FxmlController> loadNode(final FxmlNode node, final Selector selector) {
        return this.loadNode(node, Pane.class, FxmlController.class, selector);
    }

    @Override
    public <NODE extends Node, CONTROLLER extends FxmlController>
    FxmlLoadResult<NODE, CONTROLLER>
    loadNode(
        final FxmlNode node,
        final Class<? extends NODE> nodeClass,
        final Class<? extends CONTROLLER> controllerClass
    ) {
        return this.loadNodeImpl(
            this.getSingleStageFxmlLoader(node),
            node,
            nodeClass,
            controllerClass
        );
    }

    @Override
    public <NODE extends Node, CONTROLLER extends FxmlController>
    FxmlLoadResult<NODE, CONTROLLER>
    loadNode(
        final FxmlNode node,
        final Class<? extends NODE> nodeClass,
        final Class<? extends CONTROLLER> controllerClass,
        final Selector selector
    ) {
        return this.loadNodeImpl(
            this.getMultiStageFxmlLoader(node, selector),
            node,
            nodeClass,
            controllerClass
        );
    }

    /**
     * This method acts just like {@link #loadNode(FxmlNode)} but with no autoconfiguration of controller binding and
     * stylesheet application.
     *
     * @param fxmlLoader      The loader to use. See {@link FxmlLoader} for why this matters.
     * @param fxmlNode        The node to load as declared in some enum most likely
     * @param nodeClass       The class to try to cast the node to
     * @param controllerClass The class to try to cast the controller to
     * @param <NODE>          The type of the node to load
     * @param <CONTROLLER>    The type of the controller to bind to the node
     *
     * @return The {@link FxmlLoadResult} related to this node attempt. Containing a load result for both the node and
     * the controller loaded, exposed through two {@link Try} instances.
     */
    private <NODE extends Node, CONTROLLER extends FxmlController>
    FxmlLoadResult<NODE, CONTROLLER>
    loadNodeImpl(
        final FxmlLoader fxmlLoader,
        final FxmlNode fxmlNode,
        final Class<? extends NODE> nodeClass,
        final Class<? extends CONTROLLER> controllerClass
    ) {
        final String filePath = determineComponentViewFileLocation(fxmlNode);
        LOGGER.debug("Determined that view file for component {} is to be read from path: {}", fxmlNode.getClass().getSimpleName(), filePath);

        final URL urlForFile = getUrlForClasspathFile(filePath);
        if (urlForFile == null) {
            throw new IllegalArgumentException("Could not find the queried classpath-located file at: " + filePath);
        }

        fxmlLoader.setLocation(urlForFile);
        final Try<NODE> nodeLoad = Try.of(fxmlLoader::load).map(nodeClass::cast);
        final Try<CONTROLLER> controllerLoad = Try.of(fxmlLoader::getController).map(controllerClass::cast);

        nodeLoad.onSuccess(fxmlLoader::onSuccess)
                .onFailure(fxmlLoader::onFailure);

        return new FxmlLoadResult<>(nodeLoad, controllerLoad);
    }

    private FxmlLoader getSingleStageFxmlLoader(final FxmlNode node) {
        final FxmlLoader loader = this.context.getBean(FxmlLoader.class);
        final Try<FxmlController> instanceLoadingResult = this.makeControllerForNode(node);
        instanceLoadingResult.peek(instance -> {
            loader.setControllerFactory(clazz -> instance);
            loader.setOnSuccess(elem -> this.controllerManager.registerSingle(node, instance));
            loader.setOnFailure(cause -> {
                throw new FxmlNodeLoadException(node, cause);
            });
        });
        return loader;
    }

    private FxmlLoader getMultiStageFxmlLoader(final FxmlNode node, final Selector selector) {
        final FxmlLoader loader = this.context.getBean(FxmlLoader.class);
        final Try<FxmlController> instanceLoadingResult = this.makeControllerForNode(node);
        instanceLoadingResult.peek(instance -> {
            loader.setControllerFactory(clazz -> instance);
            loader.setOnSuccess(elem -> this.controllerManager.registerMultiple(node, selector, instance));
            loader.setOnFailure(cause -> {
                throw new FxmlNodeLoadException(node, cause);
            });
        });
        return loader;
    }

    /**
     * @param fxmlNode The node who's filepath we look for
     *
     * @return The node's {@link FxmlNode#getFile()} path prepended with the views root folder, as defined by
     * environment variable "moe.tristan.easyfxml.fxml.fxml_root_path".
     */
    private String determineComponentViewFileLocation(final FxmlNode fxmlNode) {
        final String rootPath = Try.of(() -> "moe.tristan.easyfxml.fxml.fxml_root_path")
                                   .map(this.environment::getRequiredProperty)
                                   .getOrElse("");

        return rootPath + fxmlNode.getFile().getPath();
    }

    private URL getUrlForClasspathFile(final String filePathString) {
        return getClass().getClassLoader().getResource(filePathString);
    }

}
