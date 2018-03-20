package moe.tristan.easyfxml.spring.application;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * This class is a convenience boilerplate set-up for you to mix JavaFX and Spring together. It is absolutely not needed
 * but I believe one could find peace in not having to deal with the configuration of two frameworks.
 * <p>
 * The usage is examplified in this project's documentation over at
 * <a href="https://tristan971.github.io/EasyFXML/">Tristan971/EasyFXML</a>.
 * <p>
 * The gist of it is that your "main" class is expected to extend this class instead of {@link Application} (which this
 * one does extend anyway) and that you just declare your main function with a call to {@link #launch(String...)}. Then
 * it is expected that your main class is annotated with {@link SpringBootApplication} (it is not really needed but IDEs
 * have a better time when they see it on a project class rather than in a library due to non-inheritance of annotations
 * in Java) and also with {@link org.springframework.context.annotation.Import}({@link FxSpringContext}).
 * <p>
 * This class initializes the application with a call to {@link #beforeSpringInit()}, then initializing Spring, then
 * calling {@link #afterSpringInit()}. These tho methods can be overriden and you can do calls to {@link #springContext}
 * in {@link #afterSpringInit()}, should you need to do things that require Spring before the UI loads (or {@link
 * #beforeSpringInit()} in the case of things to do before Spring is even started.
 * <p>
 * When initialization is done, this class calls your implementation of {@link FxUiManager}. You can of course bypass
 * that, should you not like the layout of {@link FxUiManager}, by overriding {@link #start(Stage)}.
 *
 * @see FxUiManager
 */
@SpringBootApplication
public abstract class FxSpringApplication extends Application {

    protected ConfigurableApplicationContext springContext;

    @Override
    public void init() {
        beforeSpringInit();
        this.springContext = getSab().run();
        afterSpringInit();
    }

    protected SpringApplicationBuilder getSab() {
        return new SpringApplicationBuilder(getClass())
            .headless(false)
            .web(WebApplicationType.NONE);
    }

    protected void beforeSpringInit() {
        // do nothing by default
    }

    protected void afterSpringInit() {
        // do nothing by default
    }

    @Override
    public void start(final Stage primaryStage) {
        this.springContext.getBean(FxUiManager.class).startGui(primaryStage);
    }

    @Override
    public void stop() {
        this.springContext.stop();
    }

}
