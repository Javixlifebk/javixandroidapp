package com.sumago.latestjavix;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;


import com.sumago.latestjavix.Adapters.HomeAdapter;
import com.sumago.latestjavix.Adapters.PromotionAdapter;
import com.sumago.latestjavix.Model.SettingsModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private HomeAdapter homeAdapter;
    private ArrayList<SettingsModel> settingsModelArrayList;
    private RecyclerView recyclerView;

    Integer[] iv_logo={R.drawable.appointment,R.drawable.searchpng,R.drawable.diagonistics,
            R.drawable.pharmacy,R.drawable.reports,R.drawable.logout};

    String[] tv_settings={"New Patient  \n" +
            "","Find Patient \n" +
            "","Dianostics\n" +
            "","Pharmacy \n" +
            "","Reports\n" +
            "","Logout \n" +
            ""};

    private PromotionAdapter promotionAdapter;
    private ArrayList<SettingsModel> settingsModelArrayList1;
    private RecyclerView recyclerView1;

    Integer[] iv_logo1={R.drawable.ic_launcher_background,R.drawable.ic_launcher_foreground};
    String[] tv_settings1={"New invite profit","Cash back bonus"};

    LinearLayout li_home,li_clock,li_user;
    ImageView img_home,img_clock,img_user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.rv_features);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(MainActivity.this,3);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        settingsModelArrayList = new ArrayList<>();

        for (int i = 0; i < iv_logo.length; i++) {
            SettingsModel view1 = new SettingsModel(iv_logo[i],tv_settings[i]);
            settingsModelArrayList.add(view1);
        }
        homeAdapter = new HomeAdapter(MainActivity.this,settingsModelArrayList);
        recyclerView.setAdapter(homeAdapter);

        recyclerView1 = findViewById(R.id.rv_specialpromotion);
        RecyclerView.LayoutManager layoutManager1 = new GridLayoutManager(MainActivity.this,2);
        recyclerView1.setLayoutManager(layoutManager1);
        recyclerView1.setItemAnimator(new DefaultItemAnimator());
        settingsModelArrayList1 = new ArrayList<>();

        for (int i = 0; i < iv_logo1.length; i++) {
            SettingsModel view1 = new SettingsModel(iv_logo1[i],tv_settings1[i]);
            settingsModelArrayList1.add(view1);
        }
        promotionAdapter = new PromotionAdapter(MainActivity.this,settingsModelArrayList1);
    }

    @Override
    public void onClick(View view) {


        /*switch (view.getId()){
            case R.id.li_home:
                img_home.setColorFilter(Color.parseColor("#ff2e63"));
                img_clock.setColorFilter(Color.parseColor("#bcbcbc"));
                img_user.setColorFilter(Color.parseColor("#bcbcbc"));
                break;
            case R.id.li_clock:
                img_home.setColorFilter(Color.parseColor("#bcbcbc"));
                img_clock.setColorFilter(Color.parseColor("#ff2e63"));
                img_user.setColorFilter(Color.parseColor("#bcbcbc"));
                break;
            case R.id.li_user:
                img_home.setColorFilter(Color.parseColor("#bcbcbc"));
                img_clock.setColorFilter(Color.parseColor("#bcbcbc"));
                img_user.setColorFilter(Color.parseColor("#ff2e63"));
                break;
        }*/
    }
}