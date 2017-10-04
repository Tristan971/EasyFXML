package moe.tristan.easyfxml;

import io.vavr.control.Try;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import moe.tristan.easyfxml.model.FxmlNode;
import moe.tristan.easyfxml.model.beanmanagement.ControllerManager;
import moe.tristan.easyfxml.model.fxml.BaseEasyFxml;

public interface EasyFxml {

    /**
     * Loads a {@link FxmlNode} as a Pane (this is the safest base class for all sorts of hierarchies)
     * since most of the base containers are subclasses of it.
     * It also registers its controller in the {@link ControllerManager} for later usage.
     *
     * It returns a {@link Try} which is a monadic structure which allows us to do clean exception-handling.
     *
     * @param node The element's {@link FxmlNode} counterpart. Try to avoid loading things using manual path
     *             as it implies losing a lot of coding safety, controller binding and stylesheet control.
     *             It does work fine as well though if you really want to, using
     *             {@link BaseEasyFxml#loadNodeImpl(FXMLLoader, String)} after sublcassing it.
     *
     * @return A {@link Try} containing either the file {@link Try.Success} or the exception that was first
     * raised during the chain of nested function calls needed to load it. See {@link Try#getOrElse(Object)}
     * and related methods for how to handle {@link Try.Failure}.
     */
    Try<Pane> loadNode(final FxmlNode node);

    /**
     * Same as {@link #loadNode(FxmlNode)} but offers a selector parameter for multistage usage of
     * {@link ControllerManager}
     *
     * @see ControllerManager
     */
    Try<Pane> loadNode(final FxmlNode node, final Object selector);

    /**
     * Same as {@link #loadNode(FxmlNode)} except you can choose the return type wished for instead
     * of just {@link Pane}. It's a bad idea but it can sometimes be actually useful on the rare
     * case where the element is really nothing like a Pane. Be it a very complex button or a custom
     * textfield with non-rectangular shape.
     *
     * @param node The node to load
     * @param nodeClass The JavaFX class of the node once loaded
     * @param <T> The type of the component, same as nodeClass
     * @return A {@link Try} of T. Read {@link #loadNode(FxmlNode)} for more.
     */
    <T extends Node> Try<T> loadNode(final FxmlNode node, final Class<T> nodeClass);

    /**
     * This is to {@link #loadNode(FxmlNode, Class)} just what {@link #loadNode(FxmlNode, Object)} is to
     * {@link #loadNode(FxmlNode)}.
     */
    <T extends Node> Try<T> loadNode(final FxmlNode node, final Class<T> nodeClass, final Object selector);

}
