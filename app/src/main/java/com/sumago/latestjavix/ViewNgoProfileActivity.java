package com.sumago.latestjavix;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.sumago.latestjavix.Util.Config;

import org.json.JSONArray;
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
import java.util.ArrayList;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;


public class ViewNgoProfileActivity extends AppCompatActivity {
    private static final String TAG = "_msg";
    Context context;
    HttpURLConnection conn;
    HashMap hasData;
    TextView txName,txEmail,txMobile,txQualification,txSpecialization,txDob,txCountry,txState,txDistrict,txAddress,txOwner,txRegno;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_ngo_profile);
        downloadJSON();
        txName=(TextView)findViewById(R.id.txName);
        txOwner=(TextView)findViewById(R.id.txOwner);
        txRegno=(TextView)findViewById(R.id.txRegno);
        txEmail=(TextView)findViewById(R.id.txEmail);
        txMobile=(TextView)findViewById(R.id.txMobile);
        txQualification=(TextView)findViewById(R.id.txQualification);
        txSpecialization=(TextView)findViewById(R.id.txSpecialization);
        txDob=(TextView)findViewById(R.id.txDob);
        txCountry=(TextView)findViewById(R.id.txCountry);
        txState=(TextView)findViewById(R.id.txState);
        txDistrict=(TextView)findViewById(R.id.txDistrict);
        txAddress=(TextView)findViewById(R.id.txAddress);
    }
    private void downloadJSON() {

        class DownloadJSON extends AsyncTask<Void, Void, String> {
            private Dialog loadingDialog;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingDialog = ProgressDialog.show(ViewNgoProfileActivity.this, "Please wait", "Loading...");
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int respStatus=jsonObject.getInt("status");
                    if(respStatus==1) {
                        //Toast.makeText(MyConfig.CONTEXT, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        ArrayList<DoctorData> recsArrayList=new ArrayList<DoctorData>();
                        JSONObject recsData=jsonObject.getJSONObject("data");
                        JSONArray recsArray=recsData.getJSONArray("data");
                        int recsLen=recsArray.length();
                        for(int i=0;i<recsLen;i++) {
                            JSONObject rec = recsArray.getJSONObject(i);
                            DoctorData doctorDataData = new DoctorData();
                            txName.setText("NGO Name :" +  rec.getString("name") );
                            txOwner.setText("NGO Owner :" +  rec.getString("owner"));
                            txEmail.setText("Email :" + rec.getString("email"));
                            txMobile.setText("Mobile :" + rec.getString("mobile"));
                            JSONObject info = rec.getJSONObject("info");
                            txRegno.setText("Registraion No :" +  info.getString("ngoRegistrationNo") );
                            txDob.setText("Registraton Date :" + info.getString("dateOfRegistration"));
                            txCountry.setText("Country :" + info.getString("country"));
                            txState.setText("State :" + info.getString("state"));
                            txDistrict.setText("District :" + info.getString("district"));
                            txAddress.setText("Address :" + info.getString("address"));
                        }
                    }} catch (JSONException e) {
                    //e.printStackTrace();
                    Log.e(TAG, "json err : " + e.getMessage());
                }
            }

            @Override
            protected String doInBackground(Void... voids) {

                try {
                    URL url = new URL(MyConfig.URL_NGO_PROFILE);
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
                    result.append(URLEncoder.encode("userId", "UTF-8"));
                    result.append("=");
                    result.append(URLEncoder.encode(Config._uid, "UTF-8"));
                    result.append("&");
                    result.append(URLEncoder.encode("token", "UTF-8"));
                    result.append("=");
                    result.append(URLEncoder.encode("dfjkhsdfaksjfh3756237", "UTF-8"));
                    writer.write(result.toString());
                    writer.flush();
                    writer.close();
                    os.close();

                    int responseCode = conn.getResponseCode();
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
                        return "{\"status\":0,\"message\":\"Login Failed.\"}";
                    }


                    return sb.toString().trim();
                }catch (Exception e) {
                    Log.e(TAG, "exception : " + e.getMessage());

                    return "{\"status\":0,\"message\":\"Login Failed.\"}";
                }
                finally {
                    conn.disconnect();
                }
            }
        }
        DownloadJSON getJSON = new DownloadJSON();
        getJSON.execute();
    }
}