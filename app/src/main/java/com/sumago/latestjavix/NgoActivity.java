package com.sumago.latestjavix;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.sumago.latestjavix.Adapters.HomeAdapter;
import com.sumago.latestjavix.Adapters.PromotionAdapter;
import com.sumago.latestjavix.Model.SettingsModel;
import com.sumago.latestjavix.Util.Config;

import org.w3c.dom.Text;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;


public class NgoActivity extends AppCompatActivity implements View.OnClickListener{
    private HomeAdapter homeAdapter;
    private ArrayList<SettingsModel> settingsModelArrayList;
    private RecyclerView recyclerView;
    ImageView imgmenu;
    Integer[] iv_logo={R.drawable.ngo_approve,R.drawable.searchpng,R.drawable.ngo_change,
            R.drawable.ngo_complain,R.drawable.reports,R.drawable.logout};

    String[] tv_settings={"Approval of Screener  \n" +
            "","Find Screener \n" +
            "","Change Request\n" +
            "","Raise Issue \n" +
            "","Download Reports\n" +
            "","Logout \n" +
            ""};

    private PromotionAdapter promotionAdapter;
    private ArrayList<SettingsModel> settingsModelArrayList1;
    private RecyclerView recyclerView1;

    Integer[] iv_logo1={R.drawable.ic_launcher_background,R.drawable.ic_launcher_foreground};
    String[] tv_settings1={"New invite profit","Cash back bonus"};

    LinearLayout li_home,li_clock,li_user;
    ImageView img_home,img_clock,img_user;
    TextView txtWelcome,txLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ngo);
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //Toast.makeText(getApplicationContext(),Integer.toString(item.getItemId()),Toast.LENGTH_LONG).show();
                Intent i;
                switch (item.getItemId()) {
                    case R.id.action_search:
                        // do something here
                        return true;
                    case R.id.action_settings:
                        // do something here
                        return true;
                    case R.id.action_navigation:
                        i = new Intent(NgoActivity.this, ViewNgoProfileActivity.class);
                        startActivity(i);
                        return true;
                    default: return true;
                }
                //return true;
            }
        });


        imgmenu=(ImageView) findViewById(R.id.imgmenu);
        imgmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ///PopupMenu popup = new PopupMenu(DoctorActivity.this, view);
                //popup.setOnMenuItemClickListener(DoctorActivity.this);
                //popup.inflate(R.menu.doctor_navigation_items);
                //popup.show();


                PopupMenu popup = new PopupMenu(NgoActivity.this, view);
                try {
                    Field[] fields = popup.getClass().getDeclaredFields();
                    for (Field field : fields) {
                        if ("mPopup".equals(field.getName())) {
                            field.setAccessible(true);
                            Object menuPopupHelper = field.get(popup);
                            Class<?> classPopupHelper = Class.forName(menuPopupHelper.getClass().getName());
                            Method setForceIcons = classPopupHelper.getMethod("setForceShowIcon",boolean.class);
                            setForceIcons.invoke(menuPopupHelper, true);
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                popup.getMenuInflater().inflate(R.menu.ngo_navigation_items, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    public boolean onMenuItemClick(MenuItem item) {
                        //Toast.makeText(getApplicationContext(), "You Clicked : " + item.getTitle(),  Toast.LENGTH_SHORT).show();
                        Intent i;
                        switch (item.getTitle().toString()){
                            case "Profile":
                                i = new Intent(NgoActivity.this, ViewNgoProfileActivity.class);
                                startActivity(i);
                                break;
                            case "Directory":
                                i = new Intent(NgoActivity.this, DirectoryActivity.class);
                                //i.putExtra("ParentClassSource", "com.javix.javixlifehealthcare.NgoActivity");
                                startActivity(i);
                                break;
                            case "Map Screener":
                                i = new Intent(NgoActivity.this, MapScreenerActivity.class);
                                //i.putExtra("ParentClassSource", "com.javix.javixlifehealthcare.NgoActivity");
                                startActivity(i);
                                break;
                            case "Change Request":
                                i = new Intent(NgoActivity.this, ChangeRequestActivity.class);
                                //i.putExtra("ParentClassSource", "com.javix.javixlifehealthcare.NgoActivity");
                                startActivity(i);
                                break;
                            case "Report Issues":
                                i = new Intent(NgoActivity.this, IssueListActivity.class);
                                //i.putExtra("ParentClassSource", "com.javix.javixlifehealthcare.NgoActivity");
                                startActivity(i);
                                break;
                            case "Report":
                                i = new Intent(NgoActivity.this, ReportActivity.class);
                                //i.putExtra("ParentClassSource", "com.javix.javixlifehealthcare.NgoActivity");
                                startActivity(i);
                                break;

                        }

                        return true;
                    }
                });

                popup.show();

            }
        });


        txtWelcome=(TextView) findViewById(R.id.txtWelcome);

        txtWelcome.setText("Welcome " + Config._fname + " " + Config._lname);

        txLogout=(TextView)findViewById(R.id.txLogout);
        txLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(NgoActivity.this
                );
                alertDialog.setMessage("Are your sure want to logout !");
                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog
                        Intent i = new Intent(NgoActivity.this, SplashActivity.class);
                        finish();
                        startActivity(i);
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

            }
        });


       /* recyclerView = findViewById(R.id.rv_features);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(NgoActivity.this,3);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        settingsModelArrayList = new ArrayList<>();

        for (int i = 0; i < iv_logo.length; i++) {
            SettingsModel view1 = new SettingsModel(iv_logo[i],tv_settings[i]);
            settingsModelArrayList.add(view1);
        }
        homeAdapter = new HomeAdapter(NgoActivity.this,settingsModelArrayList);
        recyclerView.setAdapter(homeAdapter);

        recyclerView1 = findViewById(R.id.rv_specialpromotion);
        RecyclerView.LayoutManager layoutManager1 = new GridLayoutManager(NgoActivity.this,2);
        recyclerView1.setLayoutManager(layoutManager1);
        recyclerView1.setItemAnimator(new DefaultItemAnimator());
        settingsModelArrayList1 = new ArrayList<>();

        for (int i = 0; i < iv_logo1.length; i++) {
            SettingsModel view1 = new SettingsModel(iv_logo1[i],tv_settings1[i]);
            settingsModelArrayList1.add(view1);
        }*/
        //promotionAdapter = new PromotionAdapter(MainActivity.this,settingsModelArrayList1);
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