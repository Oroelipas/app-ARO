package com.example.upnadeportes.login;

/**
 * Class exposing authenticated user details to the UI.
 */
public class LoggedInUserView {

    private String nombreCompleto;
    private String userId;
    private String email;

    public LoggedInUserView(String nombreCompleto, String userId, String email) {
        this.nombreCompleto = nombreCompleto;
        this.userId = userId;
        this.email = email;
    }

    String getDisplayName() {
        return nombreCompleto;
    }

    public String getUserId() { return userId; }

    public String getEmail() { return email; }

}
