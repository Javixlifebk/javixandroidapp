package com.sumago.latestjavix;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.sumago.latestjavix.Util.Config;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

class JFloat {
    static float parseFloat(String str) {
        try {
            return Float.parseFloat(str);
        } catch (Exception e) {
            return (0);
        }
    }
}

public class ScreeningActivity extends AppCompatActivity {

    Button cmdSubmit = null;
    EditText etHeight, etWeight, etBmi, etResp, etNotes;
    TextInputLayout etBpsys, etBpdia, etSpo2, etPulse, etTemp, etArm;
    String strBpsys = "", strBpdia = "";
    String CitizenId = "", doctorId = "";
    String ScreenerId = "";
    TextView txNotes;
    TextView tv_feet, tv_inches, tv_weight, tv_bmi;
    Spinner spnInches, spnFeet;

    double _varHeight = 0.0, _feet = 0.0, _inches = 0.00;
    boolean mSys, mDis, mFeet, mInches, mWeight, _flag = true;
    private ToggleButton toggleLng = null;
    boolean HINDI = false;
    SQLiteDatabase db;


    String getInputMsg() {
        if (HINDI == true) return "कृपया डाटा डाले!.";
        return "Please Input Data!.";
    }

    String getWeightInputMsg() {
        if (HINDI == true) return "अमान्य वजन (किग्रा)";
        return "Invalid Weight (Kg)";
    }

    String getBPSysInputMsg() {
        if (HINDI == true) return "अमान्य रक्त चाप (Sys) - 60 से 300 के बीच दर्ज करें";
        return "Invalid BP (Sys) - Enter between 60 to 300";
    }

    String getBPDiaInputMsg() {
        if (HINDI == true) return "अमान्य रक्त चाप (Dia) - 30 से 200 के बीच दर्ज करें";
        return "Invalid BP (Dia) - Enter between 30 to 200";
    }

    String getSpO2InputMsg() {
        if (HINDI == true) return "अमान्य ऑक्सीजन संतृप्ति(%) - 30 से 100 के बीच दर्ज करें";
        return "Invalid SpO2(%) - Enter between 30 to 100";
    }

    String getPulseInputMsg() {
        if (HINDI == true) return "अमान्य धड़कन का स्तर - 40 से 200 के बीच दर्ज करें";
        return "Invalid Pulse - Enter between 40 to 200";
    }

    String getTemperatueInputMsg() {
        if (HINDI == true) return "अमान्य तापमान (F) - 94 से 105 के बीच दर्ज करें";
        return "Invalid Temperature - Enter between 94 to 105";
    }

    String getBMIInputMessage() {
        if (HINDI == true) return "अमान्य बीएमआई - यह 12 से 60 के बीच होना चाहिए";
        return "Invalid BMI - It should be between 12 to 60";
    }

    String getArmInputMsg() {
        if (HINDI == true) return "अमान्य बाजू - 7 से 33 के बीच दर्ज करें";
        return "Invalid Arm (cm) - Enter between 7 to 33";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screening);
        MyConfig.CONTEXT = this;
        toggleLng = (ToggleButton) findViewById(R.id.toggleLng);

        tv_feet = findViewById(R.id.tv_feet);
        tv_inches = findViewById(R.id.tv_inches);
        tv_weight = findViewById(R.id.tv_weight);
        tv_bmi = findViewById(R.id.tv_bmi);

        toggleLng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String lng = toggleLng.getText().toString();
                if (lng.equalsIgnoreCase("Eng"))
                    HINDI = false;
                else HINDI = true;
                toSelectedLng();
            }
        });
        cmdSubmit = (Button) findViewById(R.id.addScreenerProfile_submit);
        cmdSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitForm(v);

            }
        });
        initViews();
    }

    String getWeightInputLabel() {
        if (HINDI == true) return "वजन (किग्रा)";
        return "Weight (Kg)";
    }

    String getWeightLabel() {
        if (HINDI == true) return "वजन";
        return "Weight";
    }

    String getFeetLabel() {
        if (HINDI == true) return "फीट";
        return "Feet";
    }

    String getInchesLabel() {
        if (HINDI == true) return "इंच";
        return "Inches";
    }

    String getBMILabel() {
        if (HINDI == true) return "बीएमआई";
        return "BMI";
    }

    String getBmiInputLabel() {
        if (HINDI == true) return "बीएमआई";
        return "BMI";
    }

    String getBpsysInputLabel() {
        if (HINDI == true) return "रक्त चाप (Sys)";
        return "BP (Sys)";
    }

    String getBpdiaInputLabel() {
        if (HINDI == true) return "रक्त चाप (Dia)";
        return "BP (Dia)";
    }

    String getSpo2InputLabel() {
        if (HINDI == true) return "ऑक्सीजन संतृप्ति(%)";
        return "SpO2 (%)";
    }

    String getPulseInputLabel() {
        if (HINDI == true) return "धड़कन का स्तर";
        return "Pulse";
    }

    String getTempInputLabel() {
        if (HINDI == true) return "तापमान (F)";
        return "Temperature (F)";
    }

    String getArmInputLabel() {
        if (HINDI == true) return "बाजू (cm)";
        return "Arm (cm)";
    }

    String getTitle1Label() {
        if (HINDI == true) return "शारीरिक माप";
        return "body Measurements";
    }

    String getTitle2Label() {
        if (HINDI == true) return "महत्वपूर्ण संकेत";
        return "Vital Signs";
    }

    String getTitle3Label() {
        if (HINDI == true) return "टिप्पणियाँ जोड़ें";
        return "Add Notes";
    }

    String getCMDLabel() {
        if (HINDI == true) return "फॉर्म जमा करें";
        return "Submit";
    }

    String getSubmitmsg() {
        if (HINDI == true) return "स्क्रीनिंग सफलतापूर्वक की गई।";
        return "Screening done successfully.";
    }

    void toSelectedLng() {
        etWeight.setHint(getWeightInputLabel());
        etBmi.setHint(getBmiInputLabel());
        etBpsys.setHint(getBpsysInputLabel());
        etBpdia.setHint(getBpdiaInputLabel());
        etSpo2.setHint(getSpo2InputLabel());
        etPulse.setHint(getPulseInputLabel());
        //etResp.setHint(getRespInputLabel());
        etTemp.setHint(getTempInputLabel());
        etArm.setHint(getArmInputLabel());
        cmdSubmit.setText(getCMDLabel());
        TextView title1 = (TextView) findViewById(R.id.tv_ac_title1);
        title1.setText(getTitle1Label());
        TextView title2 = (TextView) findViewById(R.id.tv_ac_title2);
        title2.setText(getTitle2Label());
        TextView title3 = (TextView) findViewById(R.id.txNotes);
        title3.setText(getTitle3Label());

        tv_feet.setText(getFeetLabel());
        tv_inches.setText(getInchesLabel());
        tv_weight.setText(getWeightLabel());
        tv_bmi.setText(getBMILabel());


    }

    public void initViews() {
        //etHeight=(EditText)findViewById(R.id.etHeight);
        etWeight = (EditText) findViewById(R.id.etWeight);
        etBmi = (EditText) findViewById(R.id.etBmi);
        etBpsys = findViewById(R.id.etBpsys);
        etBpdia = findViewById(R.id.etBpdia);
        etSpo2 = findViewById(R.id.etSpo2);
        etPulse = findViewById(R.id.etPulse);
        etResp = findViewById(R.id.etResp);
        etTemp = findViewById(R.id.etTemp);
        etArm = findViewById(R.id.etArm);
        toSelectedLng();
        Bundle bundle = getIntent().getExtras();
        if (Config._roleid == 1) {
            doctorId = Config._doctorid;
        } else {
            ScreenerId = Config.NGO_ID;
        }
        CitizenId = Config.tmp_citizenId;
        txNotes = (TextView) findViewById(R.id.txNotes);
        etNotes = (EditText) findViewById(R.id.etNotes);
        spnFeet = (Spinner) findViewById(R.id.spnFeet);
        spnInches = (Spinner) findViewById(R.id.spnInches);
        ArrayAdapter<CharSequence> adp1 = ArrayAdapter.createFromResource(this, R.array.strFeed, android.R.layout.simple_spinner_item);
        adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnFeet.setAdapter(adp1);

        ArrayAdapter<CharSequence> adp2 = ArrayAdapter.createFromResource(this, R.array.strInches, android.R.layout.simple_spinner_item);
        adp2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnInches.setAdapter(adp2);


        txNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etNotes.setVisibility(View.VISIBLE);
            }
        });

        spnFeet.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                boolean _flag = false;
                if (!spnFeet.getItemAtPosition(position).equals("Feet")) {
                    _feet = Double.parseDouble(spnFeet.getItemAtPosition(position).toString());
                    _flag = true;
                } else {
                    _flag = false;
                }
                if (!spnInches.getSelectedItem().equals("Inches")) {
                    _inches = Double.parseDouble(spnInches.getSelectedItem().toString());
                    _flag = true;
                } else {
                    _flag = false;
                }

                if (_flag == true) {
                    _varHeight = _feet * 30.48 + _inches * 2.54;
                    //Toast.makeText(getApplicationContext(),Double.toString(_varHeight),Toast.LENGTH_LONG).show();
                    calculateBMI();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });


        spnInches.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {


                boolean _flag = false;
                if (!spnFeet.getSelectedItem().equals("Feet")) {
                    _feet = Double.parseDouble(spnFeet.getSelectedItem().toString());
                    _flag = true;
                } else {
                    _flag = false;
                }
                if (!spnInches.getItemAtPosition(position).equals("Inches")) {
                    _inches = Double.parseDouble(spnInches.getItemAtPosition(position).toString());
                    _flag = true;
                } else {
                    _flag = false;
                }

                if (_flag == true) {
                    _varHeight = _feet * 30.48 + _inches * 2.54;
                    //Toast.makeText(getApplicationContext(),Double.toString(_varHeight),Toast.LENGTH_LONG).show();
                    calculateBMI();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        etWeight.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                Log.e("Response", "new feet" + spnFeet.getSelectedItem());
                if (!(spnFeet.getSelectedItem().equals("Feet") && spnInches.getSelectedItem().equals("Inches"))) {
                    String weightStr = etWeight.getText().toString();
                    try {
                        float _V = JFloat.parseFloat(weightStr);
                        if (_V <= 200) {
                            calculateBMI();
                        } else
                            Toast.makeText(etWeight.getContext(), "Invalid Weight", Toast.LENGTH_SHORT).show();
                    } catch (Exception ee) {

                    }
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void afterTextChanged(Editable s) {
            }
        });

    }

    public void calculateBMI() {
        String heightStr = Double.toString(_varHeight);
        String weightStr = etWeight.getText().toString();
        float bmi = 0;
        double bmi1 = 0;

        if (heightStr != null && !"".equals(heightStr)
                && weightStr != null && !"".equals(weightStr)) {
            float heightValue = JFloat.parseFloat(heightStr) / 100;
            float weightValue = JFloat.parseFloat(weightStr);
            bmi = weightValue / (heightValue * heightValue);
            bmi = JFloat.parseFloat(String.format("%.2f", bmi));
            bmi1 = bmi;
            //etBmi.setText(Double.toString(bmi));
            //bmi1=Integer.parseInt(etBmi.getText().toString());
            etBmi.setText(Float.toString(bmi));
        }
    }

    void submitForm(View v) {
        boolean submitFlag = false;
        try {
            String _SV = "";

            float _V = (float) _feet;
            if (_V > 0) {
                _V = JFloat.parseFloat(etWeight.getText().toString());
                if (_V <= 0 || _V > 200) {
                    Toast.makeText(ScreeningActivity.this, getWeightInputMsg(), Toast.LENGTH_SHORT).show();
                    return;
                }
                submitFlag = true;
            }
            _SV = etBpsys.getEditText().getText().toString();
            if (_SV != null && !_SV.equals("") && _SV.trim().length() >= 1) {
                _V = JFloat.parseFloat(etBpsys.getEditText().getText().toString());
                Log.e("Response", "_V value: " + _V);
                if (_V > 300 || _V < 60) {
                    Toast.makeText(ScreeningActivity.this, getBPSysInputMsg(), Toast.LENGTH_SHORT).show();
                    return;
                }
                submitFlag = true;
            }
            if (_SV != null && !_SV.equals("") && _SV.trim().length() >= 1) {
                _SV = etBpdia.getEditText().getText().toString();
                if (_SV != null && !_SV.equals("") && _SV.trim().length() >= 1) {
                    _V = JFloat.parseFloat(etBpdia.getEditText().getText().toString());
                    if (_V < 30 || _V > 200) {
                        Toast.makeText(ScreeningActivity.this, getBPDiaInputMsg(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    submitFlag = true;
                } else {
                    Toast.makeText(ScreeningActivity.this, getBPDiaInputMsg(), Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            _SV = etSpo2.getEditText().getText().toString();
            if (_SV != null && !_SV.equals("") && _SV.trim().length() >= 1) {
                _V = JFloat.parseFloat(etSpo2.getEditText().getText().toString());
                if (_V < 30 || _V > 100) {
                    Toast.makeText(ScreeningActivity.this, getSpO2InputMsg(), Toast.LENGTH_SHORT).show();
                    return;
                }
                submitFlag = true;
            }
            _SV = etPulse.getEditText().getText().toString();
            if (!_SV.equals("") && _SV.trim().length() >= 1) {
                _V = JFloat.parseFloat(etPulse.getEditText().getText().toString());
                if (_V < 40 || _V > 200) { // old ranges _V < 40 || _V > 170
                    Toast.makeText(ScreeningActivity.this, getPulseInputMsg(), Toast.LENGTH_SHORT).show();
                    return;
                }
                submitFlag = true;
            }
            _SV = etTemp.getEditText().getText().toString();
            if (_SV != null && !_SV.equals("") && _SV.trim().length() >= 1) {
                _V = JFloat.parseFloat(etTemp.getEditText().getText().toString());
                if (_V < 94 || _V > 105) { // (_V < 91 || _V > 107)
                    Toast.makeText(ScreeningActivity.this, getTemperatueInputMsg(), Toast.LENGTH_SHORT).show();
                    return;
                }
                submitFlag = true;
            }

            _SV = etBmi.getText().toString();
            if (_SV != null && !_SV.equals("") && _SV.trim().length() >= 1) {
                _V = JFloat.parseFloat(etBmi.getText().toString());
                if (_V < 12 || _V > 60) {
                    Toast.makeText(this, getBMIInputMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }
                submitFlag = true;
            }

            _SV = etArm.getEditText().getText().toString();
            if (_SV != null && !_SV.equals("") && _SV.trim().length() >= 1) {
                _V = JFloat.parseFloat(etArm.getEditText().getText().toString());
                if (_V > 33 || _V < 7) { //(_V>85)
                    Toast.makeText(ScreeningActivity.this, getArmInputMsg(), Toast.LENGTH_SHORT).show();
                    return;
                }
                submitFlag = true;
            }

//            if (etWeight.getText().toString().equals("")  || spnFeet.getSelectedItem().equals("Feet") || spnInches.getSelectedItem().equals("Inches")){
//
//
//                Toast.makeText(this, "invalid height", Toast.LENGTH_SHORT).show();
//                return;
//
//            }

            Log.e("Response", "weight: " + etWeight.getText().toString().trim().equals(""));
            if (!etWeight.getText().toString().trim().equals("")) {

                if (spnFeet.getSelectedItem().equals("Feet") || spnInches.getSelectedItem().equals("Inches")) {

                    Toast.makeText(this, "Invalid Height", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            if (!etBpdia.getEditText().getText().toString().trim().equals("")) {
                if (etBpsys.getEditText().getText().toString().trim().equals("")) {
                    Toast.makeText(this, getBPSysInputMsg(), Toast.LENGTH_SHORT).show();
                    return;
                }
            }


        } catch (Exception ee) {
            System.out.println("EXCPEYON 5983758923 :" + ee);
            submitFlag = false;
        }
        if (submitFlag == false) {
            Toast.makeText(ScreeningActivity.this, getInputMsg(), Toast.LENGTH_SHORT).show();
            return;
        }

        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String currentDate = simpleDateFormat.format(new Date());

        ErrBox.errorsStatus();
        // Toast.makeText(MyConfig.CONTEXT,""+ ErrBox.FIRST_ERR_MSG,Toast.LENGTH_SHORT).show();

        HashMap<String, String> paramHash = new HashMap<String, String>();
        Log.e("check", "CitizenId: " + CitizenId);
        paramHash.put("citizenId", CitizenId);
        paramHash.put("status", "1");
        if (Config._roleid == 1) {
            paramHash.put("doctorId", doctorId);
        } else {
            paramHash.put("screenerId", Config.javixid);
        }
        paramHash.put("height", Double.toString(_varHeight));
        paramHash.put("weight", etWeight.getText().toString());
        paramHash.put("bmi", etBmi.getText().toString());
        paramHash.put("bpsys", etBpsys.getEditText().getText().toString());
        paramHash.put("bpdia", etBpdia.getEditText().getText().toString());
        paramHash.put("spo2", etSpo2.getEditText().getText().toString());
        if (etNotes.getText().length() > 0) {
            paramHash.put("notes", etNotes.getText().toString());
        }
        paramHash.put("pulse", etPulse.getEditText().getText().toString());
        paramHash.put("respiratory_rate", etResp.getText().toString());
        paramHash.put("temperature", etTemp.getEditText().getText().toString());
        paramHash.put("arm", etArm.getEditText().getText().toString());
        paramHash.put("ngoId", Config.NGO_ID.toString());

        if (Config.isOffline) {
            Random rnd = new Random();
            char[] digits = new char[15];
            digits[0] = (char) (rnd.nextInt(9) + '1');
            for (int i = 1; i < digits.length; i++) {
                digits[i] = (char) (rnd.nextInt(10) + '0');
            }
            String uniqueID = new String(digits);
            paramHash.put("caseId", uniqueID);
            Config.tmp_caseId = uniqueID;
            //paramHash.put("caseId",etArm.getText().toString());
            db = openOrCreateDatabase("javixlife", Context.MODE_PRIVATE, null);
            JSONObject jsonObject = new JSONObject(paramHash);
            String SqlStr = "INSERT INTO javix_update(service_type,service_name,service_data,data_json,insert_date,_status)";
            SqlStr += " VALUES('Screening','" + MyConfig.URL_ADD_CASE + "','" + paramHash.toString() + "','" + jsonObject.toString() + "','" + currentDate + "',0);";
            //Toast.makeText(getApplicationContext(), SqlStr, Toast.LENGTH_LONG).show();
            Log.e("Offline", SqlStr);
            db.execSQL(SqlStr);
            db.close();
            android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(ScreeningActivity.this
            );
            alertDialog.setMessage(ScreeningActivity.this.getString(R.string._screeningAdd_he));
            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    Intent i = new Intent(ScreeningActivity.this, TestListActivity.class);
                    //Config.tmp_caseId="0";
                    finish();
                    startActivity(i);
                }
            });
            alertDialog.create();
            alertDialog.show();
        } else {
            SubmitForm req = new SubmitForm(this, paramHash);
            Log.e("param", "param: " + paramHash.toString());
            req.execute(MyConfig.URL_ADD_CASE);
        }
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
            progressDialog.setMessage("loading");
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
                JSONObject recsData = jsonObject.getJSONObject("data");
                recsData = recsData.getJSONObject("data");
                Log.e("Response in Array", recsData.toString());
                if (respStatus == 1) {
                    Config.tmp_caseId = recsData.getString("caseId");
                    Log.e("CaseID", Config.tmp_caseId);
                    android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(ScreeningActivity.this
                    );
//                    alertDialog.setMessage(ScreeningActivity.this.getString(R.string._screeningAdd_he));
                    alertDialog.setMessage(getSubmitmsg());

                    alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            Intent i = new Intent(ScreeningActivity.this, TestListActivity.class);
                            //Config.tmp_caseId="0";
                            finish();
                            startActivity(i);
                        }
                    });
                    alertDialog.create();
                    alertDialog.show();
                    //Toast.makeText(MyConfig.CONTEXT, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(MyConfig.CONTEXT, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

            } catch (Exception ee) {
                Toast.makeText(MyConfig.CONTEXT, "Opps !.", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void onBackPressed() {
        Log.d("CDA", "onBackPressed Called");
        this.finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // your code
            Log.e("Back Button", "Pressed");
            Intent i = new Intent(ScreeningActivity.this, TestListActivity.class);
            finish();
            startActivity(i);
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}