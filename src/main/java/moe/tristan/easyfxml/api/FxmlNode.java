package moe.tristan.easyfxml.api;

import io.vavr.control.Option;

/**
 * A {@link FxmlNode} is any element that has a graphical representation and a controller.
 * <p>
 * It is described by 3 elements :
 * - {@link FxmlFile} : The file which describes its DOM
 * - {@link FxmlController} class : The class from which we shoudl create its controller (its logic)
 * - {@link FxmlStylesheet} : By default none (i.e. : {@link Option#none()}, that is to say inherit
 * from parent), can be overriden.
 */
public interface FxmlNode {

    FxmlFile getFile();

    Class<? extends FxmlController> getControllerClass();

    default FxmlStylesheet getStylesheet() {
        return FxmlStylesheet.INHERIT;
    }
}