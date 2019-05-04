/*
 * Copyright 2017 - 2019 EasyFXML project and contributors
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package moe.tristan.easyfxml.model.fxml;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

import java.util.Objects;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

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
 * See further configuration in {@link DefaultEasyFxml}.
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
        if (this == o) {
            return true;
        }
        if (!(o instanceof FxmlLoader)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        FxmlLoader that = (FxmlLoader) o;
        return Objects.equals(onSuccess, that.onSuccess) && Objects.equals(onFailure, that.onFailure);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLocation(), onSuccess, onFailure);
    }

}
