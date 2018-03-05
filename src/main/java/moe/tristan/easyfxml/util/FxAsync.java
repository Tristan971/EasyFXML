package moe.tristan.easyfxml.util;

import io.vavr.Tuple2;

import javafx.application.Platform;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Convenience utility methods for cleaner asynchronous cross-thread calls.
 * <p>
 * Basically a glorified replacement of {@link Platform#runLater(Runnable)} that offers task-style methods and allows
 * for computational calls.
 */
public final class FxAsync {

    private FxAsync() {
    }

    /**
     * Asynchronously executes a consuming operation on the JavaFX thread.
     *
     * @param element The element to consume
     * @param action  How to consume it
     * @param <T>     The type of the element consumed
     *
     * @return A {@link CompletionStage} to have monitoring over the state of the asynchronous operation.
     */
    public static <T> CompletionStage<T> doOnFxThread(final T element, final Consumer<T> action) {
        CompletableFuture<T> asyncOp = new CompletableFuture<>();
        Platform.runLater(() -> {
            action.accept(element);
            asyncOp.complete(element);
        });
        return asyncOp;
    }

    /**
     * Asynchronously executes a computing operation on the JavaFX thread.
     *
     * @param element The element(s) required for the computation (use {@link Tuple2} for pair-based for example)
     * @param compute The computation to perform
     * @param <T>     The type of the (aggregated if necessary) inputs
     * @param <U>     The output type
     *
     * @return A {@link CompletionStage} to have monitoring over the state of the asynchronous computation.
     */
    public static <T, U> CompletionStage<U> computeOnFxThread(final T element, final Function<T, U> compute) {
        CompletableFuture<U> asyncComputeOp = new CompletableFuture<>();
        Platform.runLater(() -> asyncComputeOp.complete(compute.apply(element)));
        return asyncComputeOp;
    }
}
