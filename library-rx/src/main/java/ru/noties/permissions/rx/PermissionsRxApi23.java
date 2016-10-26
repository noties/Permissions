package ru.noties.permissions.rx;

import ru.noties.permissions.OnPermissionsResultListener;
import ru.noties.permissions.Permissions;
import ru.noties.permissions.PermissionsResult;
import rx.Observable;
import rx.Subscriber;

class PermissionsRxApi23 implements PermissionsRx {

    private final Permissions delegate;

    PermissionsRxApi23(Permissions delegate) {
        this.delegate = delegate;
    }

    @Override
    public Observable<PermissionsResult> request(final String permission, final boolean checkRationale) {
        return Observable.create(new Observable.OnSubscribe<PermissionsResult>() {
            @Override
            public void call(final Subscriber<? super PermissionsResult> subscriber) {

                if (subscriber.isUnsubscribed()) {
                    return;
                }

                request(permission, checkRationale, new OnPermissionsResultListener() {
                    @Override
                    public void onPermissionsResult(PermissionsResult result) {

                        if (subscriber.isUnsubscribed()) {
                            return;
                        }

                        if (PermissionsResult.Type.GRANTED == result.type()) {
                            subscriber.onNext(result);
                            subscriber.onCompleted();
                        } else {
                            subscriber.onError(new PermissionsRxException(result));
                            subscriber.onCompleted();
                        }
                    }
                });
            }
        });
    }

    @Override
    public boolean hasPermission(String permission) {
        return delegate.hasPermission(permission);
    }

    @Override
    public void request(String permission, boolean checkRationale, OnPermissionsResultListener listener) {
        delegate.request(permission, checkRationale, listener);
    }
}
