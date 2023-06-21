package com.sumago.latestjavix;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

public class CommonReport extends AppCompatActivity {
    RecyclerView encounterRecyclerView ;
    Context context;
    Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_report);
        MyConfig.CONTEXT=getApplicationContext();
        context=this;
        encounterRecyclerView = (RecyclerView) findViewById(R.id.encounterRecyclerView);
        HashMap<String,String> paramHash=new HashMap<String,String>();
        bundle=getIntent().getExtras();
        paramHash.put("citizenId", Config.tmp_citizenId);
        paramHash.put("token","dfjkhsdfaksjfh3756237");
        paramHash.put("ngoId", Config.NGO_ID);

        RequestEncounterList req=new RequestEncounterList(this,paramHash);
        req.execute(MyConfig.URL_ENCOUNTERS);
    }

    class RequestEncounterList extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        Activity activity;
        HashMap<String, String> paramsHash=null;
        RequestPipe requestPipe=new RequestPipe();
        public RequestEncounterList(Activity activity,HashMap<String, String> paramsHash) {
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
            try {
                JSONObject jsonObject = new JSONObject(result);
                int respStatus=jsonObject.getInt("status");
                if(respStatus==1) {
                    //Toast.makeText(MyConfig.CONTEXT, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    ArrayList<EncounterData> recsArrayList=new ArrayList<EncounterData>();
                    JSONObject recsData=jsonObject.getJSONObject("data");
                    JSONArray recsArray=recsData.getJSONArray("data");
                    int recsLen=recsArray.length();
                    for(int i=0;i<recsLen;i++){
                        JSONObject rec = recsArray.getJSONObject(i);
                        EncounterData cdata=new EncounterData();
                        cdata.caseId=rec.getString("caseId");
                        cdata.createdAt=rec.getString("createdAt");

                        if(rec.has("bloodglucosetests")) {
                            if(rec.getString("bloodglucosetests").isEmpty()==false){
                                cdata.layout_glucose="1";
                            }
                        }
                        if(rec.has("labtests")) {
                            if(rec.getString("labtests").isEmpty()==false){
                                cdata.layout_rapidtest="1";
                            }
                        }
                        if(rec.has("drugtests")) {
                            if(rec.getString("drugtests").isEmpty()==false){
                                cdata.layout_drugtests="1";
                            }
                        }
                        if(rec.has("lipidpaneltests")) {
                            if(rec.getString("lipidpaneltests").isEmpty()==false){
                                cdata.layout_lipidpaneltests="1";
                            }
                        }
                        if(rec.has("sicklecells")) {
                            if(rec.getString("sicklecells").isEmpty()==false){
                                cdata.layout_sicklecells="1";
                            }
                        }
                        if(rec.has("thalassemias")) {
                            if(rec.getString("thalassemias").isEmpty()==false){
                                cdata.layout_thalassemias="1";
                            }
                        }
                        if(rec.has("lungfunctions")) {
                            if(rec.getString("lungfunctions").isEmpty()==false){
                                cdata.layout_lungfunctions="1";
                            }
                        }
                        if(rec.has("visualexams")) {
                            if(rec.getString("visualexams").isEmpty()==false){
                                cdata.layout_visualexams="1";
                            }
                        }
                        if(rec.has("eyetests")) {
                            if(rec.getString("eyetests").isEmpty()==false){
                                cdata.layout_eyetests="1";
                            }
                        }
                        if(rec.has("hemoglobins")) {
                            if(rec.getString("hemoglobins").isEmpty()==false){
                                cdata.layout_hemoglobins="1";
                            }
                        }

                        /*cdata.fever=rec.getString("fever");
                        cdata.abdomenpain=rec.getString("abdomenpain");
                        cdata.diarrhea=rec.getString("diarrhea");
                        cdata.backneckpain=rec.getString("backneckpain");
                        cdata.hypertension=rec.getString("hypertension");

                        Log.e("info", "district: " +cdata.hypertension);*/
                        recsArrayList.add(cdata);
                        // Toast.makeText(MyConfig.CONTEXT,ngoData.ngoName,Toast.LENGTH_SHORT).show();
                    }
                    EncounterListAdapter adapter = new EncounterListAdapter(context,recsArrayList);
                    //ngoListRecyclerView.setHasFixedSize(true);
                    encounterRecyclerView.setLayoutManager(new LinearLayoutManager(CommonReport.this));
                    encounterRecyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
                else Toast.makeText(MyConfig.CONTEXT, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

            }catch (Exception ee){}

        }

    }
}

class EncounterData {
    String caseId="";
    String createdAt="";
    String layout_glucose="";
    String layout_rapidtest="";
    String layout_drugtests="";
    String layout_lipidpaneltests="";
    String layout_sicklecells="";
    String layout_thalassemias="";
    String layout_lungfunctions="";
    String layout_visualexams="";
    String layout_eyetests="";
    String layout_hemoglobins="";
    String abdomenpain="";
    String diarrhea="";
    String backneckpain="";
    String hypertension="";

    public EncounterData() {

    }
}
class EncounterViewHolder extends RecyclerView.ViewHolder {
    TextView textCaseid=null;
    TextView txDate=null;
    TextView textFever=null;
    TextView textAbdomen=null;
    TextView textDiarrhea=null;
    TextView textBackneckpain=null;
    TextView textHypertension=null;
    LinearLayout layout_glucose=null;
    LinearLayout layout_rapidtest=null;
    LinearLayout  layout_drugtests=null;
    LinearLayout  layout_lipidpaneltests=null;
    LinearLayout layout_sicklecells=null;
    LinearLayout layout_thalassemias=null;
    LinearLayout layout_lungfunctions=null;
    LinearLayout layout_visualexams=null;
    LinearLayout layout_eyetests=null;
    LinearLayout layout_hemoglobins=null;
    AppCompatButton btnView=null;
    AppCompatButton btnDownloads=null;


    public EncounterViewHolder(View itemView){
        super(itemView);
        textCaseid=(TextView)itemView.findViewById(R.id.txCaseid);
        txDate=(TextView)itemView.findViewById(R.id.txDate);
        layout_glucose=(LinearLayout)itemView.findViewById(R.id.layout_glucose);
        layout_drugtests=(LinearLayout)itemView.findViewById(R.id.layout_drugtests);
        layout_rapidtest=(LinearLayout)itemView.findViewById(R.id.layout_rapidtest);
        layout_lipidpaneltests=(LinearLayout)itemView.findViewById(R.id.layout_lipidpaneltests);
        layout_sicklecells=(LinearLayout)itemView.findViewById(R.id.layout_sicklecells);
        layout_thalassemias=(LinearLayout)itemView.findViewById(R.id.layout_thalassemias);
        layout_lungfunctions=(LinearLayout)itemView.findViewById(R.id.layout_lungfunctions);
        layout_visualexams=(LinearLayout)itemView.findViewById(R.id.layout_visualexams);
        layout_eyetests=(LinearLayout)itemView.findViewById(R.id.layout_eyetests);
        layout_hemoglobins=(LinearLayout)itemView.findViewById(R.id.layout_hemoglobins);
        btnView=(AppCompatButton)itemView.findViewById(R.id.btnView);
        btnDownloads=(AppCompatButton)itemView.findViewById(R.id.btnDownloads);

    }
}

class EncounterListAdapter extends RecyclerView.Adapter<EncounterViewHolder> {
    ArrayList<EncounterData> recs=null;
    Context context;
    public EncounterListAdapter(Context context,ArrayList<EncounterData> recs) {
        this.recs = recs;
        this.context = context;
    }
    @Override
    public EncounterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.encounter_row, parent, false);
        EncounterViewHolder viewHolder = new EncounterViewHolder(listItem);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(EncounterViewHolder holder, int position) {
        final EncounterData rec = recs.get(position);
        holder.textCaseid.setText(holder.textCaseid.getText() + rec.caseId);
        holder.txDate.setText(holder.txDate.getText() + rec.createdAt);
        String varParent="",varChild="";
        /*holder.textFever.setText(holder.textFever.getText() + rec.fever);
        holder.textAbdomen.setText(holder.textAbdomen.getText() + rec.abdomenpain);
        holder.textDiarrhea.setText(holder.textDiarrhea.getText() + rec.diarrhea);
        holder.textBackneckpain.setText(holder.textBackneckpain.getText() + rec.backneckpain);
        holder.textHypertension.setText(holder.textHypertension.getText() + rec.hypertension);*/

        if(rec.layout_glucose.equalsIgnoreCase("1")){
           holder.layout_glucose.setVisibility(View.VISIBLE);
        }
        if(rec.layout_rapidtest.equalsIgnoreCase("1")){
            holder.layout_rapidtest.setVisibility(View.VISIBLE);
        }
        if(rec.layout_drugtests.equalsIgnoreCase("1")){
            holder.layout_drugtests.setVisibility(View.VISIBLE);
        }
        if(rec.layout_lipidpaneltests.equalsIgnoreCase("1")){
            holder.layout_lipidpaneltests.setVisibility(View.VISIBLE);
        }
        if(rec.layout_sicklecells.equalsIgnoreCase("1")){
            holder.layout_sicklecells.setVisibility(View.VISIBLE);
        }

        if(rec.layout_thalassemias.equalsIgnoreCase("1")){
            holder.layout_thalassemias.setVisibility(View.VISIBLE);
        }
        if(rec.layout_lungfunctions.equalsIgnoreCase("1")){
            holder.layout_lungfunctions.setVisibility(View.VISIBLE);
        }
        if(rec.layout_visualexams.equalsIgnoreCase("1")){
            holder.layout_visualexams.setVisibility(View.VISIBLE);
        }
        if(rec.layout_eyetests.equalsIgnoreCase("1")){
            holder.layout_eyetests.setVisibility(View.VISIBLE);
        }
        if(rec.layout_hemoglobins.equalsIgnoreCase("1")){
            holder.layout_hemoglobins.setVisibility(View.VISIBLE);
        }

        holder.btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                RequestBody body = RequestBody.create(mediaType, "caseId=" + Config.tmp_caseId);
                String url="http://143.244.136.145:3010/api/report/createPrescriptionReport";
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
                        System.out.println("JCANCEL "+e.toString());
                        call.cancel();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        final String myResponse = response.body().string();
                        try {
                            JSONObject jsonObject = new JSONObject(myResponse);
                            if(jsonObject.getInt("status")==1){
                                JSONObject data=jsonObject.getJSONObject("data");
                                data=data.getJSONObject("data");
                                String fileName=data.getString("filename");
                                Intent intx=new Intent(Intent.ACTION_VIEW);
                                intx.setData(Uri.parse(fileName));
                                intx.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                MyConfig.CONTEXT.startActivity(intx);
                            }
                        }catch (Exception ee) { }
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
        holder.btnDownloads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                RequestBody body = RequestBody.create(mediaType, "caseId=" + Config.tmp_caseId);
                String url="http://143.244.136.145:3010/api/report/createCaseReport";
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
                        System.out.println("JCANCEL "+e.toString());
                        call.cancel();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        final String myResponse = response.body().string();
                        try {
                            JSONObject jsonObject = new JSONObject(myResponse);
                            if(jsonObject.getInt("status")==1){
                                JSONObject data=jsonObject.getJSONObject("data");
                                data=data.getJSONObject("data");
                                String fileName=data.getString("filename");
                                Intent intx=new Intent(Intent.ACTION_VIEW);
                                intx.setData(Uri.parse(fileName));
                                intx.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                MyConfig.CONTEXT.startActivity(intx);
                            }
                        }catch (Exception ee) { }
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
        /*String layout_glucose="";
        String layout_rapidtest="";
        String layout_drugtests="";
        String layout_lipidpaneltests="";
        String layout_sicklecells="";
        String layout_thalassemias="";
        String layout_lungfunctions="";
        String layout_visualexams="";
        String layout_eyetests="";
        String layout_hemoglobins="";*/

    }

    @Override
    public int getItemCount() {
        return recs.size();
    }
}