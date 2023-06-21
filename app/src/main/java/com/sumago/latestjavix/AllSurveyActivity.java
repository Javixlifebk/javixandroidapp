package com.sumago.latestjavix;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.sumago.latestjavix.Fragments.GeneralSurveyFragment;
import com.sumago.latestjavix.Fragments.HealthSurveyFragment;
import com.sumago.latestjavix.Fragments.SocioEconomicSurveyFragment;
import com.sumago.latestjavix.Util.Shared_Preferences;

public class AllSurveyActivity extends AppCompatActivity {

    public static ImageView imgV1, imgV2, imgV3;
    public static ProgressBar progress_one, progress_two;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_survey);

        imgV1 = findViewById(R.id.imgV1);
        imgV2 = findViewById(R.id.imgV2);
        imgV3 = findViewById(R.id.imgV3);

        progress_one = findViewById(R.id.progress_one);
        progress_two = findViewById(R.id.progress_two);


        getSupportFragmentManager().beginTransaction().replace(R.id.Frame_Container, new GeneralSurveyFragment()).commit();


        try {

            if (Shared_Preferences.getPrefs(AllSurveyActivity.this, "GS").equals("2") ){

                getSupportFragmentManager().beginTransaction().replace(R.id.Frame_Container, new HealthSurveyFragment()).commit();
                Shared_Preferences.setPrefs(AllSurveyActivity.this, "GS", "");

            }
            else if (Shared_Preferences.getPrefs(AllSurveyActivity.this, "SS").equals("3")){
                getSupportFragmentManager().beginTransaction().replace(R.id.Frame_Container, new SocioEconomicSurveyFragment()).commit();
                Shared_Preferences.setPrefs(AllSurveyActivity.this, "SS", "");

            }

            Log.e("Response", "Checking Message: " +Shared_Preferences.getPrefs(this, "GS"));

        }catch (Exception e){

            e.printStackTrace();
        }




        imgV1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(AllSurveyActivity.this, "Button is clicked", Toast.LENGTH_SHORT).show();
                GeneralSurveyFragment generalSurveyFragment = new GeneralSurveyFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.Frame_Container, generalSurveyFragment);
                transaction.commit();
            }
        });


        imgV2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HealthSurveyFragment healthSurveyFragment = new HealthSurveyFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.Frame_Container, healthSurveyFragment);
                transaction.commit();
            }
        });


        imgV3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SocioEconomicSurveyFragment socioEconomicSurveyFragment = new SocioEconomicSurveyFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.Frame_Container, socioEconomicSurveyFragment);
                transaction.commit();

            }
        });






    }
}