package ru.noties.permissions.sample;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.noties.permissions.Permissions;
import ru.noties.permissions.PermissionsFactory;
import ru.noties.permissions.rx.PermissionsFactoryRx;
import ru.noties.permissions.rx.PermissionsRx;

@Module
public class AppModule {

    private final Application application;

    public AppModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    public Permissions permissions() {
        return PermissionsFactory.create(application);
    }

    @Provides
    @Singleton
    public PermissionsRx permissionsRx() {
        return PermissionsFactoryRx.create(application);
    }
}
