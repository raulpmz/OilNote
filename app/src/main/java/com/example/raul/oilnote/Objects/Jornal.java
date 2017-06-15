package com.example.raul.oilnote.Objects;


/**
 * Clase Jornal.
 */

public class Jornal {

    // Variables:

    private String jornal_laboured, jornal_payed, jornal_date;
    private int jornal_cod, user_cod, worker_cod;

    // Constructores:

    public Jornal(){
        jornal_cod              = 0;
        user_cod                = 0;
        worker_cod              = 0;
        jornal_date             = "";
        jornal_laboured         = "";
        jornal_payed            = "";
    }

    public Jornal(String jornal_laboured, String jornal_payed, String jornal_date, int jornal_cod, int user_cod, int worker_cod) {
        this.jornal_cod         = jornal_cod;
        this.user_cod           = user_cod;
        this.worker_cod         = worker_cod;
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

    public String getJornal_date() {
        return jornal_date;
    }

    public void setJornal_date(String jornal_date) {
        this.jornal_date = jornal_date;
    }

    public int getJornal_cod() {
        return jornal_cod;
    }

    public void setJornal_cod(int jornal_cod) {
        this.jornal_cod = jornal_cod;
    }

    public int getUser_cod() {
        return user_cod;
    }

    public void setUser_cod(int user_cod) {
        this.user_cod = user_cod;
    }

    public int getWorker_cod() {
        return worker_cod;
    }

    public void setWorker_cod(int worker_cod) {
        this.worker_cod = worker_cod;
    }
}
