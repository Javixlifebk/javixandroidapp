package com.sumago.latestjavix;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.sumago.latestjavix.Util.Config;
import com.sumago.latestjavix.Util.Validations;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class InstantExam extends AppCompatActivity {
    Button cmdSubmit=null;
    EditText firstName,lastName;
    Spinner gender;
    EditText mobileNo,aadharNo;
    EditText email;
    EditText dob;
    Spinner blood_group;
    EditText txPin;
    EditText country;
    Spinner state,district;
    //EditText ;
    EditText address;
    ImageView mImageView;
    String ImagePath="";
    private static final String TAG = "_msg";
    ProgressDialog dialog1 = null;
    int serverResponseCode = 0;
    RadioButton mGenderM;
    RadioButton mGenderF;
    private String mGender;
    String res="",postImageUrl="";
    boolean mFname,mLname,mPin,mPhoto,mAadhaar,mState,mDistrict;

    Spinner spinnerState=null;
    Spinner spinnerDistrict=null;
    ArrayList<String> arrayState=new ArrayList<String>();
    ArrayList<String> arrayDistrict=new ArrayList<String>();
    ArrayAdapter aaDistrict=null;
    JSONObject jsonObject=null;
    Validations validations;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instant_exam);
        validations=new Validations();
        cmdSubmit=(Button)findViewById(R.id.addDoctorProfile_submit);
        cmdSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(MyConfig.CONTEXT," HELLO",Toast.LENGTH_SHORT).show();
                if(firstName.getText().length()==0){
                    mFname=false;
                    Toast.makeText(getApplicationContext(),"First Name Can't be blank !",Toast.LENGTH_SHORT).show();
                }
                else{
                    mFname=true;
                }
                if(mFname==true) {
                    if (lastName.getText().length() == 0) {
                        mLname = false;
                        Toast.makeText(getApplicationContext(), "Last Name Can't be blank !", Toast.LENGTH_SHORT).show();
                    } else {
                        mLname = true;
                    }
                }
                /**if(mLname==true) {
                    if (txPin.getText().length() !=6) {
                        mPin = false;
                        Toast.makeText(getApplicationContext(), "Pin Must 6 digit length  !", Toast.LENGTH_SHORT).show();
                    } else {
                        mPin = true;
                    }
                }*/


                if(mLname==true) {
                    if (aadharNo.getText().length() != 12) {
                        mAadhaar = false;
                        Toast.makeText(getApplicationContext(), "Aadhaar No must be 12 digit length !", Toast.LENGTH_SHORT).show();
                    }else {
                        mAadhaar = true;
                    }
                }

                if(mAadhaar==true) {

                    submitForm(v);

                }
                    else{
                        Toast.makeText(getApplicationContext(), "Input Valid Data !", Toast.LENGTH_SHORT).show();
                }




                //submitForm(v);
            }
        });

        firstName=(EditText)findViewById(R.id.txFname);
        lastName=(EditText)findViewById(R.id.txLname);
        //gender=(Spinner)findViewById(R.id.addDoctorProfile_gender);
        mGenderM =(RadioButton)findViewById(R.id.identification_gender_male);
        mGenderF =(RadioButton)findViewById(R.id.identification_gender_female);
        mobileNo=(EditText)findViewById(R.id.txMobile);
        email=(EditText)findViewById(R.id.txEmail);
        dob=(EditText)findViewById(R.id.addDoctorProfile_dbo);
        blood_group=(Spinner) findViewById(R.id.blood_group);
        country=(EditText)findViewById(R.id.txCountry);
        txPin=(EditText)findViewById(R.id.txPin);
        aadharNo=(EditText)findViewById(R.id.addDoctorProfile_adhar);
        // Date Picker
        final EditText editDob=dob;
        final Calendar myCalendar = Calendar.getInstance();
        //if(mGender.length()==0) {
        mGenderF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGender = "Female";
            }
        });

        mGenderM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGender = "Male";
            }
        });

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
                String pattern = "dd-MM-yyyy";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                String selDate = simpleDateFormat.format(myCalendar.getTime());
                editDob.setText(selDate);
            }
        };
        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
//                new DatePickerDialog(InstantExam.this, dateListener, myCalendar
//                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
//                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

                DatePickerDialog datePickerDialog = new DatePickerDialog(InstantExam.this, dateListener, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));

                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();



            }
        });
        ErrBox.clearAll();
    }

    void submitForm(View v){
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String currentDate = simpleDateFormat.format(new Date());
        ErrBox.errorsStatus();
        //Toast.makeText(getApplicationContext(),"Error !"+ ErrBox.FIRST_ERR_MSG,Toast.LENGTH_SHORT).show();
        //Toast.makeText(getApplicationContext(),"Screener ID"+ Config._screenerid,Toast.LENGTH_SHORT).show();
        HashMap<String,String> paramHash=new HashMap<String,String>();
        //Config._screenerid="161315136114567577";
        paramHash.put("screenerId", Config._screenerid);
        paramHash.put("token","T89878788");
        paramHash.put("firstName",firstName.getText().toString());
        paramHash.put("lastName",lastName.getText().toString());
        paramHash.put("sex",mGender);
        paramHash.put("dateOfBirth",dob.getText().toString());
        paramHash.put("dateOfOnBoarding",currentDate);
        paramHash.put("state","Javix State");
        paramHash.put("district","Javix District");
        paramHash.put("pincode",txPin.getText().toString());
        paramHash.put("aadhaar",aadharNo.getText().toString());
        paramHash.put("status","9");
        paramHash.put("ngoId", Config.NGO_ID);

        SubmitForm req=new SubmitForm(this,paramHash);
        req.execute(MyConfig.URL_ADD_CITIZEN);
    }

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

            Log.e(TAG, "parametersssss : " +paramsHash);
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
                    JSONObject recsData=jsonObject.getJSONObject("data");
                    recsData=recsData.getJSONObject("data");
                    Config.tmp_citizenId=recsData.getString("citizenId");
                    android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(InstantExam.this
                    );
                    alertDialog.setMessage("Citizen Added Successfully !");
                    alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            Intent i = new Intent(InstantExam.this, TestListActivity.class);
                            Config.tmp_caseId="0";
                            finish();
                            startActivity(i);
                        }
                    });
                    alertDialog.create();
                    alertDialog.show();
                }
                else {
                    //Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show()};
                    JSONArray recsData=jsonObject.getJSONArray("data");
                    String Msg="";
                    for(int i=0;i<recsData.length();i++){
                        JSONObject valObj=recsData.getJSONObject(i);
                        Msg+=valObj.getString("msg") + "\n\n";
                    }
                    Log.e(TAG,"Response in Array" + recsData);
                    android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(InstantExam.this
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

            }catch (Exception ee){
                //Toast.makeText(getApplicationContext(), "Records Already Exists !", Toast.LENGTH_SHORT).show();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int respStatus = jsonObject.getInt("status");
                    JSONObject recsData = jsonObject.getJSONObject("data");
                    //recsData = recsData.getJSONObject("data");

                }catch (Exception ex){}
                //JSONArray jsonArray=new JSONArray(e.ge)
            }
        }
    }
}