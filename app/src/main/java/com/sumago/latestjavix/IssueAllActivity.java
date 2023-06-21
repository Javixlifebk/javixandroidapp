package com.sumago.latestjavix;

import androidx.appcompat.app.AppCompatActivity;
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


public class IssueAllActivity extends AppCompatActivity {
    RecyclerView issueAllListRecyclerView ;
    Context context;
    private static final String TAG = "_msg";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue_all);
        MyConfig.CONTEXT=getApplicationContext();
        context=this;
        issueAllListRecyclerView = (RecyclerView) findViewById(R.id.issueAllListRecyclerView);
        HashMap<String,String> paramHash=new HashMap<String,String>();
        paramHash.put("userId", "rakesh");

        paramHash.put("ngoId", Config.NGO_ID);

        RequestAllIssueList req=new RequestAllIssueList(this,paramHash);
        req.execute(MyConfig.URL_ALLISSUE);

    }


    class RequestAllIssueList extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        Activity activity;
        HashMap<String, String> paramsHash=null;
        RequestPipe requestPipe=new RequestPipe();
        public RequestAllIssueList(Activity activity,HashMap<String, String> paramsHash) {
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
                    ArrayList<IssueAllData> recsArrayList=new ArrayList<IssueAllData>();
                    JSONObject recsData=jsonObject.getJSONObject("data");
                    JSONArray recsArray=recsData.getJSONArray("data");
                    int recsLen=recsArray.length();
                    for(int i=0;i<recsLen;i++){
                        JSONObject rec = recsArray.getJSONObject(i);
                        IssueAllData cdata=new IssueAllData();
                        cdata.issueNo=rec.getString("issueNo");
                        cdata.issue=rec.getString("issue");
                        cdata.issueDetails=rec.getString("issueDetails");
                        cdata.createdAt=rec.getString("createdAt");
                        cdata.istatus=rec.getString("status");
                        cdata.byuser=rec.getString("userId");
                        Log.e(TAG, "staus: " + cdata.istatus);
                        recsArrayList.add(cdata);
                    }
                    IssueAllListAdapter adapter = new IssueAllListAdapter(context,recsArrayList);
                    issueAllListRecyclerView.setLayoutManager(new LinearLayoutManager(IssueAllActivity.this));
                    issueAllListRecyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
                else Toast.makeText(MyConfig.CONTEXT, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

            }catch (Exception ee){}

        }

    }
}

class IssueAllData {
    String issueNo="";
    String issue="";
    String issueDetails="";
    String createdAt="";
    String istatus="";
    String byuser="";

    public IssueAllData() {

    }
}
class IssueAllViewHolder extends RecyclerView.ViewHolder {
    TextView txSubject=null;
    TextView txIssue=null;
    TextView txIssueNo=null;
    TextView IssueDate=null;
    TextView txStatus=null;
    TextView txUser=null;

    public IssueAllViewHolder(View itemView)
    {
        super(itemView);
        txSubject=(TextView)itemView.findViewById(R.id.txSubject);
        txIssue=(TextView)itemView.findViewById(R.id.txIssue);
        txIssueNo=(TextView)itemView.findViewById(R.id.txIssueNo);
        IssueDate=(TextView)itemView.findViewById(R.id.txIssueDate);
        txStatus=(TextView)itemView.findViewById(R.id.txStatus);
        txUser=(TextView)itemView.findViewById(R.id.txUser);
    }
}

class IssueAllListAdapter extends RecyclerView.Adapter<IssueAllViewHolder> {
    ArrayList<IssueAllData> recs=null;
    Context context;
    public IssueAllListAdapter(Context context,ArrayList<IssueAllData> recs) {
        this.recs = recs;
        this.context = context;

    }
    @Override
    public IssueAllViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.issuealllist_row, parent, false);
        IssueAllViewHolder viewHolder = new IssueAllViewHolder(listItem);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(IssueAllViewHolder holder, int position) {
        final IssueAllData rec = recs.get(position);
        holder.txUser.setText(holder.txUser.getText() + rec.byuser);
        holder.txSubject.setText(holder.txSubject.getText() + rec.issue);
        holder.txIssue.setText(holder.txIssue.getText() +rec.issueDetails);
        holder.txIssueNo.setText(holder.txIssueNo.getText() +rec.issueNo);
        holder.IssueDate.setText(holder.IssueDate.getText() + rec.createdAt);
        Log.e("Holder :=", holder.txSubject.getText().toString());
        HashMap<String,String> iStatus=new HashMap<String,String>();
        iStatus.put("0","New Issue");
        iStatus.put("1","Assigned");
        iStatus.put("2","Resolved");
        iStatus.put("3","Closed");
        holder.txStatus.setText(iStatus.get(rec.istatus));

    }

    @Override
    public int getItemCount() {
        return recs.size();
    }
}