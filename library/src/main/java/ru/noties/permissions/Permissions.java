package ru.noties.permissions;

public interface Permissions {

    boolean hasPermission(String permission);
    void request(String permission, boolean checkRationale, OnPermissionsResultListener listener);
}
