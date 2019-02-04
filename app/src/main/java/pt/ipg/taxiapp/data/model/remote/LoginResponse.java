package pt.ipg.taxiapp.data.model.remote;

import pt.ipg.taxiapp.data.model.User;

public class LoginResponse {

    private boolean error;
    private String message;
    private User user;
    private String id;

    public LoginResponse(boolean error, String message, User user, String id) {
        this.error = error;
        this.message = message;
        this.user = user;
        this.id = id;
    }

    public boolean isError() {
        return error;
    }


    public String getMessage() {
        return message;
    }

    public User getUser() {
        return user;
    }

    public String getId() {
        return id;
    }
}
