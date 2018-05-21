package moe.tristan.easyfxml.model.fxml;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import moe.tristan.easyfxml.api.FxmlNode;
import moe.tristan.easyfxml.model.beanmanagement.Selector;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

import java.util.Objects;
import java.util.function.Consumer;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

/**
 * The {@link FxmlLoader} object is a /SINGLE-USE/ object to load a FXML file and deserialize it as an instance of
 * {@link Node}.
 * <p>
 * It extends {@link FXMLLoader} adding {@link FxmlLoader#onSuccess(Node)} and {@link FxmlLoader#onFailure(Throwable)}
 * as utility methods to do various things.
 * <p>
 * Because of the single-use policy (on {@link FXMLLoader#load()}), it must stay as a {@link
 * ConfigurableBeanFactory#SCOPE_PROTOTYPE}.
 * <p>
 * This pre-made instance will be preloaded with {@link ApplicationContext#getBean(Class)} as the default way to
 * fabricate the controller classes which handle the UI's state.
 * <p>
 * See further configuration in {@link BaseEasyFxml#getSingleStageFxmlLoader(FxmlNode)} and {@link
 * BaseEasyFxml#getMultiStageFxmlLoader(FxmlNode, Selector)}
 */

@Component
@Scope(SCOPE_PROTOTYPE)
public class FxmlLoader extends FXMLLoader {

    private Consumer<Node> onSuccess = node -> {
    };

    private Consumer<Throwable> onFailure = cause -> {
    };

    @Autowired
    public FxmlLoader(ApplicationContext context) {
        this.setControllerFactory(context::getBean);
    }

    /**
     * @param onSuccess the consumer for the node loaded on success
     */
    public void setOnSuccess(final Consumer<Node> onSuccess) {
        this.onSuccess = onSuccess;
    }

    /**
     * @param loadResult the node to feed {@link #onSuccess} with.
     */
    public void onSuccess(final Node loadResult) {
        this.onSuccess.accept(loadResult);
    }

    /**
     * @param onFailure the consumer for the loading error on failure
     */
    public void setOnFailure(final Consumer<Throwable> onFailure) {
        this.onFailure = onFailure;
    }

    /**
     * @param cause the reason to feed {@link #onFailure} with.
     */
    public void onFailure(final Throwable cause) {
        this.onFailure.accept(cause);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FxmlLoader)) return false;
        if (!super.equals(o)) return false;
        FxmlLoader that = (FxmlLoader) o;
        return Objects.equals(onSuccess, that.onSuccess) && Objects.equals(onFailure, that.onFailure);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLocation(), onSuccess, onFailure);
    }
}
