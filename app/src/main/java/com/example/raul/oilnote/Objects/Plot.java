package com.example.raul.oilnote.Objects;

import java.util.ArrayList;

/**
 * Clase Parcela.
 */

public class Plot {

    // Variables:

    private String name, note;
    private int number_plant;
    private long latitude, longitude;
    private ArrayList delimitation;

    //Constructores:

    public Plot(String name, int number_plant, long latitude, long longitude) {
        this.name           = name;
        this.number_plant   = number_plant;
        this.latitude       = latitude;
        this.longitude      = longitude;
    }

    public Plot() {
        this.name           = "";
        this.note           = "";
        this.number_plant   = 0;
        this.latitude       = 0;
        this.longitude      = 0;
    }

    // Getter's & Setter's:

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumber_plant() {
        return number_plant;
    }

    public void setNumber_plant(int number_plant) {
        this.number_plant = number_plant;
    }

    public long getLatitude() {
        return latitude;
    }

    public void setLatitude(long latitude) {
        this.latitude = latitude;
    }

    public long getLongitude() {
        return longitude;
    }

    public void setLongitude(long longitude) {
        this.longitude = longitude;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public ArrayList getDelimitation() {
        return delimitation;
    }

    public void setDelimitation(ArrayList delimitation) {
        this.delimitation = delimitation;
    }
}
