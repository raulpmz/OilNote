package com.example.raul.oilnote.Objects;

/**
 * Clase Persona.
 */

public class Person {

    // Variables:

    private String name, last_name, email, photo;

    // Constructores:

    public Person() {
        this.name               = "";
        this.last_name          = "";
        this.email              = "";
        this.photo              = "";
    }

    public Person(String name, String last_name, String email, String photo) {
        this.name               = name;
        this.last_name          = last_name;
        this.email              = email;
        this.photo              = photo;
    }

    // Getter's & Setter's:

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
