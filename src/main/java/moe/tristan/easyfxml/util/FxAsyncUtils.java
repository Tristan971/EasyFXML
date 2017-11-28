package moe.tristan.easyfxml.util;

import javafx.application.Platform;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Consumer;
import java.util.function.Function;

public final class FxAsyncUtils {

    private FxAsyncUtils() {}

    public static <T> CompletionStage<T> doOnFxThread(final T element, final Consumer<T> action) {
        CompletableFuture<T> asyncOp = new CompletableFuture<>();
        Platform.runLater(() -> {
            action.accept(element);
            asyncOp.complete(element);
        });
        return asyncOp;
    }

    public static <T, U> CompletionStage<U> computeOnFxThread(final T element, final Function<T, U> compute) {
        CompletableFuture<U> asyncComputeOp = new CompletableFuture<>();
        Platform.runLater(() -> asyncComputeOp.complete(compute.apply(element)));
        return asyncComputeOp;
    }
}
