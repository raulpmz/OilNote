package com.example.raul.oilnote.Objects;

/**
 * Clase Usuario.
 */

public class User{

    // Variables:

    private String  userCod, user, last_name, email, password, photo;

    // Constructores:

    public User() {
        this.user       = "";
        this.user       = "";
        this.last_name  = "";
        this.email      = "";
        this.password   = "";
        this.photo      = "";
    }

    public User(String userCod, String user, String last_name, String email, String password, String photo) {
        this.userCod    = userCod;
        this.user       = user;
        this.last_name  = last_name;
        this.email      = email;
        this.password   = password;
        this.photo      = photo;
    }

    // Getter's & Setter's:


    public String getUserCod() {
        return userCod;
    }

    public void setUserCod(String userCod) {
        this.userCod = userCod;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
