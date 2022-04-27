package com.example.s344224mappe2;

import static com.example.s344224mappe2.Utils.stoppService;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import android.Manifest;
import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RestauranterFragment.ListFragmentListener, VennerFragment.VennerFragmentListener, BestillingerFragment.BestillingerFragmentListener {
    public static final String CHANNEL_ID = "NotifikasjonService";
    private DBHandler db;
    private Bundle bundle;
    private List<Restaurant> alleRestauranter;
    private List<Venn> alleVenner;
    private List<Bestilling> alleBestillinger;

    private void initDb() {
        db = new DBHandler(this);
        alleRestauranter = db.getAlleRestauranter();
        alleVenner = db.getAlleVenner();
        alleBestillinger = db.getAlleBestillinger();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initDb();
        createNotificationChannel();
        BottomNavigationView nav = findViewById(R.id.bottom_nav);
        nav.setOnNavigationItemSelectedListener(navListener);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean tillatNotifikasjoner = sharedPreferences.getBoolean("notifikasjoner", false);
        if (tillatNotifikasjoner) Utils.restartService(this);
        else stoppService(this);

        setToolbarTitle(R.string.toolbar_bestilling);
        bundle = new Bundle();
        bundle.putParcelableArrayList("alleBestillinger", new ArrayList<Bestilling>(db.getAlleBestillinger()));
        Fragment fragment = new BestillingerFragment();
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).commit();

        checkPermission();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch (item.getItemId()) {
                        case R.id.nav_bestillinger:
                            bundle = new Bundle();
                            bundle.putParcelableArrayList("alleBestillinger", new ArrayList<Bestilling>(db.getAlleBestillinger()));
                            selectedFragment = new BestillingerFragment();
                            selectedFragment.setArguments(bundle);
                            setToolbarTitle(R.string.toolbar_bestilling);
                            break;
                        case R.id.nav_restauranter:
                            bundle = new Bundle();
                            bundle.putParcelableArrayList("alleRestauranter", new ArrayList<Restaurant>(db.getAlleRestauranter()));
                            selectedFragment = new RestauranterFragment();
                            selectedFragment.setArguments(bundle);
                            setToolbarTitle(R.string.toolbar_restauranter);
                            break;
                        case R.id.nav_venner:
                            bundle = new Bundle();
                            bundle.putParcelableArrayList("alleVenner", new ArrayList<Venn>(db.getAlleVenner()));
                            selectedFragment = new VennerFragment();
                            selectedFragment.setArguments(bundle);
                            setToolbarTitle(R.string.toolbar_venner);
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, selectedFragment).commit();
                    return true;
                }
            };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.notifications:
                visPreferences();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void visPreferences(){
        Intent intent = new Intent(this,SetPreferencesActivity.class);
        startActivity(intent);
    }

    private void setToolbarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    private void setToolbarTitle(int title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    public void onListItemClick(Restaurant restaurant) {
        Intent intent = new Intent(this, LeggTilRestaurantActivity.class);
        intent.putExtra("Restaurant", restaurant);
        intent.putExtra("ToolbarNavn", getString(R.string.toolbar_endre_restaurant));
        startActivityForResult(intent, 22);
    }

    @Override
    public void onListItemClick(Venn venn) {
        Intent intent = new Intent(this, LeggTilVennActivity.class);
        intent.putExtra("Venn", venn);
        intent.putExtra("ToolbarNavn", getString(R.string.toolbar_endre_venn));
        startActivityForResult(intent, 32);
    }

    @Override
    public void onListItemClick(Bestilling bestilling) {
        Intent intent = new Intent(this, LeggTilBestillingActivity.class);
        intent.putExtra("Bestilling", bestilling);
        intent.putExtra("ToolbarNavn", getString(R.string.toolbar_endre_bestilling));
        startActivityForResult(intent, 12);
    }

    @Override
    public void onFabClick(String tag) {
        Intent intent;
        switch (tag) {
            case "bestillinger":
                intent = new Intent(this, LeggTilBestillingActivity.class);
                intent.putExtra("ToolbarNavn", getString(R.string.toolbar_legg_til_bestilling));
                startActivityForResult(intent, 11);
                break;
            case "restauranter":
                intent = new Intent(this, LeggTilRestaurantActivity.class);
                intent.putExtra("ToolbarNavn", getString(R.string.toolbar_legg_til_restaurant));
                startActivityForResult(intent, 21);
                break;
            case "venner":
                intent = new Intent(this, LeggTilVennActivity.class);
                intent.putExtra("ToolbarNavn", getString(R.string.toolbar_legg_til_venn));
                startActivityForResult(intent, 31);
                break;
        }
    }

    @Override
    public void onDeleteItemClick(Long position, String tag) {
        switch (tag) {
            case "restauranter" :
                db.slettRestaurant(position);
                break;
            case "venner" :
                db.slettVenn(position);
                break;
            case "bestillinger" :
                db.slettBestilling(position);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 11) {
            if (resultCode == RESULT_OK) {
                Bestilling nyBestilling = data.getParcelableExtra("Bestilling");
                db.leggTilBestilling(nyBestilling);
                Toast.makeText(this, "Bestilling lagret", Toast.LENGTH_SHORT).show();
            }
            if (resultCode == RESULT_CANCELED) Toast.makeText(this, "Bestilling ble ikke lagret", Toast.LENGTH_SHORT).show();
            alleBestillinger = db.getAlleBestillinger();
            bundle = new Bundle();
            bundle.putParcelableArrayList("alleBestillinger", new ArrayList<Bestilling>(alleBestillinger));
            Fragment fragment = new BestillingerFragment();
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
        }

        if (requestCode == 12) {
            if (resultCode == RESULT_OK) {
                Bestilling bestilling = data.getParcelableExtra("Bestilling");
                db.oppdaterBestilling(bestilling);
                Toast.makeText(this, "Bestilling endret", Toast.LENGTH_SHORT).show();
            }
            if (resultCode == RESULT_CANCELED) Toast.makeText(this, "Bestilling ble ikke endret", Toast.LENGTH_SHORT).show();
            alleBestillinger = db.getAlleBestillinger();
            bundle = new Bundle();
            bundle.putParcelableArrayList("alleBestillinger", new ArrayList<Bestilling>(alleBestillinger));
            Fragment fragment = new BestillingerFragment();
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
        }

        if (requestCode == 21) {
            if (resultCode == RESULT_OK) {
                Restaurant restaurant = data.getParcelableExtra("Restaurant");
                db.leggTilRestaurant(restaurant);
                Toast.makeText(this, "Restaurant lagret", Toast.LENGTH_SHORT).show();
            }
            if (resultCode == RESULT_CANCELED) Toast.makeText(this, "Restaurant ble ikke lagret", Toast.LENGTH_SHORT).show();
            alleRestauranter = db.getAlleRestauranter();
            bundle = new Bundle();
            bundle.putParcelableArrayList("alleRestauranter", new ArrayList<Restaurant>(alleRestauranter));
            Fragment fragment = new RestauranterFragment();
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
        }

        if (requestCode == 22) {
            if (resultCode == RESULT_OK) {
                Restaurant restaurant = data.getParcelableExtra("Restaurant");
                db.oppdaterRestaurant(restaurant);
                Toast.makeText(this, "Restaurant endret", Toast.LENGTH_SHORT).show();
            }
            if (resultCode == RESULT_CANCELED) Toast.makeText(this, "Restaurant ble ikke endret", Toast.LENGTH_SHORT).show();
            alleRestauranter = db.getAlleRestauranter();
            bundle = new Bundle();
            bundle.putParcelableArrayList("alleRestauranter", new ArrayList<Restaurant>(alleRestauranter));
            Fragment fragment = new RestauranterFragment();
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
        }

        if (requestCode == 31) {
            if (resultCode == RESULT_OK) {
                Venn venn = data.getParcelableExtra("Venn");
                db.leggTilVenn(venn);
                Toast.makeText(this, "Venn lagret", Toast.LENGTH_SHORT).show();
            }
            if (resultCode == RESULT_CANCELED) Toast.makeText(this, "Venn ble ikke lagret", Toast.LENGTH_SHORT).show();
            alleVenner = db.getAlleVenner();
            bundle = new Bundle();
            bundle.putParcelableArrayList("alleVenner", new ArrayList<Venn>(alleVenner));
            Fragment fragment = new VennerFragment();
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
        }

        if (requestCode == 32) {
            if (resultCode == RESULT_OK) {
                Venn venn = data.getParcelableExtra("Venn");
                db.oppdaterVenn(venn);
                Toast.makeText(this, "Venn endret", Toast.LENGTH_SHORT).show();
            }
            if (resultCode == RESULT_CANCELED) Toast.makeText(this, "Venn ble ikke endret", Toast.LENGTH_SHORT).show();
            alleVenner = db.getAlleVenner();
            bundle = new Bundle();
            bundle.putParcelableArrayList("alleVenner", new ArrayList<Venn>(alleVenner));
            Fragment fragment = new VennerFragment();
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
        }

    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void checkPermission() {
        final int MY_PERMISSIONS_REQUEST_SEND_SMS = ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);
        final int MY_PHONE_STATE_PERMISSION = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
        if (MY_PERMISSIONS_REQUEST_SEND_SMS != PackageManager.PERMISSION_GRANTED && MY_PHONE_STATE_PERMISSION != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {
                    Manifest.permission.SEND_SMS,Manifest.permission.READ_PHONE_STATE
            }, 0);
        }
    }
}