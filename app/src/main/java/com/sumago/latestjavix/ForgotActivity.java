package com.sumago.latestjavix;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sumago.latestjavix.Util.Config;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

public class ForgotActivity extends AppCompatActivity {
    private static final String TAG = "_msg";
    private static EditText emailid;
    private static Button submitButton;
    Context context;
    HttpURLConnection conn;
    HashMap hasData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);

        context=this;
        emailid = (EditText) findViewById(R.id.login_emailid);
        submitButton = (Button) findViewById(R.id.submit_btn);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isInternetOn(context)) {
                    Config._targetUrl="http://143.244.136.145:3010/api/";
                    downloadJSON(Config._targetUrl + "auth/forgotpw?=", emailid.getText().toString());

                }
                else {
                    android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(ForgotActivity.this
                    );
                    alertDialog.setMessage("No Internet Connection !");
                    alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            return;
                        }
                    });
                    alertDialog.create();
                    alertDialog.show();
                }
            }
        });
    }

    private void downloadJSON(final String urlWebService, final String _uid) {

        class DownloadJSON extends AsyncTask<Void, Void, String> {
            private Dialog loadingDialog;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingDialog = ProgressDialog.show(ForgotActivity.this, "Please wait", "Loading...");
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();

                try {
                    getJSONData(s);
                } catch (JSONException e) {
                    //e.printStackTrace();
                    Log.e(TAG, "json err : " + e.getMessage());
                }
            }

            @Override
            protected String doInBackground(Void... voids) {

                try {
                    URL url = new URL(urlWebService);
                    StringBuilder sb = new StringBuilder();
                    conn = (HttpURLConnection) url.openConnection();
                    //conn.setRequestProperty("Content-Type", "application/text");
                    conn.setReadTimeout(15000);
                    conn.setConnectTimeout(15000);
                    conn.setRequestMethod("POST");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    conn.setChunkedStreamingMode(0);
                    OutputStream os = conn.getOutputStream();

                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8"));
                    StringBuilder result = new StringBuilder();
                    result.append(URLEncoder.encode("email", "UTF-8"));
                    result.append("=");
                    result.append(URLEncoder.encode(_uid, "UTF-8"));
                    writer.write(result.toString());
                    writer.flush();
                    writer.close();
                    os.close();

                    int responseCode = conn.getResponseCode();
                    Log.e(TAG, "targeturl: " + urlWebService);
                    Log.e(TAG, "postdata: " + result);
                    Log.e(TAG, "Response Code: " + Integer.toString(responseCode));
                    if (responseCode == HttpsURLConnection.HTTP_OK) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        sb = new StringBuilder();
                        String response;
                        while ((response = br.readLine()) != null) {
                            sb.append(response);
                        }
                        Log.e(TAG, "response: " + sb);
                    }
                    else{
                        Log.e(TAG, "else part : " +sb);
                        return "{\"status\":0,\"message\":\"Invalid Email.\"}";
                    }


                    return sb.toString().trim();
                }catch (Exception e) {
                    Log.e(TAG, "exception : " + e.getMessage());

                    return "{\"status\":0,\"message\":\"Invalid Email.\"}";
                }
                finally {
                    conn.disconnect();
                }
            }
        }
        DownloadJSON getJSON = new DownloadJSON();
        getJSON.execute();
    }

    private void getJSONData(String json) throws JSONException {
        String jsonStr = json;
        int _status=0,roleId=0;
        if (jsonStr != null) {
            try {
                //String in;
                JSONObject obj1 = new JSONObject(jsonStr);
                Log.e(TAG, "status : " + obj1.getString("message"));
                if(obj1.getString("status").equalsIgnoreCase("1")) {
                    Config._uid = emailid.getText().toString();
                    hasData = new HashMap<>();
                    hasData.put("_status", obj1.getString("status"));
                    _status = Integer.parseInt(hasData.get("_status").toString());
                }
                else{
                    hasData = new HashMap<>();
                    hasData.put("_status", obj1.getString("status"));
                    hasData.put("_message", obj1.getString("message"));
                    _status = Integer.parseInt(hasData.get("_status").toString());
                }

            } catch (final JSONException e) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Json exception: " + e.getMessage(),
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }
        } else {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(),
                            "Couldn't get json from server. Check LogCat for possible errors!",
                            Toast.LENGTH_LONG)
                            .show();
                }
            });

        }

       if(_status==1)
        {
            Intent  i = new Intent(ForgotActivity.this, OtpActivity.class);
            finish();
            startActivity(i);;
        }
        else
        {
            Toast.makeText(getApplicationContext(), hasData.get("_message").toString(), Toast.LENGTH_LONG).show();
        }
    }

    public boolean isInternetOn(Context context) {

        // get Connectivity Manager object to check connection
        ConnectivityManager connec =
                (ConnectivityManager)context.getSystemService(context.CONNECTIVITY_SERVICE);

        // Check for network connections
        if ( connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED ) {

            return true;

        } else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED  ) {

            return false;
        }
        return false;
    }
}