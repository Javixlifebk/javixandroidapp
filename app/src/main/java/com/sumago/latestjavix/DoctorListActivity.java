package com.sumago.latestjavix;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sumago.latestjavix.Util.Config;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;

public class DoctorListActivity extends AppCompatActivity {
    RecyclerView doctorListRecyclerView;
    private static final String TAG = "_msg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_list);
        MyConfig.CONTEXT=getApplicationContext();
        doctorListRecyclerView = (RecyclerView) findViewById(R.id.doctorListRecyclerView);
        HashMap<String,String> paramHash=new HashMap<String,String>();
        paramHash.put("userId","rahulpandeyjaiho@gmail.com");
        paramHash.put("token","dfjkhsdfaksjfh3756237");
        paramHash.put("ngoId", Config.NGO_ID);


        RequestDoctorList req=new RequestDoctorList(this,paramHash);
        req.execute(MyConfig.URL_DOCTOR_LIST);
        searchInit();
    }

    ArrayList<DoctorData> recsArrayList=null;
    ArrayList<DoctorData> recsFilteredArrayList=new ArrayList<DoctorData>();
    DoctorListAdapter adapterForAllData=null;
    DoctorListAdapter adapterForFilteredData=null;
    EditText searchBox=null;
    private void searchInit(){
       searchBox=(EditText)findViewById(R.id.jdoctorList_searchBox);
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
               if(DoctorListActivity.this.adapterForAllData!=null){
                    if(val.length()==0){
                        DoctorListAdapter adapter = new DoctorListAdapter(DoctorListActivity.this.recsArrayList);
                        DoctorListActivity.this.doctorListRecyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
                    else{
                        ArrayList<DoctorData> recs = DoctorListActivity.this.recsArrayList;
                        ArrayList<DoctorData> filteresRecs = DoctorListActivity.this.recsFilteredArrayList;
                        DoctorListActivity.this.recsFilteredArrayList.clear();
                        int allSize = recs.size();
                        for (int i = 0; i < allSize; i++) {
                            DoctorData rec = recs.get(i);
                            if (rec.ngoName.toLowerCase().indexOf(val.toLowerCase()) >= 0)
                                filteresRecs.add(rec);
                        }
                        DoctorListAdapter adapter = new DoctorListAdapter(filteresRecs);
                        DoctorListActivity.this.doctorListRecyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
                }// if Config -else
            }
       });

   }
    // Calling NGO LIST
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
                    ArrayList<DoctorData> recsArrayList=new ArrayList<DoctorData>();
                    JSONObject recsData=jsonObject.getJSONObject("data");
                    JSONArray recsArray=recsData.getJSONArray("data");
                    int recsLen=recsArray.length();
                    for(int i=0;i<recsLen;i++)
                    {
                        JSONObject rec = recsArray.getJSONObject(i);
                        DoctorData doctorDataData=new DoctorData();
                        doctorDataData.ngoId=rec.getString("doctorId");
                        doctorDataData.ngoName=rec.getString("firstName") +" " + rec.getString("lastName");
                        doctorDataData.mobile=rec.getString("mobile");
                        doctorDataData.email=rec.getString("email");

                        JSONObject info=rec.getJSONObject("info");
                        doctorDataData.address=info.getString("address");
                        doctorDataData.country=info.getString("country");
                        doctorDataData.state=info.getString("state");
                        doctorDataData.district=info.getString("district");
                        doctorDataData.Qualification=info.getString("qualification");
                        //Log.e(TAG, "status : " + doctorDataData.email);
                        //doctorDataData.Qualification= URLDecoder.decode(doctorDataData.Qualification,"utf-8");
                        doctorDataData.Specialization=info.getString("specialisation");

                        /*JSONArray is_online=rec.getJSONArray("online");
                        JSONObject is_loggedin=is_online.getJSONObject(0);

                        if(is_loggedin.has(""))
                        doctorDataData.isOnline=is_loggedin.getString("rakesh sir call me");*/


                        recsArrayList.add(doctorDataData);
                        // Toast.makeText(MyConfig.CONTEXT,ngoData.ngoName,Toast.LENGTH_SHORT).show();
                    }

                    DoctorListAdapter adapter = new DoctorListAdapter(recsArrayList);
                    DoctorListActivity.this.adapterForAllData = adapter;
                    DoctorListActivity.this.recsArrayList = recsArrayList;
                    doctorListRecyclerView.setLayoutManager(new LinearLayoutManager(DoctorListActivity.this));
                    doctorListRecyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                }
                else Toast.makeText(MyConfig.CONTEXT, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

            }catch (Exception ee){}

        }

    }
}
class DoctorData {
    String isOnline="";
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
    String Qualification="";
    String Specialization="";

    public DoctorData() {

    }
}

class DoctorViewHolder extends RecyclerView.ViewHolder {
    TextView textViewNgoName=null;
    TextView textViewNgoId=null;
    TextView textViewNgoAddress=null;
    TextView txQualification=null;
    TextView txSpecialization=null;
    TextView textViewNgoMobile=null;
    TextView textisOnline=null;
    ImageView imgOnline=null;
    public DoctorViewHolder(View itemView)
    {
        super(itemView);
        textViewNgoName=(TextView)itemView.findViewById(R.id.ngoListRow_name);
       // textViewNgoId=(TextView)itemView.findViewById(R.id.ngoListRow_id);
        textViewNgoAddress=(TextView)itemView.findViewById(R.id.ngoListRow_address);
        //txQualification=(TextView)itemView.findViewById(R.id.txQualification);
        txSpecialization=(TextView)itemView.findViewById(R.id.txSpecialization);
        textViewNgoMobile=(TextView)itemView.findViewById(R.id.ngoListRow_ngoMobileNo);
        imgOnline=(ImageView)itemView.findViewById(R.id.isOnline);
        //textisOnline=(TextView)itemView.findViewById(R.id.isOnline);
    }
}

class DoctorListAdapter extends RecyclerView.Adapter<DoctorViewHolder> {
    ArrayList<DoctorData> recs=null;
    public DoctorListAdapter(ArrayList<DoctorData> recs) {
        this.recs = recs;
    }
    @Override
    public DoctorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.doctorlist_row, parent, false);
        DoctorViewHolder viewHolder = new DoctorViewHolder(listItem);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(DoctorViewHolder holder, int position) {
        final DoctorData rec = recs.get(position);
        holder.textViewNgoName.setText(rec.ngoName);
        //Toast.makeText(MyConfig.CONTEXT, rec.ngoName, Toast.LENGTH_SHORT).show();
       // holder.textViewNgoId.setText(rec.ngoId);
        holder.textViewNgoAddress.setText(rec.address+","+rec.district+"\n"+rec.state);
       // holder.txQualification.setText(rec.Qualification);
        holder.txSpecialization.setText(rec.Specialization);
        //holder.textisOnline.setText(rec.isOnline);
        if(rec.mobile.length()==10)
        { holder.textViewNgoMobile.setText(rec.mobile);}
        else { holder.textViewNgoMobile.setText("");}

        if(rec.isOnline.equalsIgnoreCase("1")){
            holder.imgOnline.setImageResource(R.drawable.online);
        }
        else{
            holder.imgOnline.setImageResource(R.drawable.ofline);
        }

        holder.textViewNgoMobile .setOnClickListener(new View.OnClickListener() {
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
