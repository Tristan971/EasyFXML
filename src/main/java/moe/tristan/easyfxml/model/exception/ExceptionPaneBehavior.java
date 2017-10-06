package moe.tristan.easyfxml.model.exception;

import moe.tristan.easyfxml.EasyFxml;
import moe.tristan.easyfxml.model.FxmlNode;
import moe.tristan.easyfxml.model.fxml.SafeEasyFxml;

/**
 * This class is a behavioral flag-like used enumeration.
 * Its point is that when you load a view with {@link SafeEasyFxml}
 * you may want either to have the error pane display in
 * place of your expected pane, or you might on the other
 * hand wish for a disting dialog window popping up.
 * <p>
 * As you can guess, using {@link SafeEasyFxml#loadPaneForNode(EasyFxml, FxmlNode, ExceptionPaneBehavior)}
 * - {@link #INLINE} generates the error in-place, while
 * - {@link #DIALOG} generates and displays it in a separate window.
 */
public enum ExceptionPaneBehavior {
    INLINE,
    DIALOG
}
