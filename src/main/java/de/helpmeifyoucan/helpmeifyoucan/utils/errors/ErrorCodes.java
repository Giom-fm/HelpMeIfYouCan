
package de.helpmeifyoucan.helpmeifyoucan.utils.errors;

public enum ErrorCodes {
    // User
    USER_NOT_FOUND(104), USER_TAKEN(109),
    // Address
    ADDRESS_NOT_FOUND(204),
    // Auth
    PASSWORD_MISMATCH(300);

    private int code;

    private ErrorCodes(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        var message = "";
        switch (this) {
            case USER_NOT_FOUND:
                message = "User \"%s\" not found";
                break;
            case USER_TAKEN:
                message = "User \"%s\" already Taken";
                break;
            case ADDRESS_NOT_FOUND:
                message = "Address \"%s\" not found";
                break;
            case PASSWORD_MISMATCH:
                message = "Password mismatch";
                break;
            default:
                throw new IllegalArgumentException();
        }
        return message;
    }
}