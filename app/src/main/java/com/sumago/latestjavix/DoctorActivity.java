package com.sumago.latestjavix;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.SupportMenuInflater;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.ArrayList;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.sumago.latestjavix.Adapters.DoctorAdapter;
import com.sumago.latestjavix.Adapters.PromotionAdapter;
import com.sumago.latestjavix.Model.SettingsModel;
import com.sumago.latestjavix.Util.Config;
import com.sumago.latestjavix.Util.Shared_Preferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class DoctorActivity extends AppCompatActivity implements View.OnClickListener{
    private DoctorAdapter homeAdapter;
    private ArrayList<SettingsModel> settingsModelArrayList;
    private RecyclerView recyclerView;
    ImageView imgmenu;
    private static final String TAG = "_msg";
    /*Integer[] iv_logo={R.drawable.appointment,R.drawable.searchpng,R.drawable.diagonistics,
            R.drawable.pharmacy,R.drawable.reports,R.drawable.logout};

    String[] tv_settings={"New Patient  \n" +
            "","Find Patient \n" +
            "","Investigation\n" +
            "","Prescription \n" +
            "","Reports\n" +
            "","Logout \n" +
            ""};*/

    Integer[] iv_logo={R.drawable.ngo_change,R.drawable.searchpng,
            R.drawable.ngo_complain,R.drawable.reports, R.drawable.refered, R.drawable.prescribe6};
    String[] tv_settings={
            "Recent Investigation \n" +
            "","Citizen List \n" +
            "","Help\n" +
            "","Directory\n" +
            "", "Referred List \n" +
            "", "Prescribed List"};

    /*Integer[] iv_logo={R.drawable.searchpng,
            R.drawable.ngo_complain,R.drawable.reports};
    String[] tv_settings={

                   "Citizen List \n" +
            "","Report an Issue\n" +
            "","Directory\n" +


            ""};*/

    private PromotionAdapter promotionAdapter;
    private ArrayList<SettingsModel> settingsModelArrayList1;
    private RecyclerView recyclerView1;

    Integer[] iv_logo1={R.drawable.ic_launcher_background,R.drawable.ic_launcher_foreground};
    String[] tv_settings1={"New invite profit","Cash back bonus"};

    LinearLayout li_home,li_clock,li_user;
    ImageView img_home,img_clock,img_user;
    TextView txtWelcome,txLogout;
    HttpURLConnection conn;
    HashMap hasData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);
        setTitle("Doctor's Dashboard");

        Config.doctor = "1";

        Log.e("Response", "Uid :" +Config._uid);
        Log.d(TAG, "onClick:iddd "+Config._doctorid);

        Log.e("Checking", "temp_case_id: " + Config.tmp_caseId);


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
                        i = new Intent(DoctorActivity.this, ViewDoctorProfileActivity.class);
                        startActivity(i);
                        return true;
                    default: return true;
                }
                //return true;
            }
        });



        loadData();  // please change after loginupdate api will start working




        /*imgmenu=(ImageView) findViewById(R.id.imgmenu);
        imgmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PopupMenu popup = new PopupMenu(DoctorActivity.this, view);
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
                popup.getMenuInflater().inflate(R.menu.doctor_navigation_items, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    public boolean onMenuItemClick(MenuItem item) {
                        //Toast.makeText(getApplicationContext(), "You Clicked : " + item.getTitle(),  Toast.LENGTH_SHORT).show();
                        Intent i;
                        switch (item.getTitle().toString()){
                            case "Profile":
                                i = new Intent(DoctorActivity.this, ViewDoctorProfileActivity.class);
                                startActivity(i);
                                break;
                            case "Directory":
                                i = new Intent(DoctorActivity.this, DirectoryActivity.class);
                                //i.putExtra("ParentClassSource", "com.javix.javixlifehealthcare.NgoActivity");
                                startActivity(i);
                                break;
                            case "Ondemand Investigation":
                                i = new Intent(DoctorActivity.this, ScreeningListActivity.class);
                                //i.putExtra("ParentClassSource", "com.javix.javixlifehealthcare.NgoActivity");
                                startActivity(i);
                                break;
                            case "Recent Investigation":
                                i = new Intent(DoctorActivity.this, ScreeningPickedActivity.class);
                                //i.putExtra("ParentClassSource", "com.javix.javixlifehealthcare.NgoActivity");
                                startActivity(i);
                                break;
                            case "Report Issues":
                                i = new Intent(DoctorActivity.this, IssueListActivity.class);
                                //i.putExtra("ParentClassSource", "com.javix.javixlifehealthcare.NgoActivity");
                                startActivity(i);
                                break;
                            case "Report":
                                i = new Intent(DoctorActivity.this, ReportActivity.class);
                                //i.putExtra("ParentClassSource", "com.javix.javixlifehealthcare.NgoActivity");
                                startActivity(i);
                                break;
                            case "Schedules":
                                i = new Intent(DoctorActivity.this, ScheduleActivity.class);
                                //i.putExtra("ParentClassSource", "com.javix.javixlifehealthcare.NgoActivity");
                                startActivity(i);
                                break;

                        }

                        return true;
                    }
                });

                popup.show();

            }
        });*/
        Config.tmp_Pid="0";

        txtWelcome=(TextView) findViewById(R.id.txtWelcome);
        txtWelcome.setText("Welcome Dr." + Config._fname + " " + Config._lname);
        txLogout=(TextView)findViewById(R.id.txLogout);
        txLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(DoctorActivity.this
                );
                alertDialog.setMessage("Are you sure want to logout !");
                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog
                        submitForm();
                        //finish();
                        //System.exit(0);
                        dialog.dismiss();
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
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(DoctorActivity.this,3);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        settingsModelArrayList = new ArrayList<>();

        for (int i = 0; i < iv_logo.length; i++) {
            SettingsModel view1 = new SettingsModel(iv_logo[i],tv_settings[i]);
            settingsModelArrayList.add(view1);
        }
        homeAdapter = new DoctorAdapter(DoctorActivity.this,settingsModelArrayList);
        recyclerView.setAdapter(homeAdapter);
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


    public void loadData()
    {
        class DownloadJSON extends AsyncTask<Void, Void, String> {
            private Dialog loadingDialog;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingDialog = ProgressDialog.show(DoctorActivity.this, "Please wait", "Loading...");
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
                    URL url = new URL("http://143.244.136.145:3010/api/doctor/doctorById");
//                    URL url = new URL("http://159.65.148.197:3001/api/doctor/doctorById");


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

                        Log.e("Response", "Response in Doctor Activity: " +response);

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
                    Config._doctorid=rec.getString("doctorId");

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

    void submitForm()
    {
        HashMap<String,String> paramHash=new HashMap<String,String>();
        paramHash.put("doctorId", Config._uid);
        paramHash.put("loggedIn","0");
        SubmitForm req=new SubmitForm(this,paramHash);
        req.execute("http://143.244.136.145:3010/api/login/updatelogindoc");

    }

    //PUSH DATA
    class SubmitForm extends AsyncTask<String, Void, String>
    {
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
        protected void onPreExecute()
        {
            progressDialog.show();
        }
        @Override
        protected String doInBackground(String... params) {

            Log.e(TAG, "parametersssss : " +paramsHash);
            return requestPipe.requestForm(params[0],paramsHash);
        }
        protected void onProgressUpdate(Void ...progress) {
            super.onProgressUpdate(progress);

        }
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.e("Response", "Result: " +result);
            progressDialog.cancel();
            //Toast.makeText(MyConfig.CONTEXT,"RES:"+result,Toast.LENGTH_SHORT).show();
            try {
                JSONObject jsonObject = new JSONObject(result);
                int respStatus=jsonObject.getInt("status");
                if(respStatus==1) {
                    Intent i = new Intent(DoctorActivity.this, SplashActivity.class);
                    finish();
                    startActivity(i);

                    //Toast.makeText(getApplicationContext(), "Successfully Loggedout", Toast.LENGTH_SHORT).show();
                }
                else Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

            }catch (Exception ee){
                Toast.makeText(getApplicationContext(), "Exception !.", Toast.LENGTH_SHORT).show();
            }

        }

    }

    @Override
    protected void onStop() {
        super.onStop();

       Config.doctor= "0";
    }


    @Override
    public void onBackPressed() {




        AlertDialog.Builder builder = new AlertDialog.Builder(DoctorActivity.this);

        // Set the message show for the Alert time
        builder.setMessage("Do you want to exit ?");

        // Set Alert Title
        builder.setTitle("Alert !");

        // Set Cancelable false for when the user clicks on the outside the Dialog Box then it will remain show
        builder.setCancelable(false);

        // Set the positive button with yes name Lambda OnClickListener method is use of DialogInterface interface.
        builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {
            // When the user click yes button then app will close
            finish();
        });

        // Set the Negative button with No name Lambda OnClickListener method is use of DialogInterface interface.
        builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
            // If user click no then dialog box is canceled.
            dialog.cancel();
        });

        // Create the Alert dialog
        AlertDialog alertDialog = builder.create();
        // Show the Alert Dialog box
        alertDialog.show();



    }
}