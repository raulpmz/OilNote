package com.example.raul.oilnote.Objects;


/**
 * Clase Jornal.
 */

public class Jornal {

    // Variables:

    private String jornal_cod, user_cod, worker_cod, jornal_laboured, jornal_payed, jornal_date, worker_name;

    // Constructores:

    public Jornal(){
        jornal_cod              = "";
        user_cod                = "";
        worker_cod              = "";
        worker_name             = "";
        jornal_date             = "";
        jornal_laboured         = "";
        jornal_payed            = "";
    }

    public Jornal(String jornal_laboured, String jornal_payed, String jornal_date,String worker_name, String jornal_cod, String user_cod, String worker_cod) {
        this.jornal_cod         = jornal_cod;
        this.user_cod           = user_cod;
        this.worker_cod         = worker_cod;
        this.worker_name        = worker_name;
        this.jornal_date        = jornal_date;
        this.jornal_laboured    = jornal_laboured;
        this.jornal_payed       = jornal_payed;
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
}
