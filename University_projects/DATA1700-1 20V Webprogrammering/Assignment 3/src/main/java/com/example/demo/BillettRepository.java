package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BillettRepository {

    @Autowired
    private JdbcTemplate db;

    public void lagreBillett(Billett innBillett) {
        String sql = "INSERT INTO Billett (film, antall, fornavn, etternavn, telnr, epost) VALUES(?,?,?,?,?,?)";
        db.update(sql, innBillett.getFilm(), innBillett.getAntall(), innBillett.getFornavn(),
                       innBillett.getEtternavn(), innBillett.getTelnr(), innBillett.getEpost());
    }

    public List<Billett> hentBilletter() {
        String sql = "SELECT * FROM Billett";
        List<Billett> hentAlle = db.query(sql, new BeanPropertyRowMapper(Billett.class));
        return hentAlle;
    }

    public void slettAlle() {
        String sql = "DELETE FROM Billett";
        db.update(sql);
    }
}
