package moe.tristan.easyfxml;

import javafx.application.Platform;

/**
 * This is a base interface for all controllers.
 * The {@link #initialize()} method is called by the
 * JavaFX {@link Platform} after all the component's
 * subcomponents have been loaded and are ready for usage.
 *
 * Never use the constructor to do any more than
 * dependency injection as the components are not
 * guaranteed to have been loaded yet and generally
 * result in the infamous {@link NullPointerException}.
 */
public interface FxmlController {
    /**
     * This method is automatically called by the JavaFX {@link Platform}
     * as soon as all the components are loaded (not necessarily rendered).
     * This is where initial UX/UI setup should be done (enabling, disabling
     * hiding etc...)
     *
     * Calling it from the constructor is a hazard and will generally cause
     * failures.
     */
    void initialize();
}
