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
    private String id;
    private String lat;
    private String lng;
    private String bookingId;
    private String paymentStatus;
    private String rated;
    //private GeoPoint location;

    public your_booking_model(String name, String address, String speciality, String fee, String image, String status, String date, String time,String lat,String lng,String bookingId,String id,String a,String b) {
        this.name = name;
        this.address = address;
        this.speciality = speciality;
        this.fee = fee;
        this.image = image;
        this.status = status;
        this.date = date;
        this.time = time;
        this.lat=lat;
        this.lng=lng;
        this.id=id;
        this.bookingId=bookingId;
        this.paymentStatus=a;
        this.rated=b;
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

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getRated() {
        return rated;
    }

    public void setRated(String rated) {
        this.rated = rated;
    }
}
