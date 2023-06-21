package com.sumago.latestjavix;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.sumago.latestjavix.Util.Config;

public class ReferredFullDetails extends AppCompatActivity {

    String name, case_id,screening_date,height,weight, bmi, bp_sys, bp_dia, spo2, heartRateBgm, temperature, arm;

    String CitizenId,ScreenerId, doctorId, recordId, caseId;

    TextView tv_refer_full_list_name, tv_refer_full_list_gender,tv_refer_full_list_case_id,tv_refer_full_list_screening_date, tv_refer_full_list_height,tv_refer_full_list_weight,tv_refer_full_list_bmi, tv_refer_full_list_bp_sys,tv_refer_full_list_bp_dia, tv_refer_full_list_spo2, tv_refer_full_list_heart_rate_bgm, tv_refer_full_list_temperature, tv_refer_full_list_arm;

    AppCompatButton btn_refer_full_list_pick_prescribe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_referred_full_details_custom);

        tv_refer_full_list_name = findViewById(R.id.tv_refer_full_list_name);
        tv_refer_full_list_gender = findViewById(R.id.tv_refer_full_list_gender);
        tv_refer_full_list_case_id = findViewById(R.id.tv_refer_full_list_case_id);
        tv_refer_full_list_screening_date = findViewById(R.id.tv_refer_full_list_screening_date);
        tv_refer_full_list_height = findViewById(R.id.tv_refer_full_list_height);
        tv_refer_full_list_weight = findViewById(R.id.tv_refer_full_list_weight);
        tv_refer_full_list_bmi = findViewById(R.id.tv_refer_full_list_bmi);
        tv_refer_full_list_bp_sys = findViewById(R.id.tv_refer_full_list_bp_sys);
        tv_refer_full_list_bp_dia = findViewById(R.id.tv_refer_full_list_bp_dia);
        tv_refer_full_list_spo2 = findViewById(R.id.tv_refer_full_list_spo2);
        tv_refer_full_list_heart_rate_bgm = findViewById(R.id.tv_refer_full_list_heart_rate_bgm);
        tv_refer_full_list_temperature = findViewById(R.id.tv_refer_full_list_temperature);
        tv_refer_full_list_arm = findViewById(R.id.tv_refer_full_list_arm);

        btn_refer_full_list_pick_prescribe = findViewById(R.id.btn_refer_full_list_pick_prescribe);

        Intent intent = getIntent();

        name = intent.getStringExtra("name");
        case_id = intent.getStringExtra("case_id");
        screening_date = intent.getStringExtra("screening_date");
        height = intent.getStringExtra("height");
        weight = intent.getStringExtra("weight");
        bmi = intent.getStringExtra("bmi");
        bp_sys = intent.getStringExtra("bp_sys");
        bp_dia = intent.getStringExtra("bp_dia");
        spo2 = intent.getStringExtra("spo2");
        heartRateBgm = intent.getStringExtra("heartRateBgm");
        temperature = intent.getStringExtra("temperature");
        arm = intent.getStringExtra("arm");



        CitizenId = intent.getStringExtra("CitizenId");
        ScreenerId = intent.getStringExtra("ScreenerId");
        doctorId = intent.getStringExtra("doctorId");
        recordId = intent.getStringExtra("recordId");
        caseId = intent.getStringExtra("caseId");

        Log.e("check_intent_data", "CitizenId " +CitizenId + " screenerId " +ScreenerId + " doctorId " +doctorId +" recordId " +recordId + " caseId " +case_id );



        tv_refer_full_list_name.setText(name);
        tv_refer_full_list_gender.setText("Male");
        tv_refer_full_list_case_id.setText(case_id);
        tv_refer_full_list_screening_date.setText(screening_date);
        tv_refer_full_list_height.setText(height);
        tv_refer_full_list_weight.setText(weight);
        tv_refer_full_list_bmi.setText(bmi);
        tv_refer_full_list_bp_sys.setText(bp_sys);
        tv_refer_full_list_bp_dia.setText(bp_dia);
        tv_refer_full_list_spo2.setText(spo2);
        tv_refer_full_list_heart_rate_bgm.setText(heartRateBgm);
        tv_refer_full_list_temperature.setText(temperature);
        tv_refer_full_list_arm.setText(arm);


        btn_refer_full_list_pick_prescribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(btn_refer_full_list_pick_prescribe.getContext());
                alertDialog.setMessage("Are you sure want to pick & prescribe!");
                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                        Intent ii = new Intent(ReferredFullDetails.this, AddPrescriptionActivity.class);
                      //  Log.e(TAG, "parameters : " + List.get(position).getScreenerId());
//                        i.putExtra("CitizenId", rec.citizenid);
//                        i.putExtra("ScreenerId", rec.screenerid);
//                        i.putExtra("doctorId", Config._doctorid);
//                        i.putExtra("recordId", "0");
//                        i.putExtra("caseId", rec.caseid);

                        ii.putExtra("CitizenId", CitizenId);
                        ii.putExtra("ScreenerId", ScreenerId);
                        ii.putExtra("doctorId", doctorId);
                        ii.putExtra("recordId", "0");
                        ii.putExtra("caseId", case_id);
                        Log.e("Response_Check", "caseId: " + case_id);
                        startActivity(ii);
                        dialogInterface.dismiss();
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


                // Toast.makeText(context, "Button is clicked", Toast.LENGTH_SHORT).show();

            }
        });

    }
}