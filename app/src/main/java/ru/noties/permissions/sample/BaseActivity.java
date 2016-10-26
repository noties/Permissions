package ru.noties.permissions.sample;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;

import ru.noties.permissions.OnRequestPermissionDelegate;
import ru.noties.permissions.OnRequestPermissionResultDispatcher;
import ru.noties.permissions.OnRequestPermissionResultListener;

public class BaseActivity extends Activity implements OnRequestPermissionResultDispatcher {

    private final OnRequestPermissionDelegate onRequestPermissionDelegate = OnRequestPermissionDelegate.create();

    @Override
    public void registerOnRequestPermissionResultListener(OnRequestPermissionResultListener listener) {
        onRequestPermissionDelegate.registerOnRequestPermissionResultListener(listener);
    }

    @Override
    public void unregisterOnRequestPermissionResultListener(OnRequestPermissionResultListener listener) {
        onRequestPermissionDelegate.unregisterOnRequestPermissionResultListener(listener);
    }

    @Override
    @TargetApi(Build.VERSION_CODES.M)
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (onRequestPermissionDelegate.onRequestPermissionResult(requestCode, permissions, grantResults)) {
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
