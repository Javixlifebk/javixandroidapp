package com.sumago.latestjavix;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.sumago.latestjavix.Util.Config;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;


public class QuestActivity extends AppCompatActivity {
    RecyclerView recyclerView=null;
    QuestListAdapter questListAdapter=null;
    // befor calling QuestACtivity kindly set balnk into LEVEL TEXT and LEVEL=1
    static int LEVEL=0;
    static String LEVEL_TEXT_1="";
    static String LEVEL_TEXT_2="";
    static String LEVEL_TEXT_3="";
    static String LEVEL_TEXT_4="";
    static String LEVEL_TMP="";
    /* selected Data Array */
    static Boolean isBackPressed=false;
    static Hashtable<String,Hashtable> levesHash=new Hashtable<String,Hashtable>();
    static HashMap<String,String> finalHash=new HashMap<>();
    static Button btnFinal,btnBack;
  /*
  submitQuestionary
  */
  public void submitQuestionary(View view) {
    System.out.println(" JSON DATA="+parseHashtoJSONString());
      finalHash.put("caseId", Config.tmp_caseId);
      finalHash.put("abdomenpain",parseHashtoJSONString());
      finalHash.put("ngoId", Config.NGO_ID);

      RequestUpdate reqUpdate=new RequestUpdate(view,finalHash);
      reqUpdate.execute(MyConfig.URL_DETAILSCREENING);
     // addNotification();

  }
    static class RequestUpdate extends AsyncTask<String, Void, String>{
        ProgressDialog progressDialog;
        View activity;
        HashMap<String, String> paramsHash=null;
        RequestPipe requestPipe=new RequestPipe();
        public RequestUpdate(View activity,HashMap<String, String> paramsHash) {
            this.activity=activity;
            this.paramsHash=paramsHash;
            progressDialog = new ProgressDialog(activity.getContext());
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
                    ///Intent i=new Intent(MyConfig.CONTEXT,TestListActivity.class);
                    //MyConfig.CONTEXT.startActivity(i);
                }
                else Toast.makeText(MyConfig.CONTEXT, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

            }catch (Exception ee){Toast.makeText(MyConfig.CONTEXT, "Exp-90:"+ee, Toast.LENGTH_SHORT).show();}

        }

    }
 private String parseHashtoJSONString()
 {
     StringBuffer stringBuffer=new StringBuffer();
     Enumeration<String> iL1=QuestActivity.levesHash.keys();
     stringBuffer.append("{");
     int P1=0;
     int P2=0;
     int P3=0;
     while(iL1.hasMoreElements())
     {
         String keyL1=iL1.nextElement();
         if(P1>0)
         stringBuffer.append(",\""+keyL1+"\": {");
         else
         stringBuffer.append("\""+keyL1+"\": {");
         Hashtable<String,Hashtable> layer2Hash=QuestActivity.levesHash.get(keyL1);
         if(layer2Hash!=null)
         {   P2=0;
             Enumeration<String> iL2=layer2Hash.keys();
             while(iL2.hasMoreElements()) {
                 String keyL2=iL2.nextElement();
                 if(P2>0)
                 stringBuffer.append(",\""+keyL2+"\": [");
                 else
                 stringBuffer.append("\""+keyL2+"\": [");
                 Hashtable<String,Hashtable> layer3Hash=layer2Hash.get(keyL2);
                 if(layer3Hash!=null){
                     Enumeration<String> iL3=layer3Hash.keys();
                     P3=0;
                     while(iL3.hasMoreElements()) {
                         String keyL3=iL3.nextElement();
                         if(P3>0)
                         stringBuffer.append(",\""+keyL3+"\"");
                         else
                         stringBuffer.append("\""+keyL3+"\"");
                          P3=1;
                     } //iL3

                 }// if L3Hash
                 stringBuffer.append("]");
                 P2=1;
             } // il2

         }// if L2Hash

         stringBuffer.append("}");
         P1=1;
     }//iL1
     stringBuffer.append("}");

     // all data are in stringBuffer variable
     try{
         System.out.println(stringBuffer.toString());
         System.out.println("----------------------");
         JSONObject jsonObject=new JSONObject(stringBuffer.toString());
         return(jsonObject.toString());
     }catch (Exception ee){System.out.println(ee);}
     return(null);
 }
    /* end selected data array */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_screening_final);
        MyConfig.CONTEXT=getApplicationContext();
        recyclerView=(RecyclerView)findViewById(R.id.detailedScreeningFinalListRecyclerView);
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

        btnFinal=(Button)findViewById(R.id.btnFinal);
        btnBack=(Button)findViewById(R.id.btnBack);
        //bundle=getIntent().getExtras();
        btnFinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finalHash.put("abdomenpain",parseHashtoJSONString());
                RequestUpdate reqUpdate=new RequestUpdate(view,finalHash);
                reqUpdate.execute(MyConfig.URL_DETAILSCREENING);
                Intent i=new Intent(getApplicationContext(),TestListActivity.class);
                startActivity(i);
                //MyConfig.CONTEXT.startActivity(i);
                //addNotification();

                //Toast.makeText(getApplicationContext(),Integer.toString(LEVEL),Toast.LENGTH_LONG).show();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(QuestActivity.LEVEL<=1){QuestActivity.LEVEL=0;}
                else {
                    QuestActivity.LEVEL=QuestActivity.LEVEL-2;
                    btnBack.setVisibility(View.GONE);
                    btnFinal.setVisibility(View.GONE);
                    questListAdapter.onMyClick(null);
                }
                //QuestActivity.LEVEL=0;
                //questListAdapter.onMyClick(null);
                //Toast.makeText(getApplicationContext(),Integer.toString(LEVEL),Toast.LENGTH_LONG).show();
            }
        });
    }
    @Override
    public void onBackPressed() {
        QuestActivity.isBackPressed=true;
        if(QuestActivity.LEVEL<=1){super.onBackPressed();QuestActivity.LEVEL=0;}
        else
        {
            QuestActivity.LEVEL=QuestActivity.LEVEL-1;
            questListAdapter.onMyClick(null);
            QuestActivity.isBackPressed=false;
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
                    Toast.makeText(MyConfig.CONTEXT, "GOT:"+jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
                        Log.e("", "json data : " +recObj.getString("text"));
                        String text=recObj.getString("text");
                        JSONObject info=recObj.getJSONObject("child");
                        keys=info.keys();
                        ArrayList<QuestionaryY> questArray=new ArrayList<QuestionaryY>();
                        while(keys.hasNext()){
                            Object key=keys.next();
                            String keyX=key.toString();
                            Log.e("", "Test values : " +keyX);
                            JSONArray recsArray1=info.getJSONArray(keyX);
                            ArrayList<String> hintList=new ArrayList<String>();
                            for(int ia=0;ia<recsArray1.length();ia++) {
                                String keyN=recsArray1.getString(ia);
                                hintList.add(keyN);
                                Log.e("", "Test values-1 : " + keyN);
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

                // End Test level 0
                    Hashtable<String, ArrayList<QuestionaryY>> hashtable = QuestionaryX.quest;
                    Enumeration<String> en = hashtable.keys();
                    ArrayList<QuestData> recsArrayList = new ArrayList<QuestData>();
                    while (en.hasMoreElements()) {
                        String keyName = en.nextElement().toString();
                        QuestData q = new QuestData();
                        q.quest = keyName;
                        recsArrayList.add(q);
                    }
                    QuestListAdapter adapter = new QuestListAdapter(recsArrayList,QuestActivity.this);
                    //ngoListRecyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(QuestActivity.this));
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    QuestActivity.LEVEL=1;
            }catch (Exception ee){}

        }

    }
}


class QuestData {
    String quest="";
    public QuestData() {
    }
}

class QuestViewHolder extends RecyclerView.ViewHolder {
    TextView textQuest=null;
    public QuestViewHolder(View itemView)
    {
        super(itemView);
        textQuest=(TextView)itemView.findViewById(R.id.questlist_row_txtquest);
    }
}

class QuestListAdapter extends RecyclerView.Adapter<QuestViewHolder> {
    ArrayList<QuestData> recs=null;
    QuestActivity questActivity=null;
    public QuestListAdapter(ArrayList<QuestData> recs,QuestActivity questActivity) {
        this.recs = recs;
        this.questActivity=questActivity;
        this.questActivity.questListAdapter=this;
    }

    @Override
    public QuestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.questlist_row, parent, false);
        QuestViewHolder viewHolder = new QuestViewHolder(listItem);
        return viewHolder;
    }
     public void onMyClick(View v) {
        /* if selected then unselect and dont call Next Items */



        String data="";
        if(v!=null) {
            TextView textView = (TextView) v;
            data = textView.getText().toString();
            if(v.getTag()!=null && v.getTag().toString().equals("selected"))
            {   if(QuestActivity.LEVEL==1)
                {
                    if(QuestActivity.levesHash.get(data)==null)
                    {
                        v.setBackgroundResource(R.drawable.textviewbutton);
                        unselectedItem(v);
                        return;
                    }
                }
               else if(QuestActivity.LEVEL==2)
                {
                    Hashtable<String,Hashtable> level2Hash=QuestActivity.levesHash.get(QuestActivity.LEVEL_TEXT_1);
                    System.out.println("YOU CKLICK ON SELECTED Level 2 == "+QuestActivity.LEVEL_TEXT_1+"=>"+data);
                    if(level2Hash.get(data)==null)
                    {
                        v.setBackgroundResource(R.drawable.textviewbutton);
                        unselectedItem(v);
                        return;
                    }
                }
                else if(QuestActivity.LEVEL==3)
                {
                    Hashtable<String,Hashtable> level2Hash=QuestActivity.levesHash.get(QuestActivity.LEVEL_TEXT_1);
                    Hashtable<String,Hashtable> level3Hash=level2Hash.get(QuestActivity.LEVEL_TEXT_2);
                    level3Hash.remove(data);
                    if(level3Hash.isEmpty()==true) {
                        level2Hash.remove(QuestActivity.LEVEL_TEXT_2);

                    }
                    v.setBackgroundResource(R.drawable.textviewbutton);
                    unselectedItem(v);
                    return;
                }

            }
        }
        else
        {   QuestActivity.LEVEL=QuestActivity.LEVEL-1;
            if(QuestActivity.LEVEL==1) {data= QuestActivity.LEVEL_TEXT_1;}
            else  if(QuestActivity.LEVEL==2) {data=QuestActivity.LEVEL_TEXT_2;}
        }
         if(QuestActivity.LEVEL==0)
         {
             //Toast.makeText(MyConfig.CONTEXT,"ZERO",Toast.LENGTH_SHORT).show();
             Hashtable<String, ArrayList<QuestionaryY>> hashtable = QuestionaryX.quest;
             Enumeration<String> en = hashtable.keys();
             ArrayList<QuestData> recsArrayList = new ArrayList<QuestData>();
             while (en.hasMoreElements()) {
                 String keyName = en.nextElement().toString();
                 QuestData q = new QuestData();
                 q.quest = keyName;
                 recsArrayList.add(q);
             }
             QuestListAdapter adapter = new QuestListAdapter(recsArrayList,questActivity);
             //ngoListRecyclerView.setHasFixedSize(true);
             questActivity.recyclerView.setLayoutManager(new LinearLayoutManager(questActivity));
             questActivity.recyclerView.setAdapter(adapter);
             QuestActivity.LEVEL=1;
             adapter.notifyDataSetChanged();

         }
       else  if(QuestActivity.LEVEL==1) {
            QuestActivity.LEVEL_TEXT_1=data;
                if(QuestActivity.isBackPressed==false) {
                    Hashtable<String, Hashtable> level2Hash = null;
                    if (QuestActivity.levesHash.get(data) == null) {
                        level2Hash = new Hashtable<String, Hashtable>();
                    } else level2Hash = QuestActivity.levesHash.get(data);
                    QuestActivity.levesHash.put(data, level2Hash);
                }
            Hashtable<String, ArrayList<QuestionaryY>> hashtable = QuestionaryX.quest;
            Enumeration<String> en = hashtable.keys();
            ArrayList<QuestData> recsArrayList = new ArrayList<QuestData>();
            while (en.hasMoreElements()) {
                String keyName = en.nextElement().toString();
                if(keyName.equals(QuestActivity.LEVEL_TEXT_1))
                {
                    ArrayList<QuestionaryY> arrayList=QuestionaryX.quest.get(keyName);
                    int size=arrayList.size();
                    for(int ii=0;ii<size;ii++)
                    {
                        QuestionaryY q1=arrayList.get(ii);
                        String questText1=q1.childText;
                        QuestData q = new QuestData();
                        q.quest = questText1;
                        recsArrayList.add(q);
                    }
                }

            }
            QuestListAdapter adapter = new QuestListAdapter(recsArrayList,questActivity);
            //ngoListRecyclerView.setHasFixedSize(true);
            questActivity.recyclerView.setLayoutManager(new LinearLayoutManager(questActivity));
            questActivity.recyclerView.setAdapter(adapter);
            QuestActivity.LEVEL=2;
             adapter.notifyDataSetChanged();

         }
        else if(QuestActivity.LEVEL==2) {
            QuestActivity.LEVEL_TEXT_2=data;
             if(QuestActivity.isBackPressed==false) {
                 Hashtable<String, Hashtable> level2Hash = QuestActivity.levesHash.get(QuestActivity.LEVEL_TEXT_1);
                 Hashtable<String, String> level3Hash = level2Hash.get(QuestActivity.LEVEL_TEXT_2);

                 if (level3Hash == null) { System.out.println(" LEVEL3HASH NOt Found => ");
                     level3Hash = new Hashtable<String, String>();
                 }
                 level2Hash.put(data, level3Hash);

             }

             Hashtable<String, ArrayList<QuestionaryY>> hashtable = QuestionaryX.quest;
             Enumeration<String> en = hashtable.keys();
             ArrayList<QuestData> recsArrayList = new ArrayList<QuestData>();
             while (en.hasMoreElements()) {
                 String keyName = en.nextElement().toString();
                 if(keyName.equals(QuestActivity.LEVEL_TEXT_1))
                 {
                     ArrayList<QuestionaryY> arrayList=QuestionaryX.quest.get(keyName);
                     int size=arrayList.size();
                     for(int ii=0;ii<size;ii++)
                     {
                         QuestionaryY q1=arrayList.get(ii);
                         String questText1=q1.childText;
                            if(questText1.equals(QuestActivity.LEVEL_TEXT_2))
                            {
                                ArrayList<String> arrayList1=q1.hintList;
                                int size1=arrayList1.size();
                                for(int iii=0;iii<size1;iii++)
                                {
                                    QuestData q = new QuestData();
                                    q.quest =arrayList1.get(iii);
                                    recsArrayList.add(q);
                                }
                            }

                     }
                 }

             }
             QuestListAdapter adapter = new QuestListAdapter(recsArrayList,questActivity);
             //ngoListRecyclerView.setHasFixedSize(true);
             questActivity.recyclerView.setLayoutManager(new LinearLayoutManager(questActivity));
             questActivity.recyclerView.setAdapter(adapter);
             QuestActivity.LEVEL=3;
             adapter.notifyDataSetChanged();

         }
        else if(QuestActivity.LEVEL==3)
         {

             QuestActivity.LEVEL_TEXT_3=data;

                 Hashtable<String, Hashtable> level2Hash = QuestActivity.levesHash.get(QuestActivity.LEVEL_TEXT_1);
                 Hashtable<String, String> level3Hash = level2Hash.get(QuestActivity.LEVEL_TEXT_2);
                 try {
                     level3Hash.put(data, data);
                     System.out.println("CHECKD VALUE+" + data);
                     selectedItem(v);
                 }catch (Exception wee){}

         }

    }
    // new Selection 22-May-2021
    public void selectedItem(View v)
    {   if(v==null) return;
        v.setBackgroundResource(R.drawable.textviewselectedbutton);
        v.setTag("selected");
        ((TextView)v).setTextColor(Color.WHITE);

    }
    public void unselectedItem(View v)
    {
        if(v==null) return;
        v.setBackgroundResource(R.drawable.textviewbutton);
        v.setTag("unselected");
        ((TextView)v).setTextColor(Color.BLACK);

    }
    // end new Selection 22-May-2021
    @Override
    public void onBindViewHolder(QuestViewHolder holder, int position) {
        final QuestData rec = recs.get(position);
        holder.textQuest.setText(rec.quest);
        // new Selection 22-May-2021

        if(QuestActivity.LEVEL==1)
        {
            Enumeration<String> ie=QuestActivity.levesHash.keys();
            while(ie.hasMoreElements())
            {
                String key=ie.nextElement();
                if(rec.quest.equals(key))
                    selectedItem(holder.textQuest);
            }

        }
        else if(QuestActivity.LEVEL==2 )
        {
            Hashtable<String,Hashtable> lavel2Hash=QuestActivity.levesHash.get(QuestActivity.LEVEL_TEXT_1);

            if(lavel2Hash!=null) {
                Enumeration<String> ie = lavel2Hash.keys();
                while (ie.hasMoreElements()) {
                    String key = ie.nextElement();
                    if (rec.quest.equals(key))
                        selectedItem(holder.textQuest);
                }
            }
        }
        else if(QuestActivity.LEVEL==3)
        {
            Hashtable<String,Hashtable> lavel2Hash=QuestActivity.levesHash.get(QuestActivity.LEVEL_TEXT_1);
            Hashtable<String,Hashtable> lavel3Hash=lavel2Hash.get(QuestActivity.LEVEL_TEXT_2);
            if(lavel3Hash!=null) {
                Enumeration<String> ie = lavel3Hash.keys();
                while (ie.hasMoreElements()) {
                    String key = ie.nextElement();
                    if (rec.quest.equals(key))
                        selectedItem(holder.textQuest);
                }
            }
            QuestActivity.finalHash.put("caseId", Config.tmp_caseId);
            QuestActivity.btnFinal.setVisibility(View.VISIBLE);
            QuestActivity.btnBack.setVisibility(View.VISIBLE);
        }

        // end new Selection 22-May-2021
        holder.textQuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onMyClick(v);
            }
        });

    }


    @Override
    public int getItemCount() {
        return recs.size();
    }
}
