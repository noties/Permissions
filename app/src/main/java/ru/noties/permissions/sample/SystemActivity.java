package ru.noties.permissions.sample;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;

import ru.noties.debug.Debug;
import ru.noties.permissions.OnPermissionsResultListener;
import ru.noties.permissions.PermissionsResult;
import ru.noties.permissions.rx.PermissionsRxException;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

public class SystemActivity extends BaseActivity {

    private final Handler handler = new Handler();

    @Override
    public void onStart() {
        super.onStart();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
//                requestPermission(false);
                requestPermissionRx(false);
            }
        }, 2500L);
    }


    private void requestPermission(boolean fromRationale) {
        App.permissions().request(Manifest.permission.ACCESS_FINE_LOCATION, !fromRationale,  new OnPermissionsResultListener() {
            @Override
            public void onPermissionsResult(PermissionsResult result) {

                Debug.i("result: %s", result);

                switch (result.type()) {

                    case GRANTED:
                        break;

                    case DENIED:
                        break;

                    case RATIONALE:
                        new AlertDialog.Builder(SystemActivity.this)
                                .setMessage("We need this permission really badly, now come on!")
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        requestPermission(true);
                                    }
                                })
                                .setNegativeButton(android.R.string.cancel, null)
                                .show();
                        break;

                    case DO_NOT_ASK_AGAIN:
                        // open settings
                        break;

                    case NEEDS_RESOLUTION_NO_ACTIVITY:
                        // weird..
                        break;
                }
            }
        });
    }

    private void requestPermissionRx(final boolean fromRationale) {

        App.permissionsRx().request(Manifest.permission.ACCESS_FINE_LOCATION, !fromRationale)
                .flatMap(new Func1<PermissionsResult, Observable<PermissionsResult>>() {
                    @Override
                    public Observable<PermissionsResult> call(PermissionsResult result) {
                        // here it can be only GRANTED
                        return App.permissionsRx().request(Manifest.permission.WRITE_EXTERNAL_STORAGE, fromRationale);
                    }
                })
                .subscribe(new Action1<PermissionsResult>() {
                    @Override
                    public void call(PermissionsResult result) {
                        // only granted
                        Debug.i("result: %s", result);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        if (throwable instanceof PermissionsRxException) {
                            final PermissionsResult result = ((PermissionsRxException) throwable).result();
                            Debug.i("failed, result: %s", result);
                        }
                    }
                });
    }
}
