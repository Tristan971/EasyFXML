package moe.tristan.easyfxml.model.fxml;

import io.vavr.control.Try;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import moe.tristan.easyfxml.EasyFxml;
import moe.tristan.easyfxml.model.FxmlController;
import moe.tristan.easyfxml.model.FxmlNode;
import moe.tristan.easyfxml.model.beanmanagement.ControllerManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.net.URL;

/**
 * This is the standard implementation of {@link EasyFxml}.
 */
@Service
public class BaseEasyFxml implements EasyFxml {

    private final ApplicationContext context;
    private final Environment environment;
    private final ControllerManager controllerManager;

    @Autowired
    protected BaseEasyFxml(final ApplicationContext context, final Environment environment, final ControllerManager controllerManager) {
        this.context = context;
        this.environment = environment;
        this.controllerManager = controllerManager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Try<Pane> loadNode(final FxmlNode node) {
        return this.loadNode(node, Pane.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends Node> Try<T> loadNode(final FxmlNode nodeInfo, final Class<T> nodeClass) {
        final Try<T> loadedNode = this.loadNodeImpl(
            this.getSingleStageFxmlLoader(nodeInfo),
            this.filePath(nodeInfo)
        );

        return this.applyStylesheetIfNeeded(
            nodeInfo,
            loadedNode
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Try<Pane> loadNode(final FxmlNode node, final Object selector) {
        return this.loadNode(node, Pane.class, selector);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends Node> Try<T> loadNode(final FxmlNode nodeInfo, final Class<T> nodeClass, final Object selector) {
        final Try<T> loadResult = this.loadNodeImpl(
            this.getSingleStageFxmlLoader(nodeInfo),
            this.filePath(nodeInfo)
        );

        return this.applyStylesheetIfNeeded(
            nodeInfo,
            loadResult
        );
    }

    /**
     * This method acts just like {@link #loadNode(FxmlNode)} but with no
     * autoconfiguration of controller binding and stylesheet application.
     */
    protected <T> Try<T> loadNodeImpl(final FXMLLoader fxmlLoader, final String filePathString) {
        fxmlLoader.setLocation(getURLForView(filePathString));
        return Try.of(fxmlLoader::load);
    }

    private FXMLLoader getSingleStageFxmlLoader(final FxmlNode node) {
        final FXMLLoader loader = this.context.getBean(FXMLLoader.class);
        loader.setControllerFactory(clazz -> {
            final FxmlController controllerInstance = this.context.getBean(node.getControllerClass());
            this.controllerManager.registerSingle(node, controllerInstance);
            return controllerInstance;
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

    private <T extends Node> Try<T> applyStylesheetIfNeeded(final FxmlNode nodeInfo, final Try<T> loadResult) {
        nodeInfo.getStylesheet().peek(stylesheet ->
            loadResult.peek(positiveLoadResult ->
                positiveLoadResult.setStyle(stylesheet.getCssContent())
            )
        );
        return loadResult;
    }

    private FXMLLoader getMultiStageFxmlLoader(final FxmlNode node, final Object selector) {
        final FXMLLoader loader = this.context.getBean(FXMLLoader.class);
        loader.setControllerFactory(clazz -> {
            final FxmlController controllerInstance = this.context.getBean(node.getControllerClass());
            this.controllerManager.registerMultiple(node, selector, controllerInstance);
            return controllerInstance;
        });
        return loader;
    }

    private static URL getURLForView(final String filePathString) {
        return BaseEasyFxml.class.getClassLoader().getResource(filePathString);
    }
}
