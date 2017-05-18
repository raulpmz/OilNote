package com.example.raul.oilnote.Objects;

/**
 * Clase Usuario.
 */

public class User{

    // Variables:

    private String  userName, last_name, email, password, photo;
    private int userCod;

    // Constructores:

    public User() {
        this.userCod    = 0;
        this.userName   = "";
        this.last_name  = "";
        this.email      = "";
        this.password   = "";
        this.photo      = "";
    }

    public User(int userCod, String userName, String last_name, String email, String password, String photo) {
        this.userCod    = userCod;
        this.userName   = userName;
        this.last_name  = last_name;
        this.email      = email;
        this.password   = password;
        this.photo      = photo;
    }

    // Getter's & Setter's:

    public int getUserCod() {
        return userCod;
    }

    public void setUserCod(int userCod) {
        this.userCod = userCod;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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
