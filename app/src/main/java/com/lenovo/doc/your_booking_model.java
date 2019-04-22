package com.lenovo.doc;

import com.google.firebase.firestore.GeoPoint;

public class your_booking_model {
    private String name;
    private String address;
    private String speciality;
    private String fee;
    private String image;
    private String status;
    private String date;
    private String time;
    //private GeoPoint location;

    public your_booking_model(String name, String address, String speciality, String fee, String image, String status, String date, String time) {
        this.name = name;
        this.address = address;
        this.speciality = speciality;
        this.fee = fee;
        this.image = image;
        this.status = status;
        this.date = date;
        this.time = time;
        //this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}
