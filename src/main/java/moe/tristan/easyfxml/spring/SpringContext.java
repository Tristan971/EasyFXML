package moe.tristan.easyfxml.spring;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import moe.tristan.easyfxml.api.FxmlNode;
import moe.tristan.easyfxml.model.fxml.BaseEasyFxml;
import moe.tristan.easyfxml.model.fxml.FxmlLoader;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

/**
 * {@link Configuration} class for non-autonomous {@link Bean}s. Useful to bean-ify the JDK classes as can sometimes be
 * needed.
 * <p>
 * (e.g. : here with{@link FXMLLoader} as seen in {@link #fxmlLoader(ApplicationContext)}.
 * <p>
 * All beans are initialized lazily so that only the used parts of the library are generated.
 * <p>
 * This will add a few slowdown moments in the beginning of an application's lifecycle, but yield lower memory usage and
 * avoir bean name conflicts and other things like it.
 */
@Lazy
@Configuration
@ComponentScan(basePackages = "moe.tristan.easyfxml", lazyInit = true)
public class SpringContext {

    /**
     * The {@link FxmlLoader} object is a /SINGLE-USE/ object to load a FXML file and deserialize it as an instance of
     * {@link Node}.
     * <p>
     * It extends {@link FXMLLoader} adding {@link FxmlLoader#onSuccess(Node)} and {@link
     * FxmlLoader#onFailure(Throwable)} as utility methods to do various things.
     * <p>
     * Because of the single-use policy (on {@link FXMLLoader#load()}), it must stay as a {@link
     * ConfigurableBeanFactory#SCOPE_PROTOTYPE}.
     * <p>
     * This pre-made instance will be preloaded with {@link ApplicationContext#getBean(Class)} as the default way to
     * fabricate the controller classes which handle the UI's state.
     * <p>
     * See further configuration in {@link BaseEasyFxml#getSingleStageFxmlLoader(FxmlNode)} and {@link
     * BaseEasyFxml#getMultiStageFxmlLoader(FxmlNode, Object)}
     *
     * @param context The application's context for controller instanciation
     *
     * @return A spring-enabled {@link FXMLLoader}.
     */
    @Bean
    @Scope(scopeName = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public FxmlLoader fxmlLoader(final ApplicationContext context) {
        final FxmlLoader fxmlLoader = new FxmlLoader();
        fxmlLoader.setControllerFactory(context::getBean);
        return fxmlLoader;
    }
}
