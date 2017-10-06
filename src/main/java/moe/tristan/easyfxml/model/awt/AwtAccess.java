package moe.tristan.easyfxml.model.awt;

import io.vavr.control.Try;
import javafx.application.Application;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Desktop;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * This class enables the AWT-only functionnalities relating to system
 * integration that were not (yet) incorporated in JavaFX.
 */
public class AwtAccess {
    private static final Logger LOG = LoggerFactory.getLogger(AwtAccess.class);

    private static final AtomicReference<Try<Boolean>> isAwtAvailable = new AtomicReference<>(
        Try.failure(getAwtNotEnabledException())
    );
    private static final AtomicReference<Try<Boolean>> isNotHeadless = new AtomicReference<>(
        Try.failure(new HeadlessException())
    );

    private static final AtomicBoolean hasBeenValidatedAlready = new AtomicBoolean(false);

    private AwtAccess() {
    }

    /**
     * This method must be called in the main class of your application, before the
     * {@link Application#launch(String...)} method for AWT-related functionnality
     * to be usable. There is an automatic check of this having been done in Spring's
     * instantiation.
     */
    public static void enableAwt() {
        isAwtAvailable.set(Try.of(Toolkit::getDefaultToolkit).map(Objects::nonNull));
        isNotHeadless.set(Try.of(Desktop::getDesktop).map(Objects::nonNull));
    }

    /**
     * Checks that :
     * - {@link #enableAwt()} has been called and found a toolkit without any error
     * - {@link Desktop#isDesktopSupported()} is true. (i.e. if desktop features are available)
     */
    public static void ensureAwtSupport() {
        if (hasBeenValidatedAlready.get()) {
            LOG.debug("AWT Support has already been ensured !");
            return;
        }

        final Try<Boolean> awtEnablingResult = isAwtAvailable.get();
        LOG.debug("Requiring AWT support... ");
        if (!awtEnablingResult.isSuccess()) {
            LOG.error("\tAWT is not supported !");
            throw new UnsupportedOperationException(awtEnablingResult.getCause());
        } else {
            LOG.debug("\tAWT is supported !");
        }

        final Try<Boolean> isNotHeadlessCheckResult = isNotHeadless.get();
        LOG.debug("Requiring non-headless AWT functionnality...");
        if (!Desktop.isDesktopSupported()) {
            LOG.error("\tAWT is in a headless-only configuraztion !");
            throw new UnsupportedOperationException(isNotHeadlessCheckResult.getCause());
        } else {
            LOG.debug("\tNon-headless functionnality is available.");
        }
    }

    private static IllegalStateException getAwtNotEnabledException() {
        return new IllegalStateException(
            String.format(
                "You can not use AWT-related features (namely %s class and features). " +
                    "Please call AWTSupport#enableAwt() in your main function.",
                AwtAccess.class.getName()
            )
        );
    }
}
