package com.sumago.latestjavix;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.sumago.latestjavix.Adapters.GeneralSurveyListTwoAdapter;
import com.sumago.latestjavix.Model.GeneralSurveyListTwoModel;
import com.sumago.latestjavix.Util.Config;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class GeneralSurveyListTwo extends AppCompatActivity {
    RecyclerView SurveyListRecyclerView;
    private static final String TAG = "_msg";
    Context context;
    static Activity THISACTIVITY=null;

    private SearchView searchView;

    ArrayList<GeneralSurveyListTwoModel> recsArrayList=new ArrayList<GeneralSurveyListTwoModel>();
    GeneralSurveyListTwoAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_survey_list_two);

        searchView = findViewById(R.id.searchView);
        searchView.clearFocus();

        MyConfig.CONTEXT=getApplicationContext();
        context=this;
        CitizenListSurvey.THISACTIVITY=this;
        SurveyListRecyclerView = (RecyclerView) findViewById(R.id.SurveyListRecyclerView);
        HashMap<String,String> paramHash=new HashMap<String,String>();
        paramHash.put("token","dfjkhsdfaksjfh3756237");
        paramHash.put("ngoId", Config.NGO_ID);






//        if(Config.isOffline){
//            try {
//                ArrayList<GeneralSurveyListData> recsArrayList=new ArrayList<GeneralSurveyListData>();
//                SQLiteDatabase db;
//                db = openOrCreateDatabase("javixlife", Context.MODE_PRIVATE, null);
//                //Cursor c = db.rawQuery("SELECT * FROM javix_update where _status=0 and service_type='General Survey' order by insert_date desc", null);
//                Cursor c = db.rawQuery("SELECT * FROM javix_update where _status=0  order by insert_date desc", null);
//
//                if (c.getCount() == 0) {
//                    Toast.makeText(this, "No records found in local system", Toast.LENGTH_SHORT).show();
//                }
//                StringBuffer buffer = new StringBuffer();
//                int i = 1;
//                String jsonStr="{\"abc\":\"1\"}";
//                JSONObject rec;
//                while (c.moveToNext()) {
//                    try {
//                        jsonStr=c.getString(4);
//                        rec= new JSONObject(jsonStr);
//                        GeneralSurveyListData sData=new GeneralSurveyListData();
//                        sData.noOfFamilyMembers=rec.getString("noOfFamilyMembers");
//                        sData.familyId=rec.getString("familyId");
//                        sData.nameHead=rec.getString("nameHead");
//                        sData.ageHead=rec.getString("ageHead");
//                        sData.NoOfAdultMales=rec.getString("NoOfAdultMales");
//                        sData.NoOfAdultFemales=rec.getString("NoOfAdultFemales");
//                        sData.NoOfChildrenMales=rec.getString("NoOfChildrenMales");
//                        sData.NoOfChildrenFemales=rec.getString("NoOfChildrenFemales");
//                        recsArrayList.add(sData);
//
//                    } catch (Exception ex) {
//                        Toast.makeText(getApplicationContext(),"jSON eRROR Message :-"  +ex.getLocalizedMessage(),Toast.LENGTH_LONG).show();
//                    }
//
//                }
//
//                db.close();
//                Bundle bundle=getIntent().getExtras();
//                GeneralSurveyListAdapter adapter=null;
//                if(bundle!=null) {
//                    adapter = new GeneralSurveyListAdapter(bundle.getString("_type"),context, recsArrayList);
//                }else{
//                    adapter = new GeneralSurveyListAdapter("None",context, recsArrayList);
//                }
//                //ngoListRecyclerView.setHasFixedSize(true);
//                SurveyListRecyclerView.setLayoutManager(new LinearLayoutManager(GeneralSurveyListTwo.this));
//                SurveyListRecyclerView.setAdapter(adapter);
//                adapter.notifyDataSetChanged();
//
//            }catch (Exception ex){
//                Toast.makeText(context,"Error " + ex.getLocalizedMessage(),Toast.LENGTH_LONG).show();
//            }
//
//        }else
//        {
            paramHash.put("screenerId",Config._screenerid);
            GeneralSurveyListTwo.RequestGeneralSurveyList req = new GeneralSurveyListTwo.RequestGeneralSurveyList(this, paramHash);

            req.execute(MyConfig.URL_GENERALSURVEYLIST);
//        }



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

        ArrayList<GeneralSurveyListTwoModel> filteredList = new ArrayList<GeneralSurveyListTwoModel>();
        for (GeneralSurveyListTwoModel item: recsArrayList){
            Log.e("item", "item is: " +item.getNameHead() +"  \n\n");
            if (item.getNameHead().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(item);
            }
        }
        if (filteredList.isEmpty()){
            Toast.makeText(GeneralSurveyListTwo.this, "No Data Found", Toast.LENGTH_SHORT).show();
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
//                            GeneralSurveyListData sData = new GeneralSurveyListData();
//                            //sData.noOfFamilyMembers="test";
//                            sData.noOfFamilyMembers = rec.getString("noOfFamilyMembers");
//                            sData.familyId = rec.getString("familyId");
//                            sData.nameHead = rec.getString("nameHead");
//                            sData.ageHead = rec.getString("ageHead");
//                            sData.NoOfAdultMales = rec.getString("NoOfAdultMales");
//                            sData.NoOfAdultFemales = rec.getString("NoOfAdultFemales");
//                            sData.NoOfChildrenMales = rec.getString("NoOfChildrenMales");
//                            sData.NoOfChildrenFemales = rec.getString("NoOfChildrenFemales");

                            recsArrayList.add(new GeneralSurveyListTwoModel(rec));


//                            JSONArray citizenArray = rec.getJSONArray("citizenId");
//                            if (rec.has("cases")) {
//                                if (citizenArray.length() > 0) {
//                                    for (i = 0; i < citizenArray.length(); i++) {
//                                        sData.CitizeID += citizenArray.getString(i) + ",";
//                                    }
//
//                                }
//                            }




                            //Log.e(TAG,citizenArray.getString(0));
                        //    recsArrayList.add(sData);
                            // Toast.makeText(MyConfig.CONTEXT,ngoData.ngoName,Toast.LENGTH_SHORT).show();
                        }catch(Exception ee){}
                    }
                    Bundle bundle=getIntent().getExtras();

                    if(bundle!=null) {
                        adapter = new GeneralSurveyListTwoAdapter(recsArrayList,context, bundle.getString("_type"));
                    }else{
                        adapter = new GeneralSurveyListTwoAdapter(recsArrayList,context, "None");
                    }
                    //ngoListRecyclerView.setHasFixedSize(true);
                    SurveyListRecyclerView.setLayoutManager(new LinearLayoutManager(GeneralSurveyListTwo.this));
                    SurveyListRecyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                }
                else Toast.makeText(MyConfig.CONTEXT, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

            }catch (Exception ee){}

        }

    }

}