package com.sumago.latestjavix;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sumago.latestjavix.Fragments.GeneralSurveyFragment;
import com.sumago.latestjavix.Fragments.HealthSurveyFragment;
import com.sumago.latestjavix.Util.Config;
import com.sumago.latestjavix.Util.Shared_Preferences;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class GeneralSurveyList extends AppCompatActivity {
    RecyclerView SurveyListRecyclerView;
    private static final String TAG = "_msg";
    Context context;
    static Activity THISACTIVITY=null;
    private SearchView searchView;

    ArrayList<GeneralSurveyListData> recsArrayList=new ArrayList<GeneralSurveyListData>();
    GeneralSurveyListAdapter adapter=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_survey_list);

        searchView = findViewById(R.id.searchView);
        searchView.clearFocus();

        MyConfig.CONTEXT=getApplicationContext();
        context=this;
        CitizenListSurvey.THISACTIVITY=this;
        SurveyListRecyclerView = (RecyclerView) findViewById(R.id.SurveyListRecyclerView);
        HashMap<String,String> paramHash=new HashMap<String,String>();
        //paramHash.put("userId","rahulpandeyjaiho@gmail.com");
        paramHash.put("token","dfjkhsdfaksjfh3756237");
        paramHash.put("ngoId", Config.NGO_ID);

        //Config.isOffline=true;

        if(Config.isOffline){
            try {
                ArrayList<GeneralSurveyListData> recsArrayList=new ArrayList<GeneralSurveyListData>();
                SQLiteDatabase db;
                db = openOrCreateDatabase("javixlife", Context.MODE_PRIVATE, null);
                //Cursor c = db.rawQuery("SELECT * FROM javix_update where _status=0 and service_type='General Survey' order by insert_date desc", null);
                Cursor c = db.rawQuery("SELECT * FROM javix_update where _status=0  order by insert_date desc", null);

                if (c.getCount() == 0) {
                    Toast.makeText(this, "No records found in local system", Toast.LENGTH_SHORT).show();
                }
                StringBuffer buffer = new StringBuffer();
                int i = 1;
                String jsonStr="{\"abc\":\"1\"}";
                JSONObject rec;
                while (c.moveToNext()) {
                    try {
                        jsonStr=c.getString(4);
                        rec= new JSONObject(jsonStr);
                        GeneralSurveyListData sData=new GeneralSurveyListData();
                        sData.noOfFamilyMembers=rec.getString("noOfFamilyMembers");
                        sData.familyId=rec.getString("familyId");
                        sData.nameHead=rec.getString("nameHead");
                        sData.ageHead=rec.getString("ageHead");
                        sData.NoOfAdultMales=rec.getString("NoOfAdultMales");
                        sData.NoOfAdultFemales=rec.getString("NoOfAdultFemales");
                        sData.NoOfChildrenMales=rec.getString("NoOfChildrenMales");
                        sData.NoOfChildrenFemales=rec.getString("NoOfChildrenFemales");
                        recsArrayList.add(sData);

                    } catch (Exception ex) {
                        Toast.makeText(getApplicationContext(),"jSON eRROR Message :-"  +ex.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                    }

                }

                  /*JSONArray citizenArray=rec.getJSONArray("citizenId");
                    /*sData.noOfFamilyMembers=rec.getString("noOfFamilyMembers");
                    sData.familyId=rec.getString("familyId");
                    sData.nameHead=rec.getString("nameHead");
                    sData.ageHead=rec.getString("ageHead");
                    sData.NoOfAdultMales=rec.getString("NoOfAdultMales");
                    sData.NoOfAdultFemales=rec.getString("NoOfAdultFemales");
                    sData.NoOfChildrenMales=rec.getString("NoOfChildrenMales");
                    sData.NoOfChildrenFemales=rec.getString("NoOfChildrenFemales");*/
                    /*JSONArray citizenArray=rec.getJSONArray("citizenId");
                    if (rec.has("cases")) {
                        if (citizenArray.length() > 0) {
                            for (i = 0; i < citizenArray.length(); i++) {
                                sData.CitizeID += citizenArray.getString(i) + ",";
                            }

                        }
                    }*/
                //Log.e(TAG,citizenArray.getString(0));
                //recsArrayList.add(sData);


               /* String[] separated; String myData;String[] splitData;
                while (c.moveToNext()) {
                    GeneralSurveyListData sData=new GeneralSurveyListData();
                    //myTable += "<tr><td>" + Integer.toString(i) + "</td><td>" + c.getString(1) + "</td><td>" + c.getString(3) + "</td><td>" + c.getString(5) + "</td></tr>";
                    //Toast.makeText(this, c.getString(3), Toast.LENGTH_SHORT).show();
                    myData=c.getString(c.getColumnIndex("service_data"));
                    myData=myData.replace("{","");
                    myData=myData.replace("}","");
                    separated = myData.split(",");
                    HashMap<String,String> hashMap=new HashMap<>();

                    //Toast.makeText(this, separated[1], Toast.LENGTH_SHORT).show();
                    splitData = separated[4].split("=");
                    if(splitData.length==2) {
                        sData.noOfFamilyMembers = splitData[1];
                    }
                    splitData = separated[0].split("=");
                    if(splitData.length==2) {
                        sData.familyId = splitData[1];
                    }
                    splitData = separated[3].split("=");
                    if(splitData.length==2) {
                        sData.nameHead = splitData[1];
                    }
                    splitData = separated[9].split("=");
                    if(splitData.length==2) {
                        sData.ageHead = splitData[1];
                    }
                    splitData = separated[6].split("=");
                    if(splitData.length==2) {
                        sData.NoOfAdultMales = splitData[1];
                    }
                    splitData = separated[8].split("=");
                    if(splitData.length==2) {
                        sData.NoOfAdultFemales = splitData[1];
                    }
                    splitData = separated[5].split("=");
                    if(splitData.length==2) {
                        sData.NoOfChildrenMales = splitData[1];
                    }
                    splitData = separated[1].split("=");
                    if(splitData.length==2l) {
                        sData.NoOfChildrenFemales = splitData[1];
                    }

                    recsArrayList.add(sData);

                }*/
                db.close();
                Bundle bundle=getIntent().getExtras();
                GeneralSurveyListAdapter adapter=null;
                if(bundle!=null) {
                    adapter = new GeneralSurveyListAdapter(bundle.getString("_type"),context, recsArrayList);
                }else{
                    adapter = new GeneralSurveyListAdapter("None",context, recsArrayList);
                }
                //ngoListRecyclerView.setHasFixedSize(true);
                SurveyListRecyclerView.setLayoutManager(new LinearLayoutManager(GeneralSurveyList.this));
                SurveyListRecyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            }catch (Exception ex){
                Toast.makeText(context,"Error " + ex.getLocalizedMessage(),Toast.LENGTH_LONG).show();
            }

        }else {
            paramHash.put("screenerId",Config._screenerid);
            RequestGeneralSurveyList req = new RequestGeneralSurveyList(this, paramHash);
            req.execute(MyConfig.URL_GENERALSURVEYLIST);
        }



        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                filterList(newText);


                return true;
            }
        });

    }


    private void filterList(String text) {

        ArrayList<GeneralSurveyListData> filteredList = new ArrayList<GeneralSurveyListData>();
        for (GeneralSurveyListData item: recsArrayList){
            Log.e("item", "item is: " +item.nameHead +"  \n\n");
            if (item.nameHead.toLowerCase().contains(text.toLowerCase())){
                filteredList.add(item);
            }
        }
        if (filteredList.isEmpty()){
            Toast.makeText(GeneralSurveyList.this, "No Data Found", Toast.LENGTH_SHORT).show();
        }
        else {
            adapter.setFilteredList(filteredList);
        }


    }

    class RequestGeneralSurveyList extends AsyncTask<String, Void, String>
    {
        ProgressDialog progressDialog;
        Activity activity;
        HashMap<String, String> paramsHash=null;
        RequestPipe requestPipe=new RequestPipe();
        public RequestGeneralSurveyList(Activity activity,HashMap<String, String> paramsHash) {
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

                    JSONObject recsData=jsonObject.getJSONObject("data");
                    JSONArray recsArray=recsData.getJSONArray("data");
                    int recsLen=recsArray.length();
                    for(int i=0;i<recsLen;i++)
                    {
                        try {
                            JSONObject rec = recsArray.getJSONObject(i);
                            GeneralSurveyListData sData = new GeneralSurveyListData();
                            //sData.noOfFamilyMembers="test";
                            sData.noOfFamilyMembers = rec.getString("noOfFamilyMembers");
                            sData.familyId = rec.getString("familyId");
                            sData.nameHead = rec.getString("nameHead");
                            sData.ageHead = rec.getString("ageHead");
                            sData.NoOfAdultMales = rec.getString("NoOfAdultMales");
                            sData.NoOfAdultFemales = rec.getString("NoOfAdultFemales");
                            sData.NoOfChildrenMales = rec.getString("NoOfChildrenMales");
                            sData.NoOfChildrenFemales = rec.getString("NoOfChildrenFemales");
                            JSONArray citizenArray = rec.getJSONArray("citizenId");
                            if (rec.has("cases")) {
                                if (citizenArray.length() > 0) {
                                    for (i = 0; i < citizenArray.length(); i++) {
                                        sData.CitizeID += citizenArray.getString(i) + ",";
                                    }

                                }
                            }
                            //Log.e(TAG,citizenArray.getString(0));
                            recsArrayList.add(sData);
                            // Toast.makeText(MyConfig.CONTEXT,ngoData.ngoName,Toast.LENGTH_SHORT).show();
                        }catch(Exception ee){}
                    }
                    Bundle bundle=getIntent().getExtras();

                    if(bundle!=null) {
                        adapter = new GeneralSurveyListAdapter(bundle.getString("_type"),context, recsArrayList);
                    }else{
                        adapter = new GeneralSurveyListAdapter("None",context, recsArrayList);
                    }
                    //ngoListRecyclerView.setHasFixedSize(true);
                    SurveyListRecyclerView.setLayoutManager(new LinearLayoutManager(GeneralSurveyList.this));
                    SurveyListRecyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                }
                else Toast.makeText(MyConfig.CONTEXT, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

            }catch (Exception ee){}

        }

    }
}

class GeneralSurveyListData {
    String familyId="";
    String noOfFamilyMembers="";
    String nameHead="";
    String ageHead="";
    String NoOfAdultMales="";
    String NoOfAdultFemales="";
    String NoOfChildrenMales="";
    String NoOfChildrenFemales="";
    String CitizeID="";

    public GeneralSurveyListData() {

    }
}

class GeneralSurveyListViewHolder extends RecyclerView.ViewHolder {
    TextView txfamilyId=null;
    TextView txnoOfFamilyMembers=null;
    TextView txnameHead=null;
    TextView txageHead=null;
    TextView txNoOfAdultMales=null;
    TextView txNoOfAdultFemales=null;
    TextView txNoOfChildrenMales=null;
    TextView txNoOfChildrenFemales=null;
    AppCompatButton btnSelect=null;

    public GeneralSurveyListViewHolder(View itemView){
        super(itemView);
        txfamilyId=(TextView)itemView.findViewById(R.id.familyId);
        txnoOfFamilyMembers=(TextView)itemView.findViewById(R.id.noOfFamilyMembers);
        txnameHead=(TextView)itemView.findViewById(R.id.nameHead);
        txageHead=(TextView)itemView.findViewById(R.id.ageHead);
        txNoOfAdultMales=(TextView)itemView.findViewById(R.id.NoOfAdultMales);
        txNoOfAdultFemales=(TextView)itemView.findViewById(R.id.NoOfAdultFemales);
        txNoOfChildrenMales=(TextView)itemView.findViewById(R.id.NoOfChildrenMales);
        txNoOfChildrenFemales=(TextView)itemView.findViewById(R.id.NoOfChildrenFemales);
        btnSelect=(AppCompatButton)itemView.findViewById(R.id.btnSelect);

    }
}

class GeneralSurveyListAdapter extends RecyclerView.Adapter<GeneralSurveyListViewHolder>{
    ArrayList<GeneralSurveyListData> recs=null;
    Context context;
    String _type=null;

    public GeneralSurveyListAdapter(String _type,Context context, ArrayList<GeneralSurveyListData> recs) {
        this.recs = recs;
        //allList=new ArrayList<>(recs);
        this.context = context;
        this._type=_type;
    }

    public void setFilteredList(ArrayList<GeneralSurveyListData> ListToShow){
        this.recs= ListToShow;
        notifyDataSetChanged();
    }


    @Override
    public GeneralSurveyListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.generalsurveylist_row, parent, false);
        GeneralSurveyListViewHolder viewHolder = new GeneralSurveyListViewHolder(listItem);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(GeneralSurveyListViewHolder holder, int position) {
        Log.e("Data Position",Integer.toString(position));
        final GeneralSurveyListData rec = recs.get(position);
//        holder.txfamilyId.setText(holder.txfamilyId.getText() + rec.familyId);
//        holder.txnoOfFamilyMembers.setText(holder.txnoOfFamilyMembers.getText() + rec.noOfFamilyMembers);
//        holder.txnameHead.setText(holder.txnameHead.getText() + rec.nameHead);
//        holder.txageHead.setText(holder.txageHead.getText() + rec.ageHead);
//        holder.txNoOfAdultMales.setText(holder.txNoOfAdultMales.getText() + rec.NoOfAdultMales);
//        holder.txNoOfAdultFemales.setText(holder.txNoOfAdultFemales.getText() + rec.NoOfAdultFemales);
//        holder.txNoOfChildrenMales.setText(holder.txNoOfChildrenMales.getText() + rec.NoOfChildrenMales);
//        holder.txNoOfChildrenFemales.setText(holder.txNoOfChildrenFemales.getText() + rec.NoOfChildrenFemales);




        holder.txfamilyId.setText(rec.familyId);
        holder.txnoOfFamilyMembers.setText(rec.noOfFamilyMembers);
        holder.txnameHead.setText(rec.nameHead);
        holder.txageHead.setText(rec.ageHead);
        holder.txNoOfAdultMales.setText(rec.NoOfAdultMales);
        holder.txNoOfAdultFemales.setText(rec.NoOfAdultFemales);
        holder.txNoOfChildrenMales.setText(rec.NoOfChildrenMales);
        holder.txNoOfChildrenFemales.setText(rec.NoOfChildrenFemales);






        holder.btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.e("CitizenId",rec.CitizeID);
                Config.tmp_citizenId_survey=rec.CitizeID;
                Config.tmp_familyid=rec.familyId;
                android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(context
                );
                alertDialog.setMessage("Family ID " +rec.familyId+ " Selected");
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Intent i;
                        if(_type.equalsIgnoreCase("Socioeconomic")) {
//                            i = new Intent(context, SocioEconomic.class);
                            i = new Intent(context, AllSurveyActivity.class);
                            CitizenListSurvey.THISACTIVITY.finish();
                            context.startActivity(i);

                            Shared_Preferences.setPrefs(context, "SS", "3");
                        }
                        else if(_type.equalsIgnoreCase("Health")){
//                            i = new Intent(context, GeneralSurvey.class);
                            i = new Intent(context, AllSurveyActivity.class);
                            CitizenListSurvey.THISACTIVITY.finish();
                            context.startActivity(i);

                            Shared_Preferences.setPrefs(context, "GS", "2");



                        }

                    }
                });
                alertDialog.create();
                alertDialog.show();

            }
        });
    }

    @Override
    public int getItemCount() {
        //Log.e("Data Count",Integer.toString(recs.size()));
        return recs.size();
    }

    public void onBackPressed() {
        Log.d("CDA", "onBackPressed Called");
        Intent i;
        if(_type.equalsIgnoreCase("Socioeconomic")) {
            i = new Intent(context, SocioEconomic.class);
            context.startActivity(i);
        }
        else if(_type.equalsIgnoreCase("Health")){
            i = new Intent(context, HealthSurvey.class);
            context.startActivity(i);
        }
    }

}
