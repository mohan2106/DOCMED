package com.lenovo.doc;

import com.google.firebase.firestore.GeoPoint;
import com.google.type.LatLng;

public class doctor_details_class {
    private String name;
    private String address;
    private String fee;
    private String image;
    private String experiencce;
    private String id;
    private String speciality;
    private GeoPoint location;

    public doctor_details_class(String name, String address, String fee, String image, String experiencce, String speciality, GeoPoint location,String id) {
        this.name = name;
        this.address = address;
        this.fee = fee;
        this.image = image;
        this.experiencce = experiencce;
        this.speciality = speciality;
        this.location = location;
        this.id=id;
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

    public String getExperiencce() {
        return experiencce;
    }

    public void setExperiencce(String experiencce) {
        this.experiencce = experiencce;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public GeoPoint getLocation() {
        return location;
    }

    public void setLocation(GeoPoint location) {
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
