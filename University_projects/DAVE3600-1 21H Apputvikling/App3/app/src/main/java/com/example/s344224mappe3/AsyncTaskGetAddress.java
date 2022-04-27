package com.example.s344224mappe3;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;

import java.util.List;

public class AsyncTaskGetAddress extends AsyncTask<String, String, Address> {
    Context context;
    String strAddress;
    AsyncTaskGetAddressListener listener;

    public interface AsyncTaskGetAddressListener {
        void onResult(Address result);
    }

    public AsyncTaskGetAddress(Context context, String strAddress) {
        this.context = context;
        this.strAddress = strAddress;
    }

    public void setAsyncTaskGetAddressListener(AsyncTaskGetAddressListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Address doInBackground(String... params) {
        Geocoder coder = new Geocoder(context);
        List<Address> address;
        Address location = null;

        try {
            address = coder.getFromLocationName(strAddress, 1);
            if (address == null) return null;
            location = address.get(0);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return location;
    }

    @Override
    protected void onPostExecute(Address result) {
        this.listener.onResult(result);
    }
}
