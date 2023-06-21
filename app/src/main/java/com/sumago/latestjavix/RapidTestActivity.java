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

public class RapidTestActivity extends AppCompatActivity {
    Context context=null;
    HashMap<String,String> paramHash=new HashMap<String,String>();
    Hashtable<Integer, RapidTestActivity.Rec> hashRadios=new Hashtable<Integer, RapidTestActivity.Rec>();
    int radioGroups[]=null;
    //String CASEID="161797221852720888";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rapid_test);
        context=this;
        //CASEID= Config.tmp_caseId;
        hashRadios.put(R.id.ChagasAb,new RapidTestActivity.Rec());
        hashRadios.put(R.id.Chikungunya,new RapidTestActivity.Rec());
        hashRadios.put(R.id.Chlamydia,new RapidTestActivity.Rec());
        hashRadios.put(R.id.Cholera,new RapidTestActivity.Rec());
        hashRadios.put(R.id.Dengue,new RapidTestActivity.Rec());
        hashRadios.put(R.id.FecalOccultBloodTest,new RapidTestActivity.Rec());
        hashRadios.put(R.id.HPylori,new RapidTestActivity.Rec());
        hashRadios.put(R.id.HantaanVirus,new RapidTestActivity.Rec());
        hashRadios.put(R.id.HepatitisA,new RapidTestActivity.Rec());
        hashRadios.put( R.id.HepatitisB,new RapidTestActivity.Rec());
        hashRadios.put(R.id.HepatitisC,new RapidTestActivity.Rec());
        hashRadios.put(R.id.HIV,new RapidTestActivity.Rec());
        hashRadios.put(R.id.HumanAfricanTrypanosomiasis,new RapidTestActivity.Rec());
        hashRadios.put(R.id.Influenza,new RapidTestActivity.Rec());
        hashRadios.put(R.id.LegionellaAg,new RapidTestActivity.Rec());
        hashRadios.put(R.id.Malaria,new RapidTestActivity.Rec());
        hashRadios.put(R.id.Myoglobin,new RapidTestActivity.Rec());
        hashRadios.put(R.id.Norovirus,new RapidTestActivity.Rec());
        hashRadios.put(R.id.OnchoLFlgG4Biplex,new RapidTestActivity.Rec());
        hashRadios.put(R.id.RespiratorySynctialVirus,new RapidTestActivity.Rec());
        hashRadios.put(R.id.RotaAdeno,new RapidTestActivity.Rec());
        hashRadios.put(R.id.Covid,new RapidTestActivity.Rec());
        hashRadios.put(R.id.StrepA,new RapidTestActivity.Rec());
        hashRadios.put(R.id.Syphilis,new RapidTestActivity.Rec());
        hashRadios.put(R.id.Tetanus,new RapidTestActivity.Rec());
        hashRadios.put(R.id.Troponin,new RapidTestActivity.Rec());
        hashRadios.put(R.id.Tsutsugamushi,new RapidTestActivity.Rec());
        hashRadios.put(R.id.Tuberculosis,new RapidTestActivity.Rec());
        hashRadios.put(R.id.TyphoidFever,new RapidTestActivity.Rec());
        hashRadios.put(R.id.YellowFever,new RapidTestActivity.Rec());
        hashRadios.put(R.id.Salmonella,new RapidTestActivity.Rec());
        hashRadios.put(R.id.Others,new RapidTestActivity.Rec());
        Button submit=(Button)findViewById(R.id.submitDrugTest);
        int radioGroups[]={ R.id.ChagasAb,
                R.id.Chikungunya,
                R.id.Chlamydia,
                R.id.Cholera,
                R.id.Dengue,
                R.id.FecalOccultBloodTest,
                R.id.HPylori,
                R.id.HantaanVirus,
                R.id.HepatitisA,
                R.id.HepatitisB,
                R.id.HepatitisC,
                R.id.HIV,
                R.id.HumanAfricanTrypanosomiasis,
                R.id.Influenza,
                R.id.LegionellaAg,
                R.id.Malaria,
                R.id.Myoglobin,
                R.id.Norovirus,
                R.id.OnchoLFlgG4Biplex,
                R.id.RespiratorySynctialVirus,
                R.id.RotaAdeno,
                R.id.Covid,
                R.id.StrepA,
                R.id.Syphilis,
                R.id.Tetanus,
                R.id.Troponin,
                R.id.Tsutsugamushi,
                R.id.Tuberculosis,
                R.id.TyphoidFever,
                R.id.YellowFever,
                R.id.Salmonella,
                R.id.Others
        };
        this.radioGroups=radioGroups;
    }
    class Rec{ String val="3";}
    public void prepareParam(View v)
    {
        paramHash.clear();
        paramHash.put("caseId", Config.tmp_caseId);
        paramHash.put("ngoId", Config.NGO_ID);
        paramHash.put("status","1");
        String paramName[]={"ChagasAb",
                "Chikungunya",
                "Chlamydia",
                "Cholera",
                "Dengue",
                "FecalOccultBloodTest",
                "HPylori",
                "HantaanVirus",
                "HepatitisA",
                "HepatitisB",
                "HepatitisC",
                "HIV",
                "HumanAfricanTrypanosomiasis",
                "Influenza",
                "LegionellaAg",
                "Malaria",
                "Myoglobin",
                "Norovirus",
                "OnchoLFlgG4Biplex",
                "RespiratorySynctialVirus",
                "RotaAdeno",
                "Covid",
                "StrepA",
                "Syphilis",
                "Tetanus",
                "Troponin",
                "Tsutsugamushi",
                "Tuberculosis",
                "TyphoidFever",
                "YellowFever",
                "Salmonella",
                "Others"
        };

        for(int i=0;i<radioGroups.length;i++) {
            RadioGroup groupAmphetamine = (RadioGroup) findViewById(radioGroups[i]); // Colonio

            int rID = groupAmphetamine.getCheckedRadioButtonId();
            RadioButton rad = (RadioButton)findViewById(rID);       // { postive , negative, ic, none }
            String val =rad.getText().toString();
            RapidTestActivity.Rec rec = hashRadios.get(radioGroups[i]);

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
                SqlStr += " VALUES('Add RapidTest','http://143.244.136.145:3010/api/labtest/addLabTest','" + paramHash.toString() + "','"+jsonObject.toString()+"','" + currentDate + "',0);";
//                SqlStr += " VALUES('Add RapidTest','http://192.168.1.195:3010/api/labtest/addLabTest','" + paramHash.toString() + "','"+jsonObject.toString()+"','" + currentDate + "',0);";
                //Toast.makeText(getApplicationContext(), SqlStr, Toast.LENGTH_LONG).show();
                Log.e("Offline", SqlStr);
                db.execSQL(SqlStr);
                db.close();
                android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(RapidTestActivity.this
                );
                alertDialog.setMessage("Rapid Test Done Successfully !" );
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        finish();

                    }
                });
                alertDialog.create();
                alertDialog.show();

            }catch (Exception ex){

                android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(RapidTestActivity.this
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
            RapidTestActivity.SubmitForm req = new RapidTestActivity.SubmitForm(this, paramHash);
            req.execute("http://143.244.136.145:3010/api/labtest/addLabTest");
        }
    }
    class SubmitForm extends AsyncTask<String, Void, String>{
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
                    //Toast.makeText(RapidTestActivity.this.context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(RapidTestActivity.this
                    );
                    alertDialog.setMessage("Rapid Test Done Successfully !");
                    alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                           finish();

                        }
                    });
                    alertDialog.create();
                    alertDialog.show();
                }
                else Toast.makeText(RapidTestActivity.this.context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

            }catch (Exception ee){
                Toast.makeText(RapidTestActivity.this.context, "Opps !.", Toast.LENGTH_SHORT).show();
            }

        }

    }
    // ENd SUbmit Form
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();

    }
}