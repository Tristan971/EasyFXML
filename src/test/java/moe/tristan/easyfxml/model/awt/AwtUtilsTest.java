package moe.tristan.easyfxml.model.awt;

import io.vavr.control.Try;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;

public class AwtUtilsTest extends HeadlessIncompatibleTest {

    @Test
    public void asyncAwtOperation() throws ExecutionException, InterruptedException {
        final CompletionStage<JFrame> frame = AwtUtils.asyncAwtOperation(
            () -> {
                final JFrame testFrame = new JFrame();
                testFrame.setVisible(true);
                return testFrame;
            }
        );
        assertThat(frame.toCompletableFuture().get().isVisible()).isTrue();
    }

    @Test
    public void asyncAwtCallbackWithRequirement() throws ExecutionException, InterruptedException {
        final CompletionStage<Integer> cursorType = AwtUtils.asyncAwtCallbackWithRequirement(
            Cursor::getDefaultCursor,
            Cursor::getType
        );

        assertThat(cursorType.toCompletableFuture().get()).isEqualTo(Cursor.DEFAULT_CURSOR);
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

        final Try<Object> clipboardLoad = Try.of(asyncCb.toCompletableFuture()::get);

        assertThat(clipboardLoad.isSuccess()).isTrue();
        assertThat(((Try<Object>) clipboardLoad.get()).get()).isEqualTo("TEST");
    }

    private void pushClipboardTestData(Clipboard clipboard) {
        clipboard.setContents(
            new Transferable() {
                private final String value = "TEST";

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
                    return value;
                }
            },
            (t, co) -> {
            }
        );
    }
}
