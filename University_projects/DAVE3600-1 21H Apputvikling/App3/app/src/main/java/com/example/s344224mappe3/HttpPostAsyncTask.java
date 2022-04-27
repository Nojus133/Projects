package com.example.s344224mappe3;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class HttpPostAsyncTask extends AsyncTask<String, Void, String> {
    private HttpPostAsyncTaskListener listener;
    private Context context;

    public interface HttpPostAsyncTaskListener {
        void onResult(String resultCode);
    }

    public HttpPostAsyncTask(Context context) {
        this.context = context;
    }

    public void setHttpPostAsyncTaskListener(HttpPostAsyncTaskListener listener) {
        this.listener = listener;
    }

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
        listener.onResult(s);
        /*
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
        */
    }
}
