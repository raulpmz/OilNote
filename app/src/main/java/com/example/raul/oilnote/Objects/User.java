package com.example.raul.oilnote.Objects;

/**
 * Clase Usuario.
 */

public class User extends Person{

    private String user, password;

    public User() {
        super("", "", "", "", "", "");
        this.user       = "";
        this.password   = "";
    }

    public User(String name, String first_last_name, String second_last_name, String email, String dni, String photo, String user, String password) {
        super(name, first_last_name, second_last_name, email, dni, photo);
        this.user       = user;
        this.password   = password;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
