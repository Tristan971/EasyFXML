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
public class BaseEasyFxml implements EasyFxml {

    private final ApplicationContext context;

    @Autowired
    protected BaseEasyFxml(final ApplicationContext context) {
        this.context = context;
    }

    /**
     * Loads a {@link FxmlFile} as a Pane (this is the safest base class for all sorts of hierarchies)
     * since most of the base containers are subclasses of it.
     *
     * It returns a {@link Try} which is a monadic structure which allows us to do clean error-handling.
     *
     * @param view The file's {@link FxmlFile} counterpart. Try to avoid loading things using manual path
     *             as it implies losing a lot of coding safety. It does work fine as well though if you
     *             really dislike {@link FxmlFile}.
     *
     * @return A {@link Try} containing either the file {@link Try.Success} or the exception that was first
     * raised during the chain of nested function calls needed to load it. See {@link Try#getOrElse(Object)}
     * and related methods for how to handle {@link Try.Failure}.
     */
    @Override
    public Try<Pane> getPaneForView(final FxmlFile view) {
        return this.getPaneForFile(view.getPath());
    }

    /**
     * This method is the direct path access counterpart of {@link #getPaneForView(FxmlFile)}.
     * It is discouraged to use it and left out of the {@link EasyFxml} interface but left
     * accessible by casting {@link EasyFxml} to this class, or by subclassing.
     *
     * It is considered outside of the scope of the safe-to-use API and should be thought of as
     * a unofficial usage of the API.
     *
     * @param filePathString The path to a FXML file either inside or outside classpath.
     *                       If inside classpath the same root of /target/classes/ is assumed
     *                       by the usage of {@link #getURLForView(String)}.
     *
     * @return A {@link Try} working exactly in the same way as {@link #getPaneForView(FxmlFile)}.
     */
    @SuppressWarnings("PublicMethodNotExposedInInterface")
    public Try<Pane> getPaneForFile(final String filePathString) {
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
        return BaseEasyFxml.class.getClassLoader().getResource(filePathString);
    }
}
