package moe.tristan.easyfxml.util;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.testfx.framework.junit.ApplicationTest;

import javafx.application.Platform;
import moe.tristan.easyfxml.spring.SpringContext;
import static org.assertj.core.api.Assertions.assertThat;

@ContextConfiguration(classes = SpringContext.class)
@RunWith(SpringRunner.class)
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
