package moe.tristan.easyfxml.spring.application;

import moe.tristan.easyfxml.EasyFxml;
import moe.tristan.easyfxml.api.FxmlNode;
import moe.tristan.easyfxml.api.FxmlStylesheet;
import moe.tristan.easyfxml.util.Stages;

import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Optional;
import java.util.function.Function;

/**
 * The {@link FxUiManager} is one take on a bootstrapping class for the JavaFX UI.
 * <p>
 * The goal being both to decouple the GUI code from the main class and also to offer a somewhat declarative entry point
 * for the JavaFX side of your project.
 * <p>
 * Every method's overriding possibilities is detailed on it.
 * <p>
 * The minimal overriding is that of :
 * <p>
 * - {@link #getTitle()} for the main stage's title.
 * <p>
 * - {@link #getMainScene()} for the main scene info as a {@link FxmlNode}.
 * <p>
 * - {@link #getStylesheet()} that is optionally overriden if you use a custom stylesheet. If you don't need one, ignore
 * it.
 */
public abstract class FxUiManager {

    protected final EasyFxml easyFxml;

    /**
     * You are expected to call super() with an {@link EasyFxml} instance received from Spring.
     * <p>
     * i.e.:
     * <pre>
     * \@Component
     * public class MyUiManager extends FxUiManager {
     *      \@Autowired
     *      public MyUiManager(EasyFxml easyFxml) {
     *          super(easyFxml);
     *      }
     *      ...
     * }
     * </pre>
     *
     * @param easyFxml received from Spring DI
     */
    protected FxUiManager(EasyFxml easyFxml) {
        this.easyFxml = easyFxml;
    }

    /**
     * Called by {@link FxSpringApplication} after Spring and JavaFX are started. This is the equivalent of {@link
     * javafx.application.Application#start(Stage)} in traditional JavaFX apps.
     *
     * @param mainStage The main stage of the application feeded by JavaFX
     */
    public void startGui(final Stage mainStage) {
        final Scene mainScene = getScene(getMainScene());
        mainStage.setScene(mainScene);

        mainStage.setTitle(getTitle());

        getStylesheet().ifPresent(stylesheet -> Stages.setStylesheet(mainStage, stylesheet));

        mainStage.show();
    }

    /**
     * @return The title to give the main stage
     */
    protected abstract String getTitle();

    /**
     * @return The component to load in the main stage upon startup
     */
    protected abstract FxmlNode getMainScene();

    /**
     * @return If overriden, this function returns the {@link FxmlStylesheet} to apply to the main window.
     */
    protected Optional<FxmlStylesheet> getStylesheet() {
        return Optional.empty();
    }

    /**
     * Simple utility class to load an {@link FxmlNode} as a {@link Scene} for use with {@link #getMainScene()}
     *
     * @param node the node to load in the {@link Scene}
     *
     * @return The ready-to-use {@link Scene}
     *
     * @throws RuntimeException if the scene could not be loaded properly
     */
    protected Scene getScene(final FxmlNode node) {
        return easyFxml.loadNode(node)
                       .map(Scene::new)
                       .getOrElseThrow((Function<? super Throwable, RuntimeException>) RuntimeException::new);
    }
}
