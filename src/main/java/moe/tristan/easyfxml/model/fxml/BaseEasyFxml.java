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
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * This is the standard implementation of {@link EasyFxml}.
 */
@Component
public class BaseEasyFxml implements EasyFxml {

    private final Environment environment;
    private final Supplier<FXMLLoader> fxmlLoaders;
    private final Function<FxmlNode, FxmlController> controllerInstanciator;
    private final ControllerManager controllerManager;

    @Autowired
    protected BaseEasyFxml(final ApplicationContext context, final Environment environment, final ControllerManager controllerManager) {
        this.fxmlLoaders = () -> context.getBean(FXMLLoader.class);
        this.controllerInstanciator = node -> context.getBean(node.getControllerClass());
        this.environment = environment;
        this.controllerManager = controllerManager;
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
            nodeInfo
        );
    }

    @Override
    public <T extends Node> Try<T> loadNode(final FxmlNode nodeInfo, final Class<T> nodeClass, final Object selector) {
        return this.loadNodeImpl(
            this.getMultiStageFxmlLoader(nodeInfo, selector),
            nodeInfo
        );
    }

    /**
     * This method acts just like {@link #loadNode(FxmlNode)} but with no
     * autoconfiguration of controller binding and stylesheet application.
     */
    protected <T extends Node> Try<T> loadNodeImpl(final FXMLLoader fxmlLoader, final FxmlNode fxmlNode) {
        final String filePath = this.filePath(fxmlNode);
        fxmlLoader.setLocation(getUrlForResource(filePath));
        Try<T> loadResult = Try.of(fxmlLoader::load);

        return this.applyStylesheetIfNeeded(
            fxmlNode,
            loadResult
        );
    }

    private <T extends Node> Try<T> applyStylesheetIfNeeded(final FxmlNode nodeInfo, final Try<T> loadResult) {
        nodeInfo.getStylesheet().peek(stylesheet ->
            loadResult.peek(success ->
                success.setStyle(stylesheet.getCssContent())
            )
        );
        return loadResult;
    }

    private FXMLLoader getSingleStageFxmlLoader(final FxmlNode node) {
        final FXMLLoader loader = this.fxmlLoaders.get();
        loader.setControllerFactory(clazz -> {
            final FxmlController controllerInstance = this.controllerInstanciator.apply(node);
            this.controllerManager.registerSingle(node, controllerInstance);
            return controllerInstance;
        });
        return loader;
    }

    private FXMLLoader getMultiStageFxmlLoader(final FxmlNode node, final Object selector) {
        final FXMLLoader loader = this.fxmlLoaders.get();
        loader.setControllerFactory(clazz -> {
            final FxmlController controllerInstance = this.controllerInstanciator.apply(node);
            this.controllerManager.registerMultiple(node, selector, controllerInstance);
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

    private static URL getUrlForResource(final String filePathString) {
        return BaseEasyFxml.class.getClassLoader().getResource(filePathString);
    }
}
