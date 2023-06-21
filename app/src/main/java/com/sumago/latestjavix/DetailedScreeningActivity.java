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

public class DetailedScreeningActivity extends AppCompatActivity {


    RecyclerView detailedScreeningListRecyclerView ;
    Context context;
    private static final String TAG = "_msg";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_screening);
        MyConfig.CONTEXT=getApplicationContext();
        context=this;
        detailedScreeningListRecyclerView = (RecyclerView) findViewById(R.id.detailedScreeningListRecyclerView);
        Bundle bundle=getIntent().getExtras();
        HashMap<String,String> paramHash=new HashMap<String,String>();
        paramHash.put("json","parentchild");
        paramHash.put("ngoId", Config.NGO_ID);

        RequestParentChildData req=new RequestParentChildData(this,paramHash);
        req.execute(MyConfig.URL_PARENTCHILD);
    }

    class RequestParentChildData extends AsyncTask<String, Void, String>
    {
        ProgressDialog progressDialog;
        Activity activity;
        HashMap<String, String> paramsHash=null;
        RequestPipe requestPipe=new RequestPipe();
        public RequestParentChildData(Activity activity,HashMap<String, String> paramsHash) {
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
                    ArrayList<ParentChildData> recsArrayList=new ArrayList<ParentChildData>();
                    JSONObject recsData=jsonObject.getJSONObject("data");
                    JSONArray recsArray=recsData.getJSONArray("data");
                    int recsLen=recsArray.length();
                    JSONObject rec = recsArray.getJSONObject(0);
                    JSONArray recAin = rec.getJSONArray("What is the purpose of your visit?");
                    for(int i=0;i<recAin.length();i++)
                    {
                        ParentChildData cdata=new ParentChildData();
                       JSONObject recObj=recAin.getJSONObject(i);
                       //String text=recObj.getString("text");
                        Log.e(TAG, "json data : " +recObj.getString("text"));
                        cdata.parentid=recObj.getString("id");
                        cdata.strdata=recObj.getString("text");
                        recsArrayList.add(cdata);

                    }

                    ParentChildAdapter adapter = new ParentChildAdapter(context,recsArrayList);
                    //ngoListRecyclerView.setHasFixedSize(true);
                    detailedScreeningListRecyclerView.setLayoutManager(new LinearLayoutManager(DetailedScreeningActivity.this));
                    detailedScreeningListRecyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
                else Toast.makeText(MyConfig.CONTEXT, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

            }catch (Exception ee){}

        }

    }
}

class ParentChildData {
    String parentid="";
    String strdata="";
    public ParentChildData() {

    }
}

class ParentChildHolder extends RecyclerView.ViewHolder {
    TextView txData=null;
    AppCompatButton btnRef=null;
    public ParentChildHolder(View itemView)
    {
        super(itemView);
        txData=(TextView)itemView.findViewById(R.id.txData);
        btnRef=(AppCompatButton)itemView.findViewById(R.id.btnNext);
    }
}

class ParentChildAdapter extends RecyclerView.Adapter<ParentChildHolder> {
    ArrayList<ParentChildData> recs=null;
    private static final String TAG = "_msg";
    Context context;
    public ParentChildAdapter(Context context,ArrayList<ParentChildData> recs) {
        this.recs = recs;
        this.context = context;
    }
    @Override
    public ParentChildHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.detailedscreening_row, parent, false);
        ParentChildHolder viewHolder = new ParentChildHolder(listItem);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(ParentChildHolder holder, int position) {
        final ParentChildData rec = recs.get(position);
        holder.txData.setText(rec.strdata);

        Log.e(TAG, "parameters : " +holder.txData.getText());

        holder.btnRef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent i = new Intent(context, ScreenerDoctorList.class);
               // i.putExtra("CaseId",rec.caseid);
                //context.startActivity(i);

                //Toast.makeText(MyConfig.CONTEXT, holder.textViewNgoId.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return recs.size();
    }
}