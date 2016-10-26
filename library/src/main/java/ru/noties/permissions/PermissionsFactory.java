package ru.noties.permissions;

import android.app.Application;
import android.os.Build;

public class PermissionsFactory {

    public static final boolean API_23 = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;

    public static Permissions create(Application application) {
        final Permissions permissions;
        if (API_23) {
            permissions = new PermissionsApi23(application);
        } else {
            permissions = new PermissionsNoOp();
        }
        return permissions;
    }

    private PermissionsFactory() {}
}
