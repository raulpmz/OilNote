package com.example.raul.oilnote.Objects;

import android.graphics.drawable.Drawable;

/**
 * Clase Trabajador.
 */

public class Worker{

    // Variables:

    private String workerCod, workerName, workerNick, workerPhone, workerPhoto;
    private int workerJob;

    // Constructores:

    public Worker() {
        workerCod   = "";
        workerName  = "";
        workerNick  = "";
        workerPhone = "";
        workerJob   = 1;
        workerPhoto = "";
    }

    public Worker(String workerCod, String workerName, String workerNick, String workerPhone, int workerJob, String workerPhoto) {
        this.workerCod      = workerCod;
        this.workerName     = workerName;
        this.workerNick     = workerNick;
        this.workerPhone    = workerPhone;
        this.workerJob      = workerJob;
        this.workerPhoto    = workerPhoto;
    }

    // Getter's & Setters:

    public String getWorkerCod() {
        return workerCod;
    }

    public void setWorkerCod(String workerCod) {
        this.workerCod = workerCod;
    }

    public String getWorkerName() {
        return workerName;
    }

    public void setWorkerName(String workerName) {
        this.workerName = workerName;
    }

    public String getWorkerNick() {
        return workerNick;
    }

    public void setWorkerNick(String workerNick) {
        this.workerNick = workerNick;
    }

    public String getWorkerPhone() {
        return workerPhone;
    }

    public void setWorkerPhone(String workerPhone) {
        this.workerPhone = workerPhone;
    }

    public int getWorkerJob() {
        return workerJob;
    }

    public void setWorkerJob(int workerJob) {
        this.workerJob = workerJob;
    }

    public String getWorkerPhoto() {
        return workerPhoto;
    }

    public void setWorkerPhoto(String workerPhoto) {
        this.workerPhoto = workerPhoto;
    }

}
