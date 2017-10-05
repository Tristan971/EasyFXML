package moe.tristan.easyfxml.model;

import io.vavr.control.Option;

/**
 * A {@link FxmlNode} is any element that has a graphical representation and a controller.
 *
 * It is described by 3 elements :
 * - {@link FxmlFile} : The file which describes its DOM
 * - {@link FxmlController} class : The class from which we shoudl create its controller (its logic)
 * - {@link FxmlStylesheet} : By default none (i.e. : {@link Option#none()}, that is to say inherit
 * from parent), can be overriden.
 */
public interface FxmlNode {

    FxmlFile getFxmlFile();

    Class<? extends FxmlController> getControllerClass();

    default Option<FxmlStylesheet> getStylesheet() {
        return Option.none();
    }
}
