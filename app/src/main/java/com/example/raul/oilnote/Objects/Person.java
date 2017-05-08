package com.example.raul.oilnote.Objects;

/**
 * Created by raul on 6/5/17.
 *
 * Clase Persona.
 */

public class Person {

    // Variables:

    private String name, first_last_name, second_last_name, email, dni, photo;

    // Constructores:

    public Person() {
        this.name               = "";
        this.first_last_name    = "";
        this.second_last_name   = "";
        this.email              = "";
        this.dni                = "";
        this.photo              = "";
    }

    public Person(String name, String first_last_name, String second_last_name, String email, String dni, String photo) {

        this.name               = name;
        this.first_last_name    = first_last_name;
        this.second_last_name   = second_last_name;
        this.email              = email;
        this.dni                = dni;
        this.photo              = photo;
    }

    // Getter's & Setter's:

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirst_last_name() {
        return first_last_name;
    }

    public void setFirst_last_name(String first_last_name) {
        this.first_last_name = first_last_name;
    }

    public String getSecond_last_name() {
        return second_last_name;
    }

    public void setSecond_last_name(String second_last_name) {
        this.second_last_name = second_last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
