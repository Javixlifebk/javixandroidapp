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

public class LipidPanelActivity extends AppCompatActivity {
    Spinner spnType;
    EditText etCholesterol,etHdlCholesterol,etTriglycerides,etGlucose;
    TextView txLDL,txTCHDL,txLDLHDL,txNonHDL;
    double _ldl=0.00,_tclhdl=0.00,_ldlhdl=0.00,_nonhdl=0.00;
    int _flag1=0,_flag2=0;
    Button cmdSubmit=null;
    boolean mtCholesterol=false,mtHdlCholesterol=false,mtTriglycerides=false,mtGlucose=false,mType=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lipid_panel);
        initView();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();

    }
    public  void initView(){
        MyConfig.CONTEXT=this;
        spnType=(Spinner)findViewById(R.id.spnType);
        etCholesterol=(EditText)findViewById(R.id.etCholesterol);
        etHdlCholesterol=(EditText)findViewById(R.id.etHdlCholesterol);
        etTriglycerides=(EditText)findViewById(R.id.etTriglycerides);
        etGlucose=(EditText)findViewById(R.id.etGlucose);
        txLDL=(TextView)findViewById(R.id.txLDL);
        txTCHDL=(TextView)findViewById(R.id.txTCHDL);
        txLDLHDL=(TextView)findViewById(R.id.txLDLHDL);
        txNonHDL=(TextView)findViewById(R.id.txNonHDL);
        ArrayAdapter<CharSequence> adp2= ArrayAdapter.createFromResource(this, R.array.strLipidType, android.R.layout.simple_spinner_item);
        adp2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnType.setAdapter(adp2);
        etCholesterol.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before,
                                          int count) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before,
                                      int count) {
                if(etCholesterol.getText().length()>0 && etHdlCholesterol.getText().length()>0) {
                    _flag1=1;
                    CalculateLipid();
                }else{
                    txTCHDL.setText("--");
                    txNonHDL.setText("--");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        etHdlCholesterol.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before,
                                          int count) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before,
                                      int count) {
                if(etHdlCholesterol.getText().length()>0 && etCholesterol.getText().length()>0) {
                    _flag1=1;
                    CalculateLipid();
                }else{
                    txTCHDL.setText("--");
                    txNonHDL.setText("--");
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        etTriglycerides.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before,
                                          int count) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before,
                                      int count) {
                if(etTriglycerides.getText().length()>0) {
                    _flag2=1;
                    CalculateLipid();
                }else{
                    txLDL.setText("--");
                    txLDLHDL.setText("--");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        cmdSubmit=(Button)findViewById(R.id.btnSubmit);
        cmdSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(spnType.getSelectedItem().toString().equalsIgnoreCase("Select Type")){
                    Toast.makeText(MyConfig.CONTEXT,"Select Meal Type",Toast.LENGTH_SHORT).show();
                    mType=false;
                }else{
                    mType=true;
                }
                if(mType==true) {
                    if (etCholesterol.getText().length() == 0) {
                        Toast.makeText(MyConfig.CONTEXT, "Choesterol Can't be Blank", Toast.LENGTH_SHORT).show();
                        mtCholesterol = false;
                    } else {
                        mtCholesterol = true;
                    }
                }
                if(mtCholesterol==true){
                    if(etHdlCholesterol.getText().length()==0){
                        Toast.makeText(MyConfig.CONTEXT,"HDL Choesterol Can't be Blank",Toast.LENGTH_SHORT).show();
                        mtHdlCholesterol=false;
                    }else{
                        mtHdlCholesterol=true;
                    }
                }
                if(mtHdlCholesterol==true){
                    if(etTriglycerides.getText().length()==0){
                        Toast.makeText(MyConfig.CONTEXT,"Triglycerides Can't be Blank",Toast.LENGTH_SHORT).show();
                        mtTriglycerides=false;
                    }else{
                        mtTriglycerides=true;
                    }
                }
                if(mtTriglycerides==true){
                    if(etGlucose.getText().length()==0){
                        Toast.makeText(MyConfig.CONTEXT,"Glucose Can't be Blank",Toast.LENGTH_SHORT).show();
                        mtGlucose=false;
                    }else{
                        mtGlucose=true;
                    }
                }

                if(mtGlucose==true){
                    //Toast.makeText(MyConfig.CONTEXT," HELLO",Toast.LENGTH_SHORT).show();
                    submitForm(v);
                }
                //Toast.makeText(MyConfig.CONTEXT," HELLO",Toast.LENGTH_SHORT).show();
               /* if(etCholesterol.getText().length()==0 && etHdlCholesterol.getText().length()==0 && etTriglycerides.getText().length()==0 && etGlucose.getText().length()==0){
                    Toast.makeText(getApplicationContext(),"All the fields are mendator  !",Toast.LENGTH_SHORT).show();
                }else {
                    submitForm(v);
                }*/
            }
        });
    }
    public void CalculateLipid(){
        double _cho=0.00,_hdl=0.00,_trio=0.00,_glue=0.00;
        if(_flag1==1) {
            _cho = Double.parseDouble(etCholesterol.getText().toString());
            _hdl = Double.parseDouble(etHdlCholesterol.getText().toString());
            if(_cho>0 && _hdl>0) {
                _tclhdl = _cho / _hdl;
                _nonhdl = _cho - _hdl;
                txTCHDL.setText(String.format("%.2f", _tclhdl));
                txNonHDL.setText(String.format("%.2f", _nonhdl));
            }else if(_cho>=0){
                _nonhdl=_cho;
                txNonHDL.setText(String.format("%.2f", _nonhdl));
            }
            else{
                txTCHDL.setText("--");
                txNonHDL.setText("--");
            }
        }
        if(_flag2==1) {
        _trio=Double.parseDouble(etTriglycerides.getText().toString());
        //_glue=Double.parseDouble(etGlucose.getText().toString());
        if(_trio>0) {
            _ldl = _nonhdl - _trio / 5;
            _ldlhdl = _ldl / _hdl;
            txLDL.setText(String.format("%.2f",_ldl));
            txLDLHDL.setText(String.format("%.2f",_ldlhdl));
        }
        }
    }

    void submitForm(View v){
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String currentDate = simpleDateFormat.format(new Date());
        ErrBox.errorsStatus();
        // Toast.makeText(MyConfig.CONTEXT,""+ ErrBox.FIRST_ERR_MSG,Toast.LENGTH_SHORT).show();
        HashMap<String,String> paramHash=new HashMap<String,String>();
        paramHash.put("caseId", Config.tmp_caseId);
        paramHash.put("type", spnType.getSelectedItem().toString());
        paramHash.put("status", "1");
        paramHash.put("cholesterol", etCholesterol.getText().toString());
        paramHash.put("hdlcholesterol", etHdlCholesterol.getText().toString());
        paramHash.put("triglycerides", etTriglycerides.getText().toString());
        paramHash.put("glucose", etGlucose.getText().toString());
        paramHash.put("ldl", txLDL.getText().toString());
        paramHash.put("tcl_hdl", txTCHDL.getText().toString());
        paramHash.put("ldl_hdl", txLDLHDL.getText().toString());
        paramHash.put("non_hdl", txNonHDL.getText().toString());
        paramHash.put("ngoId", Config.NGO_ID);

        if(Config.isOffline){
            try {
                SQLiteDatabase db;
                db = openOrCreateDatabase("javixlife", Context.MODE_PRIVATE, null);

                JSONObject jsonObject=new JSONObject(paramHash);
                String SqlStr = "INSERT INTO javix_update(service_type,service_name,service_data,data_json,insert_date,_status)";
                SqlStr += " VALUES('Add Lipid','" + MyConfig.URL_LIPIDTEST + "','" + paramHash.toString() + "','"+jsonObject.toString()+"','" + currentDate + "',0);";
                //Toast.makeText(getApplicationContext(), SqlStr, Toast.LENGTH_LONG).show();
                Log.e("Offline", SqlStr);
                db.execSQL(SqlStr);
                db.close();
                android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(LipidPanelActivity.this
                );
                alertDialog.setMessage("Lipid Test Done Successfully !" );
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        finish();

                    }
                });
                alertDialog.create();
                alertDialog.show();

            }catch (Exception ex){

                android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(LipidPanelActivity.this
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

        }else {
            SubmitForm req = new SubmitForm(this, paramHash);
            req.execute(MyConfig.URL_LIPIDTEST);
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
                if(respStatus==1) {
                    //Intent i = new Intent(BloodGlucose.this, ScreeningDoneSuccessfully.class);
                    //finish();
                    //startActivity(i);
                    //Toast.makeText(MyConfig.CONTEXT, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(LipidPanelActivity.this
                    );
                    alertDialog.setMessage("Lipid Testing Done Successfully !");
                    alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            finish();

                        }
                    });
                    alertDialog.create();
                    alertDialog.show();

                }
                else Toast.makeText(MyConfig.CONTEXT, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

            }catch (Exception ee){
                Toast.makeText(MyConfig.CONTEXT, "Opps !.", Toast.LENGTH_SHORT).show();
            }

        }

    }


}