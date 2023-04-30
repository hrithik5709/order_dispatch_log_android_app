package com.example.trackit;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PostDataAsyncTask extends AsyncTask<String, Void, String> {

    private static final String TAG = "PostDataAsyncTask";
    private static final String BASE_URL = "https://script.google.com/macros/s/";
    private static final String API_KEY = "AKfycbwiDyOdazrPrFrSHHdp7feN58U6nN1p-kj_6oq3";
    private static final String EXEC_ENDPOINT = "exec";

    private final Context mContext;

    public PostDataAsyncTask(Context context) {
        mContext = context.getApplicationContext();
    }

    protected String doInBackground(String... params) {
        String json = params[0];
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), json);
        String url = BASE_URL + API_KEY + "/" + EXEC_ENDPOINT;
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                return response.body().string();
            } else {
                Log.e(TAG, "Error: " + response.code() + " " + response.message());
                return null;
            }
        } catch (IOException e) {
            Log.e(TAG, "Error: " + e.getMessage(), e);
            return null;
        }
    }

    protected void onPostExecute(String result) {
        if (result != null) {
            Toast.makeText(mContext, result, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(mContext, "Error: unable to add data.", Toast.LENGTH_LONG).show();
        }
    }
}
