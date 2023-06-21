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
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.sumago.latestjavix.Util.Config;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class AddThalassemia extends AppCompatActivity {
    Button cmdSubmit=null;
    EditText etNotes;
    Spinner spnType;
    TextView txNotes;
    RadioGroup rdo_type;
    RadioButton jradx_10001,jradx_10002,jradx_10003,jradx_10004;
    String strType="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_thalassemia);
        initViews();
    }

    public void initViews(){
        MyConfig.CONTEXT=this;
        txNotes=(TextView) findViewById(R.id.txNotes);
        etNotes=(EditText)findViewById(R.id.etNotes);
        spnType=(Spinner)findViewById(R.id.spnType);
        rdo_type=(RadioGroup)findViewById(R.id.rdo_type);
        jradx_10001=(RadioButton)findViewById(R.id.jradx_10001);
        jradx_10002=(RadioButton)findViewById(R.id.jradx_10002);
        jradx_10003=(RadioButton)findViewById(R.id.jradx_10003);
        jradx_10004=(RadioButton)findViewById(R.id.jradx_10004);

        cmdSubmit=(Button)findViewById(R.id.btnSubmit);
        cmdSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(MyConfig.CONTEXT," HELLO",Toast.LENGTH_SHORT).show();
                if (jradx_10001.isChecked()) {
                    strType = jradx_10001.getText().toString();
                } else if (jradx_10002.isChecked()) {
                    strType = jradx_10002.getText().toString();
                } else if (jradx_10003.isChecked()) {
                    strType = jradx_10003.getText().toString();
                } else if (jradx_10004.isChecked()) {
                    strType = jradx_10004.getText().toString();
                }

                if(strType.length()>0){
                    submitForm(v);
                }else{
                    Toast.makeText(MyConfig.CONTEXT,"Please Select !",Toast.LENGTH_SHORT).show();
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

    void submitForm(View v){
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String currentDate = simpleDateFormat.format(new Date());
        ErrBox.errorsStatus();
        HashMap<String,String> paramHash=new HashMap<String,String>();
        int _varCell=0;
        switch (strType){
            case "Positive":
                _varCell=1;
                break;
            case "Negative":
                _varCell=0;
                break;
            case "Inconclusive":
                _varCell=2;
                break;
            case "None":
                _varCell=3;
                break;
            default:
                _varCell=3;

        }
        paramHash.put("Thalassemia", Integer.toString(_varCell));
        if(etNotes.getText().length()>0 ){
            paramHash.put("notes",etNotes.getText().toString());
        }
        paramHash.put("status", "1");
        paramHash.put("caseId", Config.tmp_caseId);
        paramHash.put("ngoId", Config.NGO_ID);

        if(Config.isOffline){
            try {
                SQLiteDatabase db;
                db = openOrCreateDatabase("javixlife", Context.MODE_PRIVATE, null);

                JSONObject jsonObject=new JSONObject(paramHash);
                String SqlStr = "INSERT INTO javix_update(service_type,service_name,service_data,data_json,insert_date,_status)";
                SqlStr += " VALUES('Add Thalassemia','" + MyConfig.URL_ADDTHALASSEMIA + "','" + paramHash.toString() + "','"+jsonObject.toString()+"','" + currentDate + "',0);";
                //Toast.makeText(getApplicationContext(), SqlStr, Toast.LENGTH_LONG).show();
                Log.e("Offline", SqlStr);
                db.execSQL(SqlStr);
                db.close();
                android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(AddThalassemia.this
                );
                alertDialog.setMessage("Thalassemia Test Done Successfully !" );
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        finish();

                    }
                });
                alertDialog.create();
                alertDialog.show();

            }catch (Exception ex){

                android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(AddThalassemia.this
                );
                alertDialog.setMessage("Error in saving data " + ex.getLocalizedMessage());
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Config.tmp_caseId="0";
                        finish();

                    }
                });
                alertDialog.create();
                alertDialog.show();
            }

        }else {
            SubmitForm req = new SubmitForm(this, paramHash);
            req.execute(MyConfig.URL_ADDTHALASSEMIA);
        }

    }
    //PUSH DATA
    class SubmitForm extends AsyncTask<String, Void, String> {
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
        protected void onPreExecute(){
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
                if(respStatus==1) {

                    android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(AddThalassemia.this
                    );
                    alertDialog.setMessage("Thalassemia Test Done Successfully !");
                    alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            finish();

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
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();

    }
}