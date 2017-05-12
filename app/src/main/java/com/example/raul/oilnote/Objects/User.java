package com.example.raul.oilnote.Objects;

/**
 * Clase Usuario.
 */

public class User extends Person{

    // Variables:

    private String user, password;

    // Constructores:

    public User() {
        super("", "", "", "");
        this.user       = "";
        this.password   = "";
    }

    public User(String name, String last_name, String email, String photo, String user, String password) {
        super(name, last_name, email, photo);
        this.user       = user;
        this.password   = password;
    }

    public User(String user, String email, String password) {
        super("", "", email, "");
        this.user       = user;
        this.password   = password;
    }

    //  Getter's & Setter's:

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
