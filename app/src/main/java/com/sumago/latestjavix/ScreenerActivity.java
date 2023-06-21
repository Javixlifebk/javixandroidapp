package com.sumago.latestjavix;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;


public class ScreenerActivity extends AppCompatActivity {
    private HomeAdapter homeAdapter;
    private ArrayList<SettingsModel> settingsModelArrayList;
    private RecyclerView recyclerView;
    ImageView imgmenu;
    HttpURLConnection conn;
    TextView txBDate;
    HashMap hasData;
    Integer[] iv_logo={R.drawable.ic_citizen,R.drawable.ic_client,
            R.drawable.ic_instant_exam,R.drawable.ic_help,R.drawable.ic_directory,R.drawable.ic_offline};
    String[] tv_settings={"Add Citizen  \n" +
            "","Search Citizen \n" +
            "","Instant Exam\n" +
            "","Help\n" +
            "","Directory\n" +
            "","Offline\n" +

            ""};

   /* String[] tv_home_menu={"Add Citizen  \n" +
            "","Generate Bill \n" +
            "","Print Bill\n" +
            "","Make Payment \n" +
            "","Modification\n" +
            "","Complaints \n" +
            "","New Connection \n" +
            "","Add Missing Consumer \n" +
            "","Meter Change \n" +
            "","Lastest 10 Bills \n" +
            ""};*/

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
//        setContentView(R.layout.activity_screener);
        setContentView(R.layout.activity_screener_update);
        txBDate=(TextView)findViewById(R.id.txBDate);
        if(Config._roleid==21)
        setTitle("Sevika Dashboard");
        
       // Config.tmp_Pid="0";
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
                        i = new Intent(ScreenerActivity.this, ViewScreenerProfileActivity.class);
                        startActivity(i);
                        return true;
                    case R.id.action_logout:
                        logout();
                        return true;
                    default: return true;
                }
                //return true;
            }
        });



        loadData();
        imgmenu=(ImageView) findViewById(R.id.imgmenu);
        imgmenu.setVisibility(View.GONE);
        imgmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(ScreenerActivity.this, view);
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
                popup.getMenuInflater().inflate(R.menu.screener_navigation_items, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    public boolean onMenuItemClick(MenuItem item) {
                        //Toast.makeText(getApplicationContext(), "You Clicked : " + item.getTitle(),  Toast.LENGTH_SHORT).show();
                        Intent i;
                        switch (item.getTitle().toString()){
                            case "Profile":
                                i = new Intent(ScreenerActivity.this, ViewScreenerProfileActivity.class);
                                startActivity(i);
                                break;
                            case "Directory":
                                i = new Intent(ScreenerActivity.this, DirectoryActivity.class);
                                //i.putExtra("ParentClassSource", "com.javix.javixlifehealthcare.NgoActivity");
                                startActivity(i);
                                break;
                            case "Add Citizen":
                                i = new Intent(ScreenerActivity.this, AddCitizenActivity.class);
                                //i.putExtra("ParentClassSource", "com.javix.javixlifehealthcare.NgoActivity");
                                startActivity(i);
                                break;
                            case "Recent Investigation":
                                i = new Intent(ScreenerActivity.this, CitizenListActivity.class);
                                //i.putExtra("ParentClassSource", "com.javix.javixlifehealthcare.NgoActivity");
                                startActivity(i);
                                break;
                            case "Recent Screening":
                                i = new Intent(ScreenerActivity.this, CitizenListActivity.class);
                                //i.putExtra("ParentClassSource", "com.javix.javixlifehealthcare.NgoActivity");
                                startActivity(i);
                                break;
                            case "Report Issues":
                                i = new Intent(ScreenerActivity.this, IssueListActivity.class);
                                //i.putExtra("ParentClassSource", "com.javix.javixlifehealthcare.NgoActivity");
                                startActivity(i);
                                break;
                            case "Report":
                                i = new Intent(ScreenerActivity.this, ReportActivity.class);
                                //i.putExtra("ParentClassSource", "com.javix.javixlifehealthcare.NgoActivity");
                                startActivity(i);
                                break;
                            case "Schedules":
                                i = new Intent(ScreenerActivity.this, ScheduleActivity.class);
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
        String pattern = "dd-MM-yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String selDate = simpleDateFormat.format(Calendar.getInstance().getTime());
        txBDate.setText(selDate);
        //Config.tmp_Pid="0";
        txLogout=(TextView)findViewById(R.id.txLogout);
        txLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(ScreenerActivity.this
                );
                alertDialog.setMessage("Are you sure want to logout !");
                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog
                        Intent i = new Intent(ScreenerActivity.this, SplashActivity.class);
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

        recyclerView = findViewById(R.id.rv_features);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(ScreenerActivity.this,2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        settingsModelArrayList = new ArrayList<>();

        for (int i = 0; i < iv_logo.length; i++) {
            SettingsModel view1 = new SettingsModel(iv_logo[i],tv_settings[i]);
            settingsModelArrayList.add(view1);
        }
        homeAdapter = new HomeAdapter(ScreenerActivity.this,settingsModelArrayList);
        recyclerView.setAdapter(homeAdapter);

       /* recyclerView1 = findViewById(R.id.rv_specialpromotion);
        RecyclerView.LayoutManager layoutManager1 = new GridLayoutManager(ScreenerActivity.this,2);
        recyclerView1.setLayoutManager(layoutManager1);
        recyclerView1.setItemAnimator(new DefaultItemAnimator());
        settingsModelArrayList1 = new ArrayList<>();

        for (int i = 0; i < iv_logo1.length; i++) {
            SettingsModel view1 = new SettingsModel(iv_logo1[i],tv_settings1[i]);
            settingsModelArrayList1.add(view1);
        }
        promotionAdapter = new PromotionAdapter(ScreenerActivity.this,settingsModelArrayList1);*/
    }

    public void loadData()
    {
        class DownloadJSON extends AsyncTask<Void, Void, String> {
            private Dialog loadingDialog;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingDialog = ProgressDialog.show(ScreenerActivity.this, "Please wait", "Loading...");
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();
                try {
                    getJSONData(s);
                } catch (JSONException e) {
                    //e.printStackTrace();

                }

            }

            @Override
            protected String doInBackground(Void... voids) {

                try {
                    URL url = new URL("http://143.244.136.145:3010/api/ngo/screenerById");
                    StringBuilder sb = new StringBuilder();
                    conn = (HttpURLConnection) url.openConnection();
                    //conn.setRequestProperty("Content-Type", "application/text");
                    conn.setReadTimeout(15000);
                    conn.setConnectTimeout(15000);
                    conn.setRequestMethod("POST");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    conn.setChunkedStreamingMode(0);
                    OutputStream os = conn.getOutputStream();

                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8"));
                    StringBuilder result = new StringBuilder();
                    result.append(URLEncoder.encode("userId", "UTF-8"));
                    result.append("=");
                    result.append(URLEncoder.encode(Config._uid, "UTF-8"));
                    result.append("&");
                    result.append(URLEncoder.encode("token", "UTF-8"));
                    result.append("=");
                    result.append(URLEncoder.encode("dfjkhsdfaksjfh3756237", "UTF-8"));
                    writer.write(result.toString());
                    writer.flush();
                    writer.close();
                    os.close();

                    int responseCode = conn.getResponseCode();

                    if (responseCode == HttpsURLConnection.HTTP_OK) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        sb = new StringBuilder();
                        String response;
                        while ((response = br.readLine()) != null) {
                            sb.append(response);
                        }

                    }
                    else{

                        return "{\"status\":0,\"message\":\" Failed.\"}";
                    }


                    return sb.toString().trim();
                }catch (Exception e) {


                    return "{\"status\":0,\"message\":\"Failed.\"}";
                }
                finally {
                    conn.disconnect();
                }
            }
        }
        DownloadJSON getJSON = new DownloadJSON();
        getJSON.execute();
    }
    private void getJSONData(String json) throws JSONException {
        String jsonStr = json;
        int _status=0,roleId=0;
        if (jsonStr != null) {
            try {
                //String in;
                JSONObject obj1 = new JSONObject(jsonStr);
                JSONObject jsonObject = new JSONObject(jsonStr);
                //Log.e(TAG, "status : " + obj1.getString("message"));
                if(obj1.getString("status").equalsIgnoreCase("1")) {
                    JSONObject recsData=jsonObject.getJSONObject("data");
                    JSONArray recsArray=recsData.getJSONArray("data");
                    JSONObject rec = recsArray.getJSONObject(0);
                    Config._screenerid=rec.getString("screenerId");
                    //Toast.makeText(getApplicationContext(), "ScreenerID" +rec.getString("screenerId"), Toast.LENGTH_SHORT).show();

                }

            } catch (final JSONException e) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Json exception: " + e.getMessage(),
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }
        } else {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(),
                            "Couldn't get json from server. Check LogCat for possible errors!",
                            Toast.LENGTH_LONG)
                            .show();
                }
            });

        }

    }

    @Override
    public void onBackPressed() {
        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(ScreenerActivity.this
        );
        alertDialog.setMessage("Do you want to exit ?");
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // Do nothing but close the dialog
//                Intent i = new Intent(ScreenerActivity.this, SplashActivity.class);
                finishAffinity();
                finish();
//                startActivity(i);
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

    private void logout() {
        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(ScreenerActivity.this);
        alertDialog.setMessage("Are you sure want to logout !");
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // Do nothing but close the dialog
                startActivity(new Intent(ScreenerActivity.this, SplashActivity.class));
                finish();
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


}