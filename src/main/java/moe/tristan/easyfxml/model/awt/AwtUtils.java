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

    public static <RES> CompletionStage<RES> asyncAwtOperation(final Supplier<RES> awtOperation) {
        final CompletableFuture<RES> futureResult = new CompletableFuture<>();
        SwingUtilities.invokeLater(() -> {
            final RES value = awtOperation.get();
            futureResult.complete(value);
        });
        return futureResult;
    }

    public static <RES, REQ> CompletionStage<RES> asyncAwtCallbackWithRequirement(
        Supplier<REQ> requirement,
        Function<REQ, RES> awtOperation
    ) {
        return asyncAwtOperation(requirement)
            .thenCompose(
                req -> asyncAwtOperation(
                    () -> awtOperation.apply(req)
                )
            );
    }

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
