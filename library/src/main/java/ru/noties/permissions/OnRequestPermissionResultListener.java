package ru.noties.permissions;

public interface OnRequestPermissionResultListener {
    // safe to unregister here
    boolean onRequestPermissionResult(
            OnRequestPermissionResultDispatcher dispatcher,
            int requestCode,
            String[] permissions,
            int[] grantResults
    );
}
