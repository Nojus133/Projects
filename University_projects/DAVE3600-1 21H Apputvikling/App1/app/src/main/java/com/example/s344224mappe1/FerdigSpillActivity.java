package com.example.s344224mappe1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class FerdigSpillActivity extends AppCompatActivity implements View.OnClickListener{
    private String galeSvar;
    private String riktigeSvar;
    private String sprak;
    private String regnestykke;

    Button btnSpillPaNytt;
    Button btnTilbake;
    TextView txtResultatRiktige;
    TextView txtResultatGale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        galeSvar = intent.getStringExtra(getString(R.string.ANTALL_GALE_SVAR));
        riktigeSvar = intent.getStringExtra(getString(R.string.ANTALL_RIKTIGE_SVAR));
        sprak = intent.getStringExtra(getString(R.string.VALGT_SPRAK));
        regnestykke = intent.getStringExtra(getString(R.string.ANTALL_REGNESTYKKER));
        Language.setLanguage(this, sprak);

        setContentView(R.layout.activity_ferdig_spill);

        btnSpillPaNytt = findViewById(R.id.btnSpillPaNytt);
        btnTilbake = findViewById(R.id.btnTilbake);
        txtResultatRiktige = findViewById(R.id.txtResultatRiktige);
        txtResultatGale = findViewById(R.id.txtResultatGale);
        btnSpillPaNytt.setOnClickListener(this);
        btnTilbake.setOnClickListener(this);
        txtResultatRiktige.setOnClickListener(this);
        txtResultatGale.setOnClickListener(this);

        txtResultatRiktige.setText(riktigeSvar+" "+getString(R.string.riktige_resultat));
        txtResultatGale.setText(galeSvar+" "+getString(R.string.gale_resultat));
    }

    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSpillPaNytt:
                Intent intent = new Intent(this, SpillActivity.class);
                intent.putExtra(getString(R.string.ANTALL_REGNESTYKKER), regnestykke);
                intent.putExtra(getString(R.string.VALGT_SPRAK), sprak);
                startActivity(intent);
                finish();
                break;
            case R.id.btnTilbake:
                finish();
                break;
        }
    }
}