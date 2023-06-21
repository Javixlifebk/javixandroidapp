package com.sumago.latestjavix;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.sumago.latestjavix.Util.Config;
import com.sumago.latestjavix.WebService.ApiInterface;
import com.sumago.latestjavix.WebService.MyNewConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RefereBySevika extends AppCompatActivity {

    String token = "dfjkhsdfaksjfh3756237";
    String isUnrefer = "", citizenId = "";

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {    
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refere_by_sevika);


        Intent ii = getIntent();

        isUnrefer = ii.getStringExtra("isUnrefer");
        citizenId = ii.getStringExtra("citizenId");


        Log.e("Response", "citizenId: " + citizenId);
        Log.e("Response", "pstatus: " + isUnrefer);


//        progressDialog = new ProgressDialog(RefereBySevika.this);
//        progressDialog.setMessage("Please Wait...");
//        progressDialog.setCancelable(true);
//        progressDialog.show();


        String state_id = "1";

        ApiInterface apiInterface = MyNewConfig.getRetrofit().create(ApiInterface.class);
//        final Call<ResponseBody> result = (Call<ResponseBody>) apiInterface.getDistrict(state_id);
        final Call<ResponseBody> result = (Call<ResponseBody>) apiInterface.ReferBySevika(1, "1", "0", citizenId, "1", token);

        result.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                Log.e("Response", "Response is success: ");

                try {
                    if (response.isSuccessful()) {

                        String output = response.body().string();
                        Log.e("Response", "output: " + output);

                        JSONObject object = new JSONObject(output);

                        Log.e("Response", "status: " + object.getString("status"));


                        if (object.getString("status").equals("1")) {

//                            Toast.makeText(RefereBySevika.this, "" +object.getString("message"), Toast.LENGTH_SHORT).show();

                            Toast.makeText(RefereBySevika.this, "Referred successfully", Toast.LENGTH_SHORT).show();

                            if (Config._roleid == 21) {

                                Intent ii = new Intent(RefereBySevika.this, SevikaActivity.class);
                                startActivity(ii);
                                finish();
                            } else if (Config._roleid == 2) {

                                Intent ii = new Intent(RefereBySevika.this, ScreenerActivity.class);
                                startActivity(ii);
                                finish();
                            }

                        } else if (object.getString("status").equals("0")) {
                            Toast.makeText(RefereBySevika.this, "" + object.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    }


                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                Log.e("Response", "Response is Failure: ");
            }
        });


        // submitForm();


    }

//    void submitForm() {
//
////        HashMap<String,String> ReferField = new HashMap<String,String>();
////
////        ReferField.put("isUnrefer", "1");
////        ReferField.put("pstatus", "1");
////        ReferField.put("isInstant", "0");
////        ReferField.put("citizenId", "163163407014614260");
////        ReferField.put("status", "1");
////        ReferField.put("token", "dfjkhsdfaksjfh3756237");
//
//
//        String pattern = "yyyy-MM-dd";
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
//        String currentDate = simpleDateFormat.format(new Date());
//        ErrBox.errorsStatus();
//        HashMap<String, String> paramHash = new HashMap<String, String>();
//
//
//
//        paramHash.put("isUnrefer", "1");
//        paramHash.put("pstatus", "1");
//        paramHash.put("isInstant", "0");
//        paramHash.put("citizenId", citizenId);
//        paramHash.put("status", "1");
//        paramHash.put("token", token);
//
//        System.out.println("zzzzzzzzzzzzzzzzzzzzzz--SUBMIT FORM DATA:" + paramHash.toString());
//
//
//        RefereBySevika.SubmitForm req = new RefereBySevika.SubmitForm(this, paramHash);
//        req.execute(MyConfig.URL_Refer_by_Sevika);
//
//
//    }


//    class SubmitForm extends AsyncTask<String, Void, String> {
//        ProgressDialog progressDialog;
//        Activity activity;
//        HashMap<String, String> paramsHash = null;
//        RequestPipe requestPipe = new RequestPipe();
//
//        public SubmitForm(Activity activity, HashMap<String, String> paramsHash) {
//            this.activity = activity;
//            this.paramsHash = paramsHash;
//            progressDialog = new ProgressDialog(activity);
//            progressDialog.setMessage("loading");
//        }
//
//        @Override
//        protected void onPreExecute() {
//            progressDialog.show();
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//
//            return requestPipe.requestForm(params[0], paramsHash);
//        }
//
//        protected void onProgressUpdate(Void... progress) {
//            super.onProgressUpdate(progress);
//            //ProgressDialog.show(MainActivity.this, "loading", "wait...", true, true);
//        }
//
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//            progressDialog.cancel();
//            //Toast.makeText(MyConfig.CONTEXT,"RES:"+result,Toast.LENGTH_SHORT).show();
//            try {
//                JSONObject jsonObject = new JSONObject(result);
//                int respStatus = jsonObject.getInt("status");
//                if (respStatus == 1) {
//
//                    android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(RefereBySevika.this
//                    );
//                    alertDialog.setMessage(RefereBySevika.this.getString(R.string._generalSurveyAdd_he));
//                    alertDialog.setPositiveButton(RefereBySevika.this.getString(R.string._ok_he), new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int whichButton) {
//                            Intent i = new Intent(RefereBySevika.this, SevikaActivity.class);
//                            finish();
//                            startActivity(i);
//                        }
//                    });
//                    alertDialog.create();
//                    alertDialog.show();
//                    //Toast.makeText(MyConfig.CONTEXT, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
//                } else
//                    Toast.makeText(MyConfig.CONTEXT, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
//
//            } catch (Exception ee) {
//                Toast.makeText(MyConfig.CONTEXT, "Error !.", Toast.LENGTH_SHORT).show();
//            }
//
//        }
//
//    }


}


