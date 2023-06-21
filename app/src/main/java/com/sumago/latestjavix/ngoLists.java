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

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;

public class ngoLists extends AppCompatActivity {
    RecyclerView ngoListRecyclerView ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ngo_lists);
        MyConfig.CONTEXT=getApplicationContext();
        ngoListRecyclerView = (RecyclerView) findViewById(R.id.ngoListRecyclerView);


        HashMap<String,String> paramHash=new HashMap<String,String>();
        paramHash.put("userId","4632746328");
        paramHash.put("ngoId","123456790");
        paramHash.put("token","dfjkhsdfaksjfh3756237");


        RequestNGOList req=new RequestNGOList(this,paramHash);
        req.execute(MyConfig.URL_NGO_LIST);
        // END Calling NGO Task List
        searchInit();
    }
    ArrayList<NGOData> recsArrayList=null;
    ArrayList<NGOData> recsFilteredArrayList=new ArrayList<NGOData>();
    NGOListAdapter adapterForAllData=null;
    NGOListAdapter adapterForFilteredData=null;
    EditText searchBox=null;
    private void searchInit(){
        searchBox=(EditText)findViewById(R.id.jngoList_searchBox);
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
                if(ngoLists.this.adapterForAllData!=null){
                    if(val.length()==0){
                        NGOListAdapter adapter = new NGOListAdapter(ngoLists.this.recsArrayList);
                        ngoLists.this.ngoListRecyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
                    else{
                        ArrayList<NGOData> recs = ngoLists.this.recsArrayList;
                        ArrayList<NGOData> filteresRecs = ngoLists.this.recsFilteredArrayList;
                        ngoLists.this.recsFilteredArrayList.clear();
                        int allSize = recs.size();
                        for (int i = 0; i < allSize; i++) {
                            NGOData rec = recs.get(i);
                            if (rec.ngoName.toLowerCase().indexOf(val.toLowerCase()) >= 0)
                                filteresRecs.add(rec);
                        }
                        NGOListAdapter adapter = new NGOListAdapter(filteresRecs);
                        ngoLists.this.ngoListRecyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
                }// if Config -else
            }
        });

    }

    // Calling NGO LIST
    class RequestNGOList extends AsyncTask<String, Void, String>
    {
        ProgressDialog progressDialog;
        Activity activity;
        HashMap<String, String> paramsHash=null;
        RequestPipe requestPipe=new RequestPipe();
        public RequestNGOList(Activity activity,HashMap<String, String> paramsHash) {
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
                    ArrayList<NGOData> recsArrayList=new ArrayList<NGOData>();
                    JSONObject recsData=jsonObject.getJSONObject("data");
                    JSONArray recsArray=recsData.getJSONArray("data");
                    int recsLen=recsArray.length();
                    for(int i=0;i<recsLen;i++)
                    {
                        JSONObject rec = recsArray.getJSONObject(i);
                        NGOData ngoData=new NGOData();
                        ngoData.ngoId=rec.getString("ngoId");
                        ngoData.ngoName=rec.getString("name");
                        ngoData.mobile=rec.getString("mobile");
                        ngoData.email=rec.getString("email");
                        JSONObject info=rec.getJSONObject("info");
                        ngoData.address=info.getString("address");
                        ngoData.country=info.getString("country");
                        ngoData.state=info.getString("state");
                        ngoData.district=info.getString("district");
                        ngoData.ngoRegistrationNo=info.getString("ngoRegistrationNo");
                        ngoData.ngoRegistrationNo= URLDecoder.decode(ngoData.ngoRegistrationNo,"utf-8");


                        recsArrayList.add(ngoData);
                       // Toast.makeText(MyConfig.CONTEXT,ngoData.ngoName,Toast.LENGTH_SHORT).show();
                    }
                    NGOListAdapter adapter = new NGOListAdapter(recsArrayList);
                    ngoLists.this.adapterForAllData = adapter;
                    ngoLists.this.recsArrayList = recsArrayList;
                    ngoListRecyclerView.setLayoutManager(new LinearLayoutManager(ngoLists.this));
                    ngoListRecyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                }
                else Toast.makeText(MyConfig.CONTEXT, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

            }catch (Exception ee){}

        }

    }
}

class NGOData {
    String ngoId="";
    String ngoName="";
    String ngoOwner="";
    String mobile="";
    String email="";
    String address="";
    String district="";
    String state="";
    String country="";
    String ngoRegistrationNo="";

    public NGOData() {

    }
}

class NGOViewHolder extends RecyclerView.ViewHolder {
    TextView textViewNgoName=null;
    TextView textViewNgoId=null;
    TextView textViewNgoAddress=null;
    TextView textViewNgoRegistrationNo=null;
    TextView textViewNgoMobile=null;
    public NGOViewHolder(View itemView)
    {
        super(itemView);
        textViewNgoName=(TextView)itemView.findViewById(R.id.ngoListRow_name);
        textViewNgoId=(TextView)itemView.findViewById(R.id.ngoListRow_id);
        textViewNgoAddress=(TextView)itemView.findViewById(R.id.ngoListRow_address);
        textViewNgoRegistrationNo=(TextView)itemView.findViewById(R.id.ngoListRow_ngoRegistrationNo);
        textViewNgoMobile=(TextView)itemView.findViewById(R.id.ngoListRow_ngoMobileNo);
    }
}

class NGOListAdapter extends RecyclerView.Adapter<NGOViewHolder> {
    ArrayList<NGOData> recs=null;
    public NGOListAdapter(ArrayList<NGOData> recs) {
        this.recs = recs;
    }
    @Override
    public NGOViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.ngolist_row, parent, false);
        NGOViewHolder viewHolder = new NGOViewHolder(listItem);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(NGOViewHolder holder, int position) {
        final NGOData rec = recs.get(position);
        holder.textViewNgoName.setText(rec.ngoName);
        holder.textViewNgoId.setText(rec.ngoId);
        holder.textViewNgoAddress.setText(rec.address+","+rec.district+"\n"+rec.state);
        holder.textViewNgoRegistrationNo.setText(rec.ngoRegistrationNo);
        if(rec.mobile.length()==10)
        { holder.textViewNgoMobile.setText("call: "+rec.mobile);}
        else { holder.textViewNgoMobile.setText("");}

        holder.textViewNgoMobile.setOnClickListener(new View.OnClickListener() {
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
