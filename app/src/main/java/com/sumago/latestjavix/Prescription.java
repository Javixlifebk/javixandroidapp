package com.sumago.latestjavix;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sumago.latestjavix.Util.Config;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class Prescription extends AppCompatActivity {

    EditText medicine=null;
    EditText comments=null;
    EditText tests=null;
    String doctorId="";
    String screenerId="";
    String citizenId="";
    String recordId="";
    String caseId="";
    Button cmdSubmit=null;
    private static final String TAG = "_msg";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription);
        Bundle bundle=getIntent().getExtras();
        doctorId=bundle.getString("doctorId");
        screenerId=bundle.getString("ScreenerId");
        citizenId=bundle.getString("CitizenId");
        recordId=bundle.getString("recordId");
        caseId=bundle.getString("caseId");
        Toast.makeText(MyConfig.CONTEXT,screenerId,Toast.LENGTH_SHORT).show();
        medicine=(EditText)findViewById(R.id.prescription_medicine);
        comments=(EditText)findViewById(R.id.prescription_comments);
        tests=(EditText)findViewById(R.id.prescription_tests);
        cmdSubmit=(Button)findViewById(R.id.prescription_submit);
        cmdSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(MyConfig.CONTEXT," HELLO",Toast.LENGTH_SHORT).show();
                submitForm(v);
            }
        });
        ErrBox.clearAll();
        ErrBox.add(medicine,"MIXED","Please enter medicine box",true);
        ErrBox.add(comments,"MIXED","Please fill comments",true);
    }

    void submitForm(View v)
    {
        ErrBox.errorsStatus();
        //Toast.makeText(MyConfig.CONTEXT,""+ErrBox.FIRST_ERR_MSG,Toast.LENGTH_SHORT).show();
        HashMap<String,String> paramHash=new HashMap<String,String>();
        paramHash.put("doctorId",doctorId);
        paramHash.put("screenerId",screenerId);
        paramHash.put("citizenId",citizenId);
        paramHash.put("recordId",recordId);
        paramHash.put("caseId",caseId);
        paramHash.put("status","3");
        paramHash.put("medicine",medicine.getText().toString());
        paramHash.put("tests",tests.getText().toString());
        paramHash.put("comments",comments.getText().toString());
        paramHash.put("ngoId", Config.NGO_ID);
        SubmitForm req=new SubmitForm(this,paramHash);
        req.execute(MyConfig.URL_ADD_PRESCRIPTION);
        Log.e(TAG, "else part : " +paramHash);
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
            progressDialog.setMessage("wait...");
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

                    //Intent i =new Intent(Prescription.this,Screen)
                    Toast.makeText(MyConfig.CONTEXT, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                }
                else Toast.makeText(MyConfig.CONTEXT, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

            }catch (Exception ee){
                Toast.makeText(MyConfig.CONTEXT, "Opps !.", Toast.LENGTH_SHORT).show();
            }

        }

    }
    // ENd SUbmit Form
}