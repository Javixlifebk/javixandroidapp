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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.sumago.latestjavix.Util.Config;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class AddActorsActivity extends AppCompatActivity {
    Spinner spnActor;
    EditText etFname,etLname,etMobile,etEmail,etUser,etPassword,etCPassword;
    Button btSubmit;

    Boolean boolActor=false,boolFname=false,boolLname=false,boolMobile=false,boolEmail=false,boolUser=false,boolPassword=false,boolCPassword=false;
    String[] strActors ={"Select Actors","Doctor","NGO","Screener","Sevika","Pharmacy"};
    HashMap<String,String> hasActor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_actors);
        MyConfig.CONTEXT=this;
        initView();

    }
    public void initView(){
        spnActor=(Spinner)findViewById(R.id.spnActor);
        etFname=(EditText)findViewById(R.id.etFname);
        etLname=(EditText)findViewById(R.id.etLname);
        etMobile=(EditText)findViewById(R.id.etMobile);
        etEmail=(EditText)findViewById(R.id.etEmail);
        etUser=(EditText)findViewById(R.id.etUser);
        etPassword=(EditText)findViewById(R.id.etPassword);
        etCPassword=(EditText)findViewById(R.id.etCPassword);
        btSubmit=(Button)findViewById(R.id.btSubmit);
        ArrayAdapter adp1 = new ArrayAdapter(this,android.R.layout.simple_spinner_item,strActors);
        adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnActor.setAdapter(adp1);
        hasActor=new HashMap<String,String>();
        hasActor.put("Doctor","1");
        hasActor.put("NGO","3");
        hasActor.put("Screener","2");
        hasActor.put("Sevika","21");
        hasActor.put("Pharmacy","4");

        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(spnActor.getSelectedItem().toString().equalsIgnoreCase("Select Actors")){
                    boolActor=false;
                    Toast.makeText(getApplicationContext(),"Please Select Actors !",Toast.LENGTH_SHORT).show();
                }
                else{
                    boolActor=true;
                }

                if(boolActor==true) {
                    if (etFname.getText().length() == 0) {
                        boolFname = false;
                        Toast.makeText(getApplicationContext(), "First Name Can't be blank !", Toast.LENGTH_SHORT).show();
                    } else {
                        boolFname = true;
                    }
                }


                if(boolFname==true) {
                    // Toast.makeText(getApplicationContext(),Integer.toString(dob.getText().length()),Toast.LENGTH_SHORT).show();
                    if (etLname.getText().length()==0) {
                        boolLname = false;
                        Toast.makeText(getApplicationContext(), "Last Name Can't be blank !", Toast.LENGTH_SHORT).show();
                    } else {
                        boolLname = true;
                    }
                }
                if(boolLname==true) {
                    // Toast.makeText(getApplicationContext(),Integer.toString(dob.getText().length()),Toast.LENGTH_SHORT).show();
                    if (etMobile.getText().length()==0) {
                        boolMobile = false;
                        Toast.makeText(getApplicationContext(), "Mobile Can't be blank !", Toast.LENGTH_SHORT).show();
                    } else {
                        boolMobile = true;
                    }
                }

                if(boolMobile==true) {
                    // Toast.makeText(getApplicationContext(),Integer.toString(dob.getText().length()),Toast.LENGTH_SHORT).show();
                    if (etEmail.getText().length()==0) {
                        boolEmail = false;
                        Toast.makeText(getApplicationContext(), "Email Can't be blank !", Toast.LENGTH_SHORT).show();
                    } else {
                        boolEmail = true;
                    }
                }

                if(boolEmail==true) {
                    // Toast.makeText(getApplicationContext(),Integer.toString(dob.getText().length()),Toast.LENGTH_SHORT).show();
                    if (etUser.getText().length()==0) {
                        boolUser = false;
                        Toast.makeText(getApplicationContext(), "User Can't be blank !", Toast.LENGTH_SHORT).show();
                    } else {
                        boolUser = true;
                    }
                }


                if(boolUser==true) {
                    // Toast.makeText(getApplicationContext(),Integer.toString(dob.getText().length()),Toast.LENGTH_SHORT).show();
                    if (etPassword.getText().length()==0) {
                        boolPassword = false;
                        Toast.makeText(getApplicationContext(), "Password Can't be blank !", Toast.LENGTH_SHORT).show();
                    } else {
                        boolPassword = true;
                    }
                }

                if(boolPassword==true) {
                    // Toast.makeText(getApplicationContext(),Integer.toString(dob.getText().length()),Toast.LENGTH_SHORT).show();
                    if (etCPassword.getText().length()==0) {
                        boolCPassword = false;
                        Toast.makeText(getApplicationContext(), "Confirm Password Can't be blank !", Toast.LENGTH_SHORT).show();
                    } else {
                        boolCPassword = true;
                    }
                }

                if(etCPassword.getText().toString().equals(etPassword.getText().toString())){
                    submitForm(v);

                }else{
                    Toast.makeText(getApplicationContext(), "Password Mismatch !", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }


    void submitForm(View v){
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String currentDate = simpleDateFormat.format(new Date());


        ErrBox.errorsStatus();
        // Toast.makeText(MyConfig.CONTEXT,""+ ErrBox.FIRST_ERR_MSG,Toast.LENGTH_SHORT).show();

        HashMap<String,String> paramHash=new HashMap<String,String>();
        paramHash.put("roleId", hasActor.get(spnActor.getSelectedItem().toString()));
        paramHash.put("firstName", etFname.getText().toString());
        paramHash.put("lastName",etLname.getText().toString());
        paramHash.put("phoneNo",etMobile.getText().toString());
        paramHash.put("email",etEmail.getText().toString());
        paramHash.put("userName",etUser.getText().toString());
        paramHash.put("password",etPassword.getText().toString());
        paramHash.put("isConfirmed","true");
        paramHash.put("ngoId", Config.NGO_ID);



        SubmitForm req = new SubmitForm(this, paramHash);
        req.execute(MyConfig.URL_ADDACTORS);


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

                    android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(AddActorsActivity.this
                    );
                    alertDialog.setMessage("Actor Added Successfully !");
                    alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            Intent i = new Intent(AddActorsActivity.this, AdminActivity.class);
                            finish();
                            startActivity(i);
                        }
                    });
                    alertDialog.create();
                    alertDialog.show();


                    //Toast.makeText(MyConfig.CONTEXT, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                }
                else {

                    JSONArray recsData=jsonObject.getJSONArray("data");
                    String Msg="";
                    for(int i=0;i<recsData.length();i++){
                        JSONObject valObj=recsData.getJSONObject(i);
                        Msg+=valObj.getString("msg") + "\n\n";
                        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(AddActorsActivity.this
                        );
                        alertDialog.setMessage(Msg);
                        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                            /*Intent i = new Intent(AddCitizenActivity.this, TestListActivity.class);
                            Config.tmp_caseId="0";
                            finish();
                            startActivity(i);*/
                            }
                        });
                        alertDialog.create();
                        alertDialog.show();
                    }
                }

            }catch (Exception ee){
                Toast.makeText(MyConfig.CONTEXT, "Opps !.", Toast.LENGTH_SHORT).show();
            }

        }

    }
}