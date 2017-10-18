package moe.tristan.easyfxml.model.fxml;

import java.net.URL;

import io.vavr.control.Option;
import io.vavr.control.Try;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import moe.tristan.easyfxml.EasyFxml;
import moe.tristan.easyfxml.model.FxmlController;
import moe.tristan.easyfxml.model.FxmlNode;
import moe.tristan.easyfxml.model.beanmanagement.ControllerManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

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
    protected BaseEasyFxml(final ApplicationContext context, final Environment environment, final ControllerManager controllerManager) {
        this.context = context;
        this.environment = environment;
        this.controllerManager = controllerManager;
    }

    private Option<FxmlController> makeControllerForNode(final FxmlNode node) {
        return node.getControllerClass().map(this.context::getBean);
    }

    @Override
    public Try<Pane> loadNode(final FxmlNode node) {
        return this.loadNode(node, Pane.class);
    }

    @Override
    public Try<Pane> loadNode(final FxmlNode node, final Object selector) {
        return this.loadNode(node, Pane.class, selector);
    }

    @Override
    public <T extends Node> Try<T> loadNode(final FxmlNode nodeInfo, final Class<T> nodeClass) {
        return this.loadNodeImpl(
            this.getSingleStageFxmlLoader(nodeInfo),
            nodeInfo,
            nodeClass
        );
    }

    @Override
    public <T extends Node> Try<T> loadNode(final FxmlNode nodeInfo, final Class<T> nodeClass, final Object selector) {
        return this.loadNodeImpl(
            this.getMultiStageFxmlLoader(nodeInfo, selector),
            nodeInfo,
            nodeClass
        );
    }

    /**
     * This method acts just like {@link #loadNode(FxmlNode)} but with no
     * autoconfiguration of controller binding and stylesheet application.
     */
    private  <T extends Node> Try<T> loadNodeImpl(final FxmlLoader fxmlLoader, final FxmlNode fxmlNode, final Class<T> clazz) {
        final String filePath = this.filePath(fxmlNode);
        fxmlLoader.setLocation(getUrlForResource(filePath));
        final Try<T> loadResult = Try.of(fxmlLoader::load).map(clazz::cast);

        loadResult.onSuccess(fxmlLoader::onSuccess).onFailure(fxmlLoader::onFailure);

        return this.applyStylesheetIfNeeded(
            fxmlNode,
            loadResult
        );
    }

    private <T extends Node> Try<T> applyStylesheetIfNeeded(final FxmlNode nodeInfo, final Try<T> nodeLoadResult) {
         nodeInfo.getStylesheet().peek(
            stylesheet -> nodeLoadResult.peek(
                    loadedNode -> loadedNode.setStyle(stylesheet.getStyle())
            )
        );

        return nodeLoadResult;
    }

    private FxmlLoader getSingleStageFxmlLoader(final FxmlNode node) {
        final FxmlLoader loader = this.context.getBean(FxmlLoader.class);
        final Option<FxmlController> instanceLoadingResult = this.makeControllerForNode(node);
        instanceLoadingResult.peek(instance -> {
            loader.setControllerFactory(clazz -> instance);
            loader.setOnSuccess(elem -> this.controllerManager.registerSingle(node, instance));
            loader.setOnFailure(cause -> LOG.error("Could not load node {}", node, cause));
        });
        return loader;
    }

    private FxmlLoader getMultiStageFxmlLoader(final FxmlNode node, final Object selector) {
        final FxmlLoader loader = this.context.getBean(FxmlLoader.class);
        final Option<FxmlController> instanceLoadingResult = this.makeControllerForNode(node);
        instanceLoadingResult.peek(instance -> {
            loader.setControllerFactory(clazz -> instance);
            loader.setOnSuccess(elem -> this.controllerManager.registerMultiple(node, selector, instance));
            loader.setOnFailure(cause -> LOG.error("Could not load node {}", node, cause));
        });
        return loader;
    }

    /**
     * @param fxmlNode The node who's filepath we look for
     * @return The node's {@link FxmlNode#getFxmlFile()} path prepended with the views root folder,
     * as defined by environment variable "moe.tristan.easyfxml.fxml.fxml_root_path".
     */
    private String filePath(final FxmlNode fxmlNode) {
        final String rootPath = Try.of(() -> "moe.tristan.easyfxml.fxml.fxml_root_path")
            .map(this.environment::getRequiredProperty)
            .getOrElse("");

        return rootPath + fxmlNode.getFxmlFile().getPath();
    }

    private static URL getUrlForResource(final String filePathString) {
        return BaseEasyFxml.class.getClassLoader().getResource(filePathString);
    }
}
