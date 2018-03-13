package moe.tristan.easyfxml.spring.application;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import javafx.application.Application;
import javafx.stage.Stage;

@SpringBootApplication
public abstract class FxSpringApplication extends Application {

    private ConfigurableApplicationContext context;

    @Override
    public void init() {
        this.context = getSab().run();
    }

    protected SpringApplicationBuilder getSab() {
        return new SpringApplicationBuilder(getClass())
            .headless(false)
            .web(WebApplicationType.NONE);
    }

    protected void beforeSpringInit() {
        // do nothing by default
    }

    @Override
    public void start(final Stage primaryStage) {
        this.context.getBean(FxUiManager.class).startGui(primaryStage);
    }

    @Override
    public void stop() {
        this.context.stop();
    }

}
