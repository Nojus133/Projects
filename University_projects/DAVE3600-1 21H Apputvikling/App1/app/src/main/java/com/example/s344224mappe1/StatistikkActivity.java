package com.example.s344224mappe1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class StatistikkActivity extends AppCompatActivity implements View.OnClickListener, MyDialog.DialogClickListener {
    private String sprak;
    private String galeSvar;
    private String riktigeSvar;

    Button btnSlettStatistikk;
    Button btnTilbake;
    TextView txtAntallRiktige;
    TextView txtAntallGale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        sprak = intent.getStringExtra(getString(R.string.VALGT_SPRAK));
        Language.setLanguage(this, sprak);

        setContentView(R.layout.activity_statistikk);

        btnSlettStatistikk = findViewById(R.id.btnSlettStatistikk);
        btnTilbake = findViewById(R.id.btnTilbake);
        txtAntallRiktige = findViewById(R.id.textAntallRiktige);
        txtAntallGale = findViewById(R.id.textAntallGale);

        btnSlettStatistikk.setOnClickListener(this);
        btnTilbake.setOnClickListener(this);
        setStatistikk();
    }

    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSlettStatistikk:
                DialogFragment dialog = new MyDialog(
                        getString(R.string.statistikk_dialog_tittel),
                        getString(R.string.statistikk_dialog_melding),
                        getString(R.string.dialog_confirm),
                        getString(R.string.dialog_cancel)
                );
                dialog.show(getSupportFragmentManager(), "Nullstill statistikk");
                break;
            case R.id.btnTilbake:
                finish();
                break;
        }
    }

    @Override
    public void onYesClick() {
        slettStatistikk();
        setStatistikk();
    }

    @Override
    public void onNoClick() {
        return;
    }

    public void setStatistikk() {
        riktigeSvar = Preference.getPreference(this, getString(R.string.ANTALL_RIKTIGE_SVAR), "0");
        galeSvar = Preference.getPreference(this, getString(R.string.ANTALL_GALE_SVAR), "0");
        txtAntallRiktige.setText(riktigeSvar);
        txtAntallGale.setText(galeSvar);
    }

    public void slettStatistikk() {
        Preference.savePreference(this, getString(R.string.ANTALL_RIKTIGE_SVAR), "0");
        Preference.savePreference(this, getString(R.string.ANTALL_GALE_SVAR), "0");
    }

}