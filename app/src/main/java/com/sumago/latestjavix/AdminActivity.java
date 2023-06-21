package com.sumago.latestjavix;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
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
import java.util.ArrayList;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;


public class AdminActivity extends AppCompatActivity implements View.OnClickListener {
    private HomeAdapter homeAdapter;
    private ArrayList<SettingsModel> settingsModelArrayList;
    private RecyclerView recyclerView;
    HttpURLConnection conn;
    HashMap hasData;
    String strScreeners, strDoctors, strNGO, strCitizen, strPharmacy, strScreening, strSevika;

    private static final String TAG = "_msg";
    Integer[] iv_logo = {R.drawable.appointment, R.drawable.searchpng, R.drawable.diagonistics,
            R.drawable.pharmacy, R.drawable.reports, R.drawable.ngo_complain, R.drawable.users, R.drawable.logout};

    String[] tv_settings = {"Doctors  \n" +
            "", "NGO \n" +
            "", "Screener \n" +
            "", "Pharmacy \n" +
            "", "Reports\n" +
            "", "Complaints \n" +
            "", "Users \n" +
            "", "Logout \n" +
            ""};

    private PromotionAdapter promotionAdapter;
    private ArrayList<SettingsModel> settingsModelArrayList1;
    private RecyclerView recyclerView1;
    ImageView imgmenu;
    Integer[] iv_logo1 = {R.drawable.ic_launcher_background, R.drawable.ic_launcher_foreground};
    String[] tv_settings1 = {"New invite profit", "Cash back bonus"};

    LinearLayout li_home, li_clock, li_user;
    ImageView img_home, img_clock, img_user;
    TextView txtWelcome, txLogout;
    PieChart pieChart;
    ArrayList<PieEntry> visitorss;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        pieChart = findViewById(R.id.pieChart);
        loadData();
        visitorss = new ArrayList<>();
        pieChart.animate();


        imgmenu = (ImageView) findViewById(R.id.imgmenu);
        imgmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PopupMenu popup = new PopupMenu(AdminActivity.this, view);
                try {
                    Field[] fields = popup.getClass().getDeclaredFields();
                    for (Field field : fields) {
                        if ("mPopup".equals(field.getName())) {
                            field.setAccessible(true);
                            Object menuPopupHelper = field.get(popup);
                            Class<?> classPopupHelper = Class.forName(menuPopupHelper.getClass().getName());
                            Method setForceIcons = classPopupHelper.getMethod("setForceShowIcon", boolean.class);
                            setForceIcons.invoke(menuPopupHelper, true);
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                popup.getMenuInflater().inflate(R.menu.admin_navigation_items, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    public boolean onMenuItemClick(MenuItem item) {
                        Toast.makeText(getApplicationContext(), "You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                        Intent i;
                        switch (item.getTitle().toString()) {
                            case "Doctors":
                                i = new Intent(AdminActivity.this, DoctorListActivity.class);
                                startActivity(i);
                                break;
                            case "NGO":
                                i = new Intent(AdminActivity.this, ngoLists.class);
                                startActivity(i);
                                break;
                            case "Screener":
                                i = new Intent(AdminActivity.this, ScreenerList.class);
                                startActivity(i);
                                break;
                            case "Pharmacy":
                                i = new Intent(AdminActivity.this, PharmacyListActivity.class);
                                startActivity(i);
                                break;
                            case "Users":
                                i = new Intent(AdminActivity.this, UserList.class);
                                startActivity(i);
                                break;
                            case "Directory":
                                i = new Intent(AdminActivity.this, DirectoryActivity.class);
                                //i.putExtra("ParentClassSource", "com.javix.javixlifehealthcare.NgoActivity");
                                startActivity(i);
                                break;
                            case "Add Citizen":
                                i = new Intent(AdminActivity.this, AddCitizenActivity.class);
                                //i.putExtra("ParentClassSource", "com.javix.javixlifehealthcare.NgoActivity");
                                startActivity(i);
                                break;
                            case "Recent Investigation":
                                i = new Intent(AdminActivity.this, CitizenListActivity.class);
                                //i.putExtra("ParentClassSource", "com.javix.javixlifehealthcare.NgoActivity");
                                startActivity(i);
                                break;
                            case "Ondemand Screening":
                                i = new Intent(AdminActivity.this, CitizenListActivity.class);
                                //i.putExtra("ParentClassSource", "com.javix.javixlifehealthcare.NgoActivity");
                                startActivity(i);
                                break;
                            case "Report Issues":
                                i = new Intent(AdminActivity.this, IssueAllActivity.class);
                                //i.putExtra("ParentClassSource", "com.javix.javixlifehealthcare.NgoActivity");
                                startActivity(i);
                                break;
                            case "Actors":
                                i = new Intent(AdminActivity.this, AddActorsActivity.class);
                                //i.putExtra("ParentClassSource", "com.javix.javixlifehealthcare.NgoActivity");
                                startActivity(i);
                                break;
                            case "Schedules":
                                i = new Intent(AdminActivity.this, ScheduleActivity.class);
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
        txtWelcome = (TextView) findViewById(R.id.txtWelcome);
        txtWelcome.setText("Welcome " + Config._fname + " " + Config._lname);
        txLogout = (TextView) findViewById(R.id.txLogout);
        txLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(AdminActivity.this
                );
                alertDialog.setMessage("Are your sure want to logout !");
                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog
                        Intent i = new Intent(AdminActivity.this, SplashActivity.class);
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

    public void loadData() {
        class DownloadJSON extends AsyncTask<Void, Void, String> {
            private Dialog loadingDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingDialog = ProgressDialog.show(AdminActivity.this, "Please wait", "Loading...");
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
                    URL url = new URL(MyConfig.URL_GETLIST);
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

                    } else {

                        return "{\"status\":0,\"message\":\" Failed.\"}";
                    }


                    return sb.toString().trim();
                } catch (Exception e) {


                    return "{\"status\":0,\"message\":\"Failed.\"}";
                } finally {
                    conn.disconnect();
                }
            }
        }
        DownloadJSON getJSON = new DownloadJSON();
        getJSON.execute();
    }

    private void getJSONData(String json) throws JSONException {
        String jsonStr = json;
        Log.e(TAG, "Json Actor Data" + jsonStr);
        int _status = 0, roleId = 0;
        if (jsonStr != null) {
            try {

                JSONObject jsonObject = new JSONObject(json);
                JSONObject recsData = jsonObject.getJSONObject("data");
                JSONArray recsArray = recsData.getJSONArray("data");
                JSONObject screeners = recsArray.getJSONObject(0);
                JSONObject doctors = recsArray.getJSONObject(1);
                JSONObject ngos = recsArray.getJSONObject(2);
                JSONObject citizens = recsArray.getJSONObject(3);
                JSONObject pharmacy = recsArray.getJSONObject(4);
                JSONObject screening = recsArray.getJSONObject(5);
                JSONObject sevika = recsArray.getJSONObject(6);
                Log.e(TAG, "status : " + sevika.getString("Sevikas"));

                strScreeners = screeners.getString("Screeners");
                strDoctors = doctors.getString("Doctors");
                strNGO = ngos.getString("NGO");
                strCitizen = citizens.getString("Citizen");
                strPharmacy = pharmacy.getString("Pharmacy");
                strScreening = screening.getString("Screening");
                strSevika = sevika.getString("Sevikas");
                BarChart barChart = findViewById(R.id.barChart);
                ArrayList<BarEntry> barEntryArrayList = new ArrayList<>();
                ArrayList<String> labelNames = new ArrayList<>();
                barEntryArrayList.clear();
                labelNames.clear();
                TextView txBCount = (TextView) findViewById(R.id.txBCount);
                txBCount.setText(strScreening);

                barEntryArrayList.add(new BarEntry(0, Integer.parseInt(strDoctors)));
                labelNames.add("Doctor");
                barEntryArrayList.add(new BarEntry(1, Integer.parseInt(strScreeners)));
                labelNames.add("Screener");
                barEntryArrayList.add(new BarEntry(2, Integer.parseInt(strNGO)));
                labelNames.add("NGO");
                barEntryArrayList.add(new BarEntry(3, Integer.parseInt(strPharmacy)));
                labelNames.add("Pharmacy");
                barEntryArrayList.add(new BarEntry(4, Integer.parseInt(strCitizen)));
                labelNames.add("Citizen");
                barEntryArrayList.add(new BarEntry(5, Integer.parseInt(strSevika)));
                labelNames.add("Sevikas");

                BarDataSet barDataSet = new BarDataSet(barEntryArrayList, "Actors");
                barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                barDataSet.setValueTextColor(Color.BLACK);
                barDataSet.setValueTextSize(16f);

                BarData barData = new BarData(barDataSet);
                barChart.setFitBars(true);
                barChart.setData(barData);
                barChart.getDescription().setText("Actors at javix");

                XAxis xAxis = barChart.getXAxis();
                xAxis.setValueFormatter(new IndexAxisValueFormatter(labelNames));
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                xAxis.setDrawGridLines(false);
                xAxis.setDrawAxisLine(false);
                xAxis.setGranularity(1f);
                xAxis.setLabelCount(labelNames.size());
                xAxis.setLabelRotationAngle(270);

                barChart.animateY(2000);
                barChart.invalidate();


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
        LoadChart();

    }


    public class MyXAxisValueFormatter implements IAxisValueFormatter {
        private String[] mValues;

        public MyXAxisValueFormatter(String[] values) {
            this.mValues = values;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return mValues[(int) value];
        }

    }

    public void LoadChart() {
        Log.e(TAG, "status : " + strDoctors);
        visitorss.add(new PieEntry(Integer.parseInt(strDoctors), "Doctor"));
        visitorss.add(new PieEntry(Integer.parseInt(strScreeners), "Screener"));
        visitorss.add(new PieEntry(Integer.parseInt(strNGO), "NGO"));
        visitorss.add(new PieEntry(Integer.parseInt(strPharmacy), "Pharmacy"));
        visitorss.add(new PieEntry(Integer.parseInt(strCitizen), "Citizen"));
        visitorss.add(new PieEntry(Integer.parseInt(strSevika), "Sevikas"));

        PieDataSet pieDataSet = new PieDataSet(visitorss, "Actor");
        pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        pieDataSet.setValueTextColor(Color.BLACK);
        pieDataSet.setValueTextSize(16f);
        PieData pieData = new PieData(pieDataSet);
        //pieChart.setFitBars(true);
        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(true);
        pieChart.setCenterText("Actor Count");
        pieChart.invalidate();

        //txBCount.getText();

    }


//    @Override
//    public void onBackPressed() {
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(AdminActivity.this);
//        builder.setMessage("Do you want to exit ?");
//        builder.setTitle("Alert !");
//        builder.setCancelable(false);
//
//        builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {
//
//            finish();
//        });
//        builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
//
//            dialog.cancel();
//        });
//
//        AlertDialog alertDialog = builder.create();
//        alertDialog.show();
//
//
//    }

}