package com.example.s344224mappe1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SpillActivity extends AppCompatActivity implements View.OnClickListener, MyDialog.DialogClickListener {
    private String regnestykke;
    private String sprak;

    EditText editTextSvar;
    TextView txtRegnestykke;
    TextView txtMessage;
    TextView txtRunder;
    TextView txtAntallRiktige;
    TextView txtAntallGale;

    Spill etSpill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        regnestykke = intent.getStringExtra(getString(R.string.ANTALL_REGNESTYKKER));
        sprak = intent.getStringExtra(getString(R.string.VALGT_SPRAK));
        Language.setLanguage(this, sprak);

        setContentView(R.layout.activity_spill);

        editTextSvar = findViewById(R.id.textSvar);
        txtRegnestykke = findViewById(R.id.txtRegnestykke);
        txtMessage = findViewById(R.id.txtMessage);
        txtRunder = findViewById(R.id.txtRunder);
        txtAntallRiktige = findViewById(R.id.txtAntallRiktige);
        txtAntallGale = findViewById(R.id.txtAntallGale);

        findViewById(R.id.knapp1).setOnClickListener(this);
        findViewById(R.id.knapp2).setOnClickListener(this);
        findViewById(R.id.knapp3).setOnClickListener(this);
        findViewById(R.id.knapp4).setOnClickListener(this);
        findViewById(R.id.knapp5).setOnClickListener(this);
        findViewById(R.id.knapp6).setOnClickListener(this);
        findViewById(R.id.knapp7).setOnClickListener(this);
        findViewById(R.id.knapp8).setOnClickListener(this);
        findViewById(R.id.knapp9).setOnClickListener(this);
        findViewById(R.id.knapp0).setOnClickListener(this);
        findViewById(R.id.knappSlett).setOnClickListener(this);

        findViewById(R.id.btnNeste).setOnClickListener(this);
        findViewById(R.id.btnAvslutt).setOnClickListener(this);

        if (savedInstanceState != null) {
            etSpill = savedInstanceState.getParcelable("spill");
        }
        else {
            etSpill = new Spill(this, Integer.parseInt(regnestykke));
        }
        updateUI();
    }

    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        createDialog();
    }

    @Override
    public void onYesClick() {
        finish();
    }

    @Override
    public void onNoClick() {
        return;
    }

    public void addNumber(String num) {
        String text = editTextSvar.getText().toString();
        if (text.length() >= 4) {
            Toast.makeText(this, R.string.svar_toast, Toast.LENGTH_SHORT).show();
            return;
        }
        resetMessage();
        text += num;
        editTextSvar.setText(text, TextView.BufferType.NORMAL);
    }

    public void deleteNumber() {
        String text = deleteLastChar(editTextSvar.getText().toString());
        editTextSvar.setText(text, TextView.BufferType.NORMAL);
    }

    public String deleteLastChar(String string) {
        String ut = null;
        if ((string != null) && (string.length() > 0)) {
            ut = string.substring(0, string.length() - 1);
        }
        return ut;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnNeste:
                checkSvar();
                break;
            case R.id.btnAvslutt:
                createDialog();
                break;
            case R.id.knapp1:
                addNumber("1");
                break;
            case R.id.knapp2:
                addNumber("2");
                break;
            case R.id.knapp3:
                addNumber("3");
                break;
            case R.id.knapp4:
                addNumber("4");
                break;
            case R.id.knapp5:
                addNumber("5");
                break;
            case R.id.knapp6:
                addNumber("6");
                break;
            case R.id.knapp7:
                addNumber("7");
                break;
            case R.id.knapp8:
                addNumber("8");
                break;
            case R.id.knapp9:
                addNumber("9");
                break;
            case R.id.knapp0:
                addNumber("0");
                break;
            case R.id.knappSlett:
                deleteNumber();
                break;
        }
    }

    public void checkSvar() {
        String resultString = editTextSvar.getText().toString();
        if (resultString.length() == 0) {
            displayMessage(getString(R.string.spill_skriv_svar));
            return;
        }

        if (etSpill.checkAnswer(resultString)) {
            etSpill.increaseCounter();
            etSpill.increaseRiktigSvarCounter();
            displayMessage(etSpill.getRiktigSvarText());
            new android.os.Handler(Looper.getMainLooper()).postDelayed(
                    new Runnable() {
                        public void run() {
                            updateUI();
                        }
                    },
                    700);
        }
        else {
            etSpill.increaseCounter();
            etSpill.increaseFeilSvarCounter();
            displayMessage(etSpill.getFeilSvarText());
            new android.os.Handler(Looper.getMainLooper()).postDelayed(
                    new Runnable() {
                        public void run() {
                            updateUI();
                        }
                    },
                    700);
        }

    }

    public void displayMessage(String string) {
        txtMessage.setText(string);
    }

    public void resetMessage() {
        txtMessage.setText("");
    }

    public void updateUI() {
        if (etSpill.getCounter() == etSpill.getAntallRunder()) {
            finishSpill();
            return;
        }
        txtAntallRiktige.setText(etSpill.getRiktigSvarCounter()+" "+getString(R.string.spill_riktige));
        txtAntallGale.setText(etSpill.getFeilSvarCounter()+" "+getString(R.string.spill_gale));
        txtRunder.setText(etSpill.getCounter()+1+" / "+etSpill.getAntallRunder());
        txtRegnestykke.setText(etSpill.nextQuestion());
        editTextSvar.setText("");
        resetMessage();
    }

    public void createDialog() {
        DialogFragment dialog = new MyDialog(
                getString(R.string.spill_dialog_tittel),
                getString(R.string.spill_dialog_melding),
                getString(R.string.dialog_confirm),
                getString(R.string.dialog_cancel)
        );
        dialog.show(getSupportFragmentManager(), "Avslutt Spill");
    }

    public void finishSpill() {
        saveScore();
        Intent intent = new Intent(this, FerdigSpillActivity.class);
        intent.putExtra(getString(R.string.VALGT_SPRAK), sprak);
        intent.putExtra(getString(R.string.ANTALL_REGNESTYKKER), regnestykke);
        intent.putExtra(getString(R.string.ANTALL_RIKTIGE_SVAR), Integer.toString(etSpill.getRiktigSvarCounter()));
        intent.putExtra(getString(R.string.ANTALL_GALE_SVAR), Integer.toString(etSpill.getFeilSvarCounter()));
        startActivity(intent);
        finish();
    }

    public void saveScore() {
        final String ANTALL_RIKTIGE_SVAR = getString(R.string.ANTALL_RIKTIGE_SVAR);
        final String ANTALL_GALE_SVAR = getString(R.string.ANTALL_GALE_SVAR);

        int tidligereRiktige = Integer.parseInt(Preference.getPreference(this, ANTALL_RIKTIGE_SVAR, "0"));
        int tidligereGale = Integer.parseInt(Preference.getPreference(this, ANTALL_GALE_SVAR, "0"));

        String totalRiktige = Integer.toString(tidligereRiktige + etSpill.getRiktigSvarCounter());
        String totalGale = Integer.toString(tidligereGale + etSpill.getFeilSvarCounter());

        Preference.savePreference(this, getString(R.string.ANTALL_RIKTIGE_SVAR), totalRiktige);
        Preference.savePreference(this, getString(R.string.ANTALL_GALE_SVAR), totalGale);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("spill", etSpill);
    }
}