package com.sumago.latestjavix;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.sumago.latestjavix.Adapters.listviewAdapter;
import com.sumago.latestjavix.Model.Model;
import com.sumago.latestjavix.Util.Config;
import com.sumago.latestjavix.WebService.ApiInterface;
import com.sumago.latestjavix.WebService.MyNewConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class AddPrescriptionActivity extends AppCompatActivity {
    private ArrayList<Model> productList;
    Spinner spnTime, spnMeal, spnMType, spnSUnit, spnDUnit, spnRoute, spnPreparation, spnFrequency, spnDirection, spnDrUnit;
    String[] strMeal = {"Meal Type", "N/A", "Pre Meal", "Post Meal"};
    String[] strMType = {"Medicine Type", "N/A", "Tablet", "Capsule", "Teaspoon", "Ointment", "Spray"};
    String[] strTime = {"Timing", "N/A", "Morning", "Afternoon", "Evening"};
    String[] strSUnit = {"Unit", "N/A", "mg", "gm", "ng", "mcq", "%", "IU", "IU/ml"};
    String[] strDUnit = {"Unit", "N/A", "ml", "unit", "table spoon", "tea spoon"};
    String[] strPreparation = {"Preparation", "N/A", "Tablet", "Capsule", "Softgel", "Injection", "Syrup", "Drops", "Ointment", "Suppository"};
    String[] stRoute = {"Route", "N/A", "Topical", "Oral", "Rectal", "Vaginal", "Urethral", "Inhalation", "Local", "Chew", "Suck", "Intrademal", "Subcutaneous", "Intramuscular", "Intravenous", "Nasal", "Ear Drops", "Eye Drops"};
    String[] strDirection = {"Direction", "N/A", "Before meals", "After meals"};
    String[] strFrequency = {"Frequency", "N/A", "If required", "Immediately", "Once a day", "Twice daily", "Thrice daily", "Four times a day", "Every hour", "Every night at bedtime", "Everyday", "Every other day", "Ever four hours", "Once a week", "Three times a week"};
    String[] strDrUnit = {"Unit", "N/A", "hours", "days", "weeks", "years"};
    EditText etMedicine, etDosage, etDays, etTests, etSuggestions, etMedicineDescripion, etStrength, etDuration, etQuantity, etCause;
    Button addButton, prescription_submit;
    ListView listView;
    ArrayList<String> listItems;
    ArrayAdapter<String> adapter;
    AppCompatButton btnadd;
    Model item1;
    CheckBox chkMorning, chkAfternoon, chkEvening;
    String strTimes = "";
    String strMedicine = "", strDosage = "", strDir = "", strDuration = "", strPrep = "", strRoute = "", strFreq = "", strQty = "", strStrength = "";
    String stDunit = "", stDrUnit = "";
    String doctorId = "";
    String screenerId = "";
    String citizenId = "";
    String recordId = "";
    String caseId = "";
    boolean mMedicine = false, mStrength = false, mDuration = false, mQty = false, mSUnit = false, mDUnit = false, mDrUnit = false, mDosage = false;
    boolean mRoute = false, mPrep = false, mDir = false, mFreq = false;

    boolean isPrescribe = false;
    String token = "dfjkhsdfaksjfh3756237";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_prescription1);
        initView();


        Bundle bundle = getIntent().getExtras();
        doctorId = bundle.getString("doctorId");
        screenerId = bundle.getString("ScreenerId");
        citizenId = bundle.getString("CitizenId");
        recordId = bundle.getString("recordId");
        caseId = bundle.getString("caseId");

        Log.e("getIntent", "doctorId: " + doctorId);
        Log.e("getIntent", "screenerId: " + screenerId);
        Log.e("getIntent", "citizenId: " + citizenId);
        Log.e("getIntent", "recordId: " + recordId);
        Log.e("getIntent", "caseId: " + caseId);

    }

    private void initView() {
        spnSUnit = (Spinner) findViewById(R.id.spnSUnit);
        spnDUnit = (Spinner) findViewById(R.id.spnDUnit);
        spnPreparation = (Spinner) findViewById(R.id.spnPreparation);
        spnRoute = (Spinner) findViewById(R.id.spnRoute);
        spnFrequency = (Spinner) findViewById(R.id.spnFrequency);
        spnDirection = (Spinner) findViewById(R.id.spnDirection);
        spnDrUnit = (Spinner) findViewById(R.id.spnDrUnit);
        etMedicine = (EditText) findViewById(R.id.etMedicine);
        etStrength = (EditText) findViewById(R.id.etStrength);
        etDuration = (EditText) findViewById(R.id.etDuration);
        etQuantity = (EditText) findViewById(R.id.etQuantity);
        etDosage = (EditText) findViewById(R.id.etDosage);
        etCause = (EditText) findViewById(R.id.etCause);
        etTests = (EditText) findViewById(R.id.etTests);
        etMedicineDescripion = (EditText) findViewById(R.id.etMedicineDescripion);
        listView = (ListView) findViewById(R.id.listView);
        prescription_submit = (Button) findViewById(R.id.prescription_submit);
        btnadd = (AppCompatButton) findViewById(R.id.btnadd);
        productList = new ArrayList<Model>();

        ArrayAdapter adp1 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, strSUnit);
        adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spnSUnit.setAdapter(adp1);
        adp1 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, strDUnit);
        adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnDUnit.setAdapter(adp1);

        adp1 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, strPreparation);
        adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnPreparation.setAdapter(adp1);

        adp1 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, stRoute);
        adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnRoute.setAdapter(adp1);

        adp1 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, strDirection);
        adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnDirection.setAdapter(adp1);

        adp1 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, strFrequency);
        adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnFrequency.setAdapter(adp1);

        adp1 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, strDrUnit);
        adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnDrUnit.setAdapter(adp1);

        listItems = new ArrayList<String>();

        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (etMedicine.getText().toString().length() == 0) {
                    Toast.makeText(getApplicationContext(), "Please Input Medicine !", Toast.LENGTH_LONG).show();
                    mMedicine = false;
                    //return;
                } else {
                    mMedicine = true;
                }
               /*
                if(mMedicine==true){
                    if(etStrength.getText().toString().length()==0){
                        Toast.makeText(getApplicationContext(),"Please Input Strength !",Toast.LENGTH_LONG).show();
                        mStrength=false;
                    }else{
                        mStrength=true;
                    }
                }
                if(mStrength==true){
                    if(spnSUnit.getSelectedItem().toString().equalsIgnoreCase("Unit")){
                        Toast.makeText(getApplicationContext(),"Please Select Strength Unit !",Toast.LENGTH_LONG).show();
                        mSUnit=false;
                    }else{
                        mSUnit=true;
                    }
                }
                if(mSUnit==true){
                    if(etDosage.getText().toString().length()==0){
                        Toast.makeText(getApplicationContext(),"Please Input Dosage!",Toast.LENGTH_LONG).show();
                        mDosage=false;
                    }else{
                        mDosage=true;
                    }
                }
                if(mDosage==true){
                    if(spnDUnit.getSelectedItem().toString().equals("Unit")){
                        Toast.makeText(getApplicationContext(),"Please Select Dosage Unit!",Toast.LENGTH_LONG).show();
                        mDUnit=false;
                    }else{
                        mDUnit=true;
                    }
                }
                if(mDUnit==true){
                    if(spnPreparation.getSelectedItem().toString().equalsIgnoreCase("Preparation")){
                        Toast.makeText(getApplicationContext(),"Please Select Preparation!",Toast.LENGTH_LONG).show();
                        mPrep=false;
                    }else{
                        mPrep=true;
                    }
                }

                if(mPrep==true){
                    if(spnRoute.getSelectedItem().toString().equalsIgnoreCase("Route")){
                        Toast.makeText(getApplicationContext(),"Please Select Route !",Toast.LENGTH_LONG).show();
                        mRoute=false;
                    }else{
                        mRoute=true;
                    }
                }

                if(mRoute==true){
                    if(spnDirection.getSelectedItem().toString().equalsIgnoreCase("Direction")){
                        Toast.makeText(getApplicationContext(),"Please Select Direction !",Toast.LENGTH_LONG).show();
                        mDir=false;
                    }else{
                        mDir=true;
                    }
                }

                if(mDir==true){
                    if(spnFrequency.getSelectedItem().toString().equalsIgnoreCase("Frequency")){
                        Toast.makeText(getApplicationContext(),"Please Select Frequency !",Toast.LENGTH_LONG).show();
                        mFreq=false;
                    }else{
                        mFreq=true;
                    }
                }
                if(mFreq==true){
                    if(etDuration.getText().toString().length()==0){
                        Toast.makeText(getApplicationContext(),"Please Input Duration !",Toast.LENGTH_LONG).show();
                        mDuration=false;
                    }else{
                        mDuration=true;
                    }
                }

                if(mDuration==true){
                    if(spnDrUnit.getSelectedItem().toString().equalsIgnoreCase("Unit")){
                        Toast.makeText(getApplicationContext(),"Please Select Duration Unit !",Toast.LENGTH_LONG).show();
                        mDrUnit=false;
                    }else{
                        mDrUnit=true;
                    }
                }

                if(mDrUnit==true){
                    if(etQuantity.getText().toString().length()==0){
                        Toast.makeText(getApplicationContext(),"Please Input Quantity !",Toast.LENGTH_LONG).show();
                        mQty=false;
                    }else{
                        mQty=true;
                    }
                }*/


                if (mMedicine == true) {

                    item1 = new Model(etMedicine.getText().toString(), etDosage.getText().toString(), spnDirection.getSelectedItem().toString(), etDuration.getText().toString() + "-" + spnDrUnit.getSelectedItem().toString(), spnPreparation.getSelectedItem().toString(), spnFrequency.getSelectedItem().toString());
                    productList.add(item1);
                    strMedicine += etMedicine.getText().toString() + ",,,";
                    strDosage += etDosage.getText().toString() + " " + spnDUnit.getSelectedItem().toString() + ",,,";
                    strDir += spnDirection.getSelectedItem().toString() + ",,,";
                    strFreq += spnFrequency.getSelectedItem().toString() + ",,,";
                    strStrength += etStrength.getText().toString() + " " + spnSUnit.getSelectedItem().toString() + ",,,";
                    strQty += etQuantity.getText().toString() + ",,,";
                    strRoute += spnRoute.getSelectedItem().toString() + ",,,";
                    strDuration += etDuration.getText().toString() + " " + spnDrUnit.getSelectedItem().toString() + ",,,";
                    strPrep += spnPreparation.getSelectedItem().toString() + ",,,";
                    listviewAdapter adapter = new listviewAdapter(AddPrescriptionActivity.this, productList);
                    listView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    etMedicine.setText("");
                    etQuantity.setText("");
                    etDosage.setText("");
                    etQuantity.setText("");
                    etCause.setText("");

                    etStrength.setText("");
                    etDuration.setText("");
                    etMedicineDescripion.setText("");
                    etTests.setText("");


                    ArrayAdapter adp2 = new ArrayAdapter(AddPrescriptionActivity.this, android.R.layout.simple_spinner_item, strSUnit);
                    adp2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    spnSUnit.setAdapter(adp2);
                    adp2 = new ArrayAdapter(AddPrescriptionActivity.this, android.R.layout.simple_spinner_item, strDUnit);
                    adp2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spnDUnit.setAdapter(adp2);

                    adp2 = new ArrayAdapter(AddPrescriptionActivity.this, android.R.layout.simple_spinner_item, strPreparation);
                    adp2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spnPreparation.setAdapter(adp2);

                    adp2 = new ArrayAdapter(AddPrescriptionActivity.this, android.R.layout.simple_spinner_item, stRoute);
                    adp2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spnRoute.setAdapter(adp2);

                    adp2 = new ArrayAdapter(AddPrescriptionActivity.this, android.R.layout.simple_spinner_item, strDirection);
                    adp2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spnDirection.setAdapter(adp2);

                    adp2 = new ArrayAdapter(AddPrescriptionActivity.this, android.R.layout.simple_spinner_item, strFrequency);
                    adp2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spnFrequency.setAdapter(adp2);

                    adp2 = new ArrayAdapter(AddPrescriptionActivity.this, android.R.layout.simple_spinner_item, strDrUnit);
                    adp2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spnDrUnit.setAdapter(adp2);


                }

                /*strDosage += etDosage.getText().toString() + ",,,";
                strDays += etDays.getText().toString() + ",,,";
                strPMeal += spnMeal.getSelectedItem().toString() + ",,,";
                strTt += strTimes + ",,,";
                strMtype+=spnMType.getSelectedItem().toString()+",,,";
                strMedicineDesc+=etMedicineDescripion.getText().toString()+",,,";*/

                // }

                /*etDays.setText("");
                etDosage.setText("");
                strTimes="";
                etMedicineDescripion.setText("");*/


                //listItems.add(etMedicine.getText().toString());
                /*if(spnMType.getSelectedItem().toString().equalsIgnoreCase("Medicine Type")){
                    Toast.makeText(getApplicationContext(),"Please Select Medicine Type !",Toast.LENGTH_LONG).show();
                    return;
                }
                if(spnMeal.getSelectedItem().toString().equalsIgnoreCase("Meal Type")){
                    Toast.makeText(getApplicationContext(),"Please Select Meal Type !",Toast.LENGTH_LONG).show();
                }else {
                   // strTimes=getDays();


                }*/
            }

        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position,
                                    long id) {
                Toast.makeText(AddPrescriptionActivity.this, Integer.toString(listView.getCount()), Toast.LENGTH_LONG)
                        .show();
                //strMedicine+=((TextView) v.findViewById(R.id.product)).getText().toString();
                android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(AddPrescriptionActivity.this
                );
                alertDialog.setMessage("Do you want remove medicine !");
                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        productList.remove(position);

                        listviewAdapter adapter = new listviewAdapter(AddPrescriptionActivity.this, productList);
                        listView.setAdapter(adapter);

                        adapter.notifyDataSetChanged();
                    }
                });
                alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Do nothing
                        dialog.dismiss();
                    }
                });

                alertDialog.create();
                alertDialog.show();


                //listView.removeViewAt(position);
            }
        });

        prescription_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (mMedicine == true) {
                    //Toast.makeText(getApplicationContext(),strMedicine,Toast.LENGTH_LONG).show();
                    submitForm(view);



                } else {
                    Toast.makeText(getApplicationContext(), "Please input mendatory fields", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void PickCase() {


        Log.e("Response", "citizen id is: " + citizenId);
        ApiInterface apiInterface = MyNewConfig.getRetrofit().create(ApiInterface.class);
        final retrofit2.Call<ResponseBody> result = (retrofit2.Call<ResponseBody>) apiInterface.ReferBySevika(2, "1", "0", citizenId, "2", token);

        result.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {


                try {
                    String output = response.body().string();


                    Log.e("Response", "Output: " + output);

                    JSONObject object = new JSONObject(output);

                    if (object.getString("status").equals("1")) {

                        //  Toast.makeText(AddPrescriptionActivity.this, "Picked & Prescribed", Toast.LENGTH_SHORT).show();
                    } else if (object.getString("status").equals("0")) {

                        Toast.makeText(AddPrescriptionActivity.this, "" + object.getString("message"), Toast.LENGTH_SHORT).show();
                    }


                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {

            }
        });

    }

    void submitForm(View v) {
//        Bundle bundle=getIntent().getExtras();
//        doctorId=bundle.getString("doctorId");
//        screenerId=bundle.getString("ScreenerId");
//        citizenId=bundle.getString("CitizenId");
//        recordId=bundle.getString("recordId");
//        caseId=bundle.getString("caseId");
//
//        Log.e("getIntent", "doctorId: " +doctorId);
//        Log.e("getIntent", "screenerId: " +screenerId);
//        Log.e("getIntent", "citizenId: " +citizenId);
//        Log.e("getIntent", "recordId: " +recordId);
//        Log.e("getIntent", "caseId: " +caseId);
        ErrBox.errorsStatus();
        // Toast.makeText(MyConfig.CONTEXT,strMedicineDesc,Toast.LENGTH_SHORT).show();
        HashMap<String, String> paramHash = new HashMap<String, String>();
        paramHash.put("doctorId", Config.javixid);
        paramHash.put("screenerId", screenerId);
        paramHash.put("citizenId", citizenId);
        paramHash.put("recordId", recordId);
        paramHash.put("caseId", caseId);
        paramHash.put("status", "3");
        paramHash.put("medicine", strMedicine);
        paramHash.put("strength", strStrength);
        paramHash.put("quantity", strQty);
        paramHash.put("duaration", strDuration);
        paramHash.put("direction", strDir);
        paramHash.put("frequency", strFreq);
        paramHash.put("preparation", strPrep);
        paramHash.put("dose", strDosage);
        paramHash.put("preparation", strPrep);
        paramHash.put("route", strRoute);
        paramHash.put("cause", etCause.getText().toString());
        paramHash.put("tests", etTests.getText().toString());
        paramHash.put("comments", etMedicineDescripion.getText().toString());
        paramHash.put("ngoId", Config.NGO_ID);

        Log.e("Atul", "add Prescription: " +paramHash);
        SubmitForm req = new SubmitForm(this, paramHash);
        req.execute(MyConfig.URL_ADD_PRESCRIPTION);
        Log.e("Hash parameter", "else part : " + paramHash);
    }

    //PUSH DATA
    class SubmitForm extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        Activity activity;
        HashMap<String, String> paramsHash = null;
        RequestPipe requestPipe = new RequestPipe();

        public SubmitForm(Activity activity, HashMap<String, String> paramsHash) {
            this.activity = activity;
            this.paramsHash = paramsHash;
            progressDialog = new ProgressDialog(activity);
            progressDialog.setMessage("wait...");
        }

        @Override
        protected void onPreExecute() {
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            return requestPipe.requestForm(params[0], paramsHash);
        }

        protected void onProgressUpdate(Void... progress) {
            super.onProgressUpdate(progress);
            //ProgressDialog.show(MainActivity.this, "loading", "wait...", true, true);
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.cancel();
            //Toast.makeText(MyConfig.CONTEXT,"RES:"+result,Toast.LENGTH_SHORT).show();
            try {
                JSONObject jsonObject = new JSONObject(result);
                int respStatus = jsonObject.getInt("status");
                if (respStatus == 1) {
                    /*Intent i =new Intent(AddPrescriptionActivity.this,ViewPrescriptionActivity.class);
                    i.putExtra("caseId",caseId);
                    startActivity(i);*/

                    PickCase();

                    MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                    RequestBody body = RequestBody.create(mediaType, "caseId=" + caseId + "&ngoId=" + Config.NGO_ID);
                    String url = "http://143.244.136.145:3010/api/report/createPrescriptionReport";
//                    String url = "http://192.168.1.195:3010/api/report/createPrescriptionReport";
//                    String url="http://159.65.148.197:3001/api/report/createPrescriptionReport";  // Local IP just for testing
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(url)
                            .post(body)
                            .addHeader("content-type", "application/x-www-form-urlencoded")
                            .addHeader("cache-control", "no-cache")
                            .addHeader("postman-token", "08b43f25-b3a6-b582-35c8-25cfdad00694")
                            .build();
                    System.out.println("OKAY CLIENT");
                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            System.out.println("JCANCEL " + e.toString());
                            call.cancel();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            final String myResponse = response.body().string();
                            try {
                                JSONObject jsonObject = new JSONObject(myResponse);
                                if (jsonObject.getInt("status") == 1) {
                                    JSONObject data = jsonObject.getJSONObject("data");
                                    data = data.getJSONObject("data");
                                    String fileName = data.getString("filename");
                                    Intent intx = new Intent(Intent.ACTION_VIEW);
                                    intx.setData(Uri.parse(fileName));
                                    intx.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    MyConfig.CONTEXT.startActivity(intx);

                                    PickCase();

                                }
                            } catch (Exception ee) {
                            }
                        }
                    });

                    Intent i = new Intent(AddPrescriptionActivity.this, ScreeningPickedActivity.class);
                    finish();
                    startActivity(i);
                    //Toast.makeText(MyConfig.CONTEXT, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(MyConfig.CONTEXT, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

            } catch (Exception ee) {
                Toast.makeText(MyConfig.CONTEXT, "Opps !.", Toast.LENGTH_SHORT).show();
            }

        }

    }

    public String getDays() {
        String strTimes = "";
        if (chkMorning.isChecked()) {
            strTimes = "0";
        }
        if (chkEvening.isChecked()) {
            if (strTimes.length() > 0) {
                strTimes += "-0";
            } else {
                strTimes += "0";
            }
        }
        if (chkAfternoon.isChecked()) {
            if (strTimes.length() > 0) {
                strTimes += "-0";
            } else {
                strTimes += "0";
            }
        }
        return strTimes;
    }

}