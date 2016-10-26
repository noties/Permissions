package ru.noties.permissions.rx;

import ru.noties.permissions.PermissionsResult;

public class PermissionsRxException extends Exception {

    private final PermissionsResult result;

    public PermissionsRxException(PermissionsResult result) {
        this.result = result;
    }

    public PermissionsResult result() {
        return result;
    }
}
