package moe.tristan.easyfxml.model.fxml;

import io.vavr.control.Try;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import lombok.extern.slf4j.Slf4j;
import moe.tristan.easyfxml.EasyFxml;
import moe.tristan.easyfxml.FxmlNode;
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

    @Autowired
    protected BaseEasyFxml(final ApplicationContext context, final Environment environment) {
        this.context = context;
        this.environment = environment;
    }

    @Override
    public Try<Pane> loadNode(final FxmlNode node) {
        return this.loadFileAsPane(this.filePath(node));
    }

    @Override
    public <T extends Node> Try<T> loadNode(final FxmlNode node, final Class<T> nodeClass) {
        return this.loadFileAsPane(this.filePath(node));
    }

    /**
     * This method acts just like {@link #loadNode(FxmlNode)} but with no
     * autoconfiguration of controller binding and stylesheet application.
     */
    protected <T> Try<T> loadFileAsPane(final String filePathString) {
        final FXMLLoader loader = this.context.getBean(FXMLLoader.class);
        loader.setLocation(getURLForView(filePathString));

        return Try.of(loader::load);
    }

    private String filePath(final FxmlNode fxmlNode) {
        return this.prependFxmlRootPath(fxmlNode.getFxmlFile().getPath());
    }

    private String prependFxmlRootPath(final String filePathString) {
        return this.environment.getRequiredProperty("moe.tristan.easyfxml.fxml.fxml_root_path") + filePathString;
    }

    private static URL getURLForView(final String filePathString) {
        return BaseEasyFxml.class.getClassLoader().getResource(filePathString);
    }
}
