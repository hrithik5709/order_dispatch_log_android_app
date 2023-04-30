package com.example.trackit;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.trackit.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private static final String BASE_URL = "https://script.google.com/macros/s/AKfycbwjU6iqeVAzg28ufS8Dndb84WZjVeKzFtYXW0i6Dnh8qLmw2rshsei5jzG0wVOLTfy2zw/exec";

    EditText editTextDate, editTextSoName, editTextDealerName, editTextMarket, editTextGrp, editTextItemDesc, editTextUnit, editTextQty, editTextRemarks;
    Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextDate = findViewById(R.id.dateInput);
        editTextSoName = findViewById(R.id.soNameInput);
        editTextDealerName = findViewById(R.id.dealerNameInput);
        editTextMarket = findViewById(R.id.marketInput);
        editTextGrp = findViewById(R.id.grpInput);
        editTextItemDesc = findViewById(R.id.itemDescInput);
        editTextUnit = findViewById(R.id.unitInput);
        editTextQty = findViewById(R.id.qtyInput);
        editTextRemarks = findViewById(R.id.remarksInput);
        btnSubmit = findViewById(R.id.submitButton);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the data from the UI
                String date = editTextDate.getText().toString();
                String soName = editTextSoName.getText().toString();
                String dealerName = editTextDealerName.getText().toString();
                String market = editTextMarket.getText().toString();
                String grp = editTextGrp.getText().toString();
                String itemDesc = editTextItemDesc.getText().toString();
                String unit = editTextUnit.getText().toString();
                int qty = Integer.parseInt(editTextQty.getText().toString());
                String remarks = editTextRemarks.getText().toString();

                // Create a JSON object
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("date", date);
                    jsonObject.put("soName", soName);
                    jsonObject.put("dealerName", dealerName);
                    jsonObject.put("market", market);
                    jsonObject.put("grp", grp);
                    jsonObject.put("itemDesc", itemDesc);
                    jsonObject.put("unit", unit);
                    jsonObject.put("qty", qty);
                    jsonObject.put("remarks", remarks);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Send the data to the Google Apps Script using OkHttp
                String json = jsonObject.toString();
                new PostDataAsyncTask().execute(json);
            }

        });
    }

    private class PostDataAsyncTask extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... params) {
            String json = params[0];
            OkHttpClient client = new OkHttpClient();
            RequestBody body = RequestBody.create(MediaType.parse("application/json"), json);
            Request request = new Request.Builder()
                    .url(BASE_URL)
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
    }
}
