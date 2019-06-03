package com.lenovo.doc;

public class prescription_class {
    String dr_name,dr_date,dr_reason,dr_image;

    public prescription_class(String dr_name, String dr_date, String dr_reason, String dr_image) {
        this.dr_name = dr_name;
        this.dr_date = dr_date;
        this.dr_reason = dr_reason;
        this.dr_image = dr_image;
    }

    public String getDr_name() {
        return dr_name;
    }

    public void setDr_name(String dr_name) {
        this.dr_name = dr_name;
    }

    public String getDr_date() {
        return dr_date;
    }

    public void setDr_date(String dr_date) {
        this.dr_date = dr_date;
    }

    public String getDr_reason() {
        return dr_reason;
    }

    public void setDr_reason(String dr_reason) {
        this.dr_reason = dr_reason;
    }

    public String getDr_image() {
        return dr_image;
    }

    public void setDr_image(String dr_image) {
        this.dr_image = dr_image;
    }
}
