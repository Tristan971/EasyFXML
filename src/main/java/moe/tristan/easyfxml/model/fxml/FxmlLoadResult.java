package moe.tristan.easyfxml.model.fxml;

import moe.tristan.easyfxml.api.FxmlController;
import moe.tristan.easyfxml.model.exception.ExceptionHandler;
import io.vavr.Tuple;
import io.vavr.collection.List;
import io.vavr.collection.Seq;
import io.vavr.control.Try;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

/**
 * This class wraps a node load result as a tuple of {@link Try} instances. Respectively one for the node itself (loaded
 * through {@link FXMLLoader}) and one for the controller associated with this node (loaded through Spring's context
 * using {@link org.springframework.context.ApplicationContext#getBean(String)}).
 *
 * @param <NODE>       The type of the node loaded
 * @param <CONTROLLER> The type of the controller bound to it
 */
public class FxmlLoadResult<NODE extends Node, CONTROLLER extends FxmlController> implements Tuple {
    private final Try<NODE> node;
    private final Try<CONTROLLER> controller;

    /**
     * Creates a {@link FxmlLoadResult} from two {@link Try} values.
     *
     * @param node       The node load result
     * @param controller The controller load result
     */
    public FxmlLoadResult(Try<NODE> node, Try<CONTROLLER> controller) {
        this.node = node;
        this.controller = controller;
    }

    /**
     * @return The result of the attempt to load the {@link Node} as a {@link Try} instance.
     */
    public Try<NODE> getNode() {
        return node;
    }

    /**
     * @return The result of the attempt to build the controller associated to the node as a {@link Try} instance.
     */
    public Try<CONTROLLER> getController() {
        return controller;
    }

    @SuppressWarnings("unchecked")
    public Try<Pane> orExceptionPane() {
        return ((Try<Pane>) getNode()).recover(ExceptionHandler::fromThrowable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int arity() {
        return 2;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Seq<?> toSeq() {
        return List.of(node, controller);
    }
}
