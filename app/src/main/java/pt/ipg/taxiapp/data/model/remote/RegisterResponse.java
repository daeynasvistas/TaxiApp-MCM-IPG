package pt.ipg.taxiapp.data.model.remote;

import java.util.List;

import pt.ipg.taxiapp.data.model.User;

public class RegisterResponse {
    private String username;
    private String email;
    private String id;

    public RegisterResponse(String username, String email, String id) {
        this.username = username;
        this.email = email;
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getId() {
        return id;
    }




    public class error {
        private String name;
        private int statusCode;
        private String message;
    }

}
