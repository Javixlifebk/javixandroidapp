package com.sumago.latestjavix;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sumago.latestjavix.Util.Config;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


public class ScreenerDoctorList extends AppCompatActivity {
    RecyclerView doctorListRecyclerView;
    private static final String TAG = "_msg";
    String CaseId="",ScreenerId="";
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screener_doctor_list);
        context=this;
        Bundle bundle=getIntent().getExtras();
        CaseId=bundle.getString("CaseId");
        MyConfig.CONTEXT=getApplicationContext();
        doctorListRecyclerView = (RecyclerView) findViewById(R.id.doctorListRecyclerView);
        HashMap<String,String> paramHash=new HashMap<String,String>();
        paramHash.put("userId","rahulpandeyjaiho@gmail.com");
        paramHash.put("token","dfjkhsdfaksjfh3756237");
        paramHash.put("ngoId", Config.NGO_ID);

        RequestDoctorList req=new RequestDoctorList(this,paramHash);
        req.execute(MyConfig.URL_DOCTOR_LIST);
    }

    class RequestDoctorList extends AsyncTask<String, Void, String>
    {
        ProgressDialog progressDialog;
        Activity activity;
        HashMap<String, String> paramsHash=null;
        RequestPipe requestPipe=new RequestPipe();
        public RequestDoctorList(Activity activity,HashMap<String, String> paramsHash) {
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
                    ArrayList<ScreenerDoctorData> recsArrayList=new ArrayList<ScreenerDoctorData>();
                    JSONObject recsData=jsonObject.getJSONObject("data");
                    JSONArray recsArray=recsData.getJSONArray("data");
                    int recsLen=recsArray.length();
                    for(int i=0;i<recsLen;i++)
                    {
                        JSONObject rec = recsArray.getJSONObject(i);
                        ScreenerDoctorData screenerDoctorData=new ScreenerDoctorData();
                        screenerDoctorData.DocId=rec.getString("doctorId");
                        screenerDoctorData.DocName=rec.getString("firstName") +" " + rec.getString("lastName");
                        JSONObject info=rec.getJSONObject("info");
                        screenerDoctorData.Qualification=info.getString("qualification");
                        screenerDoctorData.Specialization=info.getString("specialisation");
                        screenerDoctorData.Exp=info.getString("dateOfOnBoarding");
                        screenerDoctorData.CaseId=CaseId;


                        recsArrayList.add(screenerDoctorData);
                        // Toast.makeText(MyConfig.CONTEXT,ngoData.ngoName,Toast.LENGTH_SHORT).show();
                    }
                    ScreenerDoctorListAdapter adapter = new ScreenerDoctorListAdapter(context,recsArrayList);
                    //ngoListRecyclerView.setHasFixedSize(true);
                    doctorListRecyclerView.setLayoutManager(new LinearLayoutManager(ScreenerDoctorList.this));
                    doctorListRecyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
                else Toast.makeText(MyConfig.CONTEXT, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

            }catch (Exception ee){}

        }

    }

    static class RequestUpdate extends AsyncTask<String, Void, String>
    {
        ProgressDialog progressDialog;
        View activity;
        HashMap<String, String> paramsHash=null;
        RequestPipe requestPipe=new RequestPipe();
        public RequestUpdate(View activity,HashMap<String, String> paramsHash) {
            this.activity=activity;
            this.paramsHash=paramsHash;
            progressDialog = new ProgressDialog(activity.getContext());
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

                }
                else Toast.makeText(MyConfig.CONTEXT, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

            }catch (Exception ee){Toast.makeText(MyConfig.CONTEXT, "Exp-90:"+ee, Toast.LENGTH_SHORT).show();}

        }

    }
}

class ScreenerDoctorData {
    String DocId="";
    String DocName="";
    String Exp="";
    String Qualification="";
    String Specialization="";
    String CaseId="";

    public ScreenerDoctorData() {

    }
}

class ScreenerDoctorViewHolder extends RecyclerView.ViewHolder {

    TextView txName=null;
    TextView txExp=null;
    TextView txQualification=null;
    TextView txSpecialization=null;
    AppCompatImageView imgRefer=null;

    public ScreenerDoctorViewHolder(View itemView)
    {
        super(itemView);
        txName=(TextView)itemView.findViewById(R.id.txName);
        txExp=(TextView)itemView.findViewById(R.id.txExp);
        txQualification=(TextView)itemView.findViewById(R.id.txQualification);
        txSpecialization=(TextView)itemView.findViewById(R.id.txSpecialization);
        imgRefer=(AppCompatImageView)itemView.findViewById(R.id.imgRefer);
    }
}

class ScreenerDoctorListAdapter extends RecyclerView.Adapter<ScreenerDoctorViewHolder> {
    ArrayList<ScreenerDoctorData> recs=null;
    Context context;
    private static final String TAG = "_msg";
    public ScreenerDoctorListAdapter(Context context,ArrayList<ScreenerDoctorData> recs) {
        this.recs = recs;
        this.context = context;
    }
    @Override
    public ScreenerDoctorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.screenerdoclist_row, parent, false);
        ScreenerDoctorViewHolder viewHolder = new ScreenerDoctorViewHolder(listItem);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(ScreenerDoctorViewHolder holder, int position) {
        final ScreenerDoctorData rec = recs.get(position);
        holder.txName.setText(rec.DocName);
        int numOfDays;
        String[] arrSplit = rec.Exp.split("-");
        String dob=arrSplit[0]+ "-" + arrSplit[1]+ "-" + arrSplit[2];
        try {
            Date userDob = new SimpleDateFormat("yyyy-MM-dd").parse(dob);
            Date today = new Date();
            long diff =  today.getTime() - userDob.getTime();
            numOfDays = (int) (diff / (1000 * 60 * 60 * 24));
            Log.e(TAG, "exception : " + Integer.toString(numOfDays));
            holder.txExp.setText("At Javix " +Integer.toString(numOfDays) + " Days");
        }catch (Exception e){}
        holder.txQualification.setText(rec.Qualification);
        holder.txSpecialization.setText(rec.Specialization);
        final ScreenerDoctorViewHolder finalHolder=holder;
        holder.imgRefer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(holder.imgRefer.getContext()
                );
                alertDialog.setMessage("Are your sure want to refer a case !");
                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        HashMap<String,String> paramHash=new HashMap<String,String>();
                        paramHash.put("caseId",rec.CaseId);
                        paramHash.put("status","2");
                        paramHash.put("doctorId", rec.DocId);
                        paramHash.put("referDocId", rec.DocId);
                        paramHash.put("ngoId", Config.NGO_ID);

                        ScreeningListActivity.RequestUpdate reqUpdate=new ScreeningListActivity.RequestUpdate(finalHolder.txSpecialization,paramHash);
                        reqUpdate.execute(MyConfig.URL_PICK_CASE);
                        dialog.dismiss();
                        Toast.makeText(MyConfig.CONTEXT, "Case Referred Successfully !", Toast.LENGTH_SHORT).show();
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

                //Toast.makeText(MyConfig.CONTEXT, holder.textViewNgoId.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });



        /*if(rec.isOnline.equalsIgnoreCase("1")){
            holder.imgOnline.setImageResource(R.drawable.online);
        }
        else{
            holder.imgOnline.setImageResource(R.drawable.ofline);
        }*/
    }


    @Override
    public int getItemCount() {
        return recs.size();
    }
}
