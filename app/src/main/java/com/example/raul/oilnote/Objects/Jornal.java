package com.example.raul.oilnote.Objects;


/**
 * Clase Jornal.
 */

public class Jornal {

    // Variables:

    private String jornal_cod, user_cod, worker_cod, jornal_laboured, jornal_payed, jornal_date, worker_name, jornal_salary;

    // Constructores:


    public Jornal(String jornal_cod, String user_cod, String worker_cod, String jornal_laboured, String jornal_payed, String jornal_date, String worker_name, String jornal_salary) {
        this.jornal_cod         = jornal_cod;
        this.user_cod           = user_cod;
        this.worker_cod         = worker_cod;
        this.jornal_laboured    = jornal_laboured;
        this.jornal_payed       = jornal_payed;
        this.jornal_date        = jornal_date;
        this.worker_name        = worker_name;
        this.jornal_salary      = jornal_salary;
    }

    public Jornal(){
        this.jornal_cod         = "";
        this.user_cod           = "";
        this.worker_cod         = "";
        this.jornal_laboured    = "";
        this.jornal_payed       = "";
        this.jornal_date        = "";
        this.worker_name        = "";
        this.jornal_salary      = "";
    }

    // Getter's & Setter's:

    public String getJornal_laboured() {
        return jornal_laboured;
    }

    public void setJornal_laboured(String jornal_laboured) {
        this.jornal_laboured = jornal_laboured;
    }

    public String getJornal_payed() {
        return jornal_payed;
    }

    public void setJornal_payed(String jornal_payed) {
        this.jornal_payed = jornal_payed;
    }

    public String getWorker_name() {
        return worker_name;
    }

    public void setWorker_name(String worker_name) {
        this.worker_name = worker_name;
    }

    public String getJornal_date() {
        return jornal_date;
    }

    public void setJornal_date(String jornal_date) {
        this.jornal_date = jornal_date;
    }

    public String getJornal_cod() {
        return jornal_cod;
    }

    public void setJornal_cod(String jornal_cod) {
        this.jornal_cod = jornal_cod;
    }

    public String getUser_cod() {
        return user_cod;
    }

    public void setUser_cod(String user_cod) {
        this.user_cod = user_cod;
    }

    public String getWorker_cod() {
        return worker_cod;
    }

    public void setWorker_cod(String worker_cod) {
        this.worker_cod = worker_cod;
    }

    public String getJornal_salary() {
        return jornal_salary;
    }

    public void setJornal_salary(String jornal_salary) {
        this.jornal_salary = jornal_salary;
    }
}
