package it.mo.fermi.unnamed_1.enumeration;

import java.util.Optional;

public enum Action {

    RELEASE,
    PRESS,
    REPEAT;

    public static Optional<Action> getAction(int code) {
        for (Action action : values())
            if (action.ordinal() == code)
                return Optional.of(action);
        return Optional.empty();
    }
}
