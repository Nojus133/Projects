package com.example.s344224mappe1;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Spill implements Parcelable {
    private List<String> listRegnestykker;
    private List<String> listSvar;
    private String riktigSvarText;
    private String feilSvarText;
    private int counter;
    private int riktigSvarCounter;
    private int feilSvarCounter;
    private int antallRunder;
    private Resources resources;

    public Spill(Activity activity, int antallRunder) {
        listRegnestykker = new ArrayList<>();
        listSvar = new ArrayList<>();
        this.resources = activity.getResources();
        this.riktigSvarText = resources.getString(R.string.spill_riktig_svar);
        this.feilSvarText = resources.getString(R.string.spill_feil_svar);
        this.antallRunder = antallRunder;
        this.counter = 0;
        this.riktigSvarCounter = 0;
        this.feilSvarCounter = 0;
        random(this.antallRunder);
    }

    protected Spill(Parcel in) {
        listRegnestykker = in.createStringArrayList();
        listSvar = in.createStringArrayList();
        riktigSvarText = in.readString();
        feilSvarText = in.readString();
        counter = in.readInt();
        riktigSvarCounter = in.readInt();
        feilSvarCounter = in.readInt();
        antallRunder = in.readInt();
    }

    public static final Creator<Spill> CREATOR = new Creator<Spill>() {
        @Override
        public Spill createFromParcel(Parcel in) {
            return new Spill(in);
        }

        @Override
        public Spill[] newArray(int size) {
            return new Spill[size];
        }
    };

    private void random(int antallRunder) {
        List<String> regnestykker = arrayToList(resources.getStringArray(R.array.listRegnestykker));
        List<String> svarene = arrayToList(resources.getStringArray(R.array.listSvar));

        int randomValueLimit = regnestykker.size();
        Random randomNumber = new Random();

        for (int i = 0; i < antallRunder; i++) {
            int randomInt = randomNumber.nextInt(randomValueLimit);
            String etRegnestykke = regnestykker.remove(randomInt);
            String etSvar = svarene.remove(randomInt);

            listRegnestykker.add(etRegnestykke);
            listSvar.add(etSvar);

            randomValueLimit--;
        }

    }

    private List<String> arrayToList(String[] array) {
        List<String> list = new ArrayList<>();
        for (String element : array) {
            list.add(element);
        }
        return list;
    }

    public void increaseCounter() {
        counter++;
    }

    public void increaseRiktigSvarCounter() {
        riktigSvarCounter++;
    }

    public void increaseFeilSvarCounter() {
        feilSvarCounter++;
    }

    public boolean checkAnswer(String svar) {
        int answer = Integer.parseInt(svar);
        int correctAnswer = Integer.parseInt(listSvar.get(counter));
        return answer == correctAnswer;
    }

    public String nextQuestion() {
        return listRegnestykker.get(counter);
    }

    public String getRiktigSvarText() {
        return riktigSvarText;
    }

    public void setRiktigSvarText(String riktigSvarText) {
        this.riktigSvarText = riktigSvarText;
    }

    public String getFeilSvarText() {
        return feilSvarText;
    }

    public void setFeilSvarText(String feilSvarText) {
        this.feilSvarText = feilSvarText;
    }

    public int getCounter() {
        return counter;
    }

    public int getAntallRunder() {
        return antallRunder;
    }

    public int getRiktigSvarCounter() {
        return  riktigSvarCounter;
    }

    public int getFeilSvarCounter() {
        return feilSvarCounter;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringList(listRegnestykker);
        parcel.writeStringList(listSvar);
        parcel.writeString(riktigSvarText);
        parcel.writeString(feilSvarText);
        parcel.writeInt(counter);
        parcel.writeInt(riktigSvarCounter);
        parcel.writeInt(feilSvarCounter);
        parcel.writeInt(antallRunder);
    }
}
