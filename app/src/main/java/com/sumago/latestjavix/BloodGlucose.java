package com.sumago.latestjavix;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.sumago.latestjavix.Util.Config;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;


public class BloodGlucose extends AppCompatActivity {
    Button cmdSubmit = null;
    EditText bloodglucose;
    TextView txNotes;
    EditText etNotes;
    Spinner spnType;
    String GlucoseMsr = "";
    int intGlucoseMeasurement = 0;
    String[] courses = {"C", "Data structures",
            "Interview prep", "Algorithms",
            "DSA with java", "OS"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_glucose);
        initViews();
    }

    public void initViews() {
        MyConfig.CONTEXT = this;
        bloodglucose = (EditText) findViewById(R.id.etGlucose);
        txNotes = (TextView) findViewById(R.id.txNotes);
        etNotes = (EditText) findViewById(R.id.etNotes);
        spnType = (Spinner) findViewById(R.id.spnType);
        ArrayAdapter<CharSequence> adp2 = ArrayAdapter.createFromResource(this, R.array.strLipidType, android.R.layout.simple_spinner_item);
        adp2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnType.setAdapter(adp2);

        txNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etNotes.setVisibility(View.VISIBLE);
            }
        });

        cmdSubmit = (Button) findViewById(R.id.btnSubmit);
        cmdSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  //Toast.makeText(MyConfig.CONTEXT," HELLO",Toast.LENGTH_SHORT).show();
                if (spnType.getSelectedItem().toString().equalsIgnoreCase("Select Type")) {
                    Toast.makeText(MyConfig.CONTEXT, "Select Type", Toast.LENGTH_SHORT).show();

                    GlucoseMsr = bloodglucose.getText().toString().trim();

                    intGlucoseMeasurement = Integer.parseInt(bloodglucose.getText().toString().trim());
                }
                else{
                    submitForm(v);
                }*/

                if (!bloodglucose.getText().toString().trim().equalsIgnoreCase("")) {
                    intGlucoseMeasurement = Integer.parseInt(bloodglucose.getText().toString().trim());
                }


                if (validation()) {
                    submitForm(v);
                }
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

        paramHash.put("type", spnType.getSelectedItem().toString());
        paramHash.put("bloodglucose", bloodglucose.getText().toString());
        paramHash.put("status", "1");
        paramHash.put("ngoId", Config.NGO_ID);

        if (etNotes.getText().length() > 0) {
            paramHash.put("notes", etNotes.getText().toString());
        }
        paramHash.put("caseId", Config.tmp_caseId);
        if (Config.isOffline) {
            try {
                SQLiteDatabase db;
                db = openOrCreateDatabase("javixlife", Context.MODE_PRIVATE, null);

                JSONObject jsonObject = new JSONObject(paramHash);
                String SqlStr = "INSERT INTO javix_update(service_type,service_name,service_data,data_json,insert_date,_status)";
                SqlStr += " VALUES('Add Glucose','" + MyConfig.URL_GLUCOSETEST + "','" + paramHash.toString() + "','" + jsonObject.toString() + "','" + currentDate + "',0);";
                //Toast.makeText(getApplicationContext(), SqlStr, Toast.LENGTH_LONG).show();
                Log.e("Offline", SqlStr);
                db.execSQL(SqlStr);
                db.close();
                android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(BloodGlucose.this
                );
                alertDialog.setMessage("Glucose Test Done Successfully !");
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Intent i = new Intent(BloodGlucose.this, LabTestActivity.class);
                        //Config.tmp_caseId="0";
                        finish();
                        //startActivity(i);
                    }
                });
                alertDialog.create();
                alertDialog.show();

            } catch (Exception ex) {

                android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(BloodGlucose.this
                );
                alertDialog.setMessage("Error in saving data " + ex.getLocalizedMessage());
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Intent i = new Intent(BloodGlucose.this, LabTestActivity.class);
                        //Config.tmp_caseId="0";
                        finish();
                        //startActivity(i);
                    }
                });
                alertDialog.create();
                alertDialog.show();
            }

        } else {
            SubmitForm req = new SubmitForm(this, paramHash);
            req.execute(MyConfig.URL_GLUCOSETEST);
        }

    }

    //PUSH DATA
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

                    android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(BloodGlucose.this
                    );
                    alertDialog.setMessage("Glucose Test Done Successfully !");
                    alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            //Intent i = new Intent(BloodGlucose.this, LabTestActivity.class);
                            finish();
                            //startActivity(i);
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

    /* public void addReport() throws UnirestException {

         RequestParams params = new RequestParams();
         params.put("type", "Post Meal");
         params.put("bloodglucose",bloodglucose.getText());
         params.put("status", "1");
         params.put("caseId","161387331407139599");
         final int DEFAULT_TIMEOUT = 20 * 1000;
         AsyncHttpClient client = new AsyncHttpClient();
         client.setTimeout(DEFAULT_TIMEOUT);
         String url="http://143.244.136.145:3001/api/labtest/addBloodGlucoseTest";


         client.addHeader("content-type", "application/x-www-form-urlencoded");
         RequestHandle result=client.post(url, params, new TextHttpResponseHandler() {
             @Override
             public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                 System.out.println("Error ho gaya");
                 System.out.println(responseString.toString());
             }

             @Override
             public void onSuccess(int statusCode, Header[] headers, String responseString) {
                 System.out.println("Pass ho gaya");
                 byte[] mapData = responseString.getBytes();
                 System.out.println(responseString);
                 startActivity(new Intent(getApplicationContext(),MainActivity.class));
             }
         });

     }*/
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();

    }


    private boolean validation() {
        boolean VALID = true;

        Log.e("Response", "SPN: " + spnType.getSelectedItem().toString());
        if (spnType.getSelectedItem().toString().equalsIgnoreCase("Select Type")) {
            VALID = false;
            Toast.makeText(MyConfig.CONTEXT, "Select Type", Toast.LENGTH_SHORT).show();
        }

        Log.e("Response", "bloodglucose: " + intGlucoseMeasurement);
        if ((TextUtils.isEmpty(bloodglucose.getText().toString().trim()) || intGlucoseMeasurement < 30 || intGlucoseMeasurement > 700)) {
            VALID = false;
            bloodglucose.setError("Please enter range between 30 to 700");
        }
        return VALID;
    }
}