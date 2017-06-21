package com.example.raul.oilnote.Objects;

/**
 * Clase Pesar.
 */

public class Weight {

    // Variables:

    private String weight_date, plot_name, weight_cod, user_cod , plot_cod ,weight_number,weight_efficiency;

    // Constructores:

    public Weight(String weight_date, String plot_name, String weight_cod, String user_cod, String plot_cod, String weight_number, String weight_efficiency) {
        this.weight_date        = weight_date;
        this.plot_name          = plot_name;
        this.weight_cod         = weight_cod;
        this.user_cod           = user_cod;
        this.plot_cod           = plot_cod;
        this.weight_number      = weight_number;
        this.weight_efficiency  = weight_efficiency;
    }

    public Weight() {
        this.weight_date        = "";
        this.plot_name          = "";
        this.weight_cod         = "";
        this.user_cod           = "";
        this.plot_cod           = "";
        this.weight_number      = "";
        this.weight_efficiency  = "";
    }

    // Getter's & Setter's:

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

    public String getWeight_cod() {
        return weight_cod;
    }

    public void setWeight_cod(String weight_cod) {
        this.weight_cod = weight_cod;
    }

    public String getUser_cod() {
        return user_cod;
    }

    public void setUser_cod(String user_cod) {
        this.user_cod = user_cod;
    }

    public String getPlot_cod() {
        return plot_cod;
    }

    public void setPlot_cod(String plot_cod) {
        this.plot_cod = plot_cod;
    }

    public String getWeight_number() {
        return weight_number;
    }

    public void setWeight_number(String weight_number) {
        this.weight_number = weight_number;
    }

    public String getWeight_efficiency() {
        return weight_efficiency;
    }

    public void setWeight_efficiency(String weight_efficiency) {
        this.weight_efficiency = weight_efficiency;
    }
}
