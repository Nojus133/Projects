package com.example.s344224mappe3;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.Marker;

public class Hus implements Parcelable {
    private int id;
    private String beskrivelse;
    private String adresse;
    private String koordinater;
    private int antallEtasjer;
    private boolean expanded;
    private Marker marker;

    public Hus(int id, String beskrivelse, String adresse, String koordinater, int antallEtasjer) {
        this.id = id;
        this.beskrivelse = beskrivelse;
        this.adresse = adresse;
        this.koordinater = koordinater;
        this.antallEtasjer = antallEtasjer;
        this.expanded = false;
    }

    public Hus(String beskrivelse, String adresse, String koordinater, int antallEtasjer) {
        this.beskrivelse = beskrivelse;
        this.adresse = adresse;
        this.koordinater = koordinater;
        this.antallEtasjer = antallEtasjer;
        this.expanded = false;
    }

    protected Hus(Parcel in) {
        id = in.readInt();
        beskrivelse = in.readString();
        adresse = in.readString();
        koordinater = in.readString();
        antallEtasjer = in.readInt();
        expanded = in.readByte() != 0;
    }

    public static final Creator<Hus> CREATOR = new Creator<Hus>() {
        @Override
        public Hus createFromParcel(Parcel in) {
            return new Hus(in);
        }

        @Override
        public Hus[] newArray(int size) {
            return new Hus[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBeskrivelse() {
        return beskrivelse;
    }

    public void setBeskrivelse(String beskrivelse) {
        this.beskrivelse = beskrivelse;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getKoordinater() {
        return koordinater;
    }

    public void setKoordinater(String koordinater) {
        this.koordinater = koordinater;
    }

    public int getAntallEtasjer() {
        return antallEtasjer;
    }

    public void setAntallEtasjer(int antallEtasjer) {
        this.antallEtasjer = antallEtasjer;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(beskrivelse);
        parcel.writeString(adresse);
        parcel.writeString(koordinater);
        parcel.writeInt(antallEtasjer);
        parcel.writeByte((byte) (expanded ? 1 : 0));
    }
}
