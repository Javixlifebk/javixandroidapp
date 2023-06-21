package com.sumago.latestjavix;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.sumago.latestjavix.Util.Config;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

public class SocioEconomic extends AppCompatActivity {
    EditText noOfEarners,nameOfEarners,ageOfEarners;
    Spinner occupationOfEarners,isBankAccount,statusOfHouse,totalIncome,foodExpense,healthExpense,educationExpense,intoxicationExpense;
    Spinner conveyanceExpense,cultivableLand;
    Button btnSubmit;
    AppCompatButton btnadd;
    String[] strOccupation = { "Occupation","Government job/ large scale to middle scale industry", "Professional job in private sector/Small scale industries/big shop owner","Technician/craftsman/small shop owner/other skills (driver, mason etc)/large scale\n" +
            "farmer","Daily wage earner/small scale farmer/farm worker","Unemployed"};
    String[] strBankAccount ={"Bank Account","Yes","No"};
    String[] strHouse ={"House","Kutcha House","Pakka House","No House"};
    String[] strIncome ={"Total Income","0 - 30,000","30,000 - 50,000","50,000 - 1,00,000","1,00,000 - 2,50,000","More than 2,50,000"};
    String[] strFoodExpencse ={"Food Expense","0 - 1,500","1,500 - 2,500","2,500 - 5,000","5,000 - 10,000","More than 10,000"};
    String[] strHealthExpense ={"Health Expense","0 - 3,000","3,000 - 5,000","5,000 - 10,000","10,000 - 25,000","More than 25,000"};
    String[] strEducationExpense ={"Education Expense","0 - 3,000","3,000 - 5,000","5,000 - 10,000","10,000 - 25,000","More than 25,000"};
    String[] strIntoxicationExpense ={"Intoxication Expense","0 - 600","600 - 1,000","1,000 -1,500","1,500 - 2,500","More than 2,500"};
    String[] strConveyanceExpense ={"Conveyance Expense","0 - 10,000","10,000 - 20,000","20,000 - 40,000","40,000 - 1,00,000","More than 1,00,000"};
    String[] strCultivableLand ={"Cultivable Land Sq.Ft","0-100,000 Sq.Ft","100,000-200,000 Sq.Ft","200,000-500,000 Sq.Ft","500,000-10,00,000 Sq.Ft","More than 10,00,000 Sq.Ft"};

    Context context;
    //Hindi Section
    TextView lblHeader,lblInfo,lblFamilyId;
    String lbHeader_Hn="सामाजिक आर्थिक सर्वेक्षण ",lbHeader_En="Socioecomonic Survey",strInfo_Hn="परिवार चुने",strInfo_En="Select Family";
    String noOfEarners_Hn="कमाने वालों की संख्या ",noOfEarners_En="No of Earners",nameOfEarners_Hn="कमाने वालों का नाम ",nameOfEarners_En="Name of Earners";
    String ageOfEarners_Hn="कमाने वालों की आयु ",ageOfEarners_En="Age of Earners";
    RadioButton radioHindi,radioEnglish;
    String[] strOccupation_Hn = { "पेशा ","सरकारी नौकरी/बड़े पैमाने से मध्यम स्तर के उद्योग ", "निजी क्षेत्र में पेशेवर नौकरी/लघु उद्योग/बड़े दुकान के मालिक","तकनीशियन / शिल्पकार / छोटी दुकान के मालिक / अन्य कौशल (चालक, राजमिस्त्री आदि) / बड़े पैमाने पर" +
            "किसान","दैनिक वेतन भोगी/छोटे पैमाने के किसान/कृषि कार्यकर्ता ","बेरोज़गार "};
    String[] strBankAccount_Hn ={"बैंक खाता ","हां","नहीं"};
    String[] strHouse_Hn ={"मकान","कच्चा घर","पक्का घर","कोई घर नहीं"};
    String[] strIncome_Hn={"कुल आय","0 - 30,000","30,000 - 50,000","50,000 - 1,00,000","1,00,000 - 2,50,000","2,50,000 से अधिक"};
    String[] strFoodExpencse_Hn ={"भोजन व्यय","0 - 1,500","1,500 - 2,500","2,500 - 5,000","5,000 - 10,000","10,000. से अधिक"};
    String[] strHealthExpense_Hn ={"स्वास्थ्य व्यय","0 - 3,000","3,000 - 5,000","5,000 - 10,000","10,000 - 25,000","25,000. से अधिक"};
    String[] strEducationExpense_Hn ={"शिक्षा व्यय","0 - 3,000","3,000 - 5,000","5,000 - 10,000","10,000 - 25,000","25,000. से अधिक"};
    String[] strIntoxicationExpense_Hn ={"नशा खर्च","0 - 600","600 - 1,000","1,000 -1,500","1,500 - 2,500","2,500 से अधिक"};
    String[] strConveyanceExpense_Hn ={"वाहन व्यय","0 - 10,000","10,000 - 20,000","20,000 - 40,000","40,000 - 1,00,000","1,00,000 से अधिक"};
    String[] strCultivableLand_Hn ={"कृषि योग्य भूमि वर्ग फुट","0-100,000 वर्ग फुट","100,000-200,000 वर्ग फुट","200,000-500,000 वर्ग फुट","500,000-10,00,000 वर्ग फुट","More than 10,00,000 वर्ग फुट"};
    //String[] strCultivableLand_Hn ={"कृषि योग्य भूमि वर्ग फुट","0-100,000 हेक्टेयर","1 - 2 हेक्टेयर","2 - 5 हेक्टेयर","5 - 10 हेक्टेयर","10 हेक्टेयर से अधिक "};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socio_economic);
        lblFamilyId=(TextView) findViewById(R.id.lblFamilyId);
        lblHeader=(TextView) findViewById(R.id.lblHeader);
        lblInfo=(TextView) findViewById(R.id.lblInfo);
        noOfEarners=(EditText)findViewById(R.id.noOfEarners);
        nameOfEarners=(EditText)findViewById(R.id.nameOfEarners);
        ageOfEarners=(EditText)findViewById(R.id.ageOfEarners);
        occupationOfEarners=(Spinner)findViewById(R.id.occupationOfEarners);
        isBankAccount=(Spinner)findViewById(R.id.isBankAccount);
        statusOfHouse=(Spinner)findViewById(R.id.statusOfHouse);
        totalIncome=(Spinner)findViewById(R.id.totalIncome);
        foodExpense=(Spinner)findViewById(R.id.foodExpense);
        healthExpense=(Spinner)findViewById(R.id.healthExpense);
        educationExpense=(Spinner)findViewById(R.id.educationExpense);
        intoxicationExpense=(Spinner)findViewById(R.id.intoxicationExpense);
        conveyanceExpense=(Spinner)findViewById(R.id.conveyanceExpense);
        cultivableLand=(Spinner)findViewById(R.id.cultivableLand);
        btnadd=(AppCompatButton)findViewById(R.id.btnadd);
        radioEnglish = (RadioButton) findViewById(R.id.radioEnglish);
        radioHindi = (RadioButton) findViewById(R.id.radioHindi);
        if(Config.tmp_familyid!=null)
        lblFamilyId.setText("Family ID : " + Config.tmp_familyid);
        radioEnglish.setActivated(true);
        radioEnglish.setChecked(true);
        context=this;
        ArrayAdapter adp1 = new ArrayAdapter(context,android.R.layout.simple_spinner_item,strOccupation);
        adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        occupationOfEarners.setAdapter(adp1);


        adp1 = new ArrayAdapter(context,android.R.layout.simple_spinner_item,strBankAccount);
        adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        isBankAccount.setAdapter(adp1);

        adp1 = new ArrayAdapter(context,android.R.layout.simple_spinner_item,strHouse);
        adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusOfHouse.setAdapter(adp1);


        adp1 = new ArrayAdapter(context,android.R.layout.simple_spinner_item,strIncome);
        adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        totalIncome.setAdapter(adp1);

        adp1 = new ArrayAdapter(context,android.R.layout.simple_spinner_item,strFoodExpencse);
        adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        foodExpense.setAdapter(adp1);

        adp1 = new ArrayAdapter(context,android.R.layout.simple_spinner_item,strHealthExpense);
        adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        healthExpense.setAdapter(adp1);

        adp1 = new ArrayAdapter(context,android.R.layout.simple_spinner_item,strEducationExpense);
        adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        educationExpense.setAdapter(adp1);

        adp1 = new ArrayAdapter(context,android.R.layout.simple_spinner_item,strIntoxicationExpense);
        adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        intoxicationExpense.setAdapter(adp1);

        adp1 = new ArrayAdapter(context,android.R.layout.simple_spinner_item,strConveyanceExpense);
        adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        conveyanceExpense.setAdapter(adp1);

        adp1 = new ArrayAdapter(context,android.R.layout.simple_spinner_item,strCultivableLand);
        adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cultivableLand.setAdapter(adp1);

        radioEnglish.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    lblHeader.setText(lbHeader_En);
                    lblInfo.setText(strInfo_En);
                    noOfEarners.setHint(noOfEarners_En);
                    nameOfEarners.setHint(nameOfEarners_En);
                    ageOfEarners.setHint(ageOfEarners_En);

                    ArrayAdapter adp1 = new ArrayAdapter(context,android.R.layout.simple_spinner_item,strOccupation);
                    adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    occupationOfEarners.setAdapter(adp1);


                    adp1 = new ArrayAdapter(context,android.R.layout.simple_spinner_item,strBankAccount);
                    adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    isBankAccount.setAdapter(adp1);

                    adp1 = new ArrayAdapter(context,android.R.layout.simple_spinner_item,strHouse);
                    adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    statusOfHouse.setAdapter(adp1);


                    adp1 = new ArrayAdapter(context,android.R.layout.simple_spinner_item,strIncome);
                    adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    totalIncome.setAdapter(adp1);

                    adp1 = new ArrayAdapter(context,android.R.layout.simple_spinner_item,strFoodExpencse);
                    adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    foodExpense.setAdapter(adp1);

                    adp1 = new ArrayAdapter(context,android.R.layout.simple_spinner_item,strHealthExpense);
                    adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    healthExpense.setAdapter(adp1);

                    adp1 = new ArrayAdapter(context,android.R.layout.simple_spinner_item,strEducationExpense);
                    adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    educationExpense.setAdapter(adp1);

                    adp1 = new ArrayAdapter(context,android.R.layout.simple_spinner_item,strIntoxicationExpense);
                    adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    intoxicationExpense.setAdapter(adp1);

                    adp1 = new ArrayAdapter(context,android.R.layout.simple_spinner_item,strConveyanceExpense);
                    adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    conveyanceExpense.setAdapter(adp1);

                    adp1 = new ArrayAdapter(context,android.R.layout.simple_spinner_item,strCultivableLand);
                    adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    cultivableLand.setAdapter(adp1);
                }
            }
        });

        radioHindi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){

                    lblHeader.setText(lbHeader_Hn);
                    lblInfo.setText(strInfo_Hn);
                    noOfEarners.setHint(noOfEarners_Hn);
                    nameOfEarners.setHint(nameOfEarners_Hn);
                    ageOfEarners.setHint(ageOfEarners_Hn);
                    ArrayAdapter adp1 = new ArrayAdapter(context,android.R.layout.simple_spinner_item,strOccupation_Hn);
                    adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    occupationOfEarners.setAdapter(adp1);


                    adp1 = new ArrayAdapter(context,android.R.layout.simple_spinner_item,strBankAccount_Hn);
                    adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    isBankAccount.setAdapter(adp1);

                    adp1 = new ArrayAdapter(context,android.R.layout.simple_spinner_item,strHouse_Hn);
                    adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    statusOfHouse.setAdapter(adp1);


                    adp1 = new ArrayAdapter(context,android.R.layout.simple_spinner_item,strIncome_Hn);
                    adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    totalIncome.setAdapter(adp1);

                    adp1 = new ArrayAdapter(context,android.R.layout.simple_spinner_item,strFoodExpencse_Hn);
                    adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    foodExpense.setAdapter(adp1);

                    adp1 = new ArrayAdapter(context,android.R.layout.simple_spinner_item,strHealthExpense_Hn);
                    adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    healthExpense.setAdapter(adp1);

                    adp1 = new ArrayAdapter(context,android.R.layout.simple_spinner_item,strEducationExpense_Hn);
                    adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    educationExpense.setAdapter(adp1);

                    adp1 = new ArrayAdapter(context,android.R.layout.simple_spinner_item,strIntoxicationExpense_Hn);
                    adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    intoxicationExpense.setAdapter(adp1);

                    adp1 = new ArrayAdapter(context,android.R.layout.simple_spinner_item,strConveyanceExpense_Hn);
                    adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    conveyanceExpense.setAdapter(adp1);

                    adp1 = new ArrayAdapter(context,android.R.layout.simple_spinner_item,strCultivableLand_Hn);
                    adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    cultivableLand.setAdapter(adp1);
                }
            }
        });

        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), GeneralSurveyList.class);
                i.putExtra("_type","Socioeconomic");
                startActivity(i);
                //finish();
            }
        });

        btnSubmit=(Button)findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitForm(v);
                /*android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(SocioEconomic.this
                );
                alertDialog.setMessage("Socioeconomic Survey Done Successfully !");
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Intent i = new Intent(SocioEconomic.this, SurveyMenuActivity.class);
                        finish();
                        startActivity(i);
                    }
                });
                alertDialog.create();
                alertDialog.show();*/
            }
        });
    }

    void submitForm(View v){
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String currentDate = simpleDateFormat.format(new Date());
        ErrBox.errorsStatus();
        HashMap<String,String> paramHash=new HashMap<String,String>();

        paramHash.put("citizenId", "[" + Config.tmp_citizenId_survey +"]");
        paramHash.put("familyId",Config.tmp_familyid);
        paramHash.put("noOfEarners", noOfEarners.getText().toString());
        paramHash.put("nameOfEarners", nameOfEarners.getText().toString());
        paramHash.put("ageOfEarners", ageOfEarners.getText().toString());
        paramHash.put("occupationOfEarners", occupationOfEarners.getSelectedItem().toString());
        paramHash.put("isBankAccount", isBankAccount.getSelectedItem().toString());
        paramHash.put("statusOfHouse", statusOfHouse.getSelectedItem().toString());
        paramHash.put("totalIncome",totalIncome.getSelectedItem().toString());
        paramHash.put("foodExpense", foodExpense.getSelectedItem().toString());
        paramHash.put("healthExpense", healthExpense.getSelectedItem().toString());
        paramHash.put("educationExpense", educationExpense.getSelectedItem().toString());
        paramHash.put("intoxicationExpense", intoxicationExpense.getSelectedItem().toString());
        paramHash.put("conveyanceExpense", conveyanceExpense.getSelectedItem().toString());
        paramHash.put("cultivableLand", cultivableLand.getSelectedItem().toString());
        paramHash.put("screenerId",Config._screenerid);
        paramHash.put("ngoId", Config.NGO_ID);

        if(Config.isOffline){
            try {
                SQLiteDatabase db;
                db = openOrCreateDatabase("javixlife", Context.MODE_PRIVATE, null);
                Random rnd = new Random();
                char [] digits = new char[15];
                digits[0] = (char) (rnd.nextInt(9) + '1');
                for(int i=1; i<digits.length; i++) {
                    digits[i] = (char) (rnd.nextInt(10) + '0');
                }
                String uniqueID =new String(digits);
                paramHash.put("citizenId", uniqueID);

                JSONObject jsonObject=new JSONObject(paramHash);
                String SqlStr = "INSERT INTO javix_update(service_type,service_name,service_data,data_json,insert_date,_status)";
                SqlStr += " VALUES('Socioeconomic Survey','" + MyConfig.URL_ADD_CITIZEN + "','" + paramHash.toString() + "','"+jsonObject.toString()+"','" + currentDate + "',0);";
                //Toast.makeText(getApplicationContext(), SqlStr, Toast.LENGTH_LONG).show();
                Log.e("Offline", SqlStr);
                db.execSQL(SqlStr);
                db.close();
                android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(SocioEconomic.this
                );
                alertDialog.setMessage(SocioEconomic.this.getString(R.string._socioeconomicSurveyAdd_he));
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Intent i = new Intent(SocioEconomic.this, SurveyMenuActivity.class);
                        Config.tmp_caseId="0";
                        finish();
                        startActivity(i);
                    }
                });
                alertDialog.create();
                alertDialog.show();

            }catch (Exception ex){

                android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(SocioEconomic.this
                );
                alertDialog.setMessage("Error in saving data " + ex.getLocalizedMessage());
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Intent i = new Intent(SocioEconomic.this, SurveyMenuActivity.class);
                        Config.tmp_caseId="0";
                        finish();
                        startActivity(i);
                    }
                });
                alertDialog.create();
                alertDialog.show();
            }

        }else {
        SubmitForm req=new SubmitForm(this,paramHash);
        req.execute(MyConfig.URL_SOCIOECONOMIC);
        }

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
        protected void onPreExecute(){
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

                    android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(SocioEconomic.this
                    );
                    alertDialog.setMessage(SocioEconomic.this.getString(R.string._socioeconomicSurveyAdd_he));
                    alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            if(Config.tmp_familyid!=null)
                                Config.tmp_familyid=null;
                            Intent i = new Intent(SocioEconomic.this, SurveyMenuActivity.class);
                            finish();
                            startActivity(i);
                        }
                    });
                    alertDialog.create();
                    alertDialog.show();
                    //Toast.makeText(MyConfig.CONTEXT, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                }
                else Toast.makeText(MyConfig.CONTEXT, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

            }catch (Exception ee){
                Toast.makeText(MyConfig.CONTEXT, "Error !.", Toast.LENGTH_SHORT).show();
            }

        }

    }
    public void onBackPressed() {
        Intent i = new Intent(SocioEconomic.this, SurveyMenuActivity.class);
        finish();
        startActivity(i);
    }
}