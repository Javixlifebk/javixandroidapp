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
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.sumago.latestjavix.Util.Config;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;


public class DrugTestActivity extends AppCompatActivity {
    Context context=null;
    HashMap<String,String> paramHash=new HashMap<String,String>();
    Hashtable<Integer,Rec> hashRadios=new Hashtable<Integer,Rec>();
    int radioGroups[]=null;
    String CASEID="";
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drug_test);
        context=this;
        CASEID= Config.tmp_caseId;
        hashRadios.put(R.id.Amphetamine,new Rec());
        hashRadios.put(R.id.Barbiturates,new Rec());
        hashRadios.put(R.id.Buprenorphine,new Rec());
        hashRadios.put(R.id.Benzodiazepines,new Rec());
        hashRadios.put(R.id.Cocaine,new Rec());
        hashRadios.put(R.id.Marijuana,new Rec());
        hashRadios.put(R.id.Methamphetamine,new Rec());
        hashRadios.put(R.id.Methylenedioxymethamphetamine,new Rec());
        hashRadios.put(R.id.Methadone,new Rec());
        hashRadios.put( R.id.Opiate,new Rec());
        hashRadios.put(R.id.Oxycodone,new Rec());
        hashRadios.put(R.id.Phencyclidine,new Rec());
        hashRadios.put(R.id.Propoxyphene,new Rec());
        hashRadios.put(R.id.TricyclicAntidepressant,new Rec());

        Button submit=(Button)findViewById(R.id.submitDrugTest);
        int radioGroups[]={ R.id.Amphetamine,
                            R.id.Barbiturates,
                R.id.Buprenorphine,
                R.id.Benzodiazepines,
                R.id.Cocaine,
                R.id.Marijuana,
                R.id.Methamphetamine,
                R.id.Methylenedioxymethamphetamine,
                R.id.Methadone,
                R.id.Opiate,
                R.id.Oxycodone,
                R.id.Phencyclidine,
                R.id.Propoxyphene,
                R.id.TricyclicAntidepressant
                          };
        this.radioGroups=radioGroups;
    }
    class Rec{ String val="3";}

    public void prepareParam(View v)
    {
        paramHash.clear();
        paramHash.put("caseId",CASEID);
        paramHash.put("status","1");
        String paramName[]={"Amphetamine",
                "Barbiturates",
                "Buprenorphine",
                "Benzodiazepines",
                "Cocaine",
                "Marijuana",
                "Methamphetamine",
                "Methylenedioxymethamphetamine",
                "Methadone",
                "Opiate",
                "Oxycodone",
                "Phencyclidine",
                "Propoxyphene",
                "TricyclicAntidepressant"};
        

        for(int i=0;i<radioGroups.length;i++) {
            RadioGroup groupAmphetamine = (RadioGroup) findViewById(radioGroups[i]); // Colonio

            int rID = groupAmphetamine.getCheckedRadioButtonId();
            RadioButton rad = (RadioButton)findViewById(rID);       // { postive , negative, ic, none }
            String val =rad.getText().toString();
            //Toast.makeText(getApplicationContext(),val,Toast.LENGTH_LONG).show();
            Rec rec = hashRadios.get(radioGroups[i]);

            if (val.equals("Positive")) {
                rec.val = "1";
            } else if (val.equals("Negative")) {
                rec.val = "0";
            } else if (val.equals("Inconclusive")) {
                rec.val = "2";
            }
            else {
                rec.val = "3";
            }
            System.out.println("-------- SELECTION:"+val+"-->"+i);
            paramHash.put(paramName[i],rec.val);
        } // loop
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String currentDate = simpleDateFormat.format(new Date());
        if(Config.isOffline){
            try {
                SQLiteDatabase db;
                db = openOrCreateDatabase("javixlife", Context.MODE_PRIVATE, null);

                JSONObject jsonObject=new JSONObject(paramHash);
                String SqlStr = "INSERT INTO javix_update(service_type,service_name,service_data,data_json,insert_date,_status)";
                SqlStr += " VALUES('Add Drug Test','http://143.244.136.145:3010/api/labtest/addDrugTest','" + paramHash.toString() + "','"+jsonObject.toString()+"','" + currentDate + "',0);";
                //Toast.makeText(getApplicationContext(), SqlStr, Toast.LENGTH_LONG).show();
                Log.e("Offline", SqlStr);
                db.execSQL(SqlStr);
                db.close();
                android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(DrugTestActivity.this
                );
                alertDialog.setMessage("Drug Test Done Successfully !" );
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        finish();

                    }
                });
                alertDialog.create();
                alertDialog.show();

            }catch (Exception ex){

                android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(DrugTestActivity.this
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
            req.execute("http://143.244.136.145:3010/api/labtest/addDrugTest");
        }
    } // function

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
                if(respStatus==1) {
                    //Toast.makeText(DrugTestActivity.this.context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(DrugTestActivity.this
                    );
                    alertDialog.setMessage("Drug Test Done Successfully !");
                    alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                           finish();

                        }
                    });
                    alertDialog.create();
                    alertDialog.show();

                }
                else Toast.makeText(DrugTestActivity.this.context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

            }catch (Exception ee){
                Toast.makeText(DrugTestActivity.this.context, "Opps !.", Toast.LENGTH_SHORT).show();
            }

        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // your code
            Log.e("Back Button","Pressed");
            finish();

            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
    // ENd SUbmit Form

} // class