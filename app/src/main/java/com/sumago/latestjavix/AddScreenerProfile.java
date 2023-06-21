package com.sumago.latestjavix;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.sumago.latestjavix.Util.Config;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class AddScreenerProfile extends AppCompatActivity {
    Button cmdSubmit=null;
    EditText firstName,lastName;
    Spinner gender;
    EditText mobileNo;
    EditText email;
    EditText dob;
    Spinner qualification;
    EditText specialization;
    EditText country;
    Spinner state;
    EditText district;
    EditText address;

    public List<String> populateSpinner() {
        String x = getString(R.string.qualification);
        String y[]=x.split(",");
        return new ArrayList<String>(Arrays.asList(y));
    }
    public List<String> populateGender() {
        String x = getString(R.string.sex);
        String y[]=x.split(",");
        return new ArrayList<String>(Arrays.asList(y));
    }
    public List<String> populateState() {
        String x = getString(R.string.state);
        String y[]=x.split(",");
        return new ArrayList<String>(Arrays.asList(y));
    }
    public void spinersView_load()
    {
        //1.
        List<String> categories = populateSpinner();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        qualification.setAdapter(dataAdapter);

        List<String> categoriesGender = populateGender();
        ArrayAdapter<String> dataAdapterGender = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categoriesGender);
        dataAdapterGender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gender.setAdapter(dataAdapterGender);

        List<String> categoriesState= populateState();
        ArrayAdapter<String> dataAdapterState = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categoriesState);
        dataAdapterState.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        state.setAdapter(dataAdapterState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_screener_profile);

        cmdSubmit=(Button)findViewById(R.id.addScreenerProfile_submit);
        cmdSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MyConfig.CONTEXT," HELLO",Toast.LENGTH_SHORT).show();
                submitForm(v);
            }
        });
        firstName=(EditText)findViewById(R.id.addScreenerProfile_fisrtName);
        lastName=(EditText)findViewById(R.id.addScreenerProfile_lastName);
        gender=(Spinner) findViewById(R.id.addScreenerProfile_gender);
        mobileNo=(EditText)findViewById(R.id.addScreenerProfile_mobileNo);
        email=(EditText)findViewById(R.id.addScreenerProfile_email);
        dob=(EditText)findViewById(R.id.addScreenerProfile_dbo);
        qualification=(Spinner) findViewById(R.id.addScreenerProfile_qualification);
        specialization=(EditText)findViewById(R.id.addScreenerProfile_sepcialiazation);
        country=(EditText)findViewById(R.id.addScreenerProfile_country);
        state=(Spinner) findViewById(R.id.addScreenerProfile_state);
        district=(EditText)findViewById(R.id.addScreenerProfile_district);
        address=(EditText)findViewById(R.id.addScreenerProfile_address);

        // Date Picker
        final EditText editDob=dob;
        final Calendar myCalendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
            private void updateLabel() {
                String pattern = "yyyy-MM-dd";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                String selDate = simpleDateFormat.format(myCalendar.getTime());

                editDob.setText(selDate);
            }

        };
        dob.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(AddScreenerProfile.this, dateListener, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        // ENd Calandar


      ErrBox.clearAll();
      ErrBox.add(firstName,"TEXT","fill ngo name (only char)",true);
      ErrBox.add(lastName,"TEXTONLY","fill ngo owner name (only char)",true);
      ErrBox.add(gender,"TEXT","select gender",true);
      ErrBox.add(mobileNo,"MOBILE","fill mobile no (only  digits)",true);
      ErrBox.add(email,"EMAIL","fill valid email",true);
      ErrBox.add(dob,"MIXED","select date of birth",true);
      ErrBox.add(qualification,"TEXT","select qualification (only char)",true);
      ErrBox.add(specialization,"MIXED","fill specialization",true);
      ErrBox.add(country,"TEXT","fill country (only char)",true);
      ErrBox.add(state,"TEXT","fill state (only char)",true);
      ErrBox.add(district,"TEXT","fill district (only char)",true);
      ErrBox.add(address,"MIXED","fill address (No Special Char)",true);

        // update spinner

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
        paramHash.put("userId","123456790");
        paramHash.put("token","T89878788");
        paramHash.put("firstName",firstName.getText().toString());
        paramHash.put("lastName",lastName.getText().toString());
        paramHash.put("sex",gender.getSelectedItem().toString());
        paramHash.put("mobileNo",mobileNo.getText().toString());
        paramHash.put("email",email.getText().toString());
        paramHash.put("dateOfBirth",dob.getText().toString());
        paramHash.put("dateOfOnBoarding",currentDate);
        paramHash.put("qualification",qualification.getSelectedItem().toString());
        paramHash.put("specialisation",specialization.getText().toString());
        paramHash.put("country",country.getText().toString());
        paramHash.put("state",state.getSelectedItem().toString());
        paramHash.put("district",district.getText().toString());
        paramHash.put("address",address.getText().toString());
        paramHash.put("ngoId", Config.NGO_ID);

      AddScreenerProfile.SubmitForm req=new AddScreenerProfile.SubmitForm(this,paramHash);
        req.execute(MyConfig.URL_ADD_SCREENER);

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