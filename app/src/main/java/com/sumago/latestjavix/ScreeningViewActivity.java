package com.sumago.latestjavix;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sumago.latestjavix.Util.Config;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ScreeningViewActivity extends AppCompatActivity {
    RecyclerView ScreeningViewRecyclerView;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screening_view);
        //setContentView(R.layout.activity_screening_list);
        MyConfig.CONTEXT = getApplicationContext();
        context = this;
        ScreeningViewRecyclerView = (RecyclerView) findViewById(R.id.ScreeningViewRecyclerView);
        Bundle bundle = getIntent().getExtras();
        HashMap<String, String> paramHash = new HashMap<String, String>();
//        paramHash.put("citizenId", bundle.getString("CitizenId"));
        paramHash.put("citizenId", bundle.getString("CitizenId"));
        paramHash.put("ngoId", Config.NGO_ID);
        //paramHash.put("status","1");

        RequestScreeningView req = new RequestScreeningView(this, paramHash);
        req.execute(MyConfig.URL_LIST_CASE);
    }

    class RequestScreeningView extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        Activity activity;
        HashMap<String, String> paramsHash = null;
        RequestPipe requestPipe = new RequestPipe();

        public RequestScreeningView(Activity activity, HashMap<String, String> paramsHash) {
            this.activity = activity;
            this.paramsHash = paramsHash;
            progressDialog = new ProgressDialog(activity);
            progressDialog.setMessage("loading");
        }

        @Override
        protected void onPreExecute() {
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            return requestPipe.requestForm(params[0], paramsHash);
        }

        protected void onProgressUpdate(Void... progress) {
            super.onProgressUpdate(progress);
            //ProgressDialog.show(MainActivity.this, "loading", "wait...", true, true);

        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.cancel();
            //Toast.makeText(MyConfig.CONTEXT,"RES:"+result,Toast.LENGTH_SHORT).show();
            try {
                JSONObject jsonObject = new JSONObject(result);
                int respStatus = jsonObject.getInt("status");
                if (respStatus == 1) {
                    Toast.makeText(MyConfig.CONTEXT, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    ArrayList<ScreeningViewData> recsArrayList = new ArrayList<ScreeningViewData>();
                    JSONObject recsData = jsonObject.getJSONObject("data");
                    JSONArray recsArray = recsData.getJSONArray("data");
                    int recsLen = recsArray.length();

                    for (int i = 0; i < recsLen; i++) {
                        JSONObject rec = recsArray.getJSONObject(i);
                        ScreeningViewData cdata = new ScreeningViewData();
                        cdata.citizenid = rec.getString("citizenId");
                        cdata.caseid = rec.getString("caseId");
                        if (rec.has("height")) {
                            cdata.height = rec.getString("height");
                        }
                        if (rec.has("weight")) {
                            cdata.weight = rec.getString("weight");
                        }
                        if (rec.has("bmi")) {
                            cdata.bmi = rec.getString("bmi");
                        }
                        if (rec.has("bpsys")) {
                            cdata.bpsys = rec.getString("bpsys");
                        }
                        if (rec.has("bpdia")) {
                            cdata.bpdia = rec.getString("bpdia");
                        }
                        if (rec.has("spo2")) {
                            cdata.spo2 = rec.getString("spo2");
                        }
                        if (rec.has("pulse")) {
                            cdata.pulse = rec.getString("pulse");
                        }
                        if (rec.has("respiratory_rate")) {
                            cdata.respiratory_rate = rec.getString("respiratory_rate");
                        }
                        if (rec.has("temperature")) {
                            cdata.temperature = rec.getString("temperature");
                        }
                        if (rec.has("notes")) {
                            cdata.strNotes = rec.getString("notes");
                        }
                        if (rec.has("arm")) {
                            cdata.strArm = rec.getString("arm");
                        }
                        cdata.strDate = rec.getString("createdAt");
                        if (rec.has("severity_bp")) {
                            cdata.cBP = rec.getInt("severity_bp");
                        }
                        if (rec.has("severity_spo2")) {
                            cdata.cSPO2 = rec.getInt("severity_spo2");
                        }
                        if (rec.has("severity_temperature")) {
                            cdata.cTMP = rec.getInt("severity_temperature");
                        }
                        if (rec.has("severity_puls")) {
                            cdata.cPULS = rec.getInt("severity_puls");
                        }
                        if (rec.has("severity_bmi")) {
                            cdata.cBMI = rec.getInt("severity_bmi");
                        }
                        if (rec.has("severity_respiratory_rate")) {
                            cdata.cRESP = rec.getInt("severity_respiratory_rate");
                        }
                        if (rec.has("severity")) {
                            cdata.cOVERALL = rec.getInt("severity");
                        }
                        //
                        //
                        recsArrayList.add(cdata);
                    }

                    ScreeningViewAdapter adapter = new ScreeningViewAdapter(context, recsArrayList);
                    //ngoListRecyclerView.setHasFixedSize(true);
                    ScreeningViewRecyclerView.setLayoutManager(new LinearLayoutManager(ScreeningViewActivity.this));
                    ScreeningViewRecyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                } else
                    Toast.makeText(MyConfig.CONTEXT, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

            } catch (Exception ee) {
            }

        }

    }

    public static class DownloadReport {
        static Context context = null;

        public DownloadReport() {

        }

        public static void getDownload() {
            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
            RequestBody body = RequestBody.create(mediaType, "caseId=161387331407139599");
            String url = "http://143.244.136.145:3010/api/report/createCaseReport";
//            String url="http://192.168.1.195:3010/api/report/createCaseReport";

            //ScreeningViewActivity.context=this;

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .addHeader("content-type", "application/x-www-form-urlencoded")
                    .addHeader("cache-control", "no-cache")
                    .addHeader("postman-token", "08b43f25-b3a6-b582-35c8-25cfdad00694")
                    .build();
            System.out.println("OKAY CLIENT");
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    System.out.println("JCANCEL " + e.toString());
                    call.cancel();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String myResponse = response.body().string();
                    Log.e("MyResponse", myResponse);
                    try {
                        JSONObject jsonObject = new JSONObject(myResponse);
                        if (jsonObject.getInt("status") == 1) {
                            JSONObject data = jsonObject.getJSONObject("data");
                            data = data.getJSONObject("data");
                            String fileName = data.getString("filename");
                            Intent intx = new Intent(Intent.ACTION_VIEW);
                            intx.setData(Uri.parse(fileName));
                            intx.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                            //ScreeningViewActivity.this.startActivity(intx);
                            //ScreeningViewActivity.context.startActivity(intx);
                        }
                    } catch (Exception ee) {
                    }
                }
            });
        }
    }
}

class ScreeningViewData {
    String citizenid = "";
    String caseid = "";
    String height = "";
    String weight = "";
    String bmi = "";
    String bpsys = "";
    String bpdia = "";
    String spo2 = "";
    String pulse = "";
    String respiratory_rate = "";
    String temperature = "";
    String strArm = "";
    String strNotes = "";
    String strDate = "";
    int cBP;
    int cSPO2;
    int cTMP;
    int cPULS;
    int cBMI;
    int cRESP;
    int cOVERALL;

    public ScreeningViewData() {

    }
}

class ScreeningViewHolder extends RecyclerView.ViewHolder {
    TextView txCaseid = null;
    TextView txHeight = null;
    TextView txWeight = null;
    TextView txBmi = null;
    TextView txBpsys = null;
    TextView txBpdia = null;
    TextView txSpo2 = null;
    TextView txPulse = null;
    TextView txRes = null;
    TextView txTemp = null;
    TextView txArm = null;
    TextView txNotes = null;
    TextView txDate = null;
    ImageView btnRef = null;
    ImageView btnDetailed = null;
    ImageView btnView = null;
    ImageView btnDownloads = null;

    public ScreeningViewHolder(View itemView) {
        super(itemView);
        txCaseid = (TextView) itemView.findViewById(R.id.txCaseid);
        txHeight = (TextView) itemView.findViewById(R.id.txHeight);
        txWeight = (TextView) itemView.findViewById(R.id.txWeight);
        txBmi = (TextView) itemView.findViewById(R.id.txBmi);
        txBpsys = (TextView) itemView.findViewById(R.id.txBpsys);
        txBpdia = (TextView) itemView.findViewById(R.id.txBpdia);
        txSpo2 = (TextView) itemView.findViewById(R.id.txSpo2);
        txPulse = (TextView) itemView.findViewById(R.id.txPulse);
        txRes = (TextView) itemView.findViewById(R.id.txRes);
        txTemp = (TextView) itemView.findViewById(R.id.txTemp);
        txArm = (TextView) itemView.findViewById(R.id.txArm);
        txNotes = (TextView) itemView.findViewById(R.id.txNotes);
        txDate = (TextView) itemView.findViewById(R.id.txDate);
        btnRef = (ImageView) itemView.findViewById(R.id.btnRefer);
        btnDetailed = (ImageView) itemView.findViewById(R.id.btnDetailed);
        btnView = (ImageView) itemView.findViewById(R.id.btnView);
        btnDownloads = (ImageView) itemView.findViewById(R.id.btnDownloads);
        if (Config._roleid == 2) {
            btnDetailed.setVisibility(View.VISIBLE);
            btnView.setVisibility(View.VISIBLE);
        } else {
            btnDetailed.setVisibility(View.GONE);
            btnView.setVisibility(View.GONE);
        }
    }
}

class ScreeningViewAdapter extends RecyclerView.Adapter<ScreeningViewHolder> {
    ArrayList<ScreeningViewData> recs = null;
    private static final String TAG = "_msg";
    Context context;
    int _sys = 0, _dia = 0, _spo2 = 0, _pulse = 0, _resp = 0;
    float _bmi = (float) 0.0;
    float _temp = (float) 0.0;
    String[] COLORCODE = {"#4C8A06", "#FFEF7407", "#E40C0C"};

    public ScreeningViewAdapter(Context context, ArrayList<ScreeningViewData> recs) {
        this.recs = recs;
        this.context = context;
    }

    @Override
    public ScreeningViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.screeningview_row, parent, false);
        ScreeningViewHolder viewHolder = new ScreeningViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ScreeningViewHolder holder, int position) {
        final ScreeningViewData rec = recs.get(position);
        holder.txCaseid.setText(rec.caseid);
        holder.txHeight.setText(rec.height);
        holder.txWeight.setText(rec.weight);
        holder.txBmi.setText(rec.bmi);
        holder.txBpsys.setText(rec.bpsys);
        holder.txBpdia.setText(rec.bpdia);
        holder.txSpo2.setText(rec.spo2);
        holder.txPulse.setText(rec.pulse);
        //  holder.txRes.setText(rec.respiratory_rate);
        holder.txTemp.setText(rec.temperature);
        holder.txDate.setText(rec.strDate);
        if (rec.strArm.length() > 0) {
            if (!rec.strArm.equalsIgnoreCase("null"))
                holder.txArm.setText(rec.strArm);
        } else {
            holder.txArm.setVisibility(View.GONE);
        }
        if (rec.strNotes.length() > 0) {
            holder.txNotes.setText(rec.strNotes);
        } else {
            holder.txNotes.setVisibility(View.GONE);
        }
        Log.e(TAG, "BMI : " + holder.txBmi.getText());
        if (rec.bmi.length() > 0) {
            _bmi = Float.parseFloat(rec.bmi.toString());
        }
        if (rec.bpsys.length() > 0) {
            _sys = Integer.parseInt(rec.bpsys.toString());
        }
        if (rec.bpdia.length() > 0) {
            _dia = Integer.parseInt(rec.bpdia.toString());
        }
        if (rec.spo2.length() > 0) {
            _spo2 = Integer.parseInt(rec.spo2.toString());
        }
        if (rec.temperature.length() > 0) {
            _temp = Float.parseFloat(rec.temperature.toString());
        }
        if (rec.pulse.length() > 0) {
            _pulse = Integer.parseInt(rec.pulse.toString());
        }
        if (rec.respiratory_rate.length() > 0) {
            _resp = Integer.parseInt(rec.respiratory_rate.toString());
        }
        // COlor Code Display from Server
        holder.txBpsys.setTextColor(Color.parseColor(COLORCODE[rec.cBP]));
        holder.txBpdia.setTextColor(Color.parseColor(COLORCODE[rec.cBP]));
        holder.txPulse.setTextColor(Color.parseColor(COLORCODE[rec.cPULS]));
        holder.txSpo2.setTextColor(Color.parseColor(COLORCODE[rec.cSPO2]));
        // holder.txRes.setTextColor(Color.parseColor(COLORCODE[rec.cRESP]));
        holder.txBmi.setTextColor(Color.parseColor(COLORCODE[rec.cBMI]));
        holder.txTemp.setTextColor(Color.parseColor(COLORCODE[rec.cTMP]));
        //  holder.txRes.setVisibility(View.GONE);
        Log.e(TAG, "parameters : " + holder.txSpo2.getText());
        holder.btnRef.setVisibility(View.GONE);

        holder.btnRef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, ScreenerDoctorList.class);
                i.putExtra("CaseId", rec.caseid);
                context.startActivity(i);

                //Toast.makeText(MyConfig.CONTEXT, holder.textViewNgoId.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });
        holder.btnDetailed.setVisibility(View.GONE);
        holder.btnDetailed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, QuestActivity.class);
                i.putExtra("CaseId", rec.caseid);
                context.startActivity(i);

                //Toast.makeText(MyConfig.CONTEXT, holder.textViewNgoId.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });
        holder.btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent i = new Intent(context, ViewDetailedScreening.class);
                i.putExtra("CaseId",rec.caseid);
                context.startActivity(i);*/
                Intent i = new Intent(context, TestListActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Config.tmp_citizenId = rec.citizenid;
                Config.tmp_caseId = rec.caseid;
                context.startActivity(i);

                //Toast.makeText(MyConfig.CONTEXT, holder.textViewNgoId.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        holder.btnDownloads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                RequestBody body = RequestBody.create(mediaType, "caseId=" + rec.caseid +"&ngoId="+Config.NGO_ID);
                String url = "http://143.244.136.145:3010/api/report/createCaseReport";
//                String url = "http://192.168.1.195:3010/api/report/createCaseReport";
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .addHeader("content-type", "application/x-www-form-urlencoded")
                        .addHeader("cache-control", "no-cache")
                        .addHeader("postman-token", "08b43f25-b3a6-b582-35c8-25cfdad00694")
                        .build();
                System.out.println("OKAY CLIENT");
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        System.out.println("JCANCEL " + e.toString());
                        call.cancel();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        final String myResponse = response.body().string();
                        try {
                            JSONObject jsonObject = new JSONObject(myResponse);
                            if (jsonObject.getInt("status") == 1) {
                                JSONObject data = jsonObject.getJSONObject("data");
                                data = data.getJSONObject("data");
                                String fileName = data.getString("filename");
                                Intent intx = new Intent(Intent.ACTION_VIEW);
                                intx.setData(Uri.parse(fileName));
                                intx.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                MyConfig.CONTEXT.startActivity(intx);
                            }
                        } catch (Exception ee) {
                        }
                    }
                });

                //ScreeningViewActivity.DownloadReport.getDownload();
                //ScreeningViewActivity.
               /* Intent i = new Intent(context, ViewDetailedScreening.class);
                i.putExtra("CaseId",rec.caseid);
                context.startActivity(i);*/
                //Toast.makeText(MyConfig.CONTEXT, holder.textViewNgoId.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return recs.size();
    }
}