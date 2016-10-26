package ru.noties.permissions;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Process;

@TargetApi(Build.VERSION_CODES.M)
@SuppressWarnings("WeakerAccess")
class PermissionsApi23 implements Permissions {

    private static final int REQUEST_CODE_MAX = 0xFFFF;

    private final Context context;
    private final ActivityDispatcher checkPermissionDispatcher;
    private ActivityDispatcher activityDispatcher;

    PermissionsApi23(Application application) {
        this.context = application.getApplicationContext();
        application.registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks());
        checkPermissionDispatcher = new ActivityDispatcher(context, null, null);
    }

    @Override
    public boolean hasPermission(String permission) {
        return checkPermissionDispatcher.checkPermission(permission);
    }

    @Override
    public void request(final String permission, boolean checkRationale,  final OnPermissionsResultListener listener) {

        // first, check permission
        if (hasPermission(permission)) {
            listener.onPermissionsResult(new PermissionsResult(PermissionsResult.Type.GRANTED, permission));
            return;
        }

        final ActivityDispatcher dispatcher = activityDispatcher;
        if (dispatcher == null
                || !dispatcher.canRequestPermission()) {
            listener.onPermissionsResult(new PermissionsResult(PermissionsResult.Type.NEEDS_RESOLUTION_NO_ACTIVITY, permission));
        } else {

            if (checkRationale && dispatcher.shouldShowPermissionRationale(permission)) {
                listener.onPermissionsResult(new PermissionsResult(PermissionsResult.Type.RATIONALE, permission));
            } else {
                final int permissionRequestCode = requestCode(permission);
                dispatcher.registerOnRequestPermissionResultListener(new OnRequestPermissionResultListener() {
                    @Override
                    public boolean onRequestPermissionResult(OnRequestPermissionResultDispatcher dispatcher, int requestCode, String[] permissions, int[] grantResults) {

                        final ActivityDispatcher current = activityDispatcher;

                        final boolean result;
                        if (current == null) {
                            result = false;
                        } else if (permissionRequestCode == requestCode) {

                            if (grantResults != null
                                    && grantResults.length == 1) {
                                final int value = grantResults[0];
                                final PermissionsResult.Type type;
                                if (PackageManager.PERMISSION_GRANTED == value) {
                                    type = PermissionsResult.Type.GRANTED;
                                } else {
                                    if (current.shouldShowPermissionRationale(permission)) {
                                        type = PermissionsResult.Type.DENIED;
                                    } else {
                                        type = PermissionsResult.Type.DO_NOT_ASK_AGAIN;
                                    }
                                }
                                listener.onPermissionsResult(new PermissionsResult(type, permission));
                            }

                            dispatcher.unregisterOnRequestPermissionResultListener(this);
                            result = true;
                        } else {
                            result = false;
                        }
                        return result;
                    }
                });
                dispatcher.requestPermission(permission, permissionRequestCode);
            }
        }
    }

    private static int requestCode(String permission) {
        // I've seen (in Fragment I guess) that only upper 16 bits are allowed
        // better make it a rule
        return Math.abs(permission.hashCode()) % REQUEST_CODE_MAX;
    }

    private class ActivityDispatcher implements OnRequestPermissionResultDispatcher {

        final Context context;
        final Activity activity;
        final OnRequestPermissionResultDispatcher dispatcher;

        ActivityDispatcher(Context context, Activity activity, OnRequestPermissionResultDispatcher dispatcher) {
            this.context = context;
            this.activity = activity;
            this.dispatcher = dispatcher;
        }

        boolean canRequestPermission() {
            return activity != null && dispatcher != null;
        }

        void requestPermission(String permission, int requestCode) {
            activity.requestPermissions(new String[] { permission }, requestCode);
        }

        boolean shouldShowPermissionRationale(String permission) {
            return activity.shouldShowRequestPermissionRationale(permission);
        }

        boolean checkPermission(String permission) {
            return PackageManager.PERMISSION_GRANTED == context.checkPermission(permission, android.os.Process.myPid(), Process.myUid());
        }

        @Override
        public void registerOnRequestPermissionResultListener(OnRequestPermissionResultListener listener) {
            dispatcher.registerOnRequestPermissionResultListener(listener);
        }

        @Override
        public void unregisterOnRequestPermissionResultListener(OnRequestPermissionResultListener listener) {
            dispatcher.unregisterOnRequestPermissionResultListener(listener);
        }
    }

    class ActivityLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

        }

        @Override
        public void onActivityStarted(Activity activity) {
            // okay, check if it implements the dispatcher interface
            if (activity instanceof OnRequestPermissionResultDispatcher) {
                activityDispatcher = new ActivityDispatcher(context, activity, (OnRequestPermissionResultDispatcher) activity);
            }
        }

        @Override
        public void onActivityResumed(Activity activity) {

        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {
            if (activityDispatcher != null
                    && activityDispatcher.activity == activity) {
                activityDispatcher = null;
            }
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {

        }
    }
}
