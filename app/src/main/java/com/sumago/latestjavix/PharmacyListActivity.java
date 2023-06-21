package com.sumago.latestjavix;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import com.sumago.latestjavix.Util.Config;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class PharmacyListActivity extends AppCompatActivity {
    RecyclerView pharmacyListRecyclerView ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy_list);
        MyConfig.CONTEXT=getApplicationContext();
        pharmacyListRecyclerView = (RecyclerView) findViewById(R.id.pharmacyListRecyclerView);

        HashMap<String,String> paramHash=new HashMap<String,String>();

        paramHash.put("userId","rahulpandeyjaiho@gmail.com");
        paramHash.put("token","dfjkhsdfaksjfh3756237");
        paramHash.put("ngoId", Config.NGO_ID);

        RequestPharmacList req=new RequestPharmacList(this,paramHash);
        req.execute(MyConfig.URL_PHARMACY_LIST);
        searchInit();
    }

    ArrayList<PharmacyData> recsArrayList=null;
    ArrayList<PharmacyData> recsFilteredArrayList=new ArrayList<PharmacyData>();
    PharmacyListAdapter adapterForAllData=null;
    PharmacyListAdapter adapterForFilteredData=null;
    EditText searchBox=null;
    private void searchInit(){
        searchBox=(EditText)findViewById(R.id.jpharmacyList_searchBox);
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
                if(PharmacyListActivity.this.adapterForAllData!=null){
                    if(val.length()==0){
                        PharmacyListAdapter adapter = new PharmacyListAdapter(PharmacyListActivity.this.recsArrayList);
                        PharmacyListActivity.this.pharmacyListRecyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
                    else{
                        ArrayList<PharmacyData> recs = PharmacyListActivity.this.recsArrayList;
                        ArrayList<PharmacyData> filteresRecs = PharmacyListActivity.this.recsFilteredArrayList;
                        PharmacyListActivity.this.recsFilteredArrayList.clear();
                        int allSize = recs.size();
                        for (int i = 0; i < allSize; i++) {
                            PharmacyData rec = recs.get(i);
                            if (rec.name.toLowerCase().indexOf(val.toLowerCase()) >= 0)
                                filteresRecs.add(rec);
                        }
                        PharmacyListAdapter adapter = new PharmacyListAdapter(filteresRecs);
                        PharmacyListActivity.this.pharmacyListRecyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
                }// if Config -else
            }
        });

    }

    class RequestPharmacList extends AsyncTask<String, Void, String>
    {
        ProgressDialog progressDialog;
        Activity activity;
        HashMap<String, String> paramsHash=null;
        RequestPipe requestPipe=new RequestPipe();
        public RequestPharmacList(Activity activity,HashMap<String, String> paramsHash) {
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
                    ArrayList<PharmacyData> recsArrayList=new ArrayList<PharmacyData>();
                    JSONObject recsData=jsonObject.getJSONObject("data");
                    JSONArray recsArray=recsData.getJSONArray("data");
                    int recsLen=recsArray.length();
                    for(int i=0;i<recsLen;i++)
                    {
                        JSONObject rec = recsArray.getJSONObject(i);
                        PharmacyData data=new PharmacyData();
                        data.id=rec.getString("pharmacyId");
                        data.regno=rec.getString("pharmacyRegistrationNumber");
                        data.name=rec.getString("name");

                        data.owner=rec.getString("owner");
                        data.mobile=rec.getString("mobile");
                        data.email=rec.getString("email");
                        JSONObject info=rec.getJSONObject("info");
                        data.address=info.getString("address");
                        data.country=info.getString("country");
                        data.state=info.getString("state");
                        data.district=info.getString("district");
                        recsArrayList.add(data);
                        //Toast.makeText(MyConfig.data.state,Toast.LENGTH_SHORT).show();
                    }
                    PharmacyListAdapter adapter = new PharmacyListAdapter(recsArrayList);
                    PharmacyListActivity.this.adapterForAllData = adapter;
                    PharmacyListActivity.this.recsArrayList = recsArrayList;
                    pharmacyListRecyclerView.setLayoutManager(new LinearLayoutManager(PharmacyListActivity.this));
                    pharmacyListRecyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
                else Toast.makeText(MyConfig.CONTEXT, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

            }catch (Exception ee){}

        }

    }
}
class PharmacyData {
    String id="";
    String regno="";
    String name="";
    String owner="";
    String sex="";
    String mobile="";
    String email="";
    String address="";
    String district="";
    String state="";
    String country="";

    public PharmacyData() {

    }
}

class PharmacyViewHolder extends RecyclerView.ViewHolder {
    TextView PharmacyName=null;
    TextView PharmacyId=null;
    TextView PharmacyAddress=null;
    TextView PharmayNo=null;
    TextView textViewMobile=null;
    TextView PharmacyOwner=null;
    public PharmacyViewHolder(View itemView)
    {
        super(itemView);
        PharmacyName=(TextView)itemView.findViewById(R.id.PharmacyName);
        //PharmacyId=(TextView)itemView.findViewById(R.id.PharmacyId);
        PharmacyAddress=(TextView)itemView.findViewById(R.id.PharmacyAddress);
        PharmayNo=(TextView)itemView.findViewById(R.id.PharmayNo);
        textViewMobile=(TextView)itemView.findViewById(R.id.PharmacyMobile);
        PharmacyOwner=(TextView)itemView.findViewById(R.id.PharmacyOwner);
    }
}

class PharmacyListAdapter extends RecyclerView.Adapter<PharmacyViewHolder> {
    ArrayList<PharmacyData> recs=null;
    public PharmacyListAdapter(ArrayList<PharmacyData> recs) {
        this.recs = recs;
    }
    @Override
    public PharmacyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.pharmacylist_row, parent, false);
        PharmacyViewHolder viewHolder = new PharmacyViewHolder(listItem);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(PharmacyViewHolder holder, int position) {
        final PharmacyData rec = recs.get(position);
        holder.PharmacyName.setText(rec.name);
        //holder.PharmacyId.setText(rec.id);
        holder.PharmayNo.setText(rec.regno);
        holder.PharmacyAddress.setText(rec.address+","+rec.district+"\n"+rec.state);
        holder.PharmacyOwner.setText(rec.owner);
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
