package moe.tristan.easyfxml.model.awt;

import io.vavr.control.Try;
import org.junit.Test;

import javax.swing.JFrame;
import java.awt.Cursor;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;

public class AwtUtilsTest {

    private static final String TEST_CB_VALUE = "TEST";

    @Test
    public void asyncAwtOperation() throws ExecutionException, InterruptedException, TimeoutException {
        final CompletionStage<JFrame> frame = AwtUtils.asyncAwtCallback(
            () -> {
                final JFrame testFrame = new JFrame();
                testFrame.setVisible(true);
                return testFrame;
            }
        );
        assertThat(frame.toCompletableFuture().get(5, SECONDS).isVisible()).isTrue();
    }

    @Test
    public void asyncAwtCallbackWithRequirement() throws ExecutionException, InterruptedException, TimeoutException {
        final CompletionStage<Integer> cursorType = AwtUtils.asyncAwtCallbackWithRequirement(
            Cursor::getDefaultCursor,
            Cursor::getType
        );

        assertThat(cursorType.toCompletableFuture().get(5, SECONDS)).isEqualTo(Cursor.DEFAULT_CURSOR);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void asyncAwtRunnableWithRequirement() {
        AwtUtils.asyncAwtRunnableWithRequirement(
            Toolkit.getDefaultToolkit()::getSystemClipboard,
            this::pushClipboardTestData
        );

        final CompletionStage<Try<Object>> asyncCb = AwtUtils.asyncAwtCallbackWithRequirement(
            Toolkit.getDefaultToolkit()::getSystemClipboard,
            cb -> Try.of(() -> cb.getData(DataFlavor.stringFlavor))
        );

        final Try<Object> clipboardLoad = Try.of(() -> asyncCb.toCompletableFuture().get(5, SECONDS));

        assertThat(clipboardLoad.isSuccess()).isTrue();
        assertThat(((Try<Object>) clipboardLoad.get()).get()).isEqualTo(TEST_CB_VALUE);
    }

    private void pushClipboardTestData(Clipboard clipboard) {
        clipboard.setContents(
            new Transferable() {

                @Override
                public DataFlavor[] getTransferDataFlavors() {
                    return new DataFlavor[]{DataFlavor.stringFlavor};
                }

                @Override
                public boolean isDataFlavorSupported(DataFlavor flavor) {
                    return flavor.equals(DataFlavor.stringFlavor);
                }

                @Override
                public Object getTransferData(DataFlavor flavor) {
                    return TEST_CB_VALUE;
                }
            },
            (t, co) -> {
            }
        );
    }
}
