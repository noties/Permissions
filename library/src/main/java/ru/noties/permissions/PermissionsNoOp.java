package ru.noties.permissions;

class PermissionsNoOp implements Permissions {

    @Override
    public boolean hasPermission(String permission) {
        return true;
    }

    @Override
    public void request(String permission, boolean checkRationale, OnPermissionsResultListener listener) {
        listener.onPermissionsResult(new PermissionsResult(PermissionsResult.Type.GRANTED, permission));
    }
}
