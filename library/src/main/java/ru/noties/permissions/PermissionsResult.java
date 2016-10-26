package ru.noties.permissions;

public class PermissionsResult {

    public enum Type {
        GRANTED
        , DENIED
        , RATIONALE
        , DO_NOT_ASK_AGAIN
        , NEEDS_RESOLUTION_NO_ACTIVITY
    }

    private final Type type;
    private final String permission;

    public PermissionsResult(Type type, String permission) {
        this.type = type;
        this.permission = permission;
    }

    public Type type() {
        return type;
    }

    public String permission() {
        return permission;
    }

    @Override
    public String toString() {
        return "PermissionsResult{" +
                "type=" + type +
                ", permission='" + permission + '\'' +
                '}';
    }
}
