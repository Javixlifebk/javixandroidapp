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
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.sumago.latestjavix.Util.Config;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class HemoglobinActivity extends AppCompatActivity {
    Button cmdSubmit = null;
    EditText etNotes, etHemo;
    Spinner spnType;
    TextView txNotes;
    double intHemoglobinMsr = 0.0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hemoglobin);
        initViews();
    }

    public void initViews() {
        MyConfig.CONTEXT = this;
        etHemo = (EditText) findViewById(R.id.etHemo);
        etNotes = (EditText) findViewById(R.id.etNotes);
        txNotes = (TextView) findViewById(R.id.txNotes);

        cmdSubmit = (Button) findViewById(R.id.btnSubmit);
        cmdSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
 /* //Toast.makeText(MyConfig.CONTEXT," HELLO",Toast.LENGTH_SHORT).show();
 if(etHemo.getText().length()==0){
 Toast.makeText(MyConfig.CONTEXT,"Hemoglobin Can't be Blank",Toast.LENGTH_SHORT).show();
 }else{submitForm(v);}*/

                if (!etHemo.getText().toString().trim().equalsIgnoreCase("")) {
//                    intHemoglobinMsr = Integer.parseInt(etHemo.getText().toString().trim());
                    intHemoglobinMsr = Float.parseFloat(etHemo.getText().toString().trim());
                }
                if (validation()) {

                    submitForm(v);
                }


            }
        });

        txNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etNotes.setVisibility(View.VISIBLE);
            }
        });
    }

    void submitForm(View v) {
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String currentDate = simpleDateFormat.format(new Date());


        ErrBox.errorsStatus();
        // Toast.makeText(MyConfig.CONTEXT,""+ ErrBox.FIRST_ERR_MSG,Toast.LENGTH_SHORT).show();

        HashMap<String, String> paramHash = new HashMap<String, String>();
        paramHash.put("hemoglobin", etHemo.getText().toString());
        if (etNotes.getText().length() > 0) {
            paramHash.put("notes", etNotes.getText().toString());
        }
//        paramHash.put("screenerId", Config._screenerid);
        paramHash.put("screenerId", Config.javixid);
        paramHash.put("citizenId", Config.tmp_citizenId);
        paramHash.put("caseId", Config.tmp_caseId);
        paramHash.put("ngoId", Config.NGO_ID);

        Log.e("Atul", "params: " + paramHash);

        if (Config.isOffline) {
            try {
                SQLiteDatabase db;
                db = openOrCreateDatabase("javixlife", Context.MODE_PRIVATE, null);

                JSONObject jsonObject = new JSONObject(paramHash);
                String SqlStr = "INSERT INTO javix_update(service_type,service_name,service_data,data_json,insert_date,_status)";
                SqlStr += " VALUES('Add Hemoglobin','" + MyConfig.URL_ADDHEMOGLOBIN + "','" + paramHash.toString() + "','" + jsonObject.toString() + "','" + currentDate + "',0);";
                //Toast.makeText(getApplicationContext(), SqlStr, Toast.LENGTH_LONG).show();
                Log.e("Offline", SqlStr);
                db.execSQL(SqlStr);
                db.close();
                android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(HemoglobinActivity.this
                );
                alertDialog.setMessage("Hemoglobin Test Done Successfully !");
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        finish();

                    }
                });
                alertDialog.create();
                alertDialog.show();

            } catch (Exception ex) {

                android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(HemoglobinActivity.this
                );
                alertDialog.setMessage("Error in saving data " + ex.getLocalizedMessage());
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        finish();

                    }
                });
                alertDialog.create();
                alertDialog.show();
            }

        } else {
            HemoglobinActivity.SubmitForm req = new HemoglobinActivity.SubmitForm(this, paramHash);
            Log.e("Atul", "paramHash: " +paramHash.toString());
            req.execute(MyConfig.URL_ADDHEMOGLOBIN);
        }

    }

    class SubmitForm extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        Activity activity;
        HashMap<String, String> paramsHash = null;
        RequestPipe requestPipe = new RequestPipe();

        public SubmitForm(Activity activity, HashMap<String, String> paramsHash) {
            this.activity = activity;
            this.paramsHash = paramsHash;
            progressDialog = new ProgressDialog(activity);
            progressDialog.setMessage("loading");
        }

        @Override
        protected void onPreExecute() {
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            return requestPipe.requestForm(params[0], paramsHash);
        }

        protected void onProgressUpdate(Void... progress) {
            super.onProgressUpdate(progress);
            //ProgressDialog.show(MainActivity.this, "loading", "wait...", true, true);

        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.cancel();
            //Toast.makeText(MyConfig.CONTEXT,"RES:"+result,Toast.LENGTH_SHORT).show();
            try {
                JSONObject jsonObject = new JSONObject(result);
                int respStatus = jsonObject.getInt("status");
                if (respStatus == 1) {

                    android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(HemoglobinActivity.this
                    );
                    alertDialog.setMessage("Hemoglobin Test Done Successfully !");
                    alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            finish();

                        }
                    });
                    alertDialog.create();
                    alertDialog.show();


                    //Toast.makeText(MyConfig.CONTEXT, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(MyConfig.CONTEXT, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

            } catch (Exception ee) {
                Toast.makeText(MyConfig.CONTEXT, "Opps !.", Toast.LENGTH_SHORT).show();
            }

        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();

    }

    private boolean validation() {
        boolean VALID = true;

        Log.e("Respons", "etHemo " + etHemo.getText().toString());
        Log.e("Respons", "intHemoglobinMsr: " + intHemoglobinMsr);
        if (TextUtils.isEmpty(etHemo.getText().toString().trim()) || intHemoglobinMsr < 3 || intHemoglobinMsr > 24) {
            VALID = false;
            etHemo.setError("Please enter range between 3 to 24");
        }
        return VALID;
    }
}