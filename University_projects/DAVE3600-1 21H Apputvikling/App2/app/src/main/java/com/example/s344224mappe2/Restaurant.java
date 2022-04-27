package com.example.s344224mappe2;


import android.os.Parcel;
import android.os.Parcelable;

public class Restaurant implements Parcelable {
    private Long _id;
    private String navn;
    private String adresse;
    private String telefon;
    private String type;

    public Restaurant() {
    }

    public Restaurant(Long _id, String navn, String adresse, String telefon, String type) {
        this._id = _id;
        this.navn = navn;
        this.adresse = adresse;
        this.telefon = telefon;
        this.type = type;
    }

    public Restaurant(String navn, String adresse, String telefon, String type) {
        this.navn = navn;
        this.adresse = adresse;
        this.telefon = telefon;
        this.type = type;
    }

    protected Restaurant(Parcel in) {
        if (in.readByte() == 0) {
            _id = null;
        } else {
            _id = in.readLong();
        }
        navn = in.readString();
        adresse = in.readString();
        telefon = in.readString();
        type = in.readString();
    }

    public static final Creator<Restaurant> CREATOR = new Creator<Restaurant>() {
        @Override
        public Restaurant createFromParcel(Parcel in) {
            return new Restaurant(in);
        }

        @Override
        public Restaurant[] newArray(int size) {
            return new Restaurant[size];
        }
    };

    public Long get_id() {
        return _id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public String getNavn() {
        return navn;
    }

    public void setNavn(String navn) {
        this.navn = navn;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
        parcel.writeString(navn);
        parcel.writeString(adresse);
        parcel.writeString(telefon);
        parcel.writeString(type);
    }
}
