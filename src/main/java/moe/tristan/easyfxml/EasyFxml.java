package moe.tristan.easyfxml;

import io.vavr.control.Try;
import javafx.scene.layout.Pane;

public interface EasyFxml {
    Try<Pane> getPaneForView(final FxmlFile view);
}
