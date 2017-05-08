package com.example.raul.oilnote.Objects;

/**
 * Created by raul on 6/5/17.
 *
 * Clase Trabajador.
 */

public class Laborer extends Person{

    // Variables:

    private String job, salary;

    // Constructores:

    public Laborer() {
        super("", "", "", "", "", "");
        this.job = "";
        this.salary = "";
    }

    public Laborer(String name, String first_last_name, String second_last_name, String email, String dni, String photo, String job, String salary) {
        super(name, first_last_name, second_last_name, email, dni, photo);
        this.job = job;
        this.salary = salary;
    }

    // Getter's & Setter's:

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }
}
