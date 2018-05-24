package moe.tristan.easyfxml.spring.application;

import moe.tristan.easyfxml.EasyFxml;
import moe.tristan.easyfxml.api.FxmlNode;
import moe.tristan.easyfxml.api.FxmlStylesheet;
import moe.tristan.easyfxml.model.exception.ExceptionHandler;
import moe.tristan.easyfxml.model.fxml.FxmlStylesheets;
import moe.tristan.easyfxml.util.Stages;
import io.vavr.control.Option;
import io.vavr.control.Try;

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
 * - {@link #title()} for the main stage's title.
 * <p>
 * - {@link #mainComponent()} for the main scene info as a {@link FxmlNode}.
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
        onStageCreated(mainStage);

        final Scene mainScene = getScene(mainComponent());
        onSceneCreated(mainScene);

        mainStage.setScene(mainScene);
        mainStage.setTitle(title());

        Option.ofOptional(getStylesheet())
              .filter(style -> !FxmlStylesheets.DEFAULT_JAVAFX_STYLE.equals(style))
              .peek(stylesheet -> this.setTheme(stylesheet, mainStage));

        mainStage.show();
    }

    /**
     * @return The title to give the main stage
     */
    protected abstract String title();

    /**
     * @return The component to load in the main stage upon startup
     */
    protected abstract FxmlNode mainComponent();

    /**
     * Called right after the main {@link Scene} was created if you want to edit it.
     *
     * @param mainScene The main scene of the application
     */
    protected void onSceneCreated(final Scene mainScene) {
        //do nothing by default
    }

    /**
     * Called as we enter the {@link #startGui(Stage)} method.
     *
     * @param mainStage The main stage, supplied by JavaFX's {@link javafx.application.Application#start(Stage)}
     *                  method.
     */
    protected void onStageCreated(final Stage mainStage) {
        //do nothing by default
    }

    /**
     * @return If overriden, this function returns the {@link FxmlStylesheet} to apply to the main window.
     */
    protected Optional<FxmlStylesheet> getStylesheet() {
        return Optional.empty();
    }

    /**
     * Simple utility class to load an {@link FxmlNode} as a {@link Scene} for use with {@link #mainComponent()}
     *
     * @param node the node to load in the {@link Scene}
     *
     * @return The ready-to-use {@link Scene}
     * @throws RuntimeException if the scene could not be loaded properly
     */
    protected Scene getScene(final FxmlNode node) {
        return easyFxml.loadNode(node)
                       .getNode()
                       .map(Scene::new)
                       .getOrElseThrow((Function<? super Throwable, RuntimeException>) RuntimeException::new);
    }

    private void setTheme(final FxmlStylesheet stylesheet, Stage mainStage) {
        Try.of(() -> stylesheet)
           .mapTry(FxmlStylesheet::getExternalForm)
           .mapTry(stylesheetUri -> Stages.setStylesheet(mainStage, stylesheetUri))
           .orElseRun(err -> ExceptionHandler.displayExceptionPane(
               "Could not load theme!",
               "Could not load theme file with description : " + stylesheet,
               err
           ));
    }
}
