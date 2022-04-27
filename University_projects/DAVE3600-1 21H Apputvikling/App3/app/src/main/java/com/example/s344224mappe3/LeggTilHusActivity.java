package com.example.s344224mappe3;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LeggTilHusActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {
    TextInputLayout adresse, antallEtasjer, beskrivelse;
    TextInputEditText etAdresse, etAntallEtasjer, etBeskrivelse;
    View progressButtonLayout;
    Button btnLagre;
    ActivityResultLauncher<Intent> activityLauncher;
    int felterMedTekst;
    ProgressButton progressButton;
    String koordinater;
    String[] registrerteKoordinater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_legg_til_hus);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Registrer hus");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_close_24);

        PlacesClient placesClient = Places.createClient(this);

        adresse = (TextInputLayout) findViewById(R.id.txt_input_layout_adresse);
        adresse.setFocusable(false);
        etAdresse = (TextInputEditText) findViewById(R.id.edittext_adresse);
        etAdresse.setOnClickListener(this);
        etAdresse.setOnTouchListener(this);
        etAntallEtasjer = (TextInputEditText) findViewById(R.id.edittext_antall_etasjer);
        etBeskrivelse = (TextInputEditText) findViewById(R.id.edittext_beskrivelse);
        progressButtonLayout = findViewById(R.id.btnLagre);
        progressButton = new ProgressButton(this, progressButtonLayout);
        btnLagre = progressButton.getButton();
        btnLagre.setOnClickListener(this);

        Bundle b = getIntent().getExtras();
        registrerteKoordinater = b.getStringArray("registrerteKoordinater");

        activityLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            Place place = Autocomplete.getPlaceFromIntent(data);
                            etAdresse.setText(place.getAddress());
                        }
                    }
                }
        );

        listenForInputChange();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_main:
                progressButton.startLoader();
                AsyncTaskGetAddress getAddress = new AsyncTaskGetAddress(this, etAdresse.getText().toString());
                getAddress.setAsyncTaskGetAddressListener(new AsyncTaskGetAddress.AsyncTaskGetAddressListener() {
                    @Override
                    public void onResult(Address result) {
                        if (result == null) {
                            Toast.makeText(getApplicationContext(), "Kunne ikke hente adresse.", Toast.LENGTH_SHORT).show();
                            progressButton.stopLoader();
                            return;
                        }
                        else if (result.getSubThoroughfare() == null) {
                            Toast.makeText(getApplicationContext(), "Adresse er ikke gyldig.", Toast.LENGTH_SHORT).show();
                            progressButton.stopLoader();
                            return;
                        }
                        else if (koordinaterFinnes((float) result.getLatitude(), (float) result.getLongitude(), registrerteKoordinater)) {
                            Toast.makeText(getApplicationContext(), "Adresse er allerede registrert.", Toast.LENGTH_SHORT).show();
                            progressButton.stopLoader();
                            return;
                        }
                        else {
                            koordinater = String.valueOf((float) result.getLatitude()) + " : " + String.valueOf((float) result.getLongitude());
                            lagre(new Hus(
                                    etBeskrivelse.getText().toString(),
                                    etAdresse.getText().toString(),
                                    koordinater,
                                    Integer.parseInt(etAntallEtasjer.getText().toString())
                            ));
                        }
                    }
                });
                getAddress.execute();
                break;
            case R.id.edittext_adresse:
                List<Place.Field> placeList = Arrays.asList(Place.Field.ADDRESS);
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, placeList)
                        .build(LeggTilHusActivity.this);
                activityLauncher.launch(intent);
                break;
        }
    }

    public boolean koordinaterFinnes(float latitude, float longitude, String[] array) {
        String koord = String.valueOf(latitude + " : " + longitude);
        boolean finnes = false;
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals(koord)) {
                finnes = true;
                break;
            }
        }
        return finnes;
    }

    public void lagre(Hus hus) {
        String adresse = hus.getAdresse();
        String koordinater = hus.getKoordinater();
        int antallEtasjer = hus.getAntallEtasjer();
        String beskrivelse = hus.getBeskrivelse();

        String url = "http://studdata.cs.oslomet.no/~dbuser6/jsonin.php";
        String urlParams = "adresse="+adresse+"&koordinater="+koordinater+"&antallEtasjer="+antallEtasjer+"&beskrivelse="+beskrivelse+"";
        HttpPostAsyncTask task = new HttpPostAsyncTask(this);
        task.setHttpPostAsyncTaskListener(new HttpPostAsyncTask.HttpPostAsyncTaskListener() {
            @Override
            public void onResult(String resultCode) {
                handleResult(resultCode);
            }
        });
        task.execute(url, urlParams);
    }

    public void handleResult(String resultCode) {
        progressButton.stopLoader();
        int responseCode = Integer.parseInt(resultCode);
        if (responseCode == 200) {
            Toast.makeText(getApplicationContext(), "Hus lagret", Toast.LENGTH_SHORT).show();
            Intent returnIntent = new Intent();
            returnIntent.putExtra("koordinater", koordinater);
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        }
        else {
            Toast.makeText(getApplicationContext(), "Kunne ikke lagre hus", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                view.requestFocus();
                break;
        }
        return false;
    }

    public void listenForInputChange() {
        List<TextInputEditText> etList = new ArrayList<>();
        etList.add(etAdresse);
        etList.add(etAntallEtasjer);
        etList.add(etBeskrivelse);
        felterMedTekst = 0;

        for (TextInputEditText editText : etList) {
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (charSequence.toString().isEmpty()) felterMedTekst--;
                    else if (i == 0 && i1 == 0) felterMedTekst++;
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (felterMedTekst == etList.size()) progressButton.setDisabled(false);
                    else progressButton.setDisabled(true);
                }
            });
        }
    }
    /*
    private class HttpPostAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String urlString = params[0];
            String urlParams = params[1];
            int responseCode = 0;

            byte[] postData       = urlParams.getBytes( StandardCharsets.UTF_8 );
            int postDataLength = postData.length;
            try {
                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoOutput( true );
                conn.setInstanceFollowRedirects( false );
                conn.setRequestMethod( "POST" );
                conn.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestProperty( "charset", "utf-8");
                conn.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));
                conn.setUseCaches( false );
                try( DataOutputStream wr = new DataOutputStream( conn.getOutputStream())) {
                    wr.write( postData );
                }
                responseCode = conn.getResponseCode();

            }
            catch (Exception e) {
                System.out.println(e.getMessage());
            }
            return Integer.toString(responseCode);
        }

        @Override
        protected void onPostExecute(String s) {
            progressButton.stopLoader();
            int responseCode = Integer.parseInt(s);
            if (responseCode == 200) {
                Toast.makeText(getApplicationContext(), "Hus lagret", Toast.LENGTH_SHORT).show();
                Intent returnIntent = new Intent();
                returnIntent.putExtra("koordinater", koordinater);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
            else {
                Toast.makeText(getApplicationContext(), "Kunne ikke lagre hus", Toast.LENGTH_SHORT).show();
            }
        }
    }
    */
}