package com.sumago.latestjavix;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.icu.text.SymbolTable;
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
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;

public class DetailedScreeningFinalActivity extends AppCompatActivity {
    RecyclerView detailedScreeningFinalListRecyclerView;
    Context context;
    private static final String TAG = "_msg";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_screening_final);
        MyConfig.CONTEXT=getApplicationContext();
        context=this;
        if(QuestionaryX.isLoaded==false) {
            HashMap<String, String> paramHash = new HashMap<String, String>();
            paramHash.put("json", "parentchild");
            paramHash.put("ngoId", Config.NGO_ID);

            RequestParentChildFinalData req = new RequestParentChildFinalData(this, paramHash);
            req.execute(MyConfig.URL_PARENTCHILD);
        }
        else {
            QuestionaryX.clearAllSelectedList();
        }
    }
    class RequestParentChildFinalData extends AsyncTask<String, Void, String>
    {
        ProgressDialog progressDialog;
        Activity activity;
        HashMap<String, String> paramsHash=null;
        RequestPipe requestPipe=new RequestPipe();
        public RequestParentChildFinalData(Activity activity,HashMap<String, String> paramsHash) {
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
            try {
                JSONObject jsonObject = new JSONObject(result);
                int respStatus=jsonObject.getInt("status");
                if(respStatus==1) {
                    Toast.makeText(MyConfig.CONTEXT, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    JSONObject recsData=jsonObject.getJSONObject("data");
                    JSONArray recsArray=recsData.getJSONArray("data");
                    int recsLen=recsArray.length();
                    JSONObject rec = recsArray.getJSONObject(0);
                    JSONArray recAin = rec.getJSONArray("What is the purpose of your visit?");
                    Iterator keys=null;
                    String childData=null;
                    for(int i=0;i<recAin.length();i++)
                    {
                        JSONObject recObj=recAin.getJSONObject(i);
                        //String text=recObj.getString("text");
                        Log.e(TAG, "json data : " +recObj.getString("text"));
                        String text=recObj.getString("text");
                        JSONObject info=recObj.getJSONObject("child");
                        keys=info.keys();
                        ArrayList<QuestionaryY> questArray=new ArrayList<QuestionaryY>();
                        while(keys.hasNext()){
                            Object key=keys.next();
                            String keyX=key.toString();
                            Log.e(TAG, "Rahul values : " +keyX);
                            JSONArray recsArray1=info.getJSONArray(keyX);
                            ArrayList<String> hintList=new ArrayList<String>();
                            for(int ia=0;ia<recsArray1.length();ia++) {
                                String keyN=recsArray1.getString(ia);
                                hintList.add(keyN);
                                Log.e(TAG, "Rahul values-1 : " + keyN);
                            }
                            QuestionaryY questionaryY=new QuestionaryY();
                            questionaryY.hintList=hintList;
                            questionaryY.childText=keyX;
                            questArray.add(questionaryY);
                        } // while
                        QuestionaryX.add(text,questArray);
                    } // for
                } // if
                else Toast.makeText(MyConfig.CONTEXT, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                //  Test filed Data

                // End Test
                Hashtable<String,ArrayList<QuestionaryY>> hashtable=QuestionaryX.quest;
                Enumeration<String> en=hashtable.keys();
                ArrayList<String> arrayList=new ArrayList<String>();
                while(en.hasMoreElements())
                {
                    String keyName=en.nextElement().toString();
                    arrayList.add(keyName);


                }

                QueryActivity.arrayListStatic=arrayList;
                Intent intent=new Intent(DetailedScreeningFinalActivity.this,QueryActivity.class);
                DetailedScreeningFinalActivity.this.startActivity(intent);

            }catch (Exception ee){}

        }

    }
}
