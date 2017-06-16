package com.example.raul.oilnote.Objects;

import java.util.ArrayList;

/**
 * Clase Parcela.
 */

public class Plot {

    // Variables:

    private String name, note,cod, number_plant;
    private long latitude, longitude;
    private ArrayList delimitation;

    //Constructores:


    public Plot(String name, String note, String cod, String number_plant, long latitude, long longitude, ArrayList delimitation) {
        this.name           = name;
        this.note           = note;
        this.cod            = cod;
        this.number_plant   = number_plant;
        this.latitude       = latitude;
        this.longitude      = longitude;
        this.delimitation   = delimitation;
    }

    public Plot() {
        this.cod            = "";
        this.name           = "";
        this.number_plant   = "";
        this.note           = "";
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public String getNumber_plant() {
        return number_plant;
    }

    public void setNumber_plant(String number_plant) {
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

    public ArrayList getDelimitation() {
        return delimitation;
    }

    public void setDelimitation(ArrayList delimitation) {
        this.delimitation = delimitation;
    }
}
