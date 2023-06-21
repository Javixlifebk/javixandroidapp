package com.sumago.latestjavix;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sumago.latestjavix.Util.Config;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ScreenerList extends AppCompatActivity {
    RecyclerView screenerListRecyclerView ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screener_list);
        MyConfig.CONTEXT=getApplicationContext();
        screenerListRecyclerView = (RecyclerView) findViewById(R.id.screenerListRecyclerView);

        HashMap<String,String> paramHash=new HashMap<String,String>();
        if(Config._roleid==3){
        paramHash.put("userId","4632746328");
        paramHash.put("ngoId",Config._nogid);
        paramHash.put("token","dfjkhsdfaksjfh3756237");}
        else{
            paramHash.put("userId","4632746328");
            //paramHash.put("ngoId","123456790");
            paramHash.put("token","dfjkhsdfaksjfh3756237");
        }

        ScreenerList.RequestScreenerList req=new ScreenerList.RequestScreenerList(this,paramHash);
        req.execute(MyConfig.URL_SCREENER_LIST);
        // END Calling NGO Task List
        searchInit();
    }

    ArrayList<ScreenerData> recsArrayList=null;
    ArrayList<ScreenerData> recsFilteredArrayList=new ArrayList<ScreenerData>();
    ScreenerListAdapter adapterForAllData=null;
    ScreenerListAdapter adapterForFilteredData=null;
    EditText searchBox=null;
    private void searchInit(){
        searchBox=(EditText)findViewById(R.id.jscreenerList_searchBox);
        final EditText searchBoxObject=searchBox;
        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                String val=searchBoxObject.getText().toString();
                System.out.println(" VALUE ="+val);
                if(ScreenerList.this.adapterForAllData!=null){
                    if(val.length()==0){
                        ScreenerListAdapter adapter = new ScreenerListAdapter(ScreenerList.this.recsArrayList);
                        ScreenerList.this.screenerListRecyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
                    else{
                        ArrayList<ScreenerData> recs = ScreenerList.this.recsArrayList;
                        ArrayList<ScreenerData> filteresRecs = ScreenerList.this.recsFilteredArrayList;
                        ScreenerList.this.recsFilteredArrayList.clear();
                        int allSize = recs.size();
                        for (int i = 0; i < allSize; i++) {
                            ScreenerData rec = recs.get(i);
                            if (rec.name.toLowerCase().indexOf(val.toLowerCase()) >= 0)
                                filteresRecs.add(rec);
                        }
                        ScreenerListAdapter adapter = new ScreenerListAdapter(filteresRecs);
                        ScreenerList.this.screenerListRecyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
                }// if Config -else
            }
        });

    }

    // Calling Screener LIST
    class RequestScreenerList extends AsyncTask<String, Void, String>
    {
        ProgressDialog progressDialog;
        Activity activity;
        HashMap<String, String> paramsHash=null;
         RequestPipe requestPipe=new RequestPipe();
        public RequestScreenerList(Activity activity,HashMap<String, String> paramsHash) {
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
                    ArrayList<ScreenerData> recsArrayList=new ArrayList<ScreenerData>();
                    JSONObject recsData=jsonObject.getJSONObject("data");
                    JSONArray recsArray=recsData.getJSONArray("data");
                    int recsLen=recsArray.length();
                    for(int i=0;i<recsLen;i++)
                    {
                        JSONObject rec = recsArray.getJSONObject(i);
                        ScreenerData data=new ScreenerData();
                        data.id=rec.getString("screenerId");
                        data.name=rec.getString("firstName")+" "+rec.getString("lastName");
                        data.sex=rec.getString("sex");
                        data.mobile=rec.getString("mobile");
                        data.email=rec.getString("email");
                        JSONObject info=rec.getJSONObject("info");
                        data.address=info.getString("address");
                        data.country=info.getString("country");
                        data.state=info.getString("state");
                        data.district=info.getString("district");
                        recsArrayList.add(data);
                        // Toast.makeText(MyConfig.CONTEXT,ngoData.ngoName,Toast.LENGTH_SHORT).show();
                    }
                    ScreenerListAdapter adapter = new ScreenerListAdapter(recsArrayList);
                    ScreenerList.this.adapterForAllData = adapter;
                    ScreenerList.this.recsArrayList = recsArrayList;
                    screenerListRecyclerView.setLayoutManager(new LinearLayoutManager(ScreenerList.this));
                    screenerListRecyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
                else Toast.makeText(MyConfig.CONTEXT, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

            }catch (Exception ee){}

        }

    }
}

class ScreenerData {
    String id="";
    String name="";
    String sex="";
    String mobile="";
    String email="";
    String address="";
    String district="";
    String state="";
    String country="";

    public ScreenerData() {

    }
}

class ScreenerViewHolder extends RecyclerView.ViewHolder {
    TextView textViewNgoName=null;
    TextView textViewNgoId=null;
    TextView textViewNgoAddress=null;
    TextView textViewEmail=null;
    TextView textViewMobile=null;
    TextView textViewSex=null;
    public ScreenerViewHolder(View itemView)
    {
        super(itemView);
        textViewNgoName=(TextView)itemView.findViewById(R.id.screenerListRow_name);
        textViewNgoId=(TextView)itemView.findViewById(R.id.screenerListRow_id);
        textViewNgoAddress=(TextView)itemView.findViewById(R.id.screenerListRow_address);
        textViewEmail=(TextView)itemView.findViewById(R.id.screenerListRow_email);
        textViewMobile=(TextView)itemView.findViewById(R.id.screenerListRow_screenerMobileNo);
        textViewSex=(TextView)itemView.findViewById(R.id.screenerListRow_sex);
    }
}

class ScreenerListAdapter extends RecyclerView.Adapter<ScreenerViewHolder> {
    ArrayList<ScreenerData> recs=null;
    public ScreenerListAdapter(ArrayList<ScreenerData> recs) {
        this.recs = recs;
    }
    @Override
    public ScreenerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.screenerlist_row, parent, false);
        ScreenerViewHolder viewHolder = new ScreenerViewHolder(listItem);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(ScreenerViewHolder holder, int position) {
        final ScreenerData rec = recs.get(position);
        holder.textViewNgoName.setText(rec.name);
        holder.textViewSex.setText(rec.sex);
        holder.textViewNgoId.setText(rec.id);
        holder.textViewNgoAddress.setText(rec.address+","+rec.district+"\n"+rec.state);
        holder.textViewEmail.setText(rec.email);
        if(rec.mobile.length()==10)
        { holder.textViewMobile.setText("call: "+rec.mobile);}
        else { holder.textViewMobile.setText("");}

        holder.textViewMobile .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(view.getContext(), Manifest.permission.CALL_PHONE) ==
                        PackageManager.PERMISSION_GRANTED){
                    //Toast.makeText(context, holder.txPhone.getText(),Toast.LENGTH_LONG).show();

                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:+91" + rec.mobile));
                    view.getContext().startActivity(callIntent);

                }
                else{
                    Toast.makeText(view.getContext(),"Please grant permission manually ! ",Toast.LENGTH_LONG).show();
                   /*ActivityCompat.requestPermissions(context,
                           new String[]{android.Manifest.permission.CALL_PHONE},
                           888);*/
                }
            }
        });

    }


    @Override
    public int getItemCount() {
        return recs.size();
    }
}
