package com.example.raul.oilnote.Objects;

/**
 * Clase jornales pedidos.
 */

public class Missing {

    // Variables:

    private String missing_cod, user_cod, worker_cod, missing_date;

    // Constructores:


    public Missing(String missing_cod, String user_cod, String worker_cod, String missing_date) {
        this.missing_cod    = missing_cod;
        this.user_cod       = user_cod;
        this.worker_cod     = worker_cod;
        this.missing_date   = missing_date;
    }

    public Missing() {
        this.missing_cod    = "";
        this.user_cod       = "";
        this.worker_cod     = "";
        this.missing_date   = "";
    }

    // Getter's & Setter's:

    public String getMissing_cod() {
        return missing_cod;
    }

    public void setMissing_cod(String missing_cod) {
        this.missing_cod = missing_cod;
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

    public String getMissing_date() {
        return missing_date;
    }

    public void setMissing_date(String missing_date) {
        this.missing_date = missing_date;
    }
}
