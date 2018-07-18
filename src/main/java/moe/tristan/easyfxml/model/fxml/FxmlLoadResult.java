package moe.tristan.easyfxml.model.fxml;

import moe.tristan.easyfxml.api.FxmlController;
import moe.tristan.easyfxml.model.exception.ExceptionHandler;
import io.vavr.Tuple;
import io.vavr.collection.List;
import io.vavr.collection.Seq;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * This class wraps a node load result as a tuple of {@link Try} instances. Respectively one for the node itself (loaded
 * through {@link FXMLLoader}) and one for the controller associated with this node (loaded through Spring's context
 * using {@link org.springframework.context.ApplicationContext#getBean(String)}).
 *
 * @param <NODE>       The type of the node loaded
 * @param <CONTROLLER> The type of the controller bound to it
 */
public class FxmlLoadResult<NODE extends Node, CONTROLLER extends FxmlController> implements Tuple {

    private static final Logger LOG = LoggerFactory.getLogger(FxmlLoadResult.class);

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

    public FxmlLoadResult<NODE, CONTROLLER> afterNodeLoaded(final Consumer<Node> onNodeLoaded) {
        ensureCorrectlyLoaded();
        return new FxmlLoadResult<>(
                node.map(val -> {
                    onNodeLoaded.accept(val);
                    return val;
                }),
                controller
        );
    }

    public FxmlLoadResult<NODE, CONTROLLER> afterControllerLoaded(final Consumer<CONTROLLER> onControllerLoaded) {
        ensureCorrectlyLoaded();
        return new FxmlLoadResult<>(
                node,
                controller.map(val -> {
                    onControllerLoaded.accept(val);
                    return val;
                })
        );
    }

    private void ensureCorrectlyLoaded() {
        if (node.isFailure()) {
            LOG.error("Node did not properly load!", node.getCause());
            throw new IllegalStateException(node.getCause());
        }
        if (controller.isFailure()) {
            LOG.error("Controller did not properly load!", controller.getCause());
            throw new IllegalStateException(controller.getCause());
        }

        Objects.requireNonNull(node.get(), "The node did not load properly and was null.");
        Objects.requireNonNull(controller.get(), "The controller did not load properly and was null.");
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
