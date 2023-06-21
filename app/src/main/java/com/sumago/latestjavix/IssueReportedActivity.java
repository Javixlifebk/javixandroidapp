package com.sumago.latestjavix;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.sumago.latestjavix.Util.Config;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;


public class IssueReportedActivity extends AppCompatActivity {

    Spinner spnIssue;
    EditText etSubject,etOther;
    Button cmdSubmit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue_reported);
        initViews();
    }
    public void initViews() {
        spnIssue = (Spinner) findViewById(R.id.spnIssue);
        etSubject = (EditText) findViewById(R.id.etSubject);
        etOther = (EditText) findViewById(R.id.etOther);

        ArrayAdapter<CharSequence> adp4 = ArrayAdapter.createFromResource(this, R.array.strIssue, android.R.layout.simple_spinner_item);
        adp4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnIssue.setAdapter(adp4);
        spnIssue.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                if(!spnIssue.getItemAtPosition(position).toString().equals("Select Subject")){

                    if(spnIssue.getItemAtPosition(position).toString().equals("Other")){
                        etOther.setVisibility(View.VISIBLE);
                    }
                    else{
                        etOther.setVisibility(View.GONE);
                        etOther.setText(spnIssue.getSelectedItem().toString());
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(),"Please Select subject",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        cmdSubmit=(Button)findViewById(R.id.btnSubmit);
        cmdSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(MyConfig.CONTEXT," Data Posting !",Toast.LENGTH_SHORT).show();
                postData(v);
            }
        });
    }
    void postData(View v){
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String currentDate = simpleDateFormat.format(new Date());
        ErrBox.errorsStatus();
        HashMap<String,String> paramHash=new HashMap<String,String>();
        paramHash.put("userId", Config._uid);
        paramHash.put("issue",etOther.getText().toString());
        paramHash.put("issueDetails",etSubject.getText().toString());
        paramHash.put("ngoId", Config.NGO_ID);

        SubmitForm req=new SubmitForm(this,paramHash);
        req.execute(MyConfig.URL_ADDISSUE);
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
                JSONObject recsData=jsonObject.getJSONObject("data");
                recsData=recsData.getJSONObject("data");
                if(respStatus==1) {
                    //Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                    android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(IssueReportedActivity.this
                    );
                    alertDialog.setMessage("Issue Reported Successfully, Your Issue No:-" + recsData.getString("issueNo"));
                    alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                            finish();

                        }
                    });
                    alertDialog.create();
                    alertDialog.show();

                }
                else Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

            }catch (Exception ee){
                Toast.makeText(getApplicationContext(), ee.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

    }

}