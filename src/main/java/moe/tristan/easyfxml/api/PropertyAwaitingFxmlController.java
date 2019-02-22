package moe.tristan.easyfxml.api;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public abstract class PropertyAwaitingFxmlController<T> implements FxmlController {

    private ObjectProperty<T> value = new SimpleObjectProperty<>();

    @Override
    public void initialize() {
        if (value.get() != null) {
            controllerDidLoad();
        }
        value.addListener((o, prev, cur) -> {
            controllerDidLoad();
            valueChanged(cur);
        });
    }

    public abstract void controllerDidLoad();

    public abstract void valueChanged(T value);

    protected T getValue() {
        return value.get();
    }

    protected ObjectProperty<T> valueProperty() {
        return value;
    }

    protected void setValue(T value) {
        this.value.set(value);
    }

}
