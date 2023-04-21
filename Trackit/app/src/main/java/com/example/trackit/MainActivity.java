package com.example.trackit;
import android.os.Bundle;
import com.example.trackit.SendDataTask;



import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.CredentialRequest;
import com.google.android.gms.auth.api.credentials.CredentialRequestResult;
import com.google.android.gms.auth.api.credentials.Credentials;
import com.google.android.gms.auth.api.credentials.CredentialsClient;
import com.google.android.gms.auth.api.credentials.CredentialsOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    // Define the Google Sheets API URL and the sheet ID
    private static final String SHEET_URL = "https://script.google.com/macros/s/AKfycbyIKd1B0cQAhmz6t4VZSP9Gg6_KJGQGGqT7n9gDj200Fkfw0F6-IjFDPdD8WmBje2uGtA/exec";
    private static final String SHEET_ID = "1J4XkNz7wfEDXF1saGzlc-MUfBTGFM2A0xB3e42ONvMw";

    // Define the input fields
    private EditText dateInput;
    private EditText soNameInput;
    private EditText dealerNameInput;
    private EditText marketInput;
    private EditText grpInput;
    private EditText itemDescInput;
    private EditText unitInput;
    private EditText qtyInput;
    private EditText remarksInput;

    private String keyContents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Read the contents of the service account key file from the app's assets folder
        try {
            InputStream is = getResources().openRawResource(R.raw.key);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            keyContents = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Initialize the input fields
        dateInput = findViewById(R.id.dateInput);
        soNameInput = findViewById(R.id.soNameInput);
        dealerNameInput = findViewById(R.id.dealerNameInput);
        marketInput = findViewById(R.id.marketInput);
        grpInput = findViewById(R.id.grpInput);
        itemDescInput = findViewById(R.id.itemDescInput);
        unitInput = findViewById(R.id.unitInput);
        qtyInput= findViewById(R.id.qtyInput);
        remarksInput= findViewById(R.id.remarksInput);

        // Define the Submit button
        Button submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Retrieve the data from the input fields
                String date = dateInput.getText().toString();
                String soName = soNameInput.getText().toString();
                String dealerName = dealerNameInput.getText().toString();
                String market = marketInput.getText().toString();
                String grp = grpInput.getText().toString();
                String itemDesc = itemDescInput.getText().toString();
                String unit = unitInput.getText().toString();
                String qty = qtyInput.getText().toString();
                String remarks= remarksInput.getText().toString();

                // Create an array of strings for the input values
                String[] inputValues = new String[] {date, soName, dealerName, market, grp, itemDesc, unit, qty, remarks};

                // Create a new instance of the SendDataTask AsyncTask
                SendDataTask sendDataTask = new SendDataTask(MainActivity.this, SHEET_URL, SHEET_ID, keyContents, Arrays.asList(inputValues));

                // Execute the AsyncTask to send the data to Google Sheets
                sendDataTask.execute();
            }
        });

    }
}

