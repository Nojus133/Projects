package com.example.s344224mappe2;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;
import java.util.List;

public class Bestilling implements Parcelable{
    private Long _id;
    private Long dato;
    private Long tidspunkt;

    private List<Venn> venner;
    private Restaurant restaurant;

    public Bestilling() {
    }

    public Bestilling(Date dato, Date tidspunkt, List<Venn> venner, Restaurant restaurant) {
        this.dato = dato.getTime();
        this.tidspunkt = tidspunkt.getTime();
        this.venner = venner;
        this.restaurant = restaurant;
    }

    public Bestilling(Long _id, Date dato, Date tidspunkt, List<Venn> venner, Restaurant restaurant) {
        this._id = _id;
        this.dato = dato.getTime();
        this.tidspunkt = tidspunkt.getTime();
        this.venner = venner;
        this.restaurant = restaurant;
    }

    protected Bestilling(Parcel in) {
        if (in.readByte() == 0) {
            _id = null;
        } else {
            _id = in.readLong();
        }
        dato = in.readLong();
        tidspunkt = in.readLong();
        venner = in.createTypedArrayList(Venn.CREATOR);
        restaurant = in.readParcelable(Restaurant.class.getClassLoader());
    }

    public static final Creator<Bestilling> CREATOR = new Creator<Bestilling>() {
        @Override
        public Bestilling createFromParcel(Parcel in) {
            return new Bestilling(in);
        }

        @Override
        public Bestilling[] newArray(int size) {
            return new Bestilling[size];
        }
    };

    public Long get_id() {
        return _id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public Date getDato() {
        return new Date(dato);
    }

    public void setDato(Date dato) {
        this.dato = dato.getTime();
    }

    public Date getTidspunkt() {
        return new Date(tidspunkt);
    }

    public void setTidspunkt(Date tidspunkt) {
        this.tidspunkt = tidspunkt.getTime();
    }

    public List<Venn> getVenner() {
        return venner;
    }

    public void setVenner(List<Venn> venner) {
        this.venner = venner;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        if (_id == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(_id);
        }
        parcel.writeLong(dato);
        parcel.writeLong(tidspunkt);
        parcel.writeTypedList(venner);
        parcel.writeParcelable(restaurant, i);
    }
}
