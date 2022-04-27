package com.example.s344224mappe2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LeggTilRestaurantActivity extends AppCompatActivity {
    ViewGroup view;
    private int felterMedTekst = 0;
    Button btnLagre;
    Restaurant restaurant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leggtil_restaurant);

        Intent data = getIntent();
        String toolbarNavn = data.getStringExtra("ToolbarNavn");
        restaurant = data.getParcelableExtra("Restaurant");

        getSupportActionBar().setTitle(toolbarNavn);
        view = findViewById(R.id.leggTilView);
        btnLagre = findViewById(R.id.btnLagre);

        if (restaurant != null) leggTilText(restaurant);

        for (int i = 0; i < view.getChildCount(); i++) {
            View child = (View) view.getChildAt(i);

            if (child instanceof TextInputLayout) {
                TextInputEditText editText = (TextInputEditText) ((TextInputLayout) child).getEditText();
                if (restaurant != null) {
                    ((TextInputLayout) child).setEndIconMode(TextInputLayout.END_ICON_CUSTOM);
                    ((TextInputLayout) child).setEndIconDrawable(R.drawable.ic_baseline_check_circle_24);
                }
                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if (child.getId() == R.id.textField3) {
                            if (charSequence.toString().length() == 8 && i == 7) {
                                ((TextInputLayout) child).setEndIconMode(TextInputLayout.END_ICON_CUSTOM);
                                ((TextInputLayout) child).setEndIconDrawable(R.drawable.ic_baseline_check_circle_24);
                                felterMedTekst++;
                            }
                            else if (charSequence.toString().length() == 7 && i1 == 1 ) {
                                ((TextInputLayout) child).setEndIconMode(TextInputLayout.END_ICON_NONE);
                                felterMedTekst--;
                            }
                        }
                        else {
                            if (charSequence.toString().isEmpty()) {
                                ((TextInputLayout) child).setEndIconMode(TextInputLayout.END_ICON_NONE);
                                felterMedTekst--;
                            }
                            else if (i == 0) {
                                ((TextInputLayout) child).setEndIconMode(TextInputLayout.END_ICON_CUSTOM);
                                ((TextInputLayout) child).setEndIconDrawable(R.drawable.ic_baseline_check_circle_24);
                                felterMedTekst++;
                            }
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        if (felterMedTekst == view.getChildCount() - 2) btnLagre.setEnabled(true);
                        else btnLagre.setEnabled(false);
                    }
                });
            }
        }

        btnLagre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
            }
        });

    }

    public void saveData() {
        TextView idText = findViewById(R.id.bestilling_id);
        TextInputLayout navnEditText = findViewById(R.id.textField);
        TextInputLayout adresseEditText = findViewById(R.id.txt_bestilling_dato);
        TextInputLayout telefonEditText = findViewById(R.id.textField3);
        TextInputLayout typeEditText = findViewById(R.id.txt_bestilling_tid);

        String id = idText.getText().toString();
        String navn = navnEditText.getEditText().getText().toString();
        String adresse = adresseEditText.getEditText().getText().toString();
        String telefon = telefonEditText.getEditText().getText().toString();
        String type = typeEditText.getEditText().getText().toString();

        Restaurant restaurant;
        if (id.isEmpty()) restaurant = new Restaurant(navn, adresse, telefon, type);
        else restaurant = new Restaurant(Long.parseLong(id), navn, adresse, telefon, type);

        Intent data = new Intent();
        data.putExtra("Restaurant", restaurant);
        setResult(RESULT_OK, data);
        finish();
    }

    public void leggTilText(Restaurant restaurant) {
        btnLagre.setEnabled(true);
        felterMedTekst = view.getChildCount() - 2;
        TextView id = findViewById(R.id.bestilling_id);
        TextInputLayout navnEditText = findViewById(R.id.textField);
        TextInputLayout adresseEditText = findViewById(R.id.txt_bestilling_dato);
        TextInputLayout telefonEditText = findViewById(R.id.textField3);
        TextInputLayout typeEditText = findViewById(R.id.txt_bestilling_tid);

        id.setText(Long.toString(restaurant.get_id()));
        navnEditText.getEditText().setText(restaurant.getNavn());
        adresseEditText.getEditText().setText(restaurant.getAdresse());
        telefonEditText.getEditText().setText(restaurant.getTelefon());
        typeEditText.getEditText().setText(restaurant.getType());
    }
}