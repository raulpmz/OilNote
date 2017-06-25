package com.example.raul.oilnote.Objects;

/**
 * Created by raul on 25/6/17.
 */

public class TypeJornal {

    // Variables:

    private String id, name, money;

    // Constructores:

    public TypeJornal(String id, String name, String money) {
        this.id     = id;
        this.name   = name;
        this.money  = money;
    }

    public TypeJornal() {
        this.id     = "";
        this.name   = "";
        this.money  = "";
    }

    // Getter's & Setter's:

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }
}
