package moe.tristan.easyfxml.model.fxml;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import moe.tristan.easyfxml.EasyFxml;
import moe.tristan.easyfxml.api.FxmlController;
import moe.tristan.easyfxml.api.FxmlNode;
import moe.tristan.easyfxml.model.beanmanagement.ControllerManager;
import moe.tristan.easyfxml.model.beanmanagement.Selector;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.scene.Node;
import javafx.scene.layout.Pane;

import java.net.URL;

/**
 * This is the standard implementation of {@link EasyFxml}.
 */
@Component
public class BaseEasyFxml implements EasyFxml {

    private static final Logger LOG = LoggerFactory.getLogger(BaseEasyFxml.class);

    private final ApplicationContext context;
    private final Environment environment;
    private final ControllerManager controllerManager;

    @Autowired
    protected BaseEasyFxml(
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
    public <N_CLAZZ extends Node, C_CLAZZ extends FxmlController>
    FxmlLoadResult<N_CLAZZ, C_CLAZZ>
    loadNode(
        final FxmlNode node,
        final Class<N_CLAZZ> nodeClass,
        final Class<C_CLAZZ> controllerClass
    ) {
        return this.loadNodeImpl(
            this.getSingleStageFxmlLoader(node),
            node,
            nodeClass,
            controllerClass
        );
    }

    @Override
    public <N_CLAZZ extends Node, C_CLAZZ extends FxmlController>
    FxmlLoadResult<N_CLAZZ, C_CLAZZ>
    loadNode(
        final FxmlNode node,
        final Class<N_CLAZZ> nodeClass,
        final Class<C_CLAZZ> controllerClass,
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
     * @param <N_CLAZZ>       The type of the node to load
     * @param <C_CLAZZ>       The type of the controller to bind to the node
     *
     * @return The {@link FxmlLoadResult} related to this node attempt. Containing a load result for both the node and
     * the controller loaded, exposed through two {@link Try} instances.
     */
    private <N_CLAZZ extends Node, C_CLAZZ extends FxmlController>
    FxmlLoadResult<N_CLAZZ, C_CLAZZ>
    loadNodeImpl(
        final FxmlLoader fxmlLoader,
        final FxmlNode fxmlNode,
        final Class<N_CLAZZ> nodeClass,
        final Class<C_CLAZZ> controllerClass
    ) {
        final String filePath = this.filePath(fxmlNode);
        fxmlLoader.setLocation(getUrlForResource(filePath));
        final Try<N_CLAZZ> nodeLoad = Try.of(fxmlLoader::load).map(nodeClass::cast);
        final Try<C_CLAZZ> controllerLoad = Try.of(fxmlLoader::getController).map(controllerClass::cast);

        nodeLoad.onSuccess(fxmlLoader::onSuccess).onFailure(fxmlLoader::onFailure);

        return new FxmlLoadResult<>(nodeLoad, controllerLoad);
    }

    private FxmlLoader getSingleStageFxmlLoader(final FxmlNode node) {
        final FxmlLoader loader = this.context.getBean(FxmlLoader.class);
        final Try<FxmlController> instanceLoadingResult = this.makeControllerForNode(node);
        instanceLoadingResult.peek(instance -> {
            loader.setControllerFactory(clazz -> instance);
            loader.setOnSuccess(elem -> this.controllerManager.registerSingle(node, instance));
            loader.setOnFailure(cause -> LOG.error("Could not load node {}", node, cause));
        });
        return loader;
    }

    private FxmlLoader getMultiStageFxmlLoader(final FxmlNode node, final Selector selector) {
        final FxmlLoader loader = this.context.getBean(FxmlLoader.class);
        final Try<FxmlController> instanceLoadingResult = this.makeControllerForNode(node);
        instanceLoadingResult.peek(instance -> {
            loader.setControllerFactory(clazz -> instance);
            loader.setOnSuccess(elem -> this.controllerManager.registerMultiple(node, selector, instance));
            loader.setOnFailure(cause -> LOG.error("Could not load node {}", node, cause));
        });
        return loader;
    }

    /**
     * @param fxmlNode The node who's filepath we look for
     *
     * @return The node's {@link FxmlNode#getFile()} path prepended with the views root folder, as defined by
     * environment variable "moe.tristan.easyfxml.fxml.fxml_root_path".
     */
    private String filePath(final FxmlNode fxmlNode) {
        final String rootPath = Try.of(() -> "moe.tristan.easyfxml.fxml.fxml_root_path")
                                   .map(this.environment::getRequiredProperty)
                                   .getOrElse("");

        return rootPath + fxmlNode.getFile().getPath();
    }

    private static URL getUrlForResource(final String filePathString) {
        return BaseEasyFxml.class.getClassLoader().getResource(filePathString);
    }
}
