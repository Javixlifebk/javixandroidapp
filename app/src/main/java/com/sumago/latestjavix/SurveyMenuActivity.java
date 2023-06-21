package com.sumago.latestjavix;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.sumago.latestjavix.Util.Config;


public class SurveyMenuActivity extends AppCompatActivity {

    CardView crd_report,crd_report1,crd_report2;
    Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survay_menu);
        initView();
    }
    public void initView(){
        crd_report=(CardView)findViewById(R.id.crd_report);
        crd_report1=(CardView)findViewById(R.id.crd_report1);
        crd_report2=(CardView)findViewById(R.id.crd_report2);
        crd_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), GeneralSurvey.class);
                Config.tmp_citizenId=null;
                Config.tmp_citizenId_survey=null;
                Config.arrayList.clear();
                startActivity(i);
                finish();
            }
        });

       // crd_report2.setVisibility(View.GONE);
        crd_report1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Config.tmp_familyid=null;
                Intent i = new Intent(getApplicationContext(), HealthSurvey.class);
                startActivity(i);
                finish();
            }
        });
        crd_report2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Config.tmp_familyid=null;
                Intent i = new Intent(getApplicationContext(), SocioEconomic.class);
                startActivity(i);
                finish();
            }
        });
    }
    public void onBackPressed() {
        Log.d("CDA", "onBackPressed Called");

        this.finish();
    }
}