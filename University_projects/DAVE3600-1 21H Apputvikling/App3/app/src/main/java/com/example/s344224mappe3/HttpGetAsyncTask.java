package com.example.s344224mappe3;

import android.os.AsyncTask;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class HttpGetAsyncTask extends AsyncTask<String, Void, List<Hus>> {
    private JSONObject jsonObject;
    private HttpGetAsyncTaskListener listener;

    public interface HttpGetAsyncTaskListener {
        void onResult(List<Hus> result);
    }

    public void setHttpGetAsyncTaskListener(HttpGetAsyncTaskListener listener) {
        this.listener = listener;
    }

    @Override
    protected List<Hus> doInBackground(String... urls) {
        List<Hus> result = new ArrayList<>();
        String s = "";
        String output = "";
        for (String url : urls) {
            try {
                URL urlen = new URL(urls[0]);
                HttpURLConnection conn = (HttpURLConnection) urlen.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");
                if (conn.getResponseCode() != 200) {
                    throw new RuntimeException("Failed: HTTP errorcode: " + conn.getResponseCode());
                }
                BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
                System.out.println("Output from Server .... \n");
                while ((s = br.readLine()) != null) {
                    output = output + s;
                }
                conn.disconnect();
                try {
                    JSONArray hus = new JSONArray(output);
                    for (int i = 0; i < hus.length(); i++) {
                        JSONObject jsonobject = hus.getJSONObject(i);
                        String adresse = jsonobject.getString("adresse");
                        String koordinater = jsonobject.getString("koordinater");
                        String beskrivelse = jsonobject.getString("beskrivelse");
                        int id = jsonobject.getInt("id");
                        int antallEtasjer = jsonobject.getInt("antallEtasjer");
                        if (!adresse.equals("null") && !koordinater.equals("null")) {
                            result.add(new Hus(id, beskrivelse, adresse, koordinater, antallEtasjer));
                        }
                    }
                    return result;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return result;
            } catch (Exception e) {
                return result;
            }
        }
        return result;
    }

    @Override
    protected void onPostExecute(List<Hus> result) {
        listener.onResult(result);
        /*
        alleHus = result;
        addMarkers(alleHus);
        initRecyclerView();

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                HusAdapter adapter = (HusAdapter) recyclerView.getAdapter();

                Hus h = adapter.getItemByCoordinates(marker.getPosition());
                int position = adapter.getItemPositionByCoordinates(marker.getPosition());

                    //h.setExpanded(true);
                    //layoutManager.scrollToPositionWithOffset(position, 0);
                    //adapter.notifyItemChanged(position);


                extendItem(h, position);
                if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_HIDDEN) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
                return false;
            }
        });*/
    }

}
