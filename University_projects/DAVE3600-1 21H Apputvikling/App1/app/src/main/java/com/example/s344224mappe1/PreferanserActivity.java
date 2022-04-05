package com.example.s344224mappe1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.Locale;

public class PreferanserActivity extends AppCompatActivity implements View.OnClickListener {
    private String regnestykke;
    private String sprak;

    RadioButton rb5Regnestykker;
    RadioButton rb10Regnestykker;
    RadioButton rb15Regnestykker;
    RadioButton rbNorsk;
    RadioButton rbTysk;
    Button btnTilbake;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        regnestykke = Preference.getPreference(this, getString(R.string.ANTALL_REGNESTYKKER), getString(R.string.default_preference_antall_regnestykker));
        sprak = Preference.getPreference(this, getString(R.string.VALGT_SPRAK), getString(R.string.default_preference_valgt_sprak));
        Language.setLanguage(this, sprak);

        setContentView(R.layout.activity_preferanser);

        rb5Regnestykker = findViewById(R.id.rb_5_regnestykker);
        rb10Regnestykker = findViewById(R.id.rb_10_regnestykker);
        rb15Regnestykker = findViewById(R.id.rb_15_regnestykker);
        rbNorsk = findViewById(R.id.rb_Norsk);
        rbTysk = findViewById(R.id.rb_Tysk);
        btnTilbake = findViewById(R.id.btnTilbake);

        rb5Regnestykker.setOnClickListener(this);
        rb10Regnestykker.setOnClickListener(this);
        rb15Regnestykker.setOnClickListener(this);
        rbNorsk.setOnClickListener(this);
        rbTysk.setOnClickListener(this);
        btnTilbake.setOnClickListener(this);

        updatePreferences(regnestykke, sprak);
    }

    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

    @Override
    public void onClick(View view) {
        RadioButton rb;
        switch (view.getId()) {
            case R.id.btnTilbake:
                finish();
                break;
            case R.id.rb_5_regnestykker:
            case R.id.rb_10_regnestykker:
            case R.id.rb_15_regnestykker:
                rb = findCheckedRadioButton(findViewById(R.id.rgAntallRegnestykker));
                Preference.savePreference(this, getString(R.string.ANTALL_REGNESTYKKER), rb.getText().toString());
                break;
            case R.id.rb_Norsk:
            case R.id.rb_Tysk:
                rb = findCheckedRadioButton(findViewById(R.id.rgSprak));
                Preference.savePreference(this, getString(R.string.VALGT_SPRAK), rb.getText().toString());
                recreate();
                break;
        }
    }

    public void reload() {
        //recreate();
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    public RadioButton findCheckedRadioButton(RadioGroup radioGroup) {
        int rbID = radioGroup.getCheckedRadioButtonId();
        return (RadioButton) findViewById(rbID);
    }

    public void updatePreferences(String preference1, String preference2) {
        if (preference1.equals(rb5Regnestykker.getText().toString())) rb5Regnestykker.setChecked(true);
        if (preference1.equals(rb10Regnestykker.getText().toString())) rb10Regnestykker.setChecked(true);
        if (preference1.equals(rb15Regnestykker.getText().toString())) rb15Regnestykker.setChecked(true);
        if (preference2.equals("Norsk")) rbNorsk.setChecked(true);
        if (preference2.equals("Tysk") || preference2.equals("Deutsch")) rbTysk.setChecked(true);
    }
}