package moe.tristan.easyfxml.model.fxml;

import io.vavr.control.Try;

public class FxmlLoadResult<NODE_TYPE, CONTROLLER_TYPE> {
    private final Try<NODE_TYPE> node;
    private final Try<CONTROLLER_TYPE> controller;

    public FxmlLoadResult(Try<NODE_TYPE> node, Try<CONTROLLER_TYPE> controller) {
        this.node = node;
        this.controller = controller;
    }

    public Try<NODE_TYPE> getNode() {
        return node;
    }

    public Try<CONTROLLER_TYPE> getController() {
        return controller;
    }
}
