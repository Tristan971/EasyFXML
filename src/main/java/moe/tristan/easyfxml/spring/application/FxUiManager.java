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
 * The {@link FxUiManager} is responsible for bootstraping the GUI of the application correctly.
 */
public abstract class FxUiManager {

    protected final EasyFxml easyFxml;

    protected FxUiManager(EasyFxml easyFxml) {
        this.easyFxml = easyFxml;
    }

    public void startGui(final Stage mainStage) {
        final Scene mainScene = getScene(getMainScene());
        mainStage.setScene(mainScene);

        mainStage.setTitle(getTitle());

        getStylesheet().ifPresent(stylesheet -> Stages.setStylesheet(mainStage, stylesheet));

        mainStage.show();
    }

    protected abstract String getTitle();

    protected abstract FxmlNode getMainScene();

    protected Optional<FxmlStylesheet> getStylesheet() {
        return Optional.empty();
    }

    protected Scene getScene(final FxmlNode node) {
        return easyFxml.loadNode(node)
                       .map(Scene::new)
                       .getOrElseThrow((Function<? super Throwable, RuntimeException>) RuntimeException::new);
    }
}
