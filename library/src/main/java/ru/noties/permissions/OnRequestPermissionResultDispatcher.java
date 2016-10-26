package ru.noties.permissions;

public interface OnRequestPermissionResultDispatcher {

    void registerOnRequestPermissionResultListener(OnRequestPermissionResultListener listener);
    void unregisterOnRequestPermissionResultListener(OnRequestPermissionResultListener listener);
}
