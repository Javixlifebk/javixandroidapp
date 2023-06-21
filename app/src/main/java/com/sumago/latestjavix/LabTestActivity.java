package com.sumago.latestjavix;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.sumago.latestjavix.Util.Config;

public class LabTestActivity extends AppCompatActivity {
    CardView crd_glucose,crd_lipid,crd_rapid,crd_drug,crd_eye,crd_hemo,crd_cell,crd_thala,crd_urine;
    Bundle bundle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab_test);
        initView();
    }
    public void initView() {
        crd_glucose = (CardView) findViewById(R.id.crd_glucose);
        crd_lipid = (CardView) findViewById(R.id.crd_lipid);
        crd_drug = (CardView) findViewById(R.id.crd_drug);
        crd_eye= (CardView) findViewById(R.id.crd_eye);
        crd_hemo= (CardView) findViewById(R.id.crd_hemo);
        crd_cell= (CardView) findViewById(R.id.crd_cell);
        crd_thala= (CardView) findViewById(R.id.crd_thala);
        crd_rapid= (CardView) findViewById(R.id.crd_rapid);
        crd_urine= (CardView) findViewById(R.id.crd_urine);
        bundle = getIntent().getExtras();
        if(Config.isOffline){
            crd_urine.setVisibility(View.GONE);
        }
        crd_urine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LabTestActivity.this, UrineAnalysis.class);
                //i.putExtra("CitizenId", bundle.getString("CitizenId"));
                //i.putExtra("ScreenerId", bundle.getString("ScreenerId"));
                startActivity(i);
            }
        });

        crd_glucose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    Intent i = new Intent(LabTestActivity.this, BloodGlucose.class);
                    startActivity(i);

            }
        });
        crd_lipid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    Intent i = new Intent(LabTestActivity.this, LipidPanelActivity.class);
                    startActivity(i);

            }
        });
        crd_drug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    Intent i = new Intent(LabTestActivity.this, DrugTestActivity.class);
                    startActivity(i);

            }
        });

        crd_eye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(LabTestActivity.this, EyeTestActivity.class);
                startActivity(i);

            }
        });

        crd_hemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(LabTestActivity.this, HemoglobinActivity.class);
                startActivity(i);

            }
        });

        crd_cell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(LabTestActivity.this, AddSickleCell.class);
                startActivity(i);

            }
        });

        crd_thala.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(LabTestActivity.this, AddThalassemia.class);
                startActivity(i);

            }
        });

        crd_rapid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(LabTestActivity.this, RapidTestActivity.class);
                startActivity(i);

            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // your code
            Log.e("Back Button","Pressed");
            Intent i = new Intent(LabTestActivity.this, TestListActivity.class);
            finish();
            startActivity(i);
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}