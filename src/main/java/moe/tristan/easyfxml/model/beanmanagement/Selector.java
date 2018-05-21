package moe.tristan.easyfxml.model.beanmanagement;

import java.lang.ref.WeakReference;
import java.util.Objects;

public class Selector {

    private final WeakReference<?> reference;

    public Selector(Object reference) {
        this.reference = new WeakReference<>(reference);
    }

    @SuppressWarnings("WeakerAccess")
    public WeakReference<?> getReference() {
        return reference;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Selector)) return false;

        final Object thisRef = reference.get();
        final Object oRef = ((Selector) o).getReference().get();

        return Objects.equals(thisRef, oRef);
    }

    @Override
    public int hashCode() {
        final Object thisRef = reference.get();
        return Objects.hashCode(thisRef);
    }

}
