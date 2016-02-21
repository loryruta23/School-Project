package it.mo.fermi.unnamed_1.enumeration;

import java.util.Optional;

public enum MouseButton {

    MOUSE_BUTTON_LEFT,
    MOUSE_BUTTON_RIGHT,
    MOUSE_BUTTON_MIDDLE;

    public static Optional<MouseButton> getMouseButton(int code) {
        for (MouseButton mouseButton : values())
            if (mouseButton.ordinal() == code)
                return Optional.of(mouseButton);
        return Optional.empty();
    }
}
