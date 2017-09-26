package moe.tristan.easyfxml.model.fxml;

import io.vavr.control.Try;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import lombok.extern.slf4j.Slf4j;
import moe.tristan.easyfxml.EasyFxml;
import moe.tristan.easyfxml.FxmlFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;

/**
 * Created by Tristan on 06/02/2017.
 */
@Slf4j
@Component
public abstract class AbstractEasyFxml implements EasyFxml {

    private final ApplicationContext context;

    @Autowired
    protected AbstractEasyFxml(final ApplicationContext context) {
        this.context = context;
    }

    @Override
    public Try<Pane> getPaneForView(final FxmlFile view) {
        return this.getPaneForFile(view.getPath());
    }

    private Try<Pane> getPaneForFile(final String filePathString) {
        final FXMLLoader loader = this.context.getBean(FXMLLoader.class);
        loader.setLocation(getURLForView(filePathString));
        try {
            final Pane filePane = loader.load();
            return Try.of(() -> filePane);
        } catch (final IOException e) {
            log.error("Could not locate file at path : " + filePathString, e);
            return Try.failure(e);
        }
    }

    private static URL getURLForView(final String filePathString) {
        return AbstractEasyFxml.class.getClassLoader().getResource(filePathString);
    }
}
