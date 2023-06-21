package com.sumago.latestjavix.Fragments;

import static com.sumago.latestjavix.AllSurveyActivity.imgV1;
import static com.sumago.latestjavix.AllSurveyActivity.imgV2;
import static com.sumago.latestjavix.AllSurveyActivity.progress_one;
import static com.sumago.latestjavix.AllSurveyActivity.progress_two;

import android.app.Activity;
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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.sumago.latestjavix.GeneralSurveyList;
import com.sumago.latestjavix.GeneralSurveyListTwo;
import com.sumago.latestjavix.HealthSurvey;
import com.sumago.latestjavix.MyConfig;
import com.sumago.latestjavix.R;
import com.sumago.latestjavix.RequestPipe;
import com.sumago.latestjavix.SevikaActivity;
import com.sumago.latestjavix.SurveyMenuActivity;
import com.sumago.latestjavix.Util.Config;
import com.sumago.latestjavix.Util.Shared_Preferences;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class
HealthSurveyFragment extends Fragment {


    Button btnSubmit;
    AppCompatButton btnadd;
    HashMap<String, HashMap<String, List<String>>> mapHindiEnglish=new HashMap<>();
    boolean isEnSelected=true;
    EditText NoOfPersonUsingToilets;
    Spinner drinkingWaterSource,drinkingWaterDistance,isdrinkingWaterTreatmentRequired,NonUsageOfToilets,DistanceOfSubcenters,DistanceOfPrimaryHealthcenters;
    Spinner DistanceOfCommunityHealthcenters,DistanceOfDistrictHospitals,DistanceOfPathologyLab,DistanceOfMedicalStore,StatusOfDeliveryOfChildren;
    Spinner StatusOfVaccinationOfChildren,StatusOfFemaleRelatedProblem,CentrallyIssuedHealthInsurance,StateIssuedHealthInsurance,PersonalHealthInsurance;
    String[] strdrinkingWaterSource ={"Drinking water source","Chapakal/Hand pump","Wells","Boring","Rivers/ponds","Any other"};
    String[] strdrinkingWaterDistance ={"Drinking Water distance","Within house","Within 50 metres","50-200 metres","200metres-1km","More than 1km"};
    String[] strisdrinkingWaterTreatmentRequired ={"Drinking water requiring treatment","Yes","No","Dont Know"};
    String[] strNonUsageOfToilets ={"Toilets","No toilet","Improperly built","Water scarcity","other","Using toilet"};
    String[] strDistanceOfSubcenters ={"Distance of subcentres (IN WALKING TIME)","Within 5 minutes","5 - 15 minutes","15 - 30 minutes","More than 30 minutes"};
    String[] strDistanceOfPrimaryHealthcenters ={"Distance of Primary health centers (IN KMS)","Within 1km","1 - 3 km","3 - 5 km","More than 5 km"};
    String[] strDistanceOfCommunityHealthcenters ={"Distance of community health centers","Within 1km","1 - 3 km","3 - 6 km","6 - 10 km","More than 10 km"};
    String[] strDistanceOfDistrictHospitals ={"Distance of district hospital","Within 5 km","5 - 10 km","10 - 20 km","20 - 30 km","More than 30 km"};
    String[] strDistanceOfPathologyLab ={"Distance of Pathology Lab","Within 3 km","3 - 6 km","6 - 10 km","More than 10 km"};
    String[] strDistanceOfMedicalStore ={"Distance of medical store","Within 3 km","3 - 6 km","6 - 10 km","More than 10 km"};
    String[] strStatusOfDeliveryOfChildren ={"Status of delivery of children","Government hospital","Private hospital/clinics","Local Dai or any other health worker","any other"};
    String[] strStatusOfVaccinationOfChildren ={"Status of vaccination of children","done","not done","Ongoing"};
    String[] strStatusOfFemaleRelatedProblem ={"Status of female related problems","Yes","No","Dont Know"};
    String[] strCentrallyIssuedHealthInsurance ={"Centrally issued health insurance","Yes","No"};
    String[] strStateIssuedHealthInsurance ={"State issued health insurance","Yes","No"};
    String[] strPersonalHealthInsurance ={"Personal health insurance","Yes","No"};

    Context context;
    //Hindi Section
    TextView lblHeader,lblInfo,lblFamilyId;
    RadioButton radioHindi,radioEnglish;
    String lblHeader_Hn="स्वास्थ्य सर्वेक्षण",lblHeader_En="Health Survey";
    String NoOfPersonUsingToilets_Hn="शौचालय का उपयोग करने वाले व्यक्तियों की संख्या",NoOfPersonUsingToilets_En="No of Person Using Toilets";
    String[] strdrinkingWaterSource_Hn ={"पेयजल स्रोत","चापकल/हैंड पंप","कुंआ","उबाऊ","नदियाँ/तालाब","कोई दूसरा"};
    String[] strdrinkingWaterDistance_Hn ={"पीने के पानी की दूरी","घर के भीतर","50 मीटर . के भीतर","50-200 मीटर","200 मीटर-1 किमी","1km . से अधिक"};
    String[] strisdrinkingWaterTreatmentRequired_Hn ={"पीने के पानी के उपचार की आवश्यकता","हां","नहीं","पता नहीं"};
    // String[] strNonUsageOfToilets_Hn ={"शौचालय का उपयोग न करना","शौचालय नहीं","गलत तरीके से बनाया गया","पानी की कमी","अन्य"};
    String[] strNonUsageOfToilets_Hn ={"शौचालय","शौचालय नहीं","गलत तरीके से बनाया गया","पानी की कमी","अन्य","मैं शौचालय का उपयोग कर रहा हूँ"};
    String[] strDistanceOfSubcenters_Hn ={"उपकेंद्रों की दूरी (चलने के समय में)","5 मिनट के भीतर","5 - 15 मिनट","15 - 30 मिनट ","30 मिनट से अधिक"};
    String[] strDistanceOfPrimaryHealthcenters_Hn ={"प्राथमिक स्वास्थ्य केन्द्रों की दूरी (किमी में)","1 किमी. के भीतर","1 - 3 किमी","3 - 5 किमी","5 किमी. से अधिक"};
    String[] strDistanceOfCommunityHealthcenters_Hn ={"सामुदायिक स्वास्थ्य केंद्रों की दूरी","1 किमी . के भीतर","1 - 3 किमी","3 - 6 किमी","6 - 10 किमी","10 किमी. से अधिक"};
    String[] strDistanceOfDistrictHospitals_Hn ={"जिला अस्पताल की दूरी","5 किमी. के भीतर ","5 - 10 किमी","10 - 20 किमी","20 - 30 किमी","30 किमी. से अधिक "};
    String[] strDistanceOfPathologyLab_Hn ={"पैथोलॉजी लैब की दूरी","3 किमी. के भीतर ","3 - 6 किमी","6 - 10 किमी","10 किमी. से अधिक"};
    String[] strDistanceOfMedicalStore_Hn ={"मेडिकल स्टोर की दूरी","3 किमी. के भीतर ","3 - 6 किमी","6 - 10 किमी","10 किमी. से अधिक"};
    String[] strStatusOfDeliveryOfChildren_Hn ={"बच्चों की डिलीवरी की स्थिति","सरकारी अस्पताल","निजी अस्पताल/क्लीनिक","स्थानीय दाई या कोई अन्य स्वास्थ्य कार्यकर्ता","कोई और"};
    String[] strStatusOfVaccinationOfChildren_Hn ={"बच्चों के टीकाकरण की स्थिति","किया हुआ","नहीं किया","चल रही है"};
    String[] strStatusOfFemaleRelatedProblem_Hn ={"महिला संबंधित समस्याओं की स्थिति","हां","नहीं","पता नहीं"};
    String[] strCentrallyIssuedHealthInsurance_Hn ={"केंद्र द्वारा जारी स्वास्थ्य बीमा","हां","नहीं"};
    String[] strStateIssuedHealthInsurance_Hn ={"राज्य द्वारा जारी स्वास्थ्य बीमा","हां","नहीं"};
    String[] strPersonalHealthInsurance_Hn ={"व्यक्तिगत स्वास्थ्य बीमा","हां","नहीं"};



    public HealthSurveyFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_health_survey, container, false);


        //Toast.makeText(getActivity(), "This is Health Fragment", Toast.LENGTH_SHORT).show();

        try {
            if (Shared_Preferences.getPrefs(getActivity(), "GS_Done").equals("1")){

                imgV1.setImageResource(R.drawable.done);
                progress_one.getProgressDrawable().setColorFilter(Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);
                progress_one.setProgress(100);
            }

        }catch (Exception e){
            e.printStackTrace();
        }


        MyConfig.CONTEXT=getContext();

        lblFamilyId=(TextView) view.findViewById(R.id.lblFamilyId);
        lblHeader=(TextView) view.findViewById(R.id.lblHeader);
        NoOfPersonUsingToilets=(EditText) view.findViewById(R.id.NoOfPersonUsingToilets);
        drinkingWaterSource=(Spinner) view.findViewById(R.id.drinkingWaterSource);
        drinkingWaterDistance=(Spinner) view.findViewById(R.id.drinkingWaterDistance);
        isdrinkingWaterTreatmentRequired=(Spinner) view.findViewById(R.id.isdrinkingWaterTreatmentRequired);
        NonUsageOfToilets=(Spinner) view.findViewById(R.id.NonUsageOfToilets);
        DistanceOfSubcenters=(Spinner) view.findViewById(R.id.DistanceOfSubcenters);
        DistanceOfPrimaryHealthcenters=(Spinner) view.findViewById(R.id.DistanceOfPrimaryHealthcenters);
        DistanceOfCommunityHealthcenters=(Spinner) view.findViewById(R.id.DistanceOfCommunityHealthcenters);
        DistanceOfDistrictHospitals=(Spinner) view.findViewById(R.id.DistanceOfDistrictHospitals);
        DistanceOfPathologyLab=(Spinner) view.findViewById(R.id.DistanceOfPathologyLab);
        DistanceOfMedicalStore=(Spinner) view.findViewById(R.id.DistanceOfMedicalStore);
        StatusOfDeliveryOfChildren=(Spinner) view.findViewById(R.id.StatusOfDeliveryOfChildren);
        StatusOfVaccinationOfChildren=(Spinner) view.findViewById(R.id.StatusOfVaccinationOfChildren);
        StatusOfFemaleRelatedProblem=(Spinner) view.findViewById(R.id.StatusOfFemaleRelatedProblem);
        CentrallyIssuedHealthInsurance=(Spinner) view.findViewById(R.id.CentrallyIssuedHealthInsurance);
        StateIssuedHealthInsurance=(Spinner) view.findViewById(R.id.StateIssuedHealthInsurance);
        PersonalHealthInsurance=(Spinner) view.findViewById(R.id.PersonalHealthInsurance);
        btnSubmit=(Button) view.findViewById(R.id.btnSubmit);
        btnadd=(AppCompatButton) view.findViewById(R.id.btnadd);
        radioEnglish = (RadioButton) view.findViewById(R.id.radioEnglish);
        radioHindi = (RadioButton) view.findViewById(R.id.radioHindi);
        if(Config.tmp_familyid!=null)
            lblFamilyId.setText("Family ID : " + Config.tmp_familyid);
        radioEnglish.setActivated(true);
        radioEnglish.setChecked(true);
        context=getContext();





        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), GeneralSurveyList.class);
//                Intent i = new Intent(getContext(), GeneralSurveyListTwo.class);

                i.putExtra("_type","Health");
                startActivity(i);
                //finish();

            }
        });



        ArrayAdapter adp1 = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,strdrinkingWaterSource);
        adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        drinkingWaterSource.setAdapter(adp1);

        adp1 = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,strdrinkingWaterDistance);
        adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        drinkingWaterDistance.setAdapter(adp1);

        adp1 = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,strisdrinkingWaterTreatmentRequired);
        adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        isdrinkingWaterTreatmentRequired.setAdapter(adp1);

        adp1 = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,strNonUsageOfToilets);
        adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        NonUsageOfToilets.setAdapter(adp1);

        adp1 = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,strDistanceOfSubcenters);
        adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        DistanceOfSubcenters.setAdapter(adp1);

        adp1 = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,strDistanceOfPrimaryHealthcenters);
        adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        DistanceOfPrimaryHealthcenters.setAdapter(adp1);

        adp1 = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,strDistanceOfCommunityHealthcenters);
        adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        DistanceOfCommunityHealthcenters.setAdapter(adp1);

        adp1 = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,strDistanceOfDistrictHospitals);
        adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        DistanceOfDistrictHospitals.setAdapter(adp1);

        adp1 = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,strDistanceOfPathologyLab);
        adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        DistanceOfPathologyLab.setAdapter(adp1);

        adp1 = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,strDistanceOfMedicalStore);
        adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        DistanceOfMedicalStore.setAdapter(adp1);

        adp1 = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,strStatusOfDeliveryOfChildren);
        adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        StatusOfDeliveryOfChildren.setAdapter(adp1);

        adp1 = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,strStatusOfVaccinationOfChildren);
        adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        StatusOfVaccinationOfChildren.setAdapter(adp1);

        adp1 = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,strStatusOfFemaleRelatedProblem);
        adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        StatusOfFemaleRelatedProblem.setAdapter(adp1);

        adp1 = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,strCentrallyIssuedHealthInsurance);
        adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        CentrallyIssuedHealthInsurance.setAdapter(adp1);

        adp1 = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,strStateIssuedHealthInsurance);
        adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        StateIssuedHealthInsurance.setAdapter(adp1);

        adp1 = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,strPersonalHealthInsurance);
        adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        PersonalHealthInsurance.setAdapter(adp1);

        radioEnglish.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    lblHeader.setText(lblHeader_En);
                    NoOfPersonUsingToilets.setHint(NoOfPersonUsingToilets_En);
                    //lblInfo.setText(strInfo_En);
                    isEnSelected=true;

                    ArrayAdapter adp1 = new ArrayAdapter(context,android.R.layout.simple_spinner_item,strdrinkingWaterSource);
                    adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    drinkingWaterSource.setAdapter(adp1);

                    adp1 = new ArrayAdapter(context,android.R.layout.simple_spinner_item,strdrinkingWaterDistance);
                    adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    drinkingWaterDistance.setAdapter(adp1);

                    adp1 = new ArrayAdapter(context,android.R.layout.simple_spinner_item,strisdrinkingWaterTreatmentRequired);
                    adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    isdrinkingWaterTreatmentRequired.setAdapter(adp1);

                    adp1 = new ArrayAdapter(context,android.R.layout.simple_spinner_item,strNonUsageOfToilets);
                    adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    NonUsageOfToilets.setAdapter(adp1);

                    adp1 = new ArrayAdapter(context,android.R.layout.simple_spinner_item,strDistanceOfSubcenters);
                    adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    DistanceOfSubcenters.setAdapter(adp1);

                    adp1 = new ArrayAdapter(context,android.R.layout.simple_spinner_item,strDistanceOfPrimaryHealthcenters);
                    adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    DistanceOfPrimaryHealthcenters.setAdapter(adp1);

                    adp1 = new ArrayAdapter(context,android.R.layout.simple_spinner_item,strDistanceOfCommunityHealthcenters);
                    adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    DistanceOfCommunityHealthcenters.setAdapter(adp1);

                    adp1 = new ArrayAdapter(context,android.R.layout.simple_spinner_item,strDistanceOfDistrictHospitals);
                    adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    DistanceOfDistrictHospitals.setAdapter(adp1);

                    adp1 = new ArrayAdapter(context,android.R.layout.simple_spinner_item,strDistanceOfPathologyLab);
                    adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    DistanceOfPathologyLab.setAdapter(adp1);

                    adp1 = new ArrayAdapter(context,android.R.layout.simple_spinner_item,strDistanceOfMedicalStore);
                    adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    DistanceOfMedicalStore.setAdapter(adp1);

                    adp1 = new ArrayAdapter(context,android.R.layout.simple_spinner_item,strStatusOfDeliveryOfChildren);
                    adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    StatusOfDeliveryOfChildren.setAdapter(adp1);

                    adp1 = new ArrayAdapter(context,android.R.layout.simple_spinner_item,strStatusOfVaccinationOfChildren);
                    adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    StatusOfVaccinationOfChildren.setAdapter(adp1);

                    adp1 = new ArrayAdapter(context,android.R.layout.simple_spinner_item,strStatusOfFemaleRelatedProblem);
                    adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    StatusOfFemaleRelatedProblem.setAdapter(adp1);

                    adp1 = new ArrayAdapter(context,android.R.layout.simple_spinner_item,strCentrallyIssuedHealthInsurance);
                    adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    CentrallyIssuedHealthInsurance.setAdapter(adp1);

                    adp1 = new ArrayAdapter(context,android.R.layout.simple_spinner_item,strStateIssuedHealthInsurance);
                    adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    StateIssuedHealthInsurance.setAdapter(adp1);

                    adp1 = new ArrayAdapter(context,android.R.layout.simple_spinner_item,strPersonalHealthInsurance);
                    adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    PersonalHealthInsurance.setAdapter(adp1);

                }
            }
        });

        radioHindi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    isEnSelected=false;
                    //lblHeader.setText(lbHeader_Hn);
                    //lblInfo.setText(strInfo_Hn);
                    lblHeader.setText(lblHeader_Hn);
                    NoOfPersonUsingToilets.setHint(NoOfPersonUsingToilets_Hn);
                    ArrayAdapter adp1 = new ArrayAdapter(context,android.R.layout.simple_spinner_item,strdrinkingWaterSource_Hn);
                    adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    drinkingWaterSource.setAdapter(adp1);

                    adp1 = new ArrayAdapter(context,android.R.layout.simple_spinner_item,strdrinkingWaterDistance_Hn);
                    adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    drinkingWaterDistance.setAdapter(adp1);

                    adp1 = new ArrayAdapter(context,android.R.layout.simple_spinner_item,strisdrinkingWaterTreatmentRequired_Hn);
                    adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    isdrinkingWaterTreatmentRequired.setAdapter(adp1);

                    adp1 = new ArrayAdapter(context,android.R.layout.simple_spinner_item,strNonUsageOfToilets_Hn);
                    adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    NonUsageOfToilets.setAdapter(adp1);

                    adp1 = new ArrayAdapter(context,android.R.layout.simple_spinner_item,strDistanceOfSubcenters_Hn);
                    adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    DistanceOfSubcenters.setAdapter(adp1);

                    adp1 = new ArrayAdapter(context,android.R.layout.simple_spinner_item,strDistanceOfPrimaryHealthcenters_Hn);
                    adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    DistanceOfPrimaryHealthcenters.setAdapter(adp1);

                    adp1 = new ArrayAdapter(context,android.R.layout.simple_spinner_item,strDistanceOfCommunityHealthcenters_Hn);
                    adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    DistanceOfCommunityHealthcenters.setAdapter(adp1);

                    adp1 = new ArrayAdapter(context,android.R.layout.simple_spinner_item,strDistanceOfDistrictHospitals_Hn);
                    adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    DistanceOfDistrictHospitals.setAdapter(adp1);

                    adp1 = new ArrayAdapter(context,android.R.layout.simple_spinner_item,strDistanceOfPathologyLab_Hn);
                    adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    DistanceOfPathologyLab.setAdapter(adp1);

                    adp1 = new ArrayAdapter(context,android.R.layout.simple_spinner_item,strDistanceOfMedicalStore_Hn);
                    adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    DistanceOfMedicalStore.setAdapter(adp1);

                    adp1 = new ArrayAdapter(context,android.R.layout.simple_spinner_item,strStatusOfDeliveryOfChildren_Hn);
                    adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    StatusOfDeliveryOfChildren.setAdapter(adp1);

                    adp1 = new ArrayAdapter(context,android.R.layout.simple_spinner_item,strStatusOfVaccinationOfChildren_Hn);
                    adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    StatusOfVaccinationOfChildren.setAdapter(adp1);

                    adp1 = new ArrayAdapter(context,android.R.layout.simple_spinner_item,strStatusOfFemaleRelatedProblem_Hn);
                    adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    StatusOfFemaleRelatedProblem.setAdapter(adp1);

                    adp1 = new ArrayAdapter(context,android.R.layout.simple_spinner_item,strCentrallyIssuedHealthInsurance_Hn);
                    adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    CentrallyIssuedHealthInsurance.setAdapter(adp1);

                    adp1 = new ArrayAdapter(context,android.R.layout.simple_spinner_item,strStateIssuedHealthInsurance_Hn);
                    adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    StateIssuedHealthInsurance.setAdapter(adp1);

                    adp1 = new ArrayAdapter(context,android.R.layout.simple_spinner_item,strPersonalHealthInsurance_Hn);
                    adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    PersonalHealthInsurance.setAdapter(adp1);

                }
            }
        });



        //getFamilyID();
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitForm(v);
            }
        });








        return view;
    }



    void submitForm(View v){
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String currentDate = simpleDateFormat.format(new Date());
       // ErrBox.errorsStatus(); // Here is error you need to solve
        HashMap<String,String> paramHash=new HashMap<String,String>();
        paramHash.put("citizenId",  "162987320156890888" );
        paramHash.put("familyId",  Config.tmp_familyid );

        paramHash.put("drinkingWaterSource", drinkingWaterSource.getSelectedItem().toString());
        paramHash.put("drinkingWaterDistance", drinkingWaterDistance.getSelectedItem().toString());
        paramHash.put("isdrinkingWaterTreatmentRequired", isdrinkingWaterTreatmentRequired.getSelectedItem().toString());
        paramHash.put("NoOfPersonUsingToilets", NoOfPersonUsingToilets.getText().toString());
        paramHash.put("NonUsageOfToilets", NonUsageOfToilets.getSelectedItem().toString());
        paramHash.put("DistanceOfSubcenters", DistanceOfSubcenters.getSelectedItem().toString());
        paramHash.put("DistanceOfPrimaryHealthcenters", DistanceOfPrimaryHealthcenters.getSelectedItem().toString());
        paramHash.put("DistanceOfCommunityHealthcenters", DistanceOfCommunityHealthcenters.getSelectedItem().toString());
        paramHash.put("DistanceOfDistrictHospitals", DistanceOfDistrictHospitals.getSelectedItem().toString());
        paramHash.put("DistanceOfPathologyLab", DistanceOfPathologyLab.getSelectedItem().toString());
        paramHash.put("DistanceOfMedicalStore", DistanceOfMedicalStore.getSelectedItem().toString());
        paramHash.put("StatusOfDeliveryOfChildren", StatusOfDeliveryOfChildren.getSelectedItem().toString());
        paramHash.put("StatusOfVaccinationOfChildren", StatusOfVaccinationOfChildren.getSelectedItem().toString());
        paramHash.put("StatusOfFemaleRelatedProblem", StatusOfFemaleRelatedProblem.getSelectedItem().toString());
        paramHash.put("CentrallyIssuedHealthInsurance", CentrallyIssuedHealthInsurance.getSelectedItem().toString());
        paramHash.put("StateIssuedHealthInsurance", StateIssuedHealthInsurance.getSelectedItem().toString());
        paramHash.put("PersonalHealthInsurance", PersonalHealthInsurance.getSelectedItem().toString());
        paramHash.put("bpStatus", "{citizenId:Yes}");
        paramHash.put("hbTestStatusFemale", "{citizenId:Yes}");
        paramHash.put("sugarTestStatus", "{citizenId:Yes}");
        paramHash.put("smokingStatus", "{citizenId:Yes}");
        paramHash.put("alcoholStatus", "{citizenId:Yes}");
        paramHash.put("tobaccoStatus", "{citizenId:Yes}");
        paramHash.put("screenerId",Config._screenerid);
        paramHash.put("ngoId", Config.NGO_ID);

        //paramHash.put("screenerId","162549430911779891");

        if(Config.isOffline){
            try {
                SQLiteDatabase db;
                db = getContext().openOrCreateDatabase("javixlife", Context.MODE_PRIVATE, null);
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
                SqlStr += " VALUES('Health Survey','" + MyConfig.URL_HEALTHSURVEY + "','" + paramHash.toString() + "','"+jsonObject.toString()+"','" + currentDate + "',0);";
                //Toast.makeText(getApplicationContext(), SqlStr, Toast.LENGTH_LONG).show();
                System.out.println("Offline:"+SqlStr);
                db.execSQL(SqlStr);
                db.close();
                android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(getActivity());
                alertDialog.setMessage(getActivity().getString(R.string._healthSurveyAdd_he));
                alertDialog.setPositiveButton(getActivity().getString(R.string._ok_he), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
//                        Intent i = new Intent(getActivity(), SurveyMenuActivity.class);
                      //  Intent i = new Intent(getActivity(), SevikaActivity.class);
                        Config.tmp_caseId="0";
                      //  getActivity().finish();
                      //  startActivity(i);


                        SocioEconomicSurveyFragment socioEconomicSurveyFragment = new SocioEconomicSurveyFragment();
                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.Frame_Container, socioEconomicSurveyFragment);
                        transaction.commit();

                        imgV2.setImageResource(R.drawable.done);
                        progress_two.getProgressDrawable().setColorFilter(Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);
                        progress_two.setProgress(100);

                        Shared_Preferences.setPrefs(getActivity(), "HS_Done", "1");


                    }
                });
                alertDialog.create();
                alertDialog.show();

            }catch (Exception ex){

                android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(getActivity()
                );
                alertDialog.setMessage("Error in saving data " + ex.getLocalizedMessage());
                alertDialog.setPositiveButton(getActivity().getString(R.string._ok_he), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
//                        Intent i = new Intent(getActivity(), SurveyMenuActivity.class);
                        Intent i = new Intent(getActivity(), SevikaActivity.class);
                        Config.tmp_caseId="0";
                        getActivity().finish();
                        startActivity(i);
                    }
                });
                alertDialog.create();
                alertDialog.show();
            }

        }else {
            HealthSurveyFragment.SubmitForm req=new HealthSurveyFragment.SubmitForm(getActivity(),paramHash);
            req.execute(MyConfig.URL_HEALTHSURVEY);
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

                    android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(getActivity());
                    alertDialog.setMessage(getActivity().getString(R.string._healthSurveyAdd_he));
                    alertDialog.setPositiveButton(getActivity().getString(R.string._ok_he), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            if(Config.tmp_familyid!=null)
                                Config.tmp_familyid=null;
                         //   Intent i = new Intent(getActivity(), SurveyMenuActivity.class);
                          //  getActivity().finish();
                          //  startActivity(i);

                            SocioEconomicSurveyFragment socioEconomicSurveyFragment = new SocioEconomicSurveyFragment();
                            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.Frame_Container, socioEconomicSurveyFragment);
                            transaction.commit();

                            imgV2.setImageResource(R.drawable.done);
                            progress_two.getProgressDrawable().setColorFilter(Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);
                            progress_two.setProgress(100);

                            Shared_Preferences.setPrefs(getActivity(), "HS_Done", "1");
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

    public void getFamilyID(){

        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String currentDate = simpleDateFormat.format(new Date());
       // ErrBox.errorsStatus();
        HashMap<String,String> paramHash=new HashMap<String,String>();
        paramHash.put("citizenId","");
        paramHash.put("ngoId", Config.NGO_ID);

        HealthSurveyFragment.getDataList req = new HealthSurveyFragment.getDataList(getActivity(),paramHash);
        req.execute(MyConfig.URL_GENERALSURVEYLIST);

    }

    class getDataList extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        Activity activity;
        HashMap<String, String> paramsHash=null;
        RequestPipe requestPipe=new RequestPipe();
        public getDataList(Activity activity,HashMap<String, String> paramsHash) {
            this.activity=activity;
            this.paramsHash=paramsHash;
            progressDialog = new ProgressDialog(activity);
            progressDialog.setMessage(getActivity().getString(R.string._loading_he));
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

                    Toast.makeText(MyConfig.CONTEXT, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                }
                else Toast.makeText(MyConfig.CONTEXT, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

            }catch (Exception ee){
                Toast.makeText(MyConfig.CONTEXT, "Error !.", Toast.LENGTH_SHORT).show();
            }

        }

    }
}