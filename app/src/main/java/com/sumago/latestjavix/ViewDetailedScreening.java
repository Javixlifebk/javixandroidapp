package com.sumago.latestjavix;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.sumago.latestjavix.Util.Config;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class ViewDetailedScreening extends AppCompatActivity {
    RecyclerView detailedScreeningFinalListRecyclerView ;
    Context context;
    Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_detailed_screening);

        MyConfig.CONTEXT=getApplicationContext();
        context=this;
        detailedScreeningFinalListRecyclerView = (RecyclerView) findViewById(R.id.detailedScreeningFinalListRecyclerView);

        HashMap<String,String> paramHash=new HashMap<String,String>();
        bundle=getIntent().getExtras();
        paramHash.put("caseId", bundle.getString("CaseId"));
        paramHash.put("token","dfjkhsdfaksjfh3756237");
        paramHash.put("ngoId", Config.NGO_ID);

        RequestCitizenList req=new RequestCitizenList(this,paramHash);
        req.execute(MyConfig.URL_GETDETAILSCREENING);
    }

    class RequestCitizenList extends AsyncTask<String, Void, String>{
        ProgressDialog progressDialog;
        Activity activity;
        HashMap<String, String> paramsHash=null;
        RequestPipe requestPipe=new RequestPipe();
        public RequestCitizenList(Activity activity,HashMap<String, String> paramsHash) {
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
                    Toast.makeText(MyConfig.CONTEXT, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    ArrayList<DetailedData> recsArrayList=new ArrayList<DetailedData>();
                    JSONObject recsData=jsonObject.getJSONObject("data");
                    JSONArray recsArray=recsData.getJSONArray("data");
                    int recsLen=recsArray.length();
                    for(int i=0;i<recsLen;i++)
                    {
                        JSONObject rec = recsArray.getJSONObject(i);
                        DetailedData cdata=new DetailedData();
                        cdata.caseId=rec.getString("caseId");
                        cdata.fever=rec.getString("fever");
                        cdata.abdomenpain=rec.getString("abdomenpain");
                        cdata.diarrhea=rec.getString("diarrhea");
                        cdata.backneckpain=rec.getString("backneckpain");
                        cdata.hypertension=rec.getString("hypertension");

                        Log.e("info", "district: " +cdata.hypertension);
                        recsArrayList.add(cdata);
                        // Toast.makeText(MyConfig.CONTEXT,ngoData.ngoName,Toast.LENGTH_SHORT).show();
                    }
                    DetailedListAdapter adapter = new DetailedListAdapter(context,recsArrayList);
                    //ngoListRecyclerView.setHasFixedSize(true);
                    detailedScreeningFinalListRecyclerView.setLayoutManager(new LinearLayoutManager(ViewDetailedScreening.this));
                    detailedScreeningFinalListRecyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
                else Toast.makeText(MyConfig.CONTEXT, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

            }catch (Exception ee){}

        }

    }
}


class DetailedData {
    String caseId="";
    String fever="";
    String abdomenpain="";
    String diarrhea="";
    String backneckpain="";
    String hypertension="";

    public DetailedData() {

    }
}
class DetailedViewHolder extends RecyclerView.ViewHolder {
    TextView textCaseid=null;
    TextView textFever=null;
    TextView textAbdomen=null;
    TextView textDiarrhea=null;
    TextView textBackneckpain=null;
    TextView textHypertension=null;

    public DetailedViewHolder(View itemView){
        super(itemView);
        textCaseid=(TextView)itemView.findViewById(R.id.txCaseid);
        textFever=(TextView)itemView.findViewById(R.id.txFever);
        textAbdomen=(TextView)itemView.findViewById(R.id.txAbdomen);
        textDiarrhea=(TextView)itemView.findViewById(R.id.txDiarrhea);
        textBackneckpain=(TextView)itemView.findViewById(R.id.txBackpain);
        textHypertension=(TextView)itemView.findViewById(R.id.txHypertension);

    }
}

class DetailedListAdapter extends RecyclerView.Adapter<DetailedViewHolder> {
    ArrayList<DetailedData> recs=null;
    Context context;
    public DetailedListAdapter(Context context,ArrayList<DetailedData> recs) {
        this.recs = recs;
        this.context = context;

    }
    @Override
    public DetailedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.detailedscreeningview_row, parent, false);
        DetailedViewHolder viewHolder = new DetailedViewHolder(listItem);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(DetailedViewHolder holder, int position) {
        final DetailedData rec = recs.get(position);
        holder.textCaseid.setText(holder.textCaseid.getText() + rec.caseId);
        String varParent="",varChild="";
        holder.textFever.setText(holder.textFever.getText() + rec.fever);
        holder.textAbdomen.setText(holder.textAbdomen.getText() + rec.abdomenpain);
        holder.textDiarrhea.setText(holder.textDiarrhea.getText() + rec.diarrhea);
        holder.textBackneckpain.setText(holder.textBackneckpain.getText() + rec.backneckpain);
        holder.textHypertension.setText(holder.textHypertension.getText() + rec.hypertension);

    }

    @Override
    public int getItemCount() {
        return recs.size();
    }
}