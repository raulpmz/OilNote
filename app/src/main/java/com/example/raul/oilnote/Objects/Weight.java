package com.example.raul.oilnote.Objects;

/**
 * Clase Pesar.
 */

public class Weight {

    // Variables:

    private int plot_cod , user_cod , plot_number;
    private String plot_name ;

    // Constructores:


    public Weight(int plot_cod, int user_cod, int plot_number, String plot_name) {
        this.plot_cod       = plot_cod;
        this.user_cod       = user_cod;
        this.plot_number    = plot_number;
        this.plot_name      = plot_name;
    }

    public Weight() {
        this.plot_cod       = 0;
        this.user_cod       = 0;
        this.plot_number    = 0;
        this.plot_name      = "";
    }

    // Getter's & Setter's:


    public int getPlot_cod() {
        return plot_cod;
    }

    public void setPlot_cod(int plot_cod) {
        this.plot_cod = plot_cod;
    }

    public int getUser_cod() {
        return user_cod;
    }

    public void setUser_cod(int user_cod) {
        this.user_cod = user_cod;
    }

    public int getPlot_number() {
        return plot_number;
    }

    public void setPlot_number(int plot_number) {
        this.plot_number = plot_number;
    }

    public String getPlot_name() {
        return plot_name;
    }

    public void setPlot_name(String plot_name) {
        this.plot_name = plot_name;
    }
}
