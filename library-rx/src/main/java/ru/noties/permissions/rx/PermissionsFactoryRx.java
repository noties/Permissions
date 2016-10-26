package ru.noties.permissions.rx;

import android.app.Application;

import ru.noties.permissions.PermissionsFactory;

public class PermissionsFactoryRx {

    public static PermissionsRx create(Application application) {

        final PermissionsRx permissionsRx;

        if (PermissionsFactory.API_23) {
            permissionsRx = new PermissionsRxApi23(PermissionsFactory.create(application));
        } else {
            permissionsRx = new PermissionsRxNoOp();
        }

        return permissionsRx;
    }
}
