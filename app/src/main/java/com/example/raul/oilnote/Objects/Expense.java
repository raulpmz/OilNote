package com.example.raul.oilnote.Objects;

/**
 * Created by Raul on 21/06/2017.
 */

public class Expense {

    private String cod, date, type, money;

    public Expense(String cod, String date, String type, String money) {
        this.cod    = cod;
        this.date   = date;
        this.type   = type;
        this.money  = money;
    }

    public Expense() {
        this.cod    = "";
        this.date   = "";
        this.type   = "";
        this.money  = "";
    }

    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }
}
