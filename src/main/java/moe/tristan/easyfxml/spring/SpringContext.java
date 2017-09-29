package moe.tristan.easyfxml.spring;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import lombok.extern.slf4j.Slf4j;
import moe.tristan.easyfxml.FxmlController;
import moe.tristan.easyfxml.model.views.ControllerManager;
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
 * {@link #fxmlLoader(ApplicationContext, ControllerManager)}}.
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
     * The use of this is to be obtain as loose a coupling as possible
     * between the front and back ends of the application.
     * Once a stage is showed up it will be registered in the
     * {@link ControllerManager} so it is reachable by other classes.
     *
     * @param context           The application's context for controller instanciation
     * @param controllerManager The controller management service
     *
     * @return A ready-to-use {@link FXMLLoader} which auto-registers created
     * controllers.
     */
    @Bean
    @Scope(scopeName = SCOPE_PROTOTYPE)
    @SuppressWarnings("unchecked")
    public FXMLLoader fxmlLoader(final ApplicationContext context, final ControllerManager controllerManager) {
        final FXMLLoader loader = new FXMLLoader();
        loader.setControllerFactory(clazz -> {
            final Class<FxmlController> clazzCtrl = (Class<FxmlController>) clazz;
            final FxmlController controllerInstance = context.getBean(clazzCtrl);
            controllerManager.registerSingleStageController(clazzCtrl, controllerInstance);
            return controllerInstance;
        });
        return loader;
    }
}
