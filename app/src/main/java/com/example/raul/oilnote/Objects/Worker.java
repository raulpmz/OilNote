package com.example.raul.oilnote.Objects;

/**
 * Clase Trabajador.
 */

public class Worker extends Person{

    // Variables:

    private String job, salary;

    // Constructores:

    public Worker() {
        super("", "", "", "");
        this.job = "";
        this.salary = "";
    }

    public Worker(String name, String last_name, String email, String photo, String job, String salary) {
        super(name, last_name, email, photo);
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
