package moe.tristan.easyfxml.api;

import moe.tristan.easyfxml.util.Stages;

import javafx.stage.Stage;

/**
 * A {@link FxmlNode} is any element that has a graphical representation and a controller.
 * <p>
 * It is described by 2 elements :
 * <p>
 * - {@link FxmlFile} : The file which describes its DOM
 * <p>
 * - {@link FxmlController} class : The class from which we shoudl create its controller (its logic)
 * <p>
 * While you might expect a separate stylesheet element to be related, it actually is not bound to the component but to
 * the window in JavaFX. See {@link Stages#setStylesheet(Stage, FxmlStylesheet)} for styling a window.
 */
public interface FxmlNode {

    FxmlFile getFile();

    Class<? extends FxmlController> getControllerClass();

}
