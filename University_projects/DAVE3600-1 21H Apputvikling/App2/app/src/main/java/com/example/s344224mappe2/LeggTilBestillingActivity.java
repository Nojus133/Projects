package com.example.s344224mappe2;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class LeggTilBestillingActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {
    private DBHandler db;
    private Button btnLagre;
    private TextInputEditText id;
    private TextInputEditText dato;
    private TextInputEditText tid;
    private AutoCompleteTextView restauranterDropdown;
    private MultiAutoCompleteTextView vennerDropdown;

    private List<Restaurant> alleRestauranter;
    private Restaurant valgtRestaurant;
    private List<Venn> alleVenner;
    private boolean[] valgteVennerBoolean;
    private List<Venn> valgteVennerList;
    private Bestilling bestilling;

    private void initParams() {
        db = new DBHandler(this);
        id = (TextInputEditText) findViewById(R.id.txt_bestilling_id_edittext);
        dato = (TextInputEditText) findViewById(R.id.txt_bestilling_dato_edittext);
        dato.setOnClickListener(this);
        dato.setOnTouchListener(this);
        tid = (TextInputEditText) findViewById(R.id.txt_bestilling_tid_edittext);
        tid.setOnClickListener(this);
        tid.setOnTouchListener(this);
        vennerDropdown = (MultiAutoCompleteTextView) findViewById(R.id.txt_bestilling_venner_autocomplete);
        vennerDropdown.setOnClickListener(this);
        vennerDropdown.setOnTouchListener(this);
        btnLagre = findViewById(R.id.btnLagre);
        btnLagre.setOnClickListener(this);
        restauranterDropdown = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        alleRestauranter = db.getAlleRestauranter();
        alleVenner = db.getAlleVenner();
        valgteVennerBoolean = new boolean[alleVenner.size()];
        valgteVennerList = new ArrayList<>();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leggtil_bestilling);
        initParams();

        Intent data = getIntent();
        bestilling = data.getParcelableExtra("Bestilling");
        String toolbarNavn = data.getStringExtra("ToolbarNavn");
        getSupportActionBar().setTitle(toolbarNavn);




        leggTilText(bestilling);
    }

    @Override
    protected void onResume() {
        super.onResume();
        alleRestauranter = db.getAlleRestauranter();


        restauranterDropdown.setAdapter(new ArrayAdapter<String>(this, R.layout.dropdown_item, getRestaurants(alleRestauranter)));
        restauranterDropdown.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                TextView tv = (TextView) view;
                if (tv.getText().toString().equals("+ Legg til restaurant")) {
                    Intent intent = new Intent(LeggTilBestillingActivity.this, LeggTilRestaurantActivity.class);
                    intent.putExtra("ToolbarNavn", getString(R.string.toolbar_legg_til_restaurant));
                    startActivityForResult(intent, 21);
                    return;
                }
                valgtRestaurant = alleRestauranter.get(position);
                btnLagre.setEnabled(true);
            }
        });
    }


    public String[] getRestaurants(List<Restaurant> restaurants) {
        if (restaurants.isEmpty()) {
            String[] ingenRestauranter = new String[1];
            ingenRestauranter[0] = "+ Legg til restaurant";
            return ingenRestauranter;
        }
        List<Restaurant> list = restaurants;
        String[] array = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            array[i] = list.get(i).getNavn();
        }
        return array;
    }

    public String[] getVenner(List<Venn> venner) {
        List<Venn> list = venner;
        String[] array = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            array[i] = list.get(i).getFirstName()+" "+list.get(i).getLastName();
        }
        return array;
    }

    public Date getCurrentTime(int leggeTilTimer) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String time = sdf.format(new Date());
        Date date = null;
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR, leggeTilTimer);
        return calendar.getTime();
    }

    public void saveData() {
        Bestilling b = new Bestilling();
        if (bestilling != null) b.set_id(bestilling.get_id());
        b.setRestaurant(valgtRestaurant);
        b.setVenner(valgteVennerList);
        b.setDato(formatDate("dd.MM.yyyy", dato.getText().toString(), "GMT"));
        b.setTidspunkt(formatDate("HH:mm", tid.getText().toString(), "GMT+1"));

        Intent data = new Intent();
        data.putExtra("Bestilling", b);
        setResult(RESULT_OK, data);
        finish();
    }

    public void leggTilText(Bestilling bestilling) {
        id.setInputType(InputType.TYPE_NULL);
        dato.setInputType(InputType.TYPE_NULL);
        tid.setInputType(InputType.TYPE_NULL);
        vennerDropdown.setInputType(InputType.TYPE_NULL);
        if (bestilling != null) {
            btnLagre.setEnabled(true);
            id.setText(bestilling.get_id().toString());
            valgtRestaurant = bestilling.getRestaurant();
            restauranterDropdown.setText(valgtRestaurant.getNavn());
            dato.setText(new SimpleDateFormat("dd.MM.yyyy").format(bestilling.getDato()));
            tid.setText(new SimpleDateFormat("HH:mm").format(bestilling.getTidspunkt()));
            for (Venn v : bestilling.getVenner()) {
                valgteVennerList.add(v);
            }
            valgteVennerBoolean = updateValgteVennerBooleans(valgteVennerBoolean, valgteVennerList, alleVenner);
            valgteVennerList = updateValgteVenner(alleVenner, valgteVennerBoolean);
            setVennerText();
        }
        else {
            TextInputLayout idParent = (TextInputLayout) findViewById(R.id.txt_bestilling_id);
            idParent.setVisibility(View.GONE);
            TextInputLayout restaurantParent = (TextInputLayout) findViewById(R.id.txt_bestilling_restaurant);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)restaurantParent.getLayoutParams();
            params.removeRule(RelativeLayout.END_OF);
            params.width = RelativeLayout.LayoutParams.MATCH_PARENT;
            dato.setText(new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(new Date()));
            tid.setText(new SimpleDateFormat("HH:mm").format(getCurrentTime(1)));
        }
    }

    public Date formatDate(String pattern, String inputDate, String timezone) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        dateFormat.setTimeZone(TimeZone.getTimeZone(timezone));
        Date date = null;
        try {
            date = dateFormat.parse(inputDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public Date formatDate(String pattern, String inputDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        Date date = null;
        try {
            date = dateFormat.parse(inputDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_bestilling_dato_edittext:
                Date date = formatDate("dd.MM.yyyy", dato.getText().toString(), "GMT");
                CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();
                constraintsBuilder.setValidator(DateValidatorPointForward.now());

                MaterialDatePicker datePicker = MaterialDatePicker.Builder.datePicker()
                        .setTitleText("Velg dato").setCalendarConstraints(constraintsBuilder.build())
                        .setSelection(date.getTime()).build();
                datePicker.show(getSupportFragmentManager(), "Dato");
                datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
                    @Override
                    public void onPositiveButtonClick(Object selection) {
                        dato.setText(new SimpleDateFormat("dd.MM.yyyy").format(selection));
                    }
                });
                break;
            case R.id.txt_bestilling_tid_edittext:
                Calendar calendar = Calendar.getInstance();
                Date timeDate = formatDate("HH:mm", tid.getText().toString(), "GMT+2");
                calendar.setTimeZone(TimeZone.getTimeZone("GMT+2"));
                calendar.setTime(timeDate);

                MaterialTimePicker timePicker = new MaterialTimePicker.Builder()
                        .setTimeFormat(TimeFormat.CLOCK_24H).setHour(calendar.get(Calendar.HOUR_OF_DAY))
                        .setMinute(calendar.get(Calendar.MINUTE)).setTitleText("Velg tidspunkt").build();
                timePicker.show(getSupportFragmentManager(), "Tid");
                timePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String hour = Integer.toString(timePicker.getHour());
                        String minute = Integer.toString(timePicker.getMinute());
                        if (hour.length() == 1) hour = "0"+timePicker.getHour();
                        if (minute.length() == 1) minute = "0"+timePicker.getMinute();
                        tid.setText(hour+":"+minute);
                    }
                });
                break;
            case R.id.txt_bestilling_venner_autocomplete:
                List<Venn> currentSelection = copyList(valgteVennerList);
                boolean[] currentSelectionBoolean = copyArray(valgteVennerBoolean, valgteVennerBoolean.length);
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this)
                        .setTitle("Legg til venner").setCancelable(false)
                        .setMultiChoiceItems(getVenner(alleVenner), valgteVennerBoolean, new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                                Venn venn = alleVenner.get(i);
                                if (b) valgteVennerList.add(venn);
                                else {
                                    valgteVennerList.remove(venn);
                                }
                            }
                        })
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                setVennerText();
                            }
                        })
                        .setNegativeButton("Avbryt", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                valgteVennerList = currentSelection;
                                valgteVennerBoolean = currentSelectionBoolean;
                            }
                        })
                        .setNeutralButton("Ny venn", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                valgteVennerList = currentSelection;
                                valgteVennerBoolean = currentSelectionBoolean;
                                Intent intent = new Intent(LeggTilBestillingActivity.this, LeggTilVennActivity.class);
                                intent.putExtra("ToolbarNavn", getString(R.string.toolbar_legg_til_venn));
                                startActivityForResult(intent, 31);
                            }
                        });
                if (alleVenner.isEmpty()) builder.setMessage("Det finnes ingen registrerte venner.");
                builder.show();
                break;
            case R.id.btnLagre:
                saveData();
                break;
        }
    }

    public List<Venn> copyList(List<Venn> inList) {
        List<Venn> list = new ArrayList<>();
        for (Venn v : inList) {
            list.add(v);
        }
        return list;
    }

    public boolean[] copyArray(boolean[] inArray, int arrayLenght) {
        boolean[] array = new boolean[arrayLenght];
        for (int i = 0; i < arrayLenght; i++) {
            if (inArray.length < array.length && i == arrayLenght-1) array[i] = false;
            else array[i] = inArray[i];
        }
        return array;
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 21) {
            if (resultCode == RESULT_OK) {
                Restaurant restaurant = data.getParcelableExtra("Restaurant");
                db.leggTilRestaurant(restaurant);
                Toast.makeText(this, "Restaurant lagret", Toast.LENGTH_SHORT).show();
            }
            if (resultCode == RESULT_CANCELED) Toast.makeText(this, "Restaurant ble ikke lagret", Toast.LENGTH_SHORT).show();
            restauranterDropdown.setText("Velg restaurant");
        }

        if (requestCode == 31) {
            if (resultCode == RESULT_OK) {
                Venn venn = data.getParcelableExtra("Venn");
                db.leggTilVenn(venn);
                Toast.makeText(this, "Venn lagret", Toast.LENGTH_SHORT).show();
            }
            if (resultCode == RESULT_CANCELED) Toast.makeText(this, "Venn ble ikke lagret", Toast.LENGTH_SHORT).show();
        }
        alleVenner = db.getAlleVenner();
        valgteVennerBoolean = copyArray(valgteVennerBoolean, alleVenner.size());
        valgteVennerList = updateValgteVenner(alleVenner, valgteVennerBoolean);
    }

    public List<Venn> updateValgteVenner(List<Venn> venner, boolean[] booleans) {
        List<Venn> list = new ArrayList<>();
        for (int i = 0; i < venner.size(); i++) {
            if (booleans[i] == true) list.add(venner.get(i));
        }
        return list;
    }

    public boolean[] updateValgteVennerBooleans(boolean[] booleans, List<Venn> valgteVenner, List<Venn> alleVenner) {
        boolean[] array = new boolean[booleans.length];
        for (int i = 0; i < valgteVenner.size(); i++) {
            for (int j = 0; j < alleVenner.size(); j++) {
                if (valgteVenner.get(i).get_id() == alleVenner.get(j).get_id()) array[j] = true;
            }
        }
        return array;
    }

    public void setVennerText() {
        String out = "";
        if (valgteVennerList.isEmpty()) out = "Legg til venner";
        for (int j = 0; j < valgteVennerList.size(); j++) {
            out += valgteVennerList.get(j).getFirstName()+" "+valgteVennerList.get(j).getLastName();
            if (j != valgteVennerList.size()-1) out += ", ";
        }
        vennerDropdown.setText(out);
    }
}