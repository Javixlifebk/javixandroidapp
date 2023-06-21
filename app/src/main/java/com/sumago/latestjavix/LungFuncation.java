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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
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

public class LungFuncation extends AppCompatActivity {
    Button cmdSubmit=null;
    EditText etfvc_predicted,etfvc_actual,etfvc_percent,etfev1_predicted,etfev1_actual,etfev1_percent;
    EditText edtfvc1_predicted,etfvc1_actual,etfvc1_percent,etpef_predicted,etpef_actual,etpef_percent,etNotes;
    String ScreenerId="161315136114567577",CitizenId="";
    TextView txNotes,etfvc,etfev1,edfvc1,etpef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lung_funcation);
        MyConfig.CONTEXT=this;
        initViews();
    }

    public void initViews(){
        //etHeight=(EditText)findViewById(R.id.etHeight);
        etfvc=(TextView)findViewById(R.id.etfvc);
        etfvc_predicted=(EditText)findViewById(R.id.etfvc_predicted);
        etfvc_actual=(EditText)findViewById(R.id.etfvc_actual);
        etfvc_percent=(EditText)findViewById(R.id.etfvc_percent);

        etfev1=(TextView)findViewById(R.id.etfev1);
        etfev1_predicted=(EditText)findViewById(R.id.etfev1_predicted);
        etfev1_actual=(EditText)findViewById(R.id.etfev1_actual);
        etfev1_percent=(EditText)findViewById(R.id.etfev1_percent);

        edfvc1=(TextView)findViewById(R.id.edfvc1);
        edtfvc1_predicted=(EditText)findViewById(R.id.edtfvc1_predicted);
        etfvc1_actual=(EditText)findViewById(R.id.etfvc1_actual);
        etfvc1_percent=(EditText)findViewById(R.id.etfvc1_percent);

        etpef=(TextView)findViewById(R.id.etpef);
        etpef_predicted=(EditText)findViewById(R.id.etpef_predicted);
        etpef_actual=(EditText)findViewById(R.id.etpef_actual);
        etpef_percent=(EditText)findViewById(R.id.etpef_percent);

        Bundle bundle=getIntent().getExtras();
        ScreenerId= Config._screenerid;
        CitizenId=Config.tmp_citizenId;
        txNotes=(TextView) findViewById(R.id.txNotes);
        etNotes=(EditText) findViewById(R.id.etNotes);
        cmdSubmit=(Button)findViewById(R.id.addScreenerProfile_submit);

        txNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etNotes.setVisibility(View.VISIBLE);
            }
        });

        cmdSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitForm(v);
            }
        });

    }

    void submitForm(View v)
    {
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String currentDate = simpleDateFormat.format(new Date());

        ErrBox.errorsStatus();
        // Toast.makeText(MyConfig.CONTEXT,""+ ErrBox.FIRST_ERR_MSG,Toast.LENGTH_SHORT).show();

        HashMap<String,String> paramHash=new HashMap<String,String>();
        paramHash.put("caseId",Config.tmp_caseId);
        paramHash.put("status","1");

        paramHash.put("fvc_predicted",etfvc_predicted.getText().toString());
        paramHash.put("fvc_actual",etfvc_actual.getText().toString());
        paramHash.put("fvc_predicted_percent",etfvc_percent.getText().toString());
        paramHash.put("fev1_predicted",etfev1_predicted.getText().toString());
        paramHash.put("fev1_actual",etfev1_actual.getText().toString());
        paramHash.put("fev1_predicted_percent",etfev1_percent.getText().toString());

        paramHash.put("fvc1_predicted",edtfvc1_predicted.getText().toString());
        paramHash.put("fvc1_actual",etfvc1_actual.getText().toString());
        paramHash.put("fvc1_predicted_percent",etfvc1_percent.getText().toString());
        paramHash.put("pef_predicted",etpef_predicted.getText().toString());
        paramHash.put("pef_actual",etpef_actual.getText().toString());
        paramHash.put("pef_predicted_percent",etpef_percent.getText().toString());
        paramHash.put("ngoId", Config.NGO_ID);

        if(etNotes.getText().length()>0 ){
            paramHash.put("notes",etNotes.getText().toString());
        }

        if(Config.isOffline){
            try {
                SQLiteDatabase db;
                db = openOrCreateDatabase("javixlife", Context.MODE_PRIVATE, null);

                JSONObject jsonObject=new JSONObject(paramHash);
                String SqlStr = "INSERT INTO javix_update(service_type,service_name,service_data,data_json,insert_date,_status)";
                SqlStr += " VALUES('Add Lung Function','" + MyConfig.URL_ADDLUNGTEST + "','" + paramHash.toString() + "','"+jsonObject.toString()+"','" + currentDate + "',0);";
                //Toast.makeText(getApplicationContext(), SqlStr, Toast.LENGTH_LONG).show();
                Log.e("Offline", SqlStr);
                db.execSQL(SqlStr);
                db.close();
                android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(LungFuncation.this
                );
                alertDialog.setMessage("Lung Function Test Done Successfully !" );
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Intent i = new Intent(LungFuncation.this, TestListActivity.class);
                        //Config.tmp_caseId="0";
                        finish();
                        startActivity(i);
                    }
                });
                alertDialog.create();
                alertDialog.show();

            }catch (Exception ex){

                android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(LungFuncation.this
                );
                alertDialog.setMessage("Error in saving data " + ex.getLocalizedMessage());
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Intent i = new Intent(LungFuncation.this, LabTestActivity.class);
                        //Config.tmp_caseId="0";
                        finish();
                        startActivity(i);
                    }
                });
                alertDialog.create();
                alertDialog.show();
            }

        }else {
            SubmitForm req = new SubmitForm(this, paramHash);
            req.execute(MyConfig.URL_ADDLUNGTEST);
        }

    }
    //PUSH DATA
    class SubmitForm extends AsyncTask<String, Void, String>
    {
        ProgressDialog progressDialog;
        Activity activity;
        HashMap<String, String> paramsHash=null;
        RequestPipe requestPipe=new RequestPipe();
        public SubmitForm(Activity activity,HashMap<String, String> paramsHash) {
            this.activity=activity;
            this.paramsHash=paramsHash;
            progressDialog = new ProgressDialog(activity);
            progressDialog.setMessage("loading");
        }
        @Override
        protected void onPreExecute()
        {
            progressDialog.show();
        }
        @Override
        protected String doInBackground(String... params) {

            return requestPipe.requestForm(params[0],paramsHash);
        }
        protected void onProgressUpdate(Void ...progress) {
            super.onProgressUpdate(progress);
            //ProgressDialog.show(MainActivity.this, "loading", "wait...", true, true);

        }
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.cancel();
            //Toast.makeText(MyConfig.CONTEXT,"RES:"+result,Toast.LENGTH_SHORT).show();
            try {
                JSONObject jsonObject = new JSONObject(result);
                int respStatus=jsonObject.getInt("status");
                JSONObject recsData=jsonObject.getJSONObject("data");
                recsData=recsData.getJSONObject("data");
                Log.e("Response in Array",recsData.toString());

                if(respStatus==1) {

                    Config.tmp_caseId=recsData.getString("caseId");
                    Log.e("CaseID",Config.tmp_caseId);
                    android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(LungFuncation.this
                    );
                    alertDialog.setMessage("Lung Function Test Done Successfully !");
                    alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            Intent i = new Intent(LungFuncation.this, TestListActivity.class);
                            //Config.tmp_caseId="0";
                            finish();
                            startActivity(i);
                        }
                    });
                    alertDialog.create();
                    alertDialog.show();

                    //Toast.makeText(MyConfig.CONTEXT, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                }
                else Toast.makeText(MyConfig.CONTEXT, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

            }catch (Exception ee){
                Toast.makeText(MyConfig.CONTEXT, "Opps !.", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // your code
            Log.e("Back Button","Pressed");
            Intent i = new Intent(LungFuncation.this, TestListActivity.class);
            finish();
            startActivity(i);
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}