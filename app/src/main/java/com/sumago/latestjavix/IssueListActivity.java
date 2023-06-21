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

public class IssueListActivity extends AppCompatActivity {
    RecyclerView issueListRecyclerView ;
    Context context;
    private static final String TAG = "_msg";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue_list);
        MyConfig.CONTEXT=getApplicationContext();
        context=this;
        issueListRecyclerView = (RecyclerView) findViewById(R.id.issueListRecyclerView);
        HashMap<String,String> paramHash=new HashMap<String,String>();
        paramHash.put("userId", Config._uid);
        paramHash.put("ngoId", Config.NGO_ID);

        RequestIssueList req=new RequestIssueList(this,paramHash);
        req.execute(MyConfig.URL_LISTISSUE);
        TextView txNewIssue=(TextView)findViewById(R.id.txNewIssue);

        txNewIssue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(IssueListActivity.this,IssueReportedActivity.class);
                startActivity(i);
            }
        });

    }

    class RequestIssueList extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        Activity activity;
        HashMap<String, String> paramsHash=null;
        RequestPipe requestPipe=new RequestPipe();
        public RequestIssueList(Activity activity,HashMap<String, String> paramsHash) {
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
                    ArrayList<IssueData> recsArrayList=new ArrayList<IssueData>();
                    JSONObject recsData=jsonObject.getJSONObject("data");
                    JSONArray recsArray=recsData.getJSONArray("data");
                    int recsLen=recsArray.length();
                    for(int i=0;i<recsLen;i++){
                        JSONObject rec = recsArray.getJSONObject(i);
                        IssueData cdata=new IssueData();
                        cdata.issueNo=rec.getString("issueNo");
                        cdata.issue=rec.getString("issue");
                        cdata.issueDetails=rec.getString("issueDetails");
                        cdata.createdAt=rec.getString("createdAt");
                        cdata.istatus=rec.getString("status");
                        //Log.e(TAG, "staus: " + cdata.istatus);
                        recsArrayList.add(cdata);
                    }
                    IssueListAdapter adapter = new IssueListAdapter(context,recsArrayList);
                    issueListRecyclerView.setLayoutManager(new LinearLayoutManager(IssueListActivity.this));
                    issueListRecyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
                else Toast.makeText(MyConfig.CONTEXT, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

            }catch (Exception ee){}

        }

    }
}

class IssueData {
    String issueNo="";
    String issue="";
    String issueDetails="";
    String createdAt="";
    String istatus="";

    public IssueData() {

    }
}
class IssueViewHolder extends RecyclerView.ViewHolder {
    TextView txSubject=null;
    TextView txIssue=null;
    TextView txIssueNo=null;
    TextView IssueDate=null;
    TextView txStatus=null;
    public IssueViewHolder(View itemView)
    {
        super(itemView);
        txSubject=(TextView)itemView.findViewById(R.id.txSubject);
        txIssue=(TextView)itemView.findViewById(R.id.txIssue);
        txIssueNo=(TextView)itemView.findViewById(R.id.txIssueNo);
        IssueDate=(TextView)itemView.findViewById(R.id.txIssueDate);
        txStatus=(TextView)itemView.findViewById(R.id.txStatus);
    }
}

class IssueListAdapter extends RecyclerView.Adapter<IssueViewHolder> {
    ArrayList<IssueData> recs=null;
    Context context;
    public IssueListAdapter(Context context,ArrayList<IssueData> recs) {
        this.recs = recs;
        this.context = context;

    }
    @Override
    public IssueViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.issuelist_row, parent, false);
        IssueViewHolder viewHolder = new IssueViewHolder(listItem);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(IssueViewHolder holder, int position) {
        final IssueData rec = recs.get(position);
        holder.txSubject.setText(holder.txSubject.getText() + rec.issue);
        holder.txIssue.setText(holder.txIssue.getText() +rec.issueDetails);
        holder.txIssueNo.setText(holder.txIssueNo.getText() +rec.issueNo);
        holder.IssueDate.setText(holder.IssueDate.getText() + rec.createdAt);
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