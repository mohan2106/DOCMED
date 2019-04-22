package com.lenovo.doc;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.GeoPoint;

public class Model implements Parcelable {
    private String something;
    private double latitude, longitude;
    private GeoPoint geoPoint;

    public Model() {
    }

    protected Model(Parcel in) {
        something = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        geoPoint = new GeoPoint(latitude, longitude);
    }

    public static final Creator<Model> CREATOR = new Creator<Model>() {
        @Override
        public Model createFromParcel(Parcel in) {
            return new Model(in);
        }

        @Override
        public Model[] newArray(int size) {
            return new Model[size];
        }
    };

    public String getSomething() {
        return something;
    }

    public void setSomething(String something) {
        this.something = something;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public GeoPoint getGeoPoint() {
        return geoPoint;
    }

    public void setGeoPoint(GeoPoint geoPoint) {
        this.geoPoint = geoPoint;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        latitude = getGeoPoint().getLatitude();
        longitude = getGeoPoint().getLongitude();
        parcel.writeString(something);
        parcel.writeDouble(latitude);
        parcel.writeDouble(longitude);
    }
}
