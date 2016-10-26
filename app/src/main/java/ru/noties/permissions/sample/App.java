package ru.noties.permissions.sample;

import android.app.Application;

import javax.inject.Inject;

import ru.noties.debug.Debug;
import ru.noties.debug.out.AndroidLogDebugOutput;
import ru.noties.permissions.Permissions;
import ru.noties.permissions.rx.PermissionsRx;

public class App extends Application {

    private static App sInstance = null;

    @Inject
    Permissions permissions;

    @Inject
    PermissionsRx permissionsRx;

    @Override
    public void onCreate() {
        super.onCreate();

        sInstance = this;

        Debug.init(new AndroidLogDebugOutput(BuildConfig.DEBUG));

        DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build()
                .inject(this);
    }

    public static Permissions permissions() {
        return sInstance.permissions;
    }

    public static PermissionsRx permissionsRx() {
        return sInstance.permissionsRx;
    }
}
