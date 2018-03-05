package moe.tristan.easyfxml.model.awt;

import javax.swing.SwingUtilities;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * This class provides an asynchronous view or UI operations relating to AWT.
 */
public final class AwtUtils {

    static {
        java.awt.Toolkit.getDefaultToolkit();
    }

    private AwtUtils() {
    }

    /**
     * Performs a computation that returns an object asynchronously.
     *
     * @param awtOperation The supplying operation
     * @param <RES>        The type for the resulting object
     *
     * @return A {@link CompletionStage} for async computation monitoring.
     */
    public static <RES> CompletionStage<RES> asyncAwtCallback(final Supplier<RES> awtOperation) {
        final CompletableFuture<RES> futureResult = new CompletableFuture<>();
        SwingUtilities.invokeLater(() -> {
            final RES value = awtOperation.get();
            futureResult.complete(value);
        });
        return futureResult;
    }

    /**
     * Same as {@link #asyncAwtCallback(Supplier)} except that it performs a 2-steps operation. First calling the
     * requirement supplier and feeds the result into the awtOperation.
     *
     * @param requirement  The first-step supplier computation
     * @param awtOperation The second-step computation which will be fed the result of the first step
     * @param <RES>        The result type for the whole computation
     * @param <REQ>        The type returned by the first part of the computation
     *
     * @return A {@link CompletionStage} for async computation monitoring.
     */
    public static <RES, REQ> CompletionStage<RES> asyncAwtCallbackWithRequirement(
        Supplier<REQ> requirement,
        Function<REQ, RES> awtOperation
    ) {
        return asyncAwtCallback(requirement)
            .thenCompose(
                req -> asyncAwtCallback(
                    () -> awtOperation.apply(req)
                )
            );
    }

    /**
     * Executes an operation asynchronously on the AWT thread.
     *
     * @param requirement  The operation's object which is operated on
     * @param awtOperation The operation to apply to the return result of the requirement
     * @param <REQ>        The requirement's type
     *
     * @return A {@link CompletionStage} for async computation monitoring.
     */
    public static <REQ> CompletionStage<Void> asyncAwtRunnableWithRequirement(
        Supplier<REQ> requirement,
        Consumer<REQ> awtOperation
    ) {
        return asyncAwtCallbackWithRequirement(
            requirement,
            req -> {
                awtOperation.accept(req);
                return null;
            }
        );
    }
}
