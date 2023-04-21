package com.example.trackit;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.script.Script;
import com.google.api.services.script.model.ExecutionRequest;
import com.google.api.services.script.model.Operation;
import com.google.api.services.script.model.Value;
import com.google.api.services.sheets.v4.SheetsScopes;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.api.services.drive.DriveScopes;


public class SendDataTask extends AsyncTask<String, Void, Boolean> {

    private Context mContext;
    private String mSheetUrl;
    private String mSheetId;
    private String mKeyContents;
    private String[] mInputValues;

    private static final List<String> SCOPES = Arrays.asList(SheetsScopes.SPREADSHEETS, DriveScopes.DRIVE_FILE);




    public SendDataTask(Context context, String sheetUrl, String sheetId, String keyContents, List<String> inputValues) {
        mContext = context;
        mSheetUrl = sheetUrl;
        mSheetId = sheetId;
        mKeyContents = keyContents;
        mInputValues = inputValues.toArray(new String[0]);
    }

    @Override
    protected Boolean doInBackground(String... params) {

        try {
            // Build the credentials object
            HttpTransport transport = new NetHttpTransport();
            JacksonFactory jsonFactory = new JacksonFactory();
            GoogleCredential credential = GoogleCredential.fromStream(new ByteArrayInputStream(mKeyContents.getBytes())).createScoped(SCOPES);
            credential = credential.createDelegated("saletrack@saletrack.iam.gserviceaccount.com");

            // Create an instance of the Script API
            Script script = new Script.Builder(transport, jsonFactory, credential)
                    .setApplicationName("com.example.trackit")
                    .build();

            // Prepare the input parameters
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("date", mInputValues[0]);
            data.put("soName", mInputValues[1]);
            data.put("dealerName", mInputValues[2]);
            data.put("market", mInputValues[3]);
            data.put("grp", mInputValues[4]);
            data.put("itemDesc", mInputValues[5]);
            data.put("unit", mInputValues[6]);
            data.put("qty", mInputValues[7]);
            data.put("remarks", mInputValues[8]);

            // Create an execution request
            ExecutionRequest request = new ExecutionRequest()
                    .setFunction("doPost") // The name of the function in your Google Apps Script
                    .setParameters(Collections.singletonList(data))
                    .setDevMode(true);

            // Execute the request
            Operation op = script.scripts().run(mSheetId, request).execute();

            // Check the response status
            if (op.getError() != null) {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    protected void onPostExecute(Boolean success) {
        if (success) {
            Toast.makeText(mContext, "Data added to Google Sheets", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mContext, "Failed to add data to Google Sheets", Toast.LENGTH_SHORT).show();
        }
    }
}
