package com.sumago.latestjavix;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sumago.latestjavix.Util.Config;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

public class OfflineDataSync extends AppCompatActivity {
    SQLiteDatabase db;
    public WebView myweb;
    TextView txSync;
    Button btnSync,btnDownload;
    ProgressBar simpleProgressBar;
    int dataCount=0;
    Context context;
    private static final String TAG = "OfflineDataSync";
    private volatile boolean stopThread = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_data_sync);
        MyConfig.CONTEXT=this;
        context=this;
        simpleProgressBar=(ProgressBar) findViewById(R.id.simpleProgressBar);
        btnSync=(Button)findViewById(R.id.btnSync);
        btnDownload=(Button)findViewById(R.id.btnDownload);
        txSync=(TextView)findViewById(R.id.txSync);
        txSync.setVisibility(View.GONE);

        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isInternetOn(context)) {
                        download_data(true);
                        txSync.setVisibility(View.VISIBLE);
                        //txSync.setText("Downloading in progress please wait...");
                }
                else {
                    android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(OfflineDataSync.this
                    );
                    alertDialog.setMessage("No Internet Connection !");
                    alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            return;
                        }
                    });
                    alertDialog.create();
                    alertDialog.show();
                }
            }
        });

        btnSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txSync.setVisibility(View.VISIBLE);
               if(dataCount==0){
                    //txSync.setText("Syncing is Done Successfully !");
                }else{

                    if(isInternetOn(context)) {
                        for(int i=0;i<dataCount;i++){
                            load_data(true);
                            txSync.setText("Syncying in progress please wait...");                            //txSync.setText(Integer.toString(i));
                        }
                    }
                    else {
                        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(OfflineDataSync.this
                        );
                        alertDialog.setMessage("No Internet Connection !");
                        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                return;
                            }
                        });
                        alertDialog.create();
                        alertDialog.show();
                    }


                }
            }
        });
        load_data(false);
    }
    /****************Download Section***********************/
    public  void download_data(boolean isSync){
        HashMap<String,String> paramHash=new HashMap<String,String>();
        paramHash.put("token","dfjkhsdfaksjfh3756237");
        paramHash.put("ngoId", Config.NGO_ID);

        DownloadCitizenList req=new DownloadCitizenList(this,paramHash);
        req.execute(MyConfig.URL_LIST_CITIZEN);
    }
    class DownloadCitizenList extends AsyncTask<String, Void, String>
    {
        ProgressDialog progressDialog;
        Activity activity;
        HashMap<String, String> paramsHash=null;
        RequestPipe requestPipe=new RequestPipe();
        public DownloadCitizenList(Activity activity,HashMap<String, String> paramsHash) {
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
            ArrayList<CitizenData> recsArrayList=new ArrayList<CitizenData>();
            //db = openOrCreateDatabase("javixlife", Context.MODE_PRIVATE, null);
            String pattern = "yyyy-MM-dd";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            String currentDate = simpleDateFormat.format(new Date());
            try {

                JSONObject jsonObject = new JSONObject(result);
                int respStatus=jsonObject.getInt("status");
                if(respStatus==1) {
                    //Toast.makeText(MyConfig.CONTEXT, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    JSONObject recsData=jsonObject.getJSONObject("data");
                    JSONArray recsArray=recsData.getJSONArray("data");
                    int recsLen=recsArray.length();
                    int j=0;
                    startThread(recsArray,recsLen);

                    /*db = openOrCreateDatabase("javixlife", Context.MODE_PRIVATE, null);
                    db.execSQL("drop table javix_citizenlist");
                    //db.delete("javix_citizenlist", null, null);
                    db.execSQL("VACUUM");
                    db.execSQL("CREATE TABLE IF NOT EXISTS javix_citizenlist(_id INTEGER PRIMARY KEY AUTOINCREMENT,citizen_data TEXT,insert_date DATETIME,_status INTEGER);");
                    String strSQL="";
                    for(int i=0;i<recsLen;i++){
                        try {
                            JSONObject rec = recsArray.getJSONObject(i);
                            //System.out.println(rec.toString());
                            if(rec!=null) {
                                strSQL = "INSERT INTO javix_citizenlist(citizen_data,insert_date,_status)";
                                strSQL += " VALUES('" + rec.toString() + "','" + currentDate + "',0);";
                                db.execSQL(strSQL);
                            }

                        }catch (SQLiteException eei){
                            android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(OfflineDataSync.this
                            );
                            alertDialog.setMessage("Error in downloaing data try again ! ");
                            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                }
                            });
                            alertDialog.create();
                            alertDialog.show();
                        }
                        // Toast.makeText(MyConfig.CONTEXT,ngoData.ngoName,Toast.LENGTH_SHORT).show();
                    }// loop
                    db = openOrCreateDatabase("javixlife", Context.MODE_PRIVATE, null);
                    Cursor c = db.rawQuery("SELECT * FROM javix_citizenlist", null);

                    dataCount = c.getCount();
                    c.moveToNext();

                    android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(OfflineDataSync.this
                    );
                    alertDialog.setMessage("Data Downloaded Successfully ! \n Total Data Downloaed :- " + c.getString(0));
                    alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                        }
                    });
                    alertDialog.create();
                    alertDialog.show();*/
                    //Toast.makeText(getApplicationContext(),"Total Data Downloaded :- " + Integer.toString(dataCount),Toast.LENGTH_LONG).show();
                }
                else Toast.makeText(MyConfig.CONTEXT, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                txSync.setText("Downloading completed...");
                db.close();
            }catch (Exception ee){
                if (ee.getMessage().contains("no such table")){
                    android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(OfflineDataSync.this
                    );
                    alertDialog.setMessage("Error in downloading data, retry again !" );
                    alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            db.execSQL("CREATE TABLE IF NOT EXISTS javix_citizenlist(_id INTEGER PRIMARY KEY AUTOINCREMENT,citizen_data TEXT,insert_date DATETIME,_status INTEGER);");
                        }
                    });
                    alertDialog.create();
                    alertDialog.show();

                    // create table
                    // re-run query, etc.
                }
            }

        }
    }

    /****************End of Download Section***********************/
    /****************Upload Section***********************/
    public  void load_data(boolean isSync){
        myweb =(WebView) findViewById(R.id.mywebview_m);
        myweb.getSettings().setJavaScriptEnabled(true);
        Bundle bundle=getIntent().getExtras();
        String myTable = "<table border='1' style='width:100%'><tr><td colspan='4' style='text-align:center;padding:6px;'><span style='font-weight:bold;color:red;'>Offline Data List</span></td></tr>" +
                "<tr><th>Sl No</th><th>Service</th><th>Data</th><th>Date</th></tr>" ;
        try {
            db = openOrCreateDatabase("javixlife", Context.MODE_PRIVATE, null);
            Cursor c = db.rawQuery("SELECT * FROM javix_update where _status=0 order by insert_date desc", null);
            dataCount = c.getCount();
            if (c.getCount() == 0) {
                Toast.makeText(this, "No records found", Toast.LENGTH_SHORT).show();
            }
            StringBuffer buffer = new StringBuffer();
            int i = 1;
            while (c.moveToNext()) {
                myTable += "<tr><td>" + Integer.toString(i) + "</td><td>" + c.getString(1) + "</td><td>" + c.getString(3) + "</td><td>" + c.getString(6) + "</td></tr>";
                if (isSync) {
                    insertData(c.getString(3), c.getString(2));
                    String strSQL = "UPDATE javix_update SET _status = 1 WHERE _id = " + c.getString(0);
                    db.execSQL(strSQL);
                }
                i++;
            }
            db.close();
            txSync.setText("Syncing is Done Successfully !");
        }catch (Exception ex){
            Toast.makeText(context,"Error Duplicate Records !" ,Toast.LENGTH_LONG).show();
        }
        myweb.loadDataWithBaseURL(null, myTable, "text/html", "utf-8", null);
    }


    public void insertData(String Data,String Url){
        Data=Data.replace("{","");
        Data=Data.replace("}","");
        Data=Data.replace(", ","&");
        //String Data1="lastName=Sinha&country=INDIA&pincode=368555&address=&sex=Male&photo=&dateOfBirth=02-09-2021&dateOfOnBoarding=2021-09-29&token=T89878788&firstName=Rakesh&bloodGroup=Blood+Group&screenerId=162480116265360010&district=Dadra+%26+Nagar+Haveli&aadhaar=556856999966&state=Dadra+and+Nagar+Haveli+";
        Log.e("Offline1",Data);
        SubmitForm req=new SubmitForm(this,Data);
       req.execute(Url);
    }
    //PUSH DATA
    class SubmitForm extends AsyncTask<String, Void, String>
    {
        ProgressDialog progressDialog;
        Activity activity;
        String paramsHash=null;
        RequestPipeOffline requestPipe=new RequestPipeOffline();
        public SubmitForm(Activity activity,String paramsHash) {
            this.activity=activity;
            this.paramsHash=paramsHash;
            progressDialog = new ProgressDialog(activity);
            progressDialog.setMessage("loading");
            txSync.setText("Syncing is in progress... !");
        }
        @Override
        protected void onPreExecute()
        {
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
                System.out.println("RESULT UPLOAD-------");
                System.out.println(result.toString());
                JSONObject jsonObject = new JSONObject(result);
                int respStatus=jsonObject.getInt("status");
                if(respStatus==1) {
                    android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(OfflineDataSync.this
                    );
                    alertDialog.setMessage("Offline Data Sync Successfully !");
                    alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            txSync.setText("Syncing done !");
                        }

                    });
                    alertDialog.create();
                    alertDialog.show();

                    //Toast.makeText(MyConfig.CONTEXT, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                }
                else Toast.makeText(MyConfig.CONTEXT, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

            }catch (Exception ee){
                Toast.makeText(MyConfig.CONTEXT, "Opps !." + ee.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        }

    }

    /********************End of Upload Section****************************/


    public boolean isInternetOn(Context context) {

        // get Connectivity Manager object to check connection
        ConnectivityManager connec =
                (ConnectivityManager)context.getSystemService(context.CONNECTIVITY_SERVICE);

        // Check for network connections
        if ( connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED ) {

            return true;

        } else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED  ) {

            return false;
        }
        return false;
    }

    /*******************Multithreading***************************/
    public void startThread(JSONArray recsArray,int recsLen) {
        stopThread = false;
        db = openOrCreateDatabase("javixlife", Context.MODE_PRIVATE, null);
        db.execSQL("drop table javix_citizenlist");
        db.execSQL("VACUUM");
        db.execSQL("CREATE TABLE IF NOT EXISTS javix_citizenlist(_id INTEGER PRIMARY KEY AUTOINCREMENT,screenerid VARCHAR,citizenid VARCHAR,name VARCHAR,sex VARCHAR,pstatus INTEGER,mobile VARCHAR,email VARCHAR,caseId VARCHAR,insert_date DATETIME,_status INTEGER);");
        CitizenRunnable runnable = new CitizenRunnable(1,recsArray,recsLen);
        new Thread(runnable).start();
    }

    public void stopThread() {
        stopThread = true;
        txSync.setText("Downlading Completed !");
    }
    class CitizenRunnable implements Runnable {
        int seconds;
        int i;
        JSONArray recsArray;int recsLen;
        TextView txtData=(TextView)findViewById(R.id.txSync);
        CitizenRunnable(int seconds,JSONArray recsArray,int recsLen) {
            this.seconds = seconds;
            this.recsArray=recsArray;
            this.recsLen=recsLen;
        }
        @Override
        public void run() {
            db = openOrCreateDatabase("javixlife", Context.MODE_PRIVATE, null);
            for (i = 0; i < recsLen; i++) {
                if (stopThread)
                    return;
                if (i > recsLen) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //buttonStartThread.setText("50%");
                            /*db = openOrCreateDatabase("javixlife", Context.MODE_PRIVATE, null);
                            Cursor c = db.rawQuery("SELECT * FROM javix_citizenlist", null);
                            dataCount = c.getCount();
                            c.moveToNext();

                            android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(OfflineDataSync.this
                            );
                            alertDialog.setMessage("Data Downloaded Successfully ! \n Total Data Downloaed :- " + c.getString(0));
                            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                }
                            });
                            alertDialog.create();
                            alertDialog.show();*/
                            //txSync.setText("Downlading Completed !");
                            stopThread();
                        }
                    });
                }else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String pattern = "yyyy-MM-dd";
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                            String currentDate = simpleDateFormat.format(new Date());
                            //txSync.setText("Downloading Please Wait.....");
                            txSync.setText("Records Downloading " + (Integer.toString(i+1)) + " Of " + Integer.toString(recsLen));
                            String strSQL="";
                                try {
                                    //currentDate
                                    String caseId="";
                                    JSONObject rec = recsArray.getJSONObject(i);
                                    if(rec!=null) {

                                        if (rec.has("cases")) {
                                            Log.e(TAG, "CaseID: ");
                                            try {
                                                JSONObject cases = rec.getJSONObject("cases");
                                                Log.e("Cases", "CaseID: " + cases);
                                                caseId = cases.getString("caseId");
                                                System.out.println("Case Id7000001=" + caseId);
                                            } catch (Exception ee) {
                                                System.out.println("Error in Case9000" + ee.getLocalizedMessage());
                                                try {
                                                    JSONArray cases = rec.getJSONArray("cases");
                                                    if (cases != null && cases.length() >= 1) {
                                                        JSONObject casesI = cases.getJSONObject(0);
                                                        Log.e("Cases", "CaseID: " + cases);
                                                        caseId = casesI.getString("caseId");

                                                    }
                                                } catch (Exception ine) {
                                                    caseId = "0";
                                                    System.out.println("Cases Error :" + ine.getLocalizedMessage());
                                                }
                                            }

                                        } else {

                                            caseId = "0";
                                        }
                                        strSQL = "INSERT INTO javix_citizenlist(screenerid,citizenid,name,sex,pstatus,mobile,email,caseId ,insert_date,_status)";
                                        strSQL += " VALUES('" + rec.getString("screenerId") + "','" +rec.getString("citizenId")+ "','" +  rec.getString("firstName") + " " + rec.getString("lastName") + "',";
                                        strSQL += "'" +rec.getString("sex")+ "'," + Integer.parseInt(rec.getString("pstatus")) + ",'" +rec.getString("mobile")+ "',";
                                        strSQL += "'" +(rec.has("email") ? rec.getString("email") : "")+ "','" +caseId+ "','" +currentDate+ "',0)";
                                        Log.d(TAG, "startThread: " +  strSQL);
                                        db.execSQL(strSQL);
                                    }
                                }catch (Exception eei){
                                    android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(OfflineDataSync.this
                                    );
                                    alertDialog.setMessage("Error in downloaing data try again ! ");
                                    alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int whichButton) {
                                            db.execSQL("CREATE TABLE IF NOT EXISTS javix_citizenlist(_id INTEGER PRIMARY KEY AUTOINCREMENT,screenerid VARCHAR,citizenid VARCHAR,name VARCHAR,sex VARCHAR,pstatus INTEGER,mobile VARCHAR,email VARCHAR,caseId VARCHAR,insert_date DATETIME,_status INTEGER);");
                                        }
                                    });
                                    alertDialog.create();
                                    alertDialog.show();
                                }
                                // Toast.makeText(MyConfig.CONTEXT,ngoData.ngoName,Toast.LENGTH_SHORT).show();



                        }
                    });
                }
                //Toast.makeText(context,"fdfs",Toast.LENGTH_LONG).show();
                //txtData.setText(Integer.toString(i));
                //txSync.setText("");
                //Log.d(TAG, "startThread: " + recsLen);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}