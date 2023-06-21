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
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.sumago.latestjavix.Util.Config;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;


public class symptomslist extends AppCompatActivity {

    int checkBox[]={
                R.id.nodeChecbox,
                R.id.coughChecbox,
                R.id.phlegmChecbox,
                R.id.feverChecbox,
                R.id.eyeChecbox,
                R.id.rashChecbox,
                R.id.abdoChecbox,
                R.id.activityChecbox,
                R.id.earChecbox,
                R.id.earDCChecbox,
                R.id.throatChecbox,
                R.id.vomitingChecbox,
                R.id.diarrheaChecbox,
                R.id.fdsolidChecbox,
                R.id.fdliquidChecbox,
                R.id.appetiteChecbox,
                R.id.sleepChecbox,
                R.id.bteethChecbox,
                R.id.dentVisitChecbox,
                R.id.stoolChecbox,
                R.id.urineChecbox,
                R.id.smokeDetectorChecbox,
                R.id.helmetChecbox,
                R.id.beltChecbox,
                R.id.asthamaChecbox



    };
    String checkBoxLabel[]={
                "Runny Nose",
                "Cough",
                "Phlegm",
                "Fever",
                "Eye Discharge",
                "Rash",
                "Abdominal Pain",
                "Activity",
                "Ear Pain",
                "Ear D/C",
                "Throat Pain",
                "Vomiting",
                "Diarrhea",
                "Food intake solid",
                "Food intake liquid",
                "Appetite",
                "Sleep",
                "Brushing Teeth",
                "Dental Visit",
                "Stool",
                "Urine",
                "Smoke Detector",
                "Helmet",
                "Belt",
                "Asthama"

    };
    int checkLayout[]={
                R.id.nodeLayout,
                R.id.coughLayout,
                R.id.phlegmLayout,
                R.id.feverLayout,
                R.id.eyeLayout,
                R.id.rashLayout,
                R.id.abdoLayout,
                R.id.activityLayout,
                R.id.earLayout,
                R.id.earDCLayout,
                R.id.throatLayout,
                R.id.vomitingLayout,
                R.id.diarrheaLayout,
                R.id.fdsolidLayout,
                R.id.fdliquidLayout,
                R.id.appetiteLayout,
                R.id.sleepLayout,
                R.id.bteethLayout,
                R.id.dentVisitLayout,
                R.id.stoolLayout,
                R.id.urineLayout,
                R.id.smokeDetectorLayout,
                R.id.helmetLayout,
                R.id.beltLayout,
                R.id.asthamaLayout

    };
    int inputs[][]={
            {R.id.noseDuration,R.id.noseDischarge},
            {R.id.coughDays,R.id.coughType},
            {R.id.phlegmDays,R.id.phlegmType},
            {R.id.feverDays,R.id.feverTemp},
            {R.id.eyeDays,R.id.eyeDischarge,R.id.eyeSide},
            {R.id.rashDays,R.id.rashType},
            {R.id.abdoDays,R.id.abdoType},
            {R.id.activityType,R.id.activityDays},
            {R.id.earDays,R.id.earType,R.id.earTimes},
            {R.id.earDCDays,R.id.earDCType},
            {R.id.throatDays},
            {R.id.vomitingDays,R.id.vomitingType,R.id.vomitingTimes},
            {R.id.diarrheaDays,R.id.diarrheaType,R.id.diarrheaTimes},
            {R.id.fdsolidType},
            {R.id.fdliquidType},
            {R.id.appetiteType},
            {R.id.sleepType},
            {R.id.bteethType,R.id.bteethTime},
            {R.id.dentVisitType},
            {R.id.stoolType},
            {R.id.urineType},
            {R.id.smokeDetectorType},
            {R.id.helmetType},
            {R.id.beltType},
            {R.id.asthamaP1Type,R.id.asthamaP2Type,R.id.asthamaP3Type,R.id.asthamaP4Type,R.id.asthamaP5Type,R.id.asthamaP6Type,R.id.asthamaP7Type}



    };
    String inputLabels[][]={
            {"Duration","Discharge"},
            {"Days","Type"},
            {"Days","Type"},
            {"Days","Temp"},
            {"Days","Discharge","Side"},
            {"Days","Type"},
            {"Days","Type"},
            {"Type","Days"},
            {"Days","Type","Times"},
            {"Days","Type"},
            {"Days"},
            {"Days","Type","Times"},
            {"Days","Type","Times"},
            {"Type"},
            {"Type"},
            {"Type"},
            {"Type"},
            {"Type","Times"},
            {"Type"},
            {"Type"},
            {"Type"},
            {"Type"},
            {"Type"},
            {"Type"},
            { "asthmaP1","asthmaP2","asthmaP3","asthmaP4","asthmaP5","asthmaP6","asthmaP7" }



    };
    public void action(View v)
    {
        int cLen=checkBox.length;
        boolean POST=false;
        StringBuffer buffer=new StringBuffer("[");
        int checkedCount=0;
        for(int i=0;i<cLen;i++) {
            CheckBox box = (CheckBox) findViewById(checkBox[i]);

                if (box.isChecked() == true) {
                    int inputsLen = inputs[i].length;
                    //@data
                    if(checkedCount>=1)
                    buffer.append(",{\"Title\":\""+checkBoxLabel[i]+"\"");
                    else
                    buffer.append("{\"Title\":\""+checkBoxLabel[i]+"\"");
                    checkedCount++;
                    for (int j = 0; j < inputsLen; j++) {
                        View input = (findViewById(inputs[i][j]));
                        String name = input.getClass().getName();
                        int pos = -1;
                        if (name != null) {
                            name = name.toLowerCase();
                            String text = "";
                            if (name.indexOf("edittext") >= 0) {
                                EditText edit = (EditText) input;
                                text = edit.getText().toString();
                                if (text != null && text.length() >= 1) {
                                    //@data
                                    if(j>=1)
                                    buffer.append(",\""+inputLabels[i][j]+"\":\""+text+"\"");
                                    else
                                    buffer.append(",\""+inputLabels[i][j]+"\":\""+text+"\"");
                                    j++;
                                } else {
                                    Toast.makeText(symptomslist.this, "enter required box!", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            } else if (name.indexOf("spinner") >= 0) {
                                Spinner spn = (Spinner) input;
                                pos = spn.getSelectedItemPosition();

                                if (pos >= 1) {
                                    //@data
                                    text=spn.getItemAtPosition(pos).toString();
                                    if(j>=1)
                                    buffer.append(",\""+inputLabels[i][j]+"\":\""+text+"\"");
                                    else
                                    buffer.append(",\""+inputLabels[i][j]+"\":\""+text+"\"");

                                } else {
                                    Toast.makeText(symptomslist.this, "enter required box!", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }
                            POST=true;
                        } // name!=null

                    }// for- loop
                    buffer.append("}");
                }// box.isCheked==true

        } // outer for loop
        buffer.append("]");

        Log.e("Response", "Symptoms: " +buffer);
        if(POST==true)
        {
        //System.out.println("Data="+buffer.toString());
          try {
              JSONArray jsonArray = new JSONArray(buffer.toString());
              System.out.println(jsonArray.toString());
              System.out.println("url:-"+MyConfig.URL_SYMPTOMS);
              HashMap<String, String> paramHash = new HashMap<String, String>();
              if(Config._roleid==1){
                  paramHash.put("doctorId", Config._doctorid);
              }else {
                  paramHash.put("screenerId", Config._screenerid);
              }
              paramHash.put("caseId", Config.tmp_caseId);
              paramHash.put("citizenId", Config.tmp_citizenId);
              paramHash.put("data", buffer.toString());
              paramHash.put("ngoId", Config.NGO_ID);


              String pattern = "yyyy-MM-dd";
              SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
              String currentDate = simpleDateFormat.format(new Date());

              if(Config.isOffline){
                  try {
                      SQLiteDatabase db;
                      db = openOrCreateDatabase("javixlife", Context.MODE_PRIVATE, null);
                      JSONObject jsonObject=new JSONObject(paramHash);
                      String SqlStr = "INSERT INTO javix_update(service_type,service_name,service_data,data_json,insert_date,_status)";
                      SqlStr += " VALUES('Add Symptomps','" + MyConfig.URL_SYMPTOMS + "','" + paramHash.toString() + "','"+jsonObject.toString()+"','" + currentDate + "',0);";
                      //Toast.makeText(getApplicationContext(), SqlStr, Toast.LENGTH_LONG).show();
                      Log.e("Offline", SqlStr);
                      db.execSQL(SqlStr);
                      db.close();
                      android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(symptomslist.this
                      );
                      alertDialog.setMessage(symptomslist.this.getString(R.string._symptomsAdd_he));
                      alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                          public void onClick(DialogInterface dialog, int whichButton) {
                              Intent i = new Intent(symptomslist.this, TestListActivity.class);
                              //Config.tmp_caseId="0";
                              finish();
                              startActivity(i);
                          }
                      });
                      alertDialog.create();
                      alertDialog.show();

                  }catch (Exception ex){

                      android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(symptomslist.this
                      );
                      alertDialog.setMessage("Error in saving data " + ex.getLocalizedMessage());
                      alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                          public void onClick(DialogInterface dialog, int whichButton) {
                              Intent i = new Intent(symptomslist.this, LabTestActivity.class);
                              //Config.tmp_caseId="0";
                              finish();
                              startActivity(i);
                          }
                      });
                      alertDialog.create();
                      alertDialog.show();
                  }

              }else {
                  SubmitData req = new SubmitData(this, paramHash);
                  req.execute(MyConfig.URL_SYMPTOMS);
              }
          }catch (Exception eee){System.out.println("JSON Exp 901:"+eee.toString());}
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symptomslist);
        jinit();
    }
    void jinit()
    {
        int cLen=checkBox.length;

        for(int i=0;i<cLen;i++)
        {
            CheckBox box=(CheckBox)findViewById(checkBox[i]);
            box.setTag(""+i);
            if(checkBox[i]!=R.id.asthamaChecbox) {

                LinearLayout layout = (LinearLayout) findViewById(checkLayout[i]);
                if (box.isChecked() == false) {
                    layout.setVisibility(View.INVISIBLE);
                    ViewGroup.LayoutParams params = layout.getLayoutParams();
                    params.height = 0;
                    layout.setLayoutParams(params);

                } else {
                    layout.setVisibility(View.VISIBLE);
                }
                box.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CheckBox _box = (CheckBox) v;
                        int _i = Integer.parseInt(_box.getTag().toString());
                        LinearLayout layout = (LinearLayout) findViewById(checkLayout[_i]);
                        ViewGroup.LayoutParams params = layout.getLayoutParams();
                        if (_box.isChecked() == true) {
                            layout.setVisibility(View.VISIBLE);
                            params.height = LinearLayout.LayoutParams.WRAP_CONTENT;

                            layout.setOrientation(LinearLayout.HORIZONTAL);
                            layout.setLayoutParams(params);
                        } else {
                            layout.setVisibility(View.INVISIBLE);
                            params.height = 0;
                            layout.setLayoutParams(params);
                        }
                    }
                });
            }else{}
        }

    }

    // submit Data
        class SubmitData extends AsyncTask<String, Void, String>
            {
                ProgressDialog progressDialog;
                Activity activity;
                HashMap<String, String> paramsHash=null;
                RequestPipe requestPipe=new RequestPipe();
                public SubmitData(Activity activity,HashMap<String, String> paramsHash) {
                    this.activity=activity;
                    this.paramsHash=paramsHash;
                    progressDialog = new ProgressDialog(activity);
                    progressDialog.setMessage("wait..");
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
                }
                protected void onPostExecute(String result) {
                    super.onPostExecute(result);
                    progressDialog.cancel();
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        int respStatus=jsonObject.getInt("status");
                        if(respStatus==1) {
                            android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(symptomslist.this
                            );
                            alertDialog.setMessage(symptomslist.this.getString(R.string._symptomsAdd_he));
                            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    Intent i = new Intent(symptomslist.this, TestListActivity.class);
                                    finish();
                                    startActivity(i);
                                }
                            });
                            alertDialog.create();
                            alertDialog.show();
                        } else Toast.makeText(symptomslist.this,"Sorry!",Toast.LENGTH_SHORT).show();

                    }catch (Exception ee){
                        Toast.makeText(symptomslist.this,"Sorry! "+ee.toString(),Toast.LENGTH_SHORT).show();
                    }

                }

            }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // your code
            Log.e("Back Button","Pressed");
            Intent i = new Intent(symptomslist.this, TestListActivity.class);
            finish();
            startActivity(i);
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}