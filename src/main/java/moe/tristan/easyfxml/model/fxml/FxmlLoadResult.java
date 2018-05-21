package moe.tristan.easyfxml.model.fxml;

import io.vavr.Tuple;
import io.vavr.collection.List;
import io.vavr.collection.Seq;
import io.vavr.control.Try;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

/**
 * This class wraps a node load result as a tuple of {@link Try} instances. Respectively one for the node itself (loaded
 * through {@link FXMLLoader}) and one for the controller associated with this node (loaded through Spring's context
 * using {@link org.springframework.context.ApplicationContext#getBean(String)}).
 *
 * @param <NODE_TYPE>       The type of the node loaded
 * @param <CONTROLLER_TYPE> The type of the controller bound to it
 */
public class FxmlLoadResult<NODE_TYPE, CONTROLLER_TYPE> implements Tuple {
    private final Try<NODE_TYPE> node;
    private final Try<CONTROLLER_TYPE> controller;

    /**
     * Creates a {@link FxmlLoadResult} from two {@link Try} values.
     *
     * @param node       The node load result
     * @param controller The controller load result
     */
    public FxmlLoadResult(Try<NODE_TYPE> node, Try<CONTROLLER_TYPE> controller) {
        this.node = node;
        this.controller = controller;
    }

    /**
     * @return The result of the attempt to load the {@link Node} as a {@link Try} instance.
     */
    public Try<NODE_TYPE> getNode() {
        return node;
    }

    /**
     * @return The result of the attempt to build the controller associated to the node as a {@link Try} instance.
     */
    public Try<CONTROLLER_TYPE> getController() {
        return controller;
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
