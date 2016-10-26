package ru.noties.permissions.rx;

import ru.noties.permissions.Permissions;
import ru.noties.permissions.PermissionsResult;
import rx.Observable;

public interface PermissionsRx extends Permissions {
    Observable<PermissionsResult> request(String permission, boolean checkRationale);
}
