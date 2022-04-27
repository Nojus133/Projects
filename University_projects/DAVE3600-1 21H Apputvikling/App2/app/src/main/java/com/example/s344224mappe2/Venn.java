package com.example.s344224mappe2;


import android.os.Parcel;
import android.os.Parcelable;

public class Venn implements Parcelable {
    private Long _id;
    private String firstName;
    private String lastName;
    private String telefon;

    public Venn() {
    }

    public Venn(Long _id, String firstName, String lastName, String telefon) {
        this._id = _id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.telefon = telefon;
    }

    public Venn(String firstName, String lastName, String telefon) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.telefon = telefon;
    }

    protected Venn(Parcel in) {
        if (in.readByte() == 0) {
            _id = null;
        } else {
            _id = in.readLong();
        }
        firstName = in.readString();
        lastName = in.readString();
        telefon = in.readString();
    }

    public static final Creator<Venn> CREATOR = new Creator<Venn>() {
        @Override
        public Venn createFromParcel(Parcel in) {
            return new Venn(in);
        }

        @Override
        public Venn[] newArray(int size) {
            return new Venn[size];
        }
    };

    public Long get_id() {
        return _id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
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
        parcel.writeString(firstName);
        parcel.writeString(lastName);
        parcel.writeString(telefon);
    }
}
