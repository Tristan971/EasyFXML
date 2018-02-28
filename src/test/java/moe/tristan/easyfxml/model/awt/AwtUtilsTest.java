package moe.tristan.easyfxml.model.awt;

import java.awt.Cursor;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.util.concurrent.ExecutionException;

import javax.swing.JFrame;

import org.junit.Test;

import io.vavr.control.Try;
import static io.vavr.API.unchecked;
import static org.assertj.core.api.Assertions.assertThat;

public class AwtUtilsTest extends HeadlessIncompatibleTest {

    @Test
    public void asyncAwtOperation() throws ExecutionException, InterruptedException {
        AwtUtils.asyncAwtOperation(
            () -> {
                final JFrame testFrame = new JFrame();
                testFrame.setVisible(true);
                return testFrame;
            }
        ).thenAccept(
            frame -> assertThat(frame.isVisible()).isTrue()
        ).toCompletableFuture().get();
    }

    @Test
    public void asyncAwtCallbackWithRequirement() throws ExecutionException, InterruptedException {
        AwtUtils.asyncAwtCallbackWithRequirement(
            Cursor::getDefaultCursor,
            Cursor::getType
        ).thenAccept(
            cursorType -> assertThat(cursorType).isEqualTo(Cursor.DEFAULT_CURSOR)
        ).toCompletableFuture().get();
    }

    @Test
    public void asyncAwtRunnableWithRequirement() throws ExecutionException, InterruptedException {
        AwtUtils.asyncAwtRunnableWithRequirement(
            Toolkit.getDefaultToolkit()::getSystemClipboard,
            this::pushClipboardTestData
        ).thenCompose(
            __ -> AwtUtils.asyncAwtRunnableWithRequirement(
                () -> Try.of(Toolkit::getDefaultToolkit)
                         .map(Toolkit::getSystemClipboard)
                         .map(unchecked(cb -> cb.getData(DataFlavor.stringFlavor))),
                cbContentReq -> {
                    assertThat(cbContentReq.isSuccess()).isTrue();
                    assertThat(cbContentReq.get()).isEqualTo("TEST");
                }
            )
        ).toCompletableFuture().get();
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
