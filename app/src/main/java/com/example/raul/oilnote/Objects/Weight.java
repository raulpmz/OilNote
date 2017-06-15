package com.example.raul.oilnote.Objects;

/**
 * Clase Pesar.
 */

public class Weight {

    // Variables:

    private int weight_cod, user_cod , plot_cod ,weight_number;
    private String weight_date, plot_name;

    // Constructores:

    public Weight(int weight_cod,int plot_cod, int user_cod, String plot_name, int weight_number) {
        this.weight_cod     = weight_cod;
        this.plot_cod       = plot_cod;
        this.user_cod       = user_cod;
        this.plot_name      = plot_name;
        this.weight_number  = weight_number;
    }

    public Weight() {
        this.weight_cod     = 0;
        this.plot_cod       = 0;
        this.user_cod       = 0;
        this.plot_name      = "";
        this.weight_number  = 0;
    }

    // Getter's & Setter's:

    public int getWeight_cod() {
        return weight_cod;
    }

    public void setWeight_cod(int weight_cod) {
        this.weight_cod = weight_cod;
    }

    public int getUser_cod() {
        return user_cod;
    }

    public void setUser_cod(int user_cod) {
        this.user_cod = user_cod;
    }

    public int getPlot_cod() {
        return plot_cod;
    }

    public void setPlot_cod(int plot_cod) {
        this.plot_cod = plot_cod;
    }

    public int getWeight_number() {
        return weight_number;
    }

    public void setWeight_number(int weight_number) {
        this.weight_number = weight_number;
    }

    public String getWeight_date() {
        return weight_date;
    }

    public void setWeight_date(String weight_date) {
        this.weight_date = weight_date;
    }

    public String getPlot_name() {
        return plot_name;
    }

    public void setPlot_name(String plot_name) {
        this.plot_name = plot_name;
    }
}
