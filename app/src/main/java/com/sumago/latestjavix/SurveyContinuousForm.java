package com.sumago.latestjavix;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.sumago.latestjavix.Util.Config;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

public class SurveyContinuousForm extends AppCompatActivity {

    Context context;
    boolean isEnSelected_hs = true;
    boolean isEnSelected_gs = true;
    boolean isEnSelected_ss = true;
    int listRowHeight = 95;

    Button btnSubmit, btnSubmit_hs, btnSubmit_ss;
    ImageView imgV1, imgV2, imgV3;
    LinearLayout ll_general_survey, ll_health_survey, ll_socioeconomic_survey;

    ProgressBar progress_one, progress_two;


    //************************************************************************ Health Survey Init Start *********************************************************************************************************************//


    private ListView listView;
    AppCompatButton btnadd;
    TextView tv_citizenid;
    EditText txNoOfMembers, txHeadOfFamily, txAgeOfHead, txNoOfAdultMales, txNoOfAdultFemales, txNoOfChildMale, txNoOfChildFemale;
    private ArrayList<String> arrayList = new ArrayList<>();

    ArrayAdapter adapter;
    //Hindi Section
    RadioButton radioHindi, radioEnglish;
    TextView lblHeader, lblInfo;
    String strHeader_Hindi = "सामान्य सर्वेक्षण", strHeader_English = "General Survey", strInfo_Hindi = "परिवार के सदस्यों को जोड़े", strInfo_English = "Add Family Members";
    String strNoOfMembers_Hn = "परिवार के सदस्यों की संख्या", strNoOfMembers_En = "No of Family Members", strHeadOfFamily_Hn = "परिवार के मुखिया का नाम ", strHeadOfFamily_En = "Name of head of the family";
    String strAgeOfHead_Hn = "परिवार के मुखिया की आयु", strAgeOfHead_En = "Age of head of the family", strNoOfAdultMales_Hn = "अन्य वयस्क पुरुषों की संख्या ", strNoOfAdultMales_En = "Number of other adult males";
    String strNoOfAdultFemales_Hn = "अन्य वयस्क महिलाओं की संख्या ", strNoOfAdultFemales_En = "Number of other adult female", strNoOfChildMale_Hn = "पुरुष बच्चों की संख्या ", trNoOfChildMale_En = "Number of male children";
    String strNoOfChildFemale_Hn = "महिला बच्चों की संख्या ", strNoOfChildFemale_En = "Number of female children";


    //************************************************************************ Health Survey Init End *********************************************************************************************************************//


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_continuous_form);

        btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit_hs = findViewById(R.id.btnSubmit_hs);
        btnSubmit_ss = findViewById(R.id.btnSubmit_ss);

        imgV1 = findViewById(R.id.imgV1);
        imgV2 = findViewById(R.id.imgV2);
        imgV3 = findViewById(R.id.imgV3);

        ll_general_survey = findViewById(R.id.ll_general_survey);
        ll_health_survey = findViewById(R.id.ll_health_survey);
        ll_socioeconomic_survey = findViewById(R.id.ll_socioeconomic_survey);
        progress_one = findViewById(R.id.progress_one);
        progress_two = findViewById(R.id.progress_two);


//        GeneralSurveyAssign();

        subMitButtonOfAllSurvey();

    }


    // End of onCreate

    //    <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


    private void subMitButtonOfAllSurvey() {

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SurveyContinuousForm.this, "General Survey Submitted Successfully", Toast.LENGTH_SHORT).show();
                ll_general_survey.setVisibility(View.GONE);
                ll_health_survey.setVisibility(View.VISIBLE);
                ll_socioeconomic_survey.setVisibility(View.GONE);

                imgV1.setImageResource(R.drawable.done);

                progress_one.getProgressDrawable().setColorFilter(Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);
//                progressBar.setProgress(100);
//                progressBar.setBackgroundColor();
                progress_one.setProgress(100);

            }
        });


        btnSubmit_hs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SurveyContinuousForm.this, "Health Survey Submitted Successfully", Toast.LENGTH_SHORT).show();

                ll_general_survey.setVisibility(View.GONE);
                ll_health_survey.setVisibility(View.GONE);
                ll_socioeconomic_survey.setVisibility(View.VISIBLE);
                imgV2.setImageResource(R.drawable.done);
                progress_two.getProgressDrawable().setColorFilter(Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);

                progress_two.setProgress(100);
            }
        });


        btnSubmit_ss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SurveyContinuousForm.this, "Socioeconomic Survey Submitted Successfully", Toast.LENGTH_SHORT).show();
                imgV3.setImageResource(R.drawable.done);

                Intent intent = new Intent(SurveyContinuousForm.this, SevikaActivity.class);
                startActivity(intent);
                finish();
            }
        });


        imgV1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ll_general_survey.setVisibility(View.VISIBLE);
                ll_health_survey.setVisibility(View.GONE);
                ll_socioeconomic_survey.setVisibility(View.GONE);

            }
        });

        imgV2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ll_general_survey.setVisibility(View.GONE);
                ll_health_survey.setVisibility(View.VISIBLE);
                ll_socioeconomic_survey.setVisibility(View.GONE);

            }
        });

        imgV3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ll_general_survey.setVisibility(View.GONE);
                ll_health_survey.setVisibility(View.GONE);
                ll_socioeconomic_survey.setVisibility(View.VISIBLE);
            }
        });


    }
//
//    private void GeneralSurveyAssign() {
//
//        lblHeader = (TextView) findViewById(R.id.lblHeader);
//        lblInfo = (TextView) findViewById(R.id.lblInfo);
//        txNoOfMembers = (EditText) findViewById(R.id.txNoOfMembers);
//        txHeadOfFamily = (EditText) findViewById(R.id.txHeadOfFamily);
//        txAgeOfHead = (EditText) findViewById(R.id.txAgeOfHead);
//        txNoOfAdultMales = (EditText) findViewById(R.id.txNoOfAdultMales);
//        txNoOfAdultFemales = (EditText) findViewById(R.id.txNoOfAdultFemales);
//        txNoOfChildMale = (EditText) findViewById(R.id.txNoOfChildMale);
//        txNoOfChildFemale = (EditText) findViewById(R.id.txNoOfChildFemale);
//        btnadd = (AppCompatButton) findViewById(R.id.btnadd);
//        listView = (ListView) findViewById(R.id.listView);
//        radioEnglish = (RadioButton) findViewById(R.id.radioEnglish);
//        radioHindi = (RadioButton) findViewById(R.id.radioHindi);
//        // aSwitch=(Switch)findViewById(R.id.switchHindi);
//        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, Config.arrayList);
//        listView.setAdapter(adapter);
//        radioEnglish.setChecked(true);
//
//        radioEnglish.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    lblHeader.setText(strHeader_English);
//                    lblInfo.setText(strInfo_English);
//                    txNoOfMembers.setHint(strNoOfMembers_En);
//                    txHeadOfFamily.setHint(strHeadOfFamily_En);
//                    txAgeOfHead.setHint(strAgeOfHead_En);
//                    txNoOfAdultMales.setHint(strNoOfAdultMales_En);
//                    txNoOfAdultFemales.setHint(strNoOfAdultFemales_En);
//                    txNoOfChildMale.setHint(trNoOfChildMale_En);
//                    txNoOfChildFemale.setHint(strNoOfChildFemale_En);
//                }
//            }
//        });
//
//        radioHindi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    lblHeader.setText(strHeader_Hindi);
//                    lblInfo.setText(strInfo_Hindi);
//                    txNoOfMembers.setHint(strNoOfMembers_Hn);
//                    txHeadOfFamily.setHint(strHeadOfFamily_Hn);
//                    txAgeOfHead.setHint(strAgeOfHead_Hn);
//                    txNoOfAdultMales.setHint(strNoOfAdultMales_Hn);
//                    txNoOfAdultFemales.setHint(strNoOfAdultFemales_Hn);
//                    txNoOfChildMale.setHint(strNoOfChildMale_Hn);
//                    txNoOfChildFemale.setHint(strNoOfChildFemale_Hn);
//                }
//            }
//        });
//
//        btnSubmit = (Button) findViewById(R.id.btnSubmit);
//        btnadd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(getApplicationContext(), CitizenListSurvey.class);
//                finish();
//                startActivity(i);
//            }
//        });
//
//        btnSubmit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                submitForm(v);
//            }
//        });
//
//        if (Config.tmp_citizenId != null) {
//            /*String [] strCid=Config.tmp_citizenId.split(",");
//            if(strCid.length==1) {
//                arrayList.add(Config.tmp_citizenId);
//            }else{
//                for (String strID:strCid) {
//                    arrayList.add(strID);
//                }
//            }*/
//            adapter.notifyDataSetChanged();
//        }
//
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView adapterView, View view, int position, long l) {
//                Toast.makeText(SurveyContinuousForm.this, "" + adapter.getItem(position), Toast.LENGTH_SHORT).show();
//
//            }
//        });
//
//        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(final AdapterView<?> adapterView, View view, int i, long l) {
//                final int item = i;
//                new AlertDialog.Builder(SurveyContinuousForm.this)
//                        .setIcon(android.R.drawable.ic_delete)
//                        .setTitle("Are you sure ?")
//                        .setMessage("Do you want to delete this item")
//                        .setPositiveButton(SurveyContinuousForm.this.getString(R.string._yes_he), new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                // listView.removeViewAt(i);
//                                Config.arrayList.remove(item);
//                                Config.arrayList_ID.remove(item);
//
//                                adapter = new ArrayAdapter(SurveyContinuousForm.this
//                                        , android.R.layout.simple_list_item_1, Config.arrayList);
//                                listView.setAdapter(adapter);
//                                adapter.notifyDataSetChanged();
//                                // listView.invalidateViews();
//                                // listView.refreshDrawableState();
//                                try {
//                                    if (Config.arrayList.size() >= 1) {
//                                        View vText = (View) listView.getAdapter().getView(0, null, listView);
//                                        vText.measure(0, 0);
//                                        listRowHeight = vText.getMeasuredHeight() + 16;
//                                    }
//                                    ViewGroup.LayoutParams lp = listView.getLayoutParams();
//                                    lp.height = Config.arrayList.size() * listRowHeight;
//                                    listView.setLayoutParams(lp);
//                                } catch (Exception ee) {
//                                }
//                                Toast.makeText(getApplicationContext(), Integer.toString(arrayList.size()), Toast.LENGTH_LONG).show();
//                            }
//                        })
//                        .setNegativeButton(SurveyContinuousForm.this.getString(R.string._no_he), null)
//                        .show();
//                return true;
//            }
//        });
//
//        //Toast.makeText(getApplicationContext(), Config.tmp_citizenId, Toast.LENGTH_LONG).show();
//
//
//        GeneralSurveyOutOfOncreate();
//    }
//
//    private void GeneralSurveyOutOfOncreate() {
//
//
//
//        void submitForm(View v){
//            String pattern = "yyyy-MM-dd";
//            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
//            String currentDate = simpleDateFormat.format(new Date());
//            ErrBox.errorsStatus();
//            HashMap<String,String> paramHash=new HashMap<String,String>();
//            System.out.println("zzzzzzzzzzzzzzzzzzzzzz--SUBMIT FORM ENTRY:");
//        /*String [] strCitizenId=Config.tmp_citizenId.split(",");
//        if(strCitizenId.length>1){
//
//            for (String strCid:strCitizenId) {
//                paramHash.put("citizenId",  strCid);
//                //paramHash.p
//            }
//
//        }
//        else
//        {
//            paramHash.put("citizenId",  Config.tmp_citizenId );
//        }*/
//            System.out.println("zzzzzzzzzzzzzzzzzzzzzz--SIZE of ArrayList ID :"+Config.arrayList_ID.size());
//            String strCitizenId="";
//            for(int i=0;i<Config.arrayList_ID.size();i++){
//                strCitizenId+=Config.arrayList_ID.get(i) + ",";
//            }
//            strCitizenId=strCitizenId.substring(0,strCitizenId.length()-1);
//            System.out.println("zzzzzzzzzzzzzzzzzzzzzz--Citizen Id:"+strCitizenId);
//            paramHash.put("citizenId",  strCitizenId);
//            paramHash.put("noOfFamilyMembers", txNoOfMembers.getText().toString());
//            paramHash.put("nameHead", txHeadOfFamily.getText().toString());
//            paramHash.put("ageHead", txAgeOfHead.getText().toString());
//            paramHash.put("NoOfAdultMales", txNoOfAdultMales.getText().toString());
//            paramHash.put("NoOfAdultFemales", txNoOfAdultFemales.getText().toString());
//            paramHash.put("NoOfChildrenMales", txNoOfChildMale.getText().toString());
//            paramHash.put("NoOfChildrenFemales", txNoOfChildFemale.getText().toString());
//            paramHash.put("screenerId",Config._screenerid);
//            System.out.println("zzzzzzzzzzzzzzzzzzzzzz--SUBMIT FORM DATA:"+paramHash.toString());
//            if(Config.isOffline){
//                try {
//                    SQLiteDatabase db;
//                    db = openOrCreateDatabase("javixlife", Context.MODE_PRIVATE, null);
//                    Random rnd = new Random();
//                    char [] digits = new char[15];
//                    digits[0] = (char) (rnd.nextInt(9) + '1');
//                    for(int i=1; i<digits.length; i++) {
//                        digits[i] = (char) (rnd.nextInt(10) + '0');
//                    }
//                    String uniqueID =new String(digits);
//                    paramHash.put("familyId", uniqueID);
//                    System.out.println("zzzzzzzzzzzzzzzzzzzzzz--FAMILY ID:"+uniqueID);
//                    JSONObject jsonObject=new JSONObject(paramHash);
//                    String SqlStr = "INSERT INTO javix_update(service_type,service_name,service_data,data_json,insert_date,_status)";
//                    SqlStr += " VALUES('General Survey','" + MyConfig.URL_GENERALSURVEY + "','" + paramHash.toString() + "','"+jsonObject.toString()+"','" + currentDate + "',0);";
//                    //Toast.makeText(getApplicationContext(), SqlStr, Toast.LENGTH_LONG).show();
//                    System.out.println("zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz--Offline"+SqlStr);
//                    db.execSQL(SqlStr);
//                    db.close();
//                    android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(SurveyContinuousForm.this
//                    );
//                    alertDialog.setMessage(SurveyContinuousForm.this.getString(R.string._generalSurveyAdd_he));
//                    alertDialog.setPositiveButton(SurveyContinuousForm.this.getString(R.string._ok_he), new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int whichButton) {
//                            Intent i = new Intent(SurveyContinuousForm.this, SurveyMenuActivity.class);
//                            Config.arrayList_ID.clear();
//                            finish();
//                            startActivity(i);
//                        }
//                    });
//                    alertDialog.create();
//                    alertDialog.show();
//
//                }catch (Exception ex){
//
//                    android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(SurveyContinuousForm.this
//                    );
//                    alertDialog.setMessage("Error in saving data " + ex.getLocalizedMessage());
//                    alertDialog.setPositiveButton(SurveyContinuousForm.this.getString(R.string._ok_he), new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int whichButton) {
//                            Intent i = new Intent(SurveyContinuousForm.this, SurveyMenuActivity.class);
//                            Config.tmp_caseId="0";
//                            finish();
//                            startActivity(i);
//                        }
//                    });
//                    alertDialog.create();
//                    alertDialog.show();
//                }
//
//            }else {
//                SurveyContinuousForm.SubmitForm req = new SurveyContinuousForm.SubmitForm(this, paramHash);
//                req.execute(MyConfig.URL_GENERALSURVEY);
//            }
//
//        }
//        //PUSH DATA
//        class SubmitForm extends AsyncTask<String, Void, String> {
//            ProgressDialog progressDialog;
//            Activity activity;
//            HashMap<String, String> paramsHash=null;
//            RequestPipe requestPipe=new RequestPipe();
//            public SubmitForm(Activity activity,HashMap<String, String> paramsHash) {
//                this.activity=activity;
//                this.paramsHash=paramsHash;
//                progressDialog = new ProgressDialog(activity);
//                progressDialog.setMessage("loading");
//            }
//            @Override
//            protected void onPreExecute(){
//                progressDialog.show();
//            }
//            @Override
//            protected String doInBackground(String... params) {
//
//                return requestPipe.requestForm(params[0],paramsHash);
//            }
//            protected void onProgressUpdate(Void ...progress) {
//                super.onProgressUpdate(progress);
//                //ProgressDialog.show(MainActivity.this, "loading", "wait...", true, true);
//            }
//            protected void onPostExecute(String result) {
//                super.onPostExecute(result);
//                progressDialog.cancel();
//                //Toast.makeText(MyConfig.CONTEXT,"RES:"+result,Toast.LENGTH_SHORT).show();
//                try {
//                    JSONObject jsonObject = new JSONObject(result);
//                    int respStatus=jsonObject.getInt("status");
//                    if(respStatus==1) {
//
//                        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(SurveyContinuousForm.this
//                        );
//                        alertDialog.setMessage(SurveyContinuousForm.this.getString(R.string._generalSurveyAdd_he));
//                        alertDialog.setPositiveButton(SurveyContinuousForm.this.getString(R.string._ok_he), new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int whichButton) {
//                                Intent i = new Intent(SurveyContinuousForm.this, SurveyMenuActivity.class);
//                                finish();
//                                startActivity(i);
//                            }
//                        });
//                        alertDialog.create();
//                        alertDialog.show();
//                        //Toast.makeText(MyConfig.CONTEXT, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
//                    }
//                    else Toast.makeText(MyConfig.CONTEXT, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
//
//                }catch (Exception ee){
//                    Toast.makeText(MyConfig.CONTEXT, "Error !.", Toast.LENGTH_SHORT).show();
//                }
//
//            }
//
//        }
//
//
//    }


}