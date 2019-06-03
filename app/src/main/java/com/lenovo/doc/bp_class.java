package com.lenovo.doc;

public class bp_class {
    private String date,ub,lb;

    public bp_class(String date, String ub, String lb) {
        this.date = date;
        this.ub = ub;
        this.lb = lb;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUb() {
        return ub;
    }

    public void setUb(String ub) {
        this.ub = ub;
    }

    public String getLb() {
        return lb;
    }

    public void setLb(String lb) {
        this.lb = lb;
    }
}
