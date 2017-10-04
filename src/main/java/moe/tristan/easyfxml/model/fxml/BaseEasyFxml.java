package moe.tristan.easyfxml.model.fxml;

import io.vavr.control.Try;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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
    public Try<Pane> loadNode(final FxmlNode node, final Object selector) {
        return this.loadNode(node, Pane.class, selector);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends Node> Try<T> loadNode(final FxmlNode node, final Class<T> nodeClass) {
        return this.loadNodeImpl(this.getSingleStageFxmlLoader(node), this.filePath(node));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends Node> Try<T> loadNode(final FxmlNode node, final Class<T> nodeClass, final Object selector) {
        return this.loadNodeImpl(this.getSingleStageFxmlLoader(node), this.filePath(node));
    }

    private FXMLLoader getSingleStageFxmlLoader(final FxmlNode node) {
        final FXMLLoader loader = this.context.getBean(FXMLLoader.class);
        loader.setControllerFactory(clazz -> {
            final FxmlController controllerInstance = context.getBean(node.getControllerClass());
            controllerManager.registerSingle(node, controllerInstance);
            return controllerInstance;
        });
        return loader;
    }

    private FXMLLoader getMultiStageFxmlLoader(final FxmlNode node, final Object selector) {
        final FXMLLoader loader = this.context.getBean(FXMLLoader.class);
        loader.setControllerFactory(clazz -> {
            final FxmlController controllerInstance = context.getBean(node.getControllerClass());
            controllerManager.registerMultiple(node, selector, controllerInstance);
            return controllerInstance;
        });
        return loader;
    }

    /**
     * This method acts just like {@link #loadNode(FxmlNode)} but with no
     * autoconfiguration of controller binding and stylesheet application.
     */
    protected <T> Try<T> loadNodeImpl(final FXMLLoader fxmlLoader, final String filePathString) {
        fxmlLoader.setLocation(getURLForView(filePathString));
        return Try.of(fxmlLoader::load);
    }

    /**
     * @param fxmlNode The node who's filepath we look for
     * @return The node's {@link FxmlNode#getFxmlFile()} path prepended with the views root folder,
     * as defined by environment variable "moe.tristan.easyfxml.fxml.fxml_root_path".
     */
    private String filePath(final FxmlNode fxmlNode) {
        return this.prependFxmlRootPath(fxmlNode.getFxmlFile().getFxmlFilePath());
    }

    private String prependFxmlRootPath(final String filePathString) {
        return this.environment.getRequiredProperty("moe.tristan.easyfxml.fxml.fxml_root_path") + filePathString;
    }

    private static URL getURLForView(final String filePathString) {
        return BaseEasyFxml.class.getClassLoader().getResource(filePathString);
    }
}
