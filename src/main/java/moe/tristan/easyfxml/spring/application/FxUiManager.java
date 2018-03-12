package moe.tristan.easyfxml.spring.application;

import moe.tristan.easyfxml.EasyFxml;
import moe.tristan.easyfxml.api.FxmlNode;
import moe.tristan.easyfxml.model.exception.ExceptionHandler;
import io.vavr.control.Try;

import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * The {@link FxUiManager} is responsible for bootstraping the GUI of the application correctly.
 */
public abstract class FxUiManager {

    protected final EasyFxml easyFxml;

    protected FxUiManager(EasyFxml easyFxml) {
        this.easyFxml = easyFxml;
    }

    public abstract void startGui(final Stage mainStage);

    protected Try<Scene> getScene(final FxmlNode node) {
        return easyFxml.loadNode(node)
                .recover(err -> new ExceptionHandler(err).asPane())
                .map(Scene::new);
    }
}
