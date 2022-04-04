package com.example.demo;

public class Billett {
    private String film;
    private String antall;
    private String fornavn;
    private String etternavn;
    private String telnr;
    private String epost;

    public Billett(String film, String antall, String fornavn, String etternavn, String telnr, String epost) {
        this.film = film;
        this.antall = antall;
        this.fornavn = fornavn;
        this.etternavn = etternavn;
        this.telnr = telnr;
        this.epost = epost;
    }

    public Billett() {
    }

    public String getFilm() {
        return film;
    }

    public String getAntall() {
        return antall;
    }

    public String getFornavn() {
        return fornavn;
    }

    public String getEtternavn() {
        return etternavn;
    }

    public String getTelnr() {
        return telnr;
    }

    public String getEpost() {
        return epost;
    }

    public void setFilm(String film) {
        this.film = film;
    }

    public void setAntall(String antall) {
        this.antall = antall;
    }

    public void setFornavn(String fornavn) {
        this.fornavn = fornavn;
    }

    public void setEtternavn(String etternavn) {
        this.etternavn = etternavn;
    }

    public void setTelnr(String telnr) {
        this.telnr = telnr;
    }

    public void setEpost(String epost) {
        this.epost = epost;
    }
}
