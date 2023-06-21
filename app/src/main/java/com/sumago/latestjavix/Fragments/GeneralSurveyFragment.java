package com.sumago.latestjavix.Fragments;

import static com.sumago.latestjavix.AllSurveyActivity.imgV1;
import static com.sumago.latestjavix.AllSurveyActivity.imgV2;
import static com.sumago.latestjavix.AllSurveyActivity.progress_one;

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

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.sumago.latestjavix.CitizenListSurvey;
import com.sumago.latestjavix.MyConfig;
import com.sumago.latestjavix.R;
import com.sumago.latestjavix.RequestPipe;
import com.sumago.latestjavix.SevikaActivity;
import com.sumago.latestjavix.SurveyMenuActivity;
import com.sumago.latestjavix.Util.Config;
import com.sumago.latestjavix.Util.Shared_Preferences;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

public class GeneralSurveyFragment extends Fragment {


//    ImageView img1,img2,img3;
//    ProgressBar progress_one, progress_two;

    boolean hindi = false;

    private ListView listView;
    Button btnSubmit;
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


    public GeneralSurveyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_general_survey, container, false);

//        img1 = view.findViewById(R.id.imgV1);
//        img2 = view.findViewById(R.id.imgV2);
//        img3 = view.findViewById(R.id.imgV3);
//        progress_one = view.findViewById(R.id.progress_one);
//        progress_two = view.findViewById(R.id.progress_two);

        lblHeader = (TextView) view.findViewById(R.id.lblHeader);
        lblInfo = (TextView) view.findViewById(R.id.lblInfo);
        txNoOfMembers = (EditText) view.findViewById(R.id.txNoOfMembers);
        txHeadOfFamily = (EditText) view.findViewById(R.id.txHeadOfFamily);
        txAgeOfHead = (EditText) view.findViewById(R.id.txAgeOfHead);
        txNoOfAdultMales = (EditText) view.findViewById(R.id.txNoOfAdultMales);
        txNoOfAdultFemales = (EditText) view.findViewById(R.id.txNoOfAdultFemales);
        txNoOfChildMale = (EditText) view.findViewById(R.id.txNoOfChildMale);
        txNoOfChildFemale = (EditText) view.findViewById(R.id.txNoOfChildFemale);
        btnadd = (AppCompatButton) view.findViewById(R.id.btnadd);
        listView = (ListView) view.findViewById(R.id.listView);
        radioEnglish = (RadioButton) view.findViewById(R.id.radioEnglish);
        radioHindi = (RadioButton) view.findViewById(R.id.radioHindi);
        // aSwitch=(Switch)findViewById(R.id.switchHindi);

        adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, Config.arrayList);
        listView.setAdapter(adapter);
        radioEnglish.setChecked(true);


        radioEnglish.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    hindi = false;
                    lblHeader.setText(strHeader_English);
                    lblInfo.setText(strInfo_English);
                    txNoOfMembers.setHint(strNoOfMembers_En);
                    txHeadOfFamily.setHint(strHeadOfFamily_En);
                    txAgeOfHead.setHint(strAgeOfHead_En);
                    txNoOfAdultMales.setHint(strNoOfAdultMales_En);
                    txNoOfAdultFemales.setHint(strNoOfAdultFemales_En);
                    txNoOfChildMale.setHint(trNoOfChildMale_En);
                    txNoOfChildFemale.setHint(strNoOfChildFemale_En);
                }
            }
        });

        radioHindi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    hindi = true;
                    lblHeader.setText(strHeader_Hindi);
                    lblInfo.setText(strInfo_Hindi);
                    txNoOfMembers.setHint(strNoOfMembers_Hn);
                    txHeadOfFamily.setHint(strHeadOfFamily_Hn);
                    txAgeOfHead.setHint(strAgeOfHead_Hn);
                    txNoOfAdultMales.setHint(strNoOfAdultMales_Hn);
                    txNoOfAdultFemales.setHint(strNoOfAdultFemales_Hn);
                    txNoOfChildMale.setHint(strNoOfChildMale_Hn);
                    txNoOfChildFemale.setHint(strNoOfChildFemale_Hn);

                } else {
                    hindi = false;
                }
            }
        });


        btnSubmit = (Button) view.findViewById(R.id.btnSubmit);
        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), CitizenListSurvey.class);
                getActivity().finish();
                startActivity(i);
            }
        });
        //tv_citizenid.setText(Config.tmp_citizenId);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validation()) {
                    submitForm(v);

                    Config.arrayList.clear();
                }


                //  img1.setImageResource(R.drawable.done);
//                progress_one.getProgressDrawable().setColorFilter(Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);
//                progress_two.setProgress(100);
            }
        });

        if (Config.tmp_citizenId != null) {
            /*String [] strCid=Config.tmp_citizenId.split(",");
            if(strCid.length==1) {
                arrayList.add(Config.tmp_citizenId);
            }else{
                for (String strID:strCid) {
                    arrayList.add(strID);
                }
            }*/
            adapter.notifyDataSetChanged();
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView adapterView, View view, int position, long l) {
                Toast.makeText(getContext(), "" + adapter.getItem(position), Toast.LENGTH_SHORT).show();

            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> adapterView, View view, int i, long l) {
                final int item = i;
                new AlertDialog.Builder(getContext())
                        .setIcon(android.R.drawable.ic_delete)
                        .setTitle("Are you sure ?")
                        .setMessage("Do you want to delete this item")
                        .setPositiveButton(getContext().getString(R.string._yes_he), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // listView.removeViewAt(i);
                                Config.arrayList.remove(item);
                                Config.arrayList_ID.remove(item);

                                adapter = new ArrayAdapter(getContext()
                                        , android.R.layout.simple_list_item_1, Config.arrayList);
                                listView.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                                // listView.invalidateViews();
                                // listView.refreshDrawableState();
                                try {
                                    if (Config.arrayList.size() >= 1) {
                                        View vText = (View) listView.getAdapter().getView(0, null, listView);
                                        vText.measure(0, 0);
                                        listRowHeight = vText.getMeasuredHeight() + 16;
                                    }
                                    ViewGroup.LayoutParams lp = listView.getLayoutParams();
                                    lp.height = Config.arrayList.size() * listRowHeight;
                                    listView.setLayoutParams(lp);
                                } catch (Exception ee) {
                                }
                                Toast.makeText(getContext(), Integer.toString(arrayList.size()), Toast.LENGTH_LONG).show();
                            }
                        })
                        .setNegativeButton(getContext().getString(R.string._no_he), null)
                        .show();
                return true;
            }
        });

        //Toast.makeText(getApplicationContext(), Config.tmp_citizenId, Toast.LENGTH_LONG).show();


        return view;
    }

    int listRowHeight = 95;

    @Override
    public void onStart() {
        super.onStart();
        if (Config.arrayList.size() >= 1) {
            View vText = (View) listView.getAdapter().getView(0, null, listView);
            vText.measure(0, 0);
            listRowHeight = vText.getMeasuredHeight() + 16;
            //Toast.makeText(this,"H:"+listRowHeight,Toast.LENGTH_SHORT).show();
        }
        ViewGroup.LayoutParams lp = listView.getLayoutParams();
        lp.height = Config.arrayList.size() * listRowHeight;
        listView.setLayoutParams(lp);
        //Toast.makeText(this,"HE:"+lp.height,Toast.LENGTH_SHORT).show();
    }

    void submitForm(View v) {
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String currentDate = simpleDateFormat.format(new Date());
        // ErrBox.errorsStatus();
        HashMap<String, String> paramHash = new HashMap<String, String>();
        System.out.println("zzzzzzzzzzzzzzzzzzzzzz--SUBMIT FORM ENTRY:");
        /*String [] strCitizenId=Config.tmp_citizenId.split(",");
        if(strCitizenId.length>1){

            for (String strCid:strCitizenId) {
                paramHash.put("citizenId",  strCid);
                //paramHash.p
            }

        }
        else
        {
            paramHash.put("citizenId",  Config.tmp_citizenId );
        }*/
        System.out.println("zzzzzzzzzzzzzzzzzzzzzz--SIZE of ArrayList ID :" + Config.arrayList_ID.size());
        String strCitizenId = "";
        for (int i = 0; i < Config.arrayList_ID.size(); i++) {
            strCitizenId += Config.arrayList_ID.get(i) + ",";
        }
        strCitizenId = strCitizenId.substring(0, strCitizenId.length() - 1);
        System.out.println("zzzzzzzzzzzzzzzzzzzzzz--Citizen Id:" + strCitizenId);
        paramHash.put("citizenId", strCitizenId);
        paramHash.put("noOfFamilyMembers", txNoOfMembers.getText().toString());
        paramHash.put("nameHead", txHeadOfFamily.getText().toString());
        paramHash.put("ageHead", txAgeOfHead.getText().toString());
        paramHash.put("NoOfAdultMales", txNoOfAdultMales.getText().toString());
        paramHash.put("NoOfAdultFemales", txNoOfAdultFemales.getText().toString());
        paramHash.put("NoOfChildrenMales", txNoOfChildMale.getText().toString());
        paramHash.put("NoOfChildrenFemales", txNoOfChildFemale.getText().toString());
        paramHash.put("screenerId", Config._screenerid);
        paramHash.put("ngoId", Config.NGO_ID);

        System.out.println("zzzzzzzzzzzzzzzzzzzzzz--SUBMIT FORM DATA:" + paramHash.toString());


        Log.e("Atul", "paramHash: " + paramHash);

        if (Config.isOffline) {
            try {
                SQLiteDatabase db;
                db = getActivity().openOrCreateDatabase("javixlife", Context.MODE_PRIVATE, null);
                Random rnd = new Random();
                char[] digits = new char[15];
                digits[0] = (char) (rnd.nextInt(9) + '1');
                for (int i = 1; i < digits.length; i++) {
                    digits[i] = (char) (rnd.nextInt(10) + '0');
                }
                String uniqueID = new String(digits);
                paramHash.put("familyId", uniqueID);
                paramHash.put("ngoId", Config.NGO_ID);

                System.out.println("zzzzzzzzzzzzzzzzzzzzzz--FAMILY ID:" + uniqueID);
                JSONObject jsonObject = new JSONObject(paramHash);
                String SqlStr = "INSERT INTO javix_update(service_type,service_name,service_data,data_json,insert_date,_status)";
                SqlStr += " VALUES('General Survey','" + MyConfig.URL_GENERALSURVEY + "','" + paramHash.toString() + "','" + jsonObject.toString() + "','" + currentDate + "',0);";
                //Toast.makeText(getApplicationContext(), SqlStr, Toast.LENGTH_LONG).show();
                System.out.println("zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz--Offline" + SqlStr);
                db.execSQL(SqlStr);
                db.close();
                android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(getActivity());
                alertDialog.setMessage(getActivity().getString(R.string._generalSurveyAdd_he));
                alertDialog.setPositiveButton(getActivity().getString(R.string._ok_he), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Intent i = new Intent(getActivity(), SurveyMenuActivity.class);
                        Config.arrayList_ID.clear();
                        getActivity().finish();
                        startActivity(i);
                    }
                });
                alertDialog.create();
                alertDialog.show();

            } catch (Exception ex) {

                android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(getContext());
                alertDialog.setMessage("Error in saving data " + ex.getLocalizedMessage());
                alertDialog.setPositiveButton(getContext().getString(R.string._ok_he), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Intent i = new Intent(getContext(), SurveyMenuActivity.class);
                        Config.tmp_caseId = "0";
                        getActivity().finish();
                        startActivity(i);
                    }
                });
                alertDialog.create();
                alertDialog.show();
            }

        } else {
            GeneralSurveyFragment.SubmitForm req = new GeneralSurveyFragment.SubmitForm(getActivity(), paramHash);
            req.execute(MyConfig.URL_GENERALSURVEY);
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
                Log.e("Response", "GSF Status: " + respStatus);
                if (respStatus == 1) {

                    android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(getContext());
                    alertDialog.setMessage(getContext().getString(R.string._generalSurveyAdd_he));
                    alertDialog.setPositiveButton(getContext().getString(R.string._ok_he), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            //  Intent i = new Intent(getContext(), HealthSurveyFragment.class);
                            Log.e("Response", "message: ");
                            //  getActivity().finish();
                            // startActivity(i);

                            HealthSurveyFragment healthSurveyFragment = new HealthSurveyFragment();
                            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.Frame_Container, healthSurveyFragment);
                            transaction.commit();


                            imgV1.setImageResource(R.drawable.done);
                            progress_one.getProgressDrawable().setColorFilter(Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);
                            progress_one.setProgress(100);

                            Shared_Preferences.setPrefs(getContext(), "GS_Done", "1");


                        }
                    });
                    alertDialog.create();
                    alertDialog.show();
                    //Toast.makeText(MyConfig.CONTEXT, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(MyConfig.CONTEXT, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

            } catch (Exception ee) {
                Toast.makeText(MyConfig.CONTEXT, "Error !.", Toast.LENGTH_SHORT).show();
            }

        }

    }


    public void onBackPressed() {
        Log.d("CDA", "onBackPressed Called");
        getActivity().finish();

    }

//    @Override
//    public void onStop() {
//        super.onStop();
//
//
//        Config.arrayList.clear();
//
//
//    }

    private boolean validation() {

        boolean VALID = true;

        if (TextUtils.isEmpty(txHeadOfFamily.getText().toString().trim())) {
            VALID = false;

            Toast.makeText(getContext(), "" + hindi, Toast.LENGTH_SHORT).show();

            if (hindi) {

                txHeadOfFamily.setError("कृपया मुखिया परिवार का नाम दर्ज करे");
            } else if (!hindi) {
                txHeadOfFamily.setError("Please enter head family name");
            }

        }


        return VALID;
    }


}