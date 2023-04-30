package com.example.trackit;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PostDataAsyncTask extends AsyncTask<String, Void, String> {

    private Context mContext;

    public PostDataAsyncTask(Context context) {
        mContext = context;
    }

    protected String doInBackground(String... params) {
        String json = params[0];
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), json);
        Request request = new Request.Builder()
                .url("https://script.google.com/macros/s/AKfycbwiDyOdazrPrFrSHHdp7feN58U6nN1p-kj_6oq3/exec")
                .post(body)
                .build();
        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
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
