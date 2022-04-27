package com.example.s344224mappe3;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.s344224mappe3.databinding.ActivityMapsBinding;
import com.google.android.libraries.places.api.Places;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    ExtendedFloatingActionButton visHusene;
    FloatingActionButton leggTilHus;
    List<Hus> alleHus;
    List<Marker> allMarkers = new ArrayList<Marker>();
    ActivityResultLauncher<Intent> leggTilHusActivityResultLauncher;
    ActivityResultLauncher<Intent> endreHusActivityResultLauncher;
    BottomSheetBehavior bottomSheetBehavior;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        visHusene = findViewById(R.id.extendedFloatingActionButton);
        leggTilHus = findViewById(R.id.floatingActionButton);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Places.initialize(getApplicationContext(), "AIzaSyCiUfWCj3G4ru7sM1qAnC_r6HJQoIIFI7I");

        leggTilHusActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            onActivityResultOk(result);
                        }
                    }
                });

        endreHusActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            onActivityResultOk(result);
                        }
                    }
                });

        bottomSheetBehavior = getBottomSheet(R.id.standard_bottom_sheet);
    }

    public void onActivityResultOk(ActivityResult result) {
        Intent data = result.getData();
        String koordinater = data.getStringExtra("koordinater");
        if (mMap != null) mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(stringToLatLng(koordinater), 17));
        HttpGetAsyncTask task = new HttpGetAsyncTask();
        task.setHttpGetAsyncTaskListener(new HttpGetAsyncTask.HttpGetAsyncTaskListener() {
            @Override
            public void onResult(List<Hus> result) {
                alleHus = result;
                addMarkers(alleHus);
                initRecyclerView();

                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        onMarkerClicked(marker);
                        return false;
                    }
                });
            }
        });
        task.execute(new String[] {"http://studdata.cs.oslomet.no/~dbuser6/jsonout.php"});
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public BottomSheetBehavior getBottomSheet(int bottomSheetID) {
        FrameLayout standardBottomSheet = findViewById(bottomSheetID);
        standardBottomSheet.getLayoutParams().height = (int) (getScreenResolution(this)[1]*0.54);
        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(standardBottomSheet);
        bottomSheetBehavior.setSkipCollapsed(true);
        bottomSheetBehavior.setHideable(true);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        return bottomSheetBehavior;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        int padding = (int) (getScreenResolution(this)[1]*0.53);
        float zoomLevel = 17;
        LatLng pilestredet = new LatLng(59.9194912, 10.7354185);
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.setPadding(0, 0, 0, padding);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pilestredet, zoomLevel));
        HttpGetAsyncTask task = new HttpGetAsyncTask();
        task.setHttpGetAsyncTaskListener(new HttpGetAsyncTask.HttpGetAsyncTaskListener() {
            @Override
            public void onResult(List<Hus> result) {
                alleHus = result;
                addMarkers(alleHus);
                initRecyclerView();

                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        onMarkerClicked(marker);
                        return false;
                    }
                });
            }
        });
        task.execute(new String[] {"http://studdata.cs.oslomet.no/~dbuser6/jsonout.php"});
    }

    private static int[] getScreenResolution(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        int[] resolution = new int[2];
        resolution[0] = width;
        resolution[1] = height;
        return resolution;
    }

    public void visList(View view) {
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    public void leggTilHus(View view) {
        String[] koordinaterArray = getKoordinaterArray(alleHus);
        Bundle bundle = new Bundle();
        bundle.putStringArray("registrerteKoordinater", koordinaterArray);
        Intent intent = new Intent(this, LeggTilHusActivity.class);
        intent.putExtras(bundle);
        leggTilHusActivityResultLauncher.launch(intent);
    }

    public String[] getKoordinaterArray(List<Hus> list) {
        String[] array = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            array[i] = list.get(i).getKoordinater();
        }
        return array;
    }

    public void onMarkerClicked(Marker marker) {
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        HusAdapter adapter = (HusAdapter) recyclerView.getAdapter();

        Hus h = adapter.getItemByCoordinates(marker.getPosition());
        int position = adapter.getItemPositionByCoordinates(marker.getPosition());

        extendItem(h, position);
        if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_HIDDEN) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
    }

    public void addMarkers(List<Hus> list) {
        for (Hus h : list) {
            LatLng koordinater = stringToLatLng(h.getKoordinater());
            MarkerOptions markerOptions = new MarkerOptions().position(koordinater).title("Hus "+h.getId());
            markerOptions.getPosition();

            boolean markerFinnes = false;
            for (Marker m : allMarkers) {
                String koordinater1 = String.valueOf((float) koordinater.latitude)+" : "+String.valueOf((float) koordinater.longitude);
                String koordinater2 = String.valueOf((float) m.getPosition().latitude)+" : "+String.valueOf((float) m.getPosition().longitude);
                LatLng pos = m.getPosition();
                if (koordinater2.equals(koordinater1)) {
                    markerFinnes = true;
                    h.setMarker(m);
                }
            }
            //sjekk hvis marker er allerede legget til
            if (allMarkers.size() == 0) {
                Marker marker = mMap.addMarker(markerOptions);
                allMarkers.add(marker);
                h.setMarker(marker);
            }
            else if (!markerFinnes) {
                Marker marker = mMap.addMarker(markerOptions);
                allMarkers.add(marker);
                h.setMarker(marker);
            }
        }
    }

    public LatLng stringToLatLng(String str) {
        float latitude = Float.parseFloat(str.substring(0, str.indexOf(":")));
        float longitude = Float.parseFloat(str.substring(str.indexOf(":")+1, str.length()));
        return new LatLng(latitude, longitude);
    }

    @Override
    public void onBackPressed() {
        if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_HIDDEN) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            return;
        }
        super.onBackPressed();
    }

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        HusAdapter husAdapter = new HusAdapter(this, alleHus);

        husAdapter.setOnItemClickListener(new HusAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Hus hus, int position) {
                extendItem(hus, position);
            }

            @Override
            public void onDeleteClick(Hus hus, int position) {
                ConfirmDeleteDialog dialog = new ConfirmDeleteDialog(new ConfirmDeleteDialog.DialogClickListener() {
                    @Override
                    public void onYesClick() {
                        slettHusFraRecyclerView(hus, position);
                        slettHusFraDb(hus);
                    }
                    @Override
                    public void onNoClick() {
                        return;
                    }
                });
                dialog.setTitle("Slett Hus "+hus.getId()+"?");
                dialog.show(getSupportFragmentManager(), "Slett_hus_dialog");
            }

            @Override
            public void onEditClick(Hus hus) {
                Hus h = new Hus(hus.getId(), hus.getBeskrivelse(), hus.getAdresse(), hus.getKoordinater(), hus.getAntallEtasjer());
                Intent intent = new Intent(getBaseContext(), EndreHusActivity.class);
                intent.putExtra("hus", h);
                leggTilHusActivityResultLauncher.launch(intent);
            }
        });
        visHusene.setVisibility(husAdapter.getItemCount() > 0 ? View.VISIBLE : View.GONE);
        recyclerView.setAdapter(husAdapter);
    }

    public void slettHusFraRecyclerView(Hus hus, int position) {
        hus.getMarker().remove();
        HusAdapter husAdapter = (HusAdapter) recyclerView.getAdapter();
        husAdapter.getHusList().remove(position);
        husAdapter.notifyItemRemoved(position);
        husAdapter.notifyItemRangeChanged(position, husAdapter.getItemCount());
        if (husAdapter.getItemCount() == 0)  {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            visHusene.setVisibility(View.GONE);
        }
    }

    public void slettHusFraDb(Hus hus) {
        String url = "http://studdata.cs.oslomet.no/~dbuser6/delete.php";
        String urlParams = "id="+hus.getId()+"";
        HttpPostAsyncTask task = new HttpPostAsyncTask(this);
        task.setHttpPostAsyncTaskListener(new HttpPostAsyncTask.HttpPostAsyncTaskListener() {
            @Override
            public void onResult(String resultCode) {
                int responseCode = Integer.parseInt(resultCode);
                if (responseCode == 200) Toast.makeText(getApplicationContext(), "Hus slettet", Toast.LENGTH_SHORT).show();
                else Toast.makeText(getApplicationContext(), "Kunne ikke slette hus fra server", Toast.LENGTH_SHORT).show();
            }
        });
        task.execute(url, urlParams);
    }

    public void extendItem(Hus hus, int position) {
        HusAdapter husAdapter = (HusAdapter) recyclerView.getAdapter();
        hus.setExpanded(!hus.isExpanded());
        if (husAdapter.getPreviousAdapterPosition() == -1) {
            husAdapter.notifyItemChanged(position);
            husAdapter.setPreviousAdapterPosition(position);
            scrollToItem(position);
            moveCamera(hus);
            return;
        }
        else if (husAdapter.getPreviousAdapterPosition() == position) {
            husAdapter.notifyItemChanged(position);
            scrollToItem(position);
            moveCamera(hus);
            return;
        }
        else {
            Hus currentlyExpanded = husAdapter.getHusList().get(husAdapter.getPreviousAdapterPosition());
            currentlyExpanded.setExpanded(false);
            husAdapter.notifyItemChanged(position);
            husAdapter.notifyItemChanged(husAdapter.getPreviousAdapterPosition());
            husAdapter.setPreviousAdapterPosition(position);
            scrollToItem(position);
            moveCamera(hus);
        }
    }

    public void scrollToItem(int position) {
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        layoutManager.scrollToPositionWithOffset(position, 0);
    }

    public void moveCamera(Hus hus) {
        hus.getMarker().showInfoWindow();
        LatLng koord = stringToLatLng(hus.getKoordinater());
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(koord, 17), 300, null);
    }

}