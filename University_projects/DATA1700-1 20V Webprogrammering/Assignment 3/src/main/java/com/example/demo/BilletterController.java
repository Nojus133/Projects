package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BilletterController {

    @Autowired
    private BillettRepository rep;

    @PostMapping("/lagreData")
    public void lagreBillett(Billett innBillett) {
        rep.lagreBillett(innBillett);
    }

    @GetMapping("/hentData")
    public List<Billett> hentBilletter() {
        return rep.hentBilletter();
    }

    @PostMapping("/slettData")
    public void slettBilletter() {
        rep.slettAlle();
    }
}
