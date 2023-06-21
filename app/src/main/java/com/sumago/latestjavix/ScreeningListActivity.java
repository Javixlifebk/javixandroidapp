package com.sumago.latestjavix;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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

public class ScreeningListActivity extends AppCompatActivity {
    RecyclerView citizenListRecyclerView;
    Context context;
    private static final String TAG = "_msg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screening_list);
        MyConfig.CONTEXT = getApplicationContext();
        context = this;
        citizenListRecyclerView = (RecyclerView) findViewById(R.id.ScreeningListRecyclerView);
        HashMap<String, String> paramHash = new HashMap<String, String>();
        // paramHash.put("citizenId","161315136114567577");
        paramHash.put("status", "1");
        paramHash.put("ngoId", Config.NGO_ID);



        RequestScreeningList req = new RequestScreeningList(this, paramHash);
        req.execute(MyConfig.URL_LIST_CASE);
    }


    class RequestScreeningList extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        Activity activity;
        HashMap<String, String> paramsHash = null;
        RequestPipe requestPipe = new RequestPipe();

        public RequestScreeningList(Activity activity, HashMap<String, String> paramsHash) {
            this.activity = activity;
            this.paramsHash = paramsHash;
            progressDialog = new ProgressDialog(activity);
            progressDialog.setMessage("loading");
        }

        @Override
        protected void onPreExecute() {
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            return requestPipe.requestForm(params[0], paramsHash);
        }

        protected void onProgressUpdate(Void... progress) {
            super.onProgressUpdate(progress);
            //ProgressDialog.show(MainActivity.this, "loading", "wait...", true, true);

        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.cancel();
            //Toast.makeText(MyConfig.CONTEXT,"RES:"+result,Toast.LENGTH_SHORT).show();
            try {
                JSONObject jsonObject = new JSONObject(result);
                int respStatus = jsonObject.getInt("status");
                if (respStatus == 1) {
                    Toast.makeText(MyConfig.CONTEXT, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    ArrayList<ScreeningData> recsArrayList = new ArrayList<ScreeningData>();
                    JSONObject recsData = jsonObject.getJSONObject("data");
                    JSONArray recsArray = recsData.getJSONArray("data");
                    int recsLen = recsArray.length();

                    for (int i = 0; i < recsLen; i++) {
                        JSONObject rec = recsArray.getJSONObject(i);

                        ScreeningData cdata = new ScreeningData();
                        cdata.citizenid = rec.getString("citizenId");
                        cdata.caseid = rec.getString("caseId");
                        cdata.height = rec.getString("height");
                        cdata.weight = rec.getString("weight");
                        cdata.bmi = rec.getString("bmi");
                        cdata.bpsys = rec.getString("bpsys");
                        cdata.bpdia = rec.getString("bpdia");
                        cdata.spo2 = rec.getString("spo2");
                        cdata.pulse = rec.getString("pulse");
                        cdata.respiratory_rate = rec.getString("respiratory_rate");
                        cdata.temperature = rec.getString("temperature");
                        recsArrayList.add(cdata);
                    }

                    ScreeningListAdapter adapter = new ScreeningListAdapter(context, recsArrayList);
                    //ngoListRecyclerView.setHasFixedSize(true);
                    citizenListRecyclerView.setLayoutManager(new LinearLayoutManager(ScreeningListActivity.this));
                    citizenListRecyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                } else
                    Toast.makeText(MyConfig.CONTEXT, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

            } catch (Exception ee) {
            }

        }

    }

    static class RequestUpdate extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        View activity;
        HashMap<String, String> paramsHash = null;
        RequestPipe requestPipe = new RequestPipe();

        public RequestUpdate(View activity, HashMap<String, String> paramsHash) {
            this.activity = activity;
            this.paramsHash = paramsHash;
            progressDialog = new ProgressDialog(activity.getContext());
            progressDialog.setMessage("loading");
        }

        @Override
        protected void onPreExecute() {
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            return requestPipe.requestForm(params[0], paramsHash);
        }

        protected void onProgressUpdate(Void... progress) {
            super.onProgressUpdate(progress);
            //ProgressDialog.show(MainActivity.this, "loading", "wait...", true, true);

        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.cancel();
            //Toast.makeText(MyConfig.CONTEXT,"RES:"+result,Toast.LENGTH_SHORT).show();
            try {
                JSONObject jsonObject = new JSONObject(result);
                int respStatus = jsonObject.getInt("status");
                if (respStatus == 1) {
                    Toast.makeText(MyConfig.CONTEXT, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                } else
                    Toast.makeText(MyConfig.CONTEXT, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

            } catch (Exception ee) {
                Toast.makeText(MyConfig.CONTEXT, "Exp-90:" + ee, Toast.LENGTH_SHORT).show();
            }

        }

    }
}

class ScreeningData {
    String citizenid = "";
    String caseid = "";
    String height = "";
    String weight = "";
    String bmi = "";
    String bpsys = "";
    String bpdia = "";
    String spo2 = "";
    String pulse = "";
    String respiratory_rate = "";
    String temperature = "";

    public ScreeningData() {

    }
}

class ScreeningListHolder extends RecyclerView.ViewHolder {
    TextView txCaseid = null;
    TextView txHeight = null;
    TextView txWeight = null;
    TextView txBmi = null;
    TextView txBpsys = null;
    TextView txBpdia = null;
    TextView txSpo2 = null;
    TextView txPulse = null;
    TextView txRes = null;
    TextView txTemp = null;
    AppCompatButton btnAdd = null;

    public ScreeningListHolder(View itemView) {
        super(itemView);
        txCaseid = (TextView) itemView.findViewById(R.id.txCaseid);
        txHeight = (TextView) itemView.findViewById(R.id.txHeight);
        txWeight = (TextView) itemView.findViewById(R.id.txWeight);
        txBmi = (TextView) itemView.findViewById(R.id.txBmi);
        txBpsys = (TextView) itemView.findViewById(R.id.txBpsys);
        txBpdia = (TextView) itemView.findViewById(R.id.txBpdia);
        txSpo2 = (TextView) itemView.findViewById(R.id.txSpo2);
        txPulse = (TextView) itemView.findViewById(R.id.txPulse);
        txRes = (TextView) itemView.findViewById(R.id.txRes);
        txTemp = (TextView) itemView.findViewById(R.id.txTemp);
        btnAdd = (AppCompatButton) itemView.findViewById(R.id.btnadd);
    }
}

class ScreeningListAdapter extends RecyclerView.Adapter<ScreeningListHolder> {
    ArrayList<ScreeningData> recs = null;
    private static final String TAG = "_msg";
    Context context;

    public ScreeningListAdapter(Context context, ArrayList<ScreeningData> recs) {
        this.recs = recs;
        this.context = context;
    }

    @Override
    public ScreeningListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.screeninglist_row, parent, false);
        ScreeningListHolder viewHolder = new ScreeningListHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ScreeningListHolder holder, int position) {
        final ScreeningData rec = recs.get(position);
        holder.txCaseid.setText(holder.txCaseid.getText() + rec.caseid);
        holder.txHeight.setText(holder.txHeight.getText() + rec.height);
        holder.txWeight.setText(holder.txWeight.getText() + rec.weight);
        holder.txBmi.setText(holder.txBmi.getText() + rec.bmi);
        holder.txBpsys.setText(holder.txBpsys.getText() + rec.bpsys);
        holder.txBpdia.setText(holder.txBpdia.getText() + rec.bpdia);
        holder.txSpo2.setText(holder.txSpo2.getText() + rec.spo2);
        holder.txPulse.setText(holder.txPulse.getText() + rec.pulse);
        holder.txRes.setText(holder.txRes.getText() + rec.respiratory_rate);
        holder.txTemp.setText(holder.txTemp.getText() + rec.temperature);
        Log.e(TAG, "parameters : " + holder.txTemp.getText());
        final ScreeningListHolder finalHolder = holder;
        holder.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(holder.btnAdd.getContext()
                );
                alertDialog.setMessage("Are your sure want to pick case !");
                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                        HashMap<String, String> paramHash = new HashMap<String, String>();
                        paramHash.put("caseId", rec.caseid);
                        paramHash.put("status", "2");
                        paramHash.put("doctorId", Config._doctorid);
                        paramHash.put("ngoId", Config.NGO_ID);
                        ScreeningListActivity.RequestUpdate reqUpdate = new ScreeningListActivity.RequestUpdate(finalHolder.txPulse, paramHash);
                        reqUpdate.execute(MyConfig.URL_PICK_CASE);

                        dialog.dismiss();
                    }
                });

                alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Do nothing
                        dialog.dismiss();
                    }
                });

                alertDialog.create();
                alertDialog.show();
                /*Intent  i = new Intent(context, ScreeningActivity.class);
                i.putExtra("CitizenId",holder.txCaseid.getText().toString());
                i.putExtra("ScreenerId",rec.screenerid);
                context.startActivity(i);*/
                //Toast.makeText(MyConfig.CONTEXT, holder.textViewNgoId.getText().toString(), Toast.LENGTH_SHORT).show();


            }
        });
    }

    @Override
    public int getItemCount() {
        return recs.size();
    }
}

