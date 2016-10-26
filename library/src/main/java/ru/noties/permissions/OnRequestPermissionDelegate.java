package ru.noties.permissions;

import java.util.HashSet;
import java.util.Set;

public abstract class OnRequestPermissionDelegate implements OnRequestPermissionResultDispatcher, OnRequestPermissionResultListener {

    public static OnRequestPermissionDelegate create() {
        final OnRequestPermissionDelegate delegate;
        if (PermissionsFactory.API_23) {
            delegate = new Api23();
        } else {
            delegate = new NoOp();
        }
        return delegate;
    }

    private OnRequestPermissionDelegate() {}

    public boolean onRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults) {
        return onRequestPermissionResult(this, requestCode, permissions, grantResults);
    }

    private static class NoOp extends OnRequestPermissionDelegate {

        @Override
        public void registerOnRequestPermissionResultListener(OnRequestPermissionResultListener listener) {

        }

        @Override
        public void unregisterOnRequestPermissionResultListener(OnRequestPermissionResultListener listener) {

        }

        @Override
        public boolean onRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults) {
            return false;
        }

        @Override
        public boolean onRequestPermissionResult(OnRequestPermissionResultDispatcher dispatcher, int requestCode, String[] permissions, int[] grantResults) {
            return false;
        }
    }

    private static class Api23 extends OnRequestPermissionDelegate {

        private Set<OnRequestPermissionResultListener> listeners;

        @Override
        public void registerOnRequestPermissionResultListener(OnRequestPermissionResultListener listener) {
            if (listeners == null) {
                listeners = new HashSet<>(2);
            }
            listeners.add(listener);
        }

        @Override
        public void unregisterOnRequestPermissionResultListener(OnRequestPermissionResultListener listener) {
            if (listeners != null) {
                listeners.remove(listener);
            }
        }

        @Override
        public boolean onRequestPermissionResult(OnRequestPermissionResultDispatcher dispatcher, int requestCode, String[] permissions, int[] grantResults) {
            if (listeners != null && listeners.size() > 0) {
                for (OnRequestPermissionResultListener listener: new HashSet<>(listeners)) {
                    if (listener.onRequestPermissionResult(this, requestCode, permissions, grantResults)) {
                        return true;
                    }
                }
            }
            return false;
        }
    }
}
