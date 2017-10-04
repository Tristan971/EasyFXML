package moe.tristan.easyfxml.spring;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import lombok.extern.slf4j.Slf4j;
import moe.tristan.easyfxml.FxmlNode;
import moe.tristan.easyfxml.model.fxml.BaseEasyFxml;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.net.URL;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

/**
 * {@link Configuration} class for non-autonomous {@link Bean}s. Useful to
 * bean-ify the JDK classes as can sometimes be needed.
 * <p>
 * (e.g. : here with{@link FXMLLoader} as seen in
 * {@link #fxmlLoader(ApplicationContext)}.
 *
 * All beans are initialized lazily so that only the used parts of the
 * library are generated. This will add a few slowdown moments in the
 * beginning of an application's lifecycle, but yield lower memory
 * usage and avoir bean name conflicts and other things like it.
 */
@Configuration
@ComponentScan(basePackages = "moe.tristan.easyfxml", lazyInit = true)
@Slf4j
public class SpringContext {

    /**
     * The {@link FXMLLoader} object is a /SINGLE-USE/ object to load
     * a FXML file and deserialize it as an instance of {@link Node}.
     * <p>
     * Because of its single-use policy on {@link FXMLLoader#load()}
     * (you can not follow it with a {@link FXMLLoader#setLocation(URL)}
     * and another load as a way to reuse it as it caches object tree in
     * the instance), it must stay as a
     * {@link ConfigurableBeanFactory#SCOPE_PROTOTYPE} - scoped bean.
     * <p>
     * This pre-made instance will be preloaded with
     * {@link ApplicationContext#getBean(Class)} as the default way to
     * fabricate the controller classes which handle the UI's state.
     *
     * See further configuration in {@link BaseEasyFxml#getSingleStageFxmlLoader(FxmlNode)}
     * and {@link BaseEasyFxml#getMultiStageFxmlLoader(FxmlNode, Object)}
     *
     * @param context           The application's context for controller instanciation
     *
     * @return A spring-enabled {@link FXMLLoader}.
     */
    @Bean
    @Scope(scopeName = SCOPE_PROTOTYPE)
    @SuppressWarnings("unchecked")
    public FXMLLoader fxmlLoader(final ApplicationContext context) {
        final FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setControllerFactory(context::getBean);
        return fxmlLoader;
    }
}
