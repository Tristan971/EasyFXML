package moe.tristan.easyfxml.util;

import javafx.application.Platform;
import org.junit.Before;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;

public class FxAsyncTest extends ApplicationTest {

    private Thread fxThread = null;

    @Before
    public void setUp() throws ExecutionException, InterruptedException {
        final CompletableFuture<Thread> fxThreadFetch = new CompletableFuture<>();
        Platform.runLater(() -> fxThreadFetch.complete(Thread.currentThread()));
        fxThread = fxThreadFetch.get();
    }

    @Test
    public void doOnFxThread() throws ExecutionException, InterruptedException {
        FxAsync.doOnFxThread(
            fxThread,
            expected -> assertThat(Thread.currentThread()).isEqualTo(expected)
        ).toCompletableFuture().get();
    }

    @Test
    public void computeOnFxThread() throws ExecutionException, InterruptedException {
        FxAsync.computeOnFxThread(
            fxThread,
            expected -> assertThat(Thread.currentThread()).isEqualTo(expected)
        ).toCompletableFuture().get();
    }
}
