package moe.tristan.easyfxml.model;

/**
 * A {@link FxmlNode} is any element that the user is meant to see/interract with.
 *
 * It is described by 3 elements :
 * - {@link FxmlFile} : The file which describes its DOM
 * - {@link FxmlController} class : The class from which we shoudl create its controller (its logic)
 * - {@link FxmlStylesheet} : The stylesheet aplied to it. Or {@link FxmlStylesheet#NONE} if none in particular.
 */
public interface FxmlNode {
    FxmlFile getFxmlFile();
    Class<? extends FxmlController> getControllerClass();
    FxmlStylesheet getStylesheet();
}
