package ru.noties.permissions.rx;

import ru.noties.permissions.OnPermissionsResultListener;
import ru.noties.permissions.PermissionsResult;
import rx.Observable;

class PermissionsRxNoOp implements PermissionsRx {

    @Override
    public boolean hasPermission(String permission) {
        return true;
    }

    @Override
    public void request(String permission, boolean checkRationale, OnPermissionsResultListener listener) {
        listener.onPermissionsResult(new PermissionsResult(PermissionsResult.Type.GRANTED, permission));
    }

    @Override
    public Observable<PermissionsResult> request(String permission, boolean checkRationale) {
        return Observable.just(new PermissionsResult(PermissionsResult.Type.GRANTED, permission));
    }
}
