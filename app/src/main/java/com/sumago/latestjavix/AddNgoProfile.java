package com.sumago.latestjavix;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.sumago.latestjavix.Util.Config;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class AddNgoProfile extends AppCompatActivity {
    Button cmdSubmit=null;
    EditText name;
    EditText ownerName;
    EditText mobileNo;
    EditText email;
    EditText regNo;
    EditText country;
    Spinner state;
    EditText district;
    EditText address;

    public List<String> populateState() {
        String x = getString(R.string.state);
        String y[]=x.split(",");
        return new ArrayList<String>(Arrays.asList(y));
    }
    public void spinersView_load()
    {
        List<String> categoriesState= populateState();
        ArrayAdapter<String> dataAdapterState = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categoriesState);
        dataAdapterState.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        state.setAdapter(dataAdapterState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ngo_profile);
        cmdSubmit=(Button)findViewById(R.id.addNgoProfile_submit);
        cmdSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MyConfig.CONTEXT," HELLO",Toast.LENGTH_SHORT).show();
                    submitForm(v);
            }
        });

        name=(EditText)findViewById(R.id.addNgoProfile_name);
        ownerName=(EditText)findViewById(R.id.addNgoProfile_ownerName);
        mobileNo=(EditText)findViewById(R.id.addNgoProfile_mobileNo);
        email=(EditText)findViewById(R.id.addNgoProfile_email);
        regNo=(EditText)findViewById(R.id.addNgoProfile_regNo);
        country=(EditText)findViewById(R.id.addNgoProfile_country);
        state=(Spinner) findViewById(R.id.addNgoProfile_state);
        district=(EditText)findViewById(R.id.addNgoProfile_district);
        address=(EditText)findViewById(R.id.addNgoProfile_address);
        ErrBox.clearAll();
        ErrBox.add(name,"TEXT","fill ngo name (only char)",true);
        ErrBox.add(ownerName,"TEXTONLY","fill ngo owner name (only char)",true);
        ErrBox.add(mobileNo,"MOBILE","fill mobile no (only  digits)",true);
        ErrBox.add(email,"EMAIL","fill valid email",true);
        ErrBox.add(name,"MIXED","fill ngo regno",true);
        ErrBox.add(country,"TEXT","fill country (only char)",true);
        ErrBox.add(state,"TEXT","fill state (only char)",true);
        ErrBox.add(district,"TEXT","fill district (only char)",true);
        ErrBox.add(address,"MIXED","fill address (No Special Char)",true);

        //
        spinersView_load();
    }

    void submitForm(View v)
    {
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String currentDate = simpleDateFormat.format(new Date());

        ErrBox.errorsStatus();
        Toast.makeText(MyConfig.CONTEXT,""+ ErrBox.FIRST_ERR_MSG,Toast.LENGTH_SHORT).show();

        HashMap<String,String> paramHash=new HashMap<String,String>();
        paramHash.put("userId","jilani10@gmail.com");
        paramHash.put("token","T89878788");
        paramHash.put("ngoName",name.getText().toString());
        paramHash.put("ownerName",ownerName.getText().toString());
        paramHash.put("mobileNo",mobileNo.getText().toString());
        paramHash.put("email",email.getText().toString());
        paramHash.put("ngoRegistrationNo",currentDate);
        paramHash.put("dateOfRegistration",currentDate);
        paramHash.put("dateOfOnBoarding",currentDate);
        paramHash.put("country",country.getText().toString());
        paramHash.put("state",state.getSelectedItem().toString());
        paramHash.put("district",district.getText().toString());
        paramHash.put("address",address.getText().toString());
        paramHash.put("ngoId", Config.NGO_ID);

        AddNgoProfile.SubmitForm req=new AddNgoProfile.SubmitForm(this,paramHash);
        req.execute(MyConfig.URL_ADD_NGO);
       /*

userId:jilani1@gmail.com
token:329382091389
ngoName:Lolomate Rafiqu
ownerName:My NGO Name
mobileNo:9665000080
email:jilani.it4@gmail.com
ngoRegistrationNo:reg/7099777
dateOfRegistration:2001-12-29
dateOfOnBoarding:2020-02-23
country:INDIA
state:JHARKHAND
district:RANCHI
address:LOHARA TOLI, NERMALI APARTMENT NEAR CYBER MARKET
        */

        // END Calling NGO Task List
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
                if(respStatus==1) {
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
