package com.sumago.latestjavix;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sumago.latestjavix.Util.Config;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ScreeningPickedActivity extends AppCompatActivity {
    RecyclerView citizenListRecyclerView;
    Context context;
    private static final String TAG = "_msg";
    Spinner spnActor;
    Button btnSearch;
    private SearchView searchView;
    ScreeningPickedAdapter adapter;
    ArrayList<ScreeningPickedData> recsArrayList = new ArrayList<ScreeningPickedData>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screening_picked);
        citizenListRecyclerView = (RecyclerView) findViewById(R.id.ScreeningListRecyclerView);
        MyConfig.CONTEXT = getApplicationContext();
        context = this;
        spnActor = (Spinner) findViewById(R.id.spnActor);
        btnSearch = (Button) findViewById(R.id.btnSearch);
        ArrayAdapter<CharSequence> adp1 = ArrayAdapter.createFromResource(this, R.array.strCitizenSearch, android.R.layout.simple_spinner_item);
        adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnActor.setAdapter(adp1);

        searchView = findViewById(R.id.PrescribedSearchView);
        searchView.clearFocus();
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!spnActor.getSelectedItem().toString().equalsIgnoreCase("Please Select")) {
                    switch (spnActor.getSelectedItem().toString()) {
                        case "All":
                            if (!recsArrayList.isEmpty()) {
                                recsArrayList.clear();
                            }
                            iniViews(2, "1");

                            break;
                        case "Case Picked":
                            if (!recsArrayList.isEmpty()) {
                                recsArrayList.clear();
                            }
                            iniViews(2, "2");
                            break;
                        case "Prescribed":
                            if (!recsArrayList.isEmpty()) {
                                recsArrayList.clear();
                            }
                            iniViews(2, "3");
                            break;
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Please Select Option", Toast.LENGTH_LONG).show();
                }
            }
        });

        searching();

    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        Config.recent_investigation = 0;
    }

    @Override
    protected void onStop() {
        super.onStop();
        Config.recent_investigation = 0;
    }

    private void searching() {

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


        ArrayList<ScreeningPickedData> filteredList = new ArrayList<ScreeningPickedData>();
        for (ScreeningPickedData item : recsArrayList) {
            Log.e("item", "item is: " + item._name + "  \n\n");
            String name = item._name;

            if (item._name.toLowerCase().contains(text.toLowerCase()) || item.caseid.toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        if (filteredList.isEmpty()) {
            //Toast.makeText(ScreeningPickedActivity.this, "No Data Found", Toast.LENGTH_LONG).show();
            adapter.setFilteredList(filteredList);
        } else {
            adapter.setFilteredList(filteredList);
        }

    }

    public void iniViews(Integer strRole, String searchData) {

        citizenListRecyclerView.removeAllViews();
        HashMap<String, String> paramHash = new HashMap<String, String>();
        //paramHash.put("citizenId",Config.tmp_citizenId);
        if (searchData.equalsIgnoreCase("1")) {
            //paramHash.put("citizenId",Config.tmp_citizenId);
            paramHash.put("status", "1");
            paramHash.put("ngoId", Config.NGO_ID);

        } else if (searchData.equalsIgnoreCase("3")) {
//            paramHash.put("doctorId", Config._doctorid);
//            paramHash.put("doctorId", Config.javixid);
            Log.e("Response", "Doctor_id: " + Config._doctorid);
            paramHash.put("status", "3");
            paramHash.put("ngoId", Config.NGO_ID);
        } else {
//            paramHash.put("doctorId", Config._doctorid);
            paramHash.put("doctorId", Config.javixid);
            Log.e("check", "doctorId: " + Config._doctorid);
            paramHash.put("status", searchData);
            Log.e("Response", "Doctor_id: " + Config._doctorid);
            paramHash.put("ngoId", Config.NGO_ID);

        }
        RequestScreeningPicked req = new RequestScreeningPicked(this, paramHash);
        req.execute(MyConfig.URL_LIST_CASE);
    }

    class RequestScreeningPicked extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        Activity activity;
        HashMap<String, String> paramsHash = null;
        RequestPipe requestPipe = new RequestPipe();

        public RequestScreeningPicked(Activity activity, HashMap<String, String> paramsHash) {
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
                System.out.println("\n\n\n HELLO MINNI");
                System.out.println(result);
                System.out.println("\n\n\n");
                JSONObject jsonObject = new JSONObject(result);
                int respStatus = jsonObject.getInt("status");
//                ArrayList<ScreeningPickedData> recsArrayList = new ArrayList<ScreeningPickedData>();
                if (respStatus == 1) {
                    //Toast.makeText(MyConfig.CONTEXT, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                    JSONObject recsData = jsonObject.getJSONObject("data");
                    JSONArray recsArray = recsData.getJSONArray("data");
                    int recsLen = recsArray.length();
                    Log.e("Response", "recsLen: " + recsLen);
                    if (recsLen > 100) recsLen = 100;
                    for (int i = 0; i < recsLen; i++) {
                        try {
                            JSONObject rec = recsArray.getJSONObject(i);
                            ScreeningPickedData cdata = new ScreeningPickedData();
                            cdata.citizenid = rec.getString("citizenId");
                            cdata.screenerid = rec.getString("screenerId");
                            cdata.caseid = rec.getString("caseId");
                            cdata._status = rec.getString("status");
                            Log.e("CITIZENS", "STATUS: " + cdata._status);
                            if (rec.has("height")) {
                                cdata.height = rec.getString("height");
                            }
                            if (rec.has("weight")) {
                                cdata.weight = rec.getString("weight");
                            }
                            if (rec.has("bmi")) {
                                cdata.bmi = rec.getString("bmi");
                            }
                            if (rec.has("bpsys")) {
                                cdata.bpsys = rec.getString("bpsys");
                            }
                            if (rec.has("bpdia")) {
                                cdata.bpdia = rec.getString("bpdia");
                            }
                            if (rec.has("spo2")) {
                                cdata.spo2 = rec.getString("spo2");
                            }
                            if (rec.has("pulse")) {
                                cdata.pulse = rec.getString("pulse");
                            }
                            if (rec.has("respiratory_rate")) {
                                cdata.respiratory_rate = rec.getString("respiratory_rate");
                            }
                            if (rec.has("temperature")) {
                                cdata.temperature = rec.getString("temperature");
                            }
                            if (rec.has("notes")) {
                                cdata.strNotes = rec.getString("notes");
                            }
                            if (rec.has("arm")) {
                                cdata.strArm = rec.getString("arm");
                            }
                            cdata.strDate = rec.getString("createdAt");
                            if (rec.has("severity_bp")) {
                                cdata.cBP = rec.getInt("severity_bp");
                            }
                            if (rec.has("severity_spo2")) {
                                cdata.cSPO2 = rec.getInt("severity_spo2");
                            }
                            if (rec.has("severity_temperature")) {
                                cdata.cTMP = rec.getInt("severity_temperature");
                            }
                            if (rec.has("severity_puls")) {
                                cdata.cPULS = rec.getInt("severity_puls");
                            }
                            if (rec.has("severity_bmi")) {
                                cdata.cBMI = rec.getInt("severity_bmi");
                            }
                            if (rec.has("severity_respiratory_rate")) {
                                cdata.cRESP = rec.getInt("severity_respiratory_rate");
                            }
                            if (rec.has("severity")) {
                                cdata.cOVERALL = rec.getInt("severity");
                            }

                            JSONArray arrinfo = rec.getJSONArray("citizens");

                            JSONObject info = arrinfo.getJSONObject(0);
                            //Log.e("CITIZENS", "citizen data: " + info);
                            cdata._name = info.getString("firstName") + " " + info.getString("lastName");
                            cdata._sex = info.getString("sex");
                            //
                            //
                            //System.out.println("\n\n" + cdata._name + "::" + cdata._sex);

                            recsArrayList.add(cdata);
                            Log.e("checkThis", "size of list: " + recsArrayList.size());
                        } catch (Exception _ex100) {
                        }
                    }
                    adapter = new ScreeningPickedAdapter(context, recsArrayList);
                    //ngoListRecyclerView.setHasFixedSize(true);
                    citizenListRecyclerView.setLayoutManager(new LinearLayoutManager(ScreeningPickedActivity.this));
                    citizenListRecyclerView.setAdapter(adapter);
                    citizenListRecyclerView.setHasFixedSize(true);

                    adapter.notifyDataSetChanged();
                } else {
                    ScreeningPickedAdapter adapter = new ScreeningPickedAdapter(context, recsArrayList);
                    //ngoListRecyclerView.setHasFixedSize(true);
                    citizenListRecyclerView.setLayoutManager(new LinearLayoutManager(ScreeningPickedActivity.this));
                    citizenListRecyclerView.setAdapter(adapter);
                    citizenListRecyclerView.setHasFixedSize(true);

                    adapter.notifyDataSetChanged();
                    Toast.makeText(MyConfig.CONTEXT, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                }

            } catch (Exception ee) {
            }

        }

    }
}

class ScreeningPickedData {
    String citizenid = "";
    String screenerid = "";
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
    String strArm = "";
    String strNotes = "";
    String strDate = "";
    String _status = "";
    String _name = "";
    String _sex = "";

    int cBP;
    int cSPO2;
    int cTMP;
    int cPULS;
    int cBMI;
    int cRESP;
    int cOVERALL;

    public ScreeningPickedData() {

    }
}

class ScreeningPickedHolder extends RecyclerView.ViewHolder {
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
    TextView txArm = null;
    TextView txNotes = null;
    TextView txDate = null;
    TextView txName = null;
    TextView txSex = null;
    ImageView btnAdd = null;
    ImageView btnView = null;
    AppCompatButton btnPick = null;

    public ScreeningPickedHolder(View itemView) {
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
        txArm = (TextView) itemView.findViewById(R.id.txArm);
        txNotes = (TextView) itemView.findViewById(R.id.txNotes);
        txDate = (TextView) itemView.findViewById(R.id.txDate);
        txName = (TextView) itemView.findViewById(R.id.txName);
        txSex = (TextView) itemView.findViewById(R.id.txSex);
        btnAdd = (ImageView) itemView.findViewById(R.id.btnadd);
        btnView = (ImageView) itemView.findViewById(R.id.btnView);
        btnPick = (AppCompatButton) itemView.findViewById(R.id.btnPick);
    }
}

class ScreeningPickedAdapter extends RecyclerView.Adapter<ScreeningPickedHolder> {
    ArrayList<ScreeningPickedData> recs = null;
    private static final String TAG = "_msg";
    Context context;
    int _sys = 0, _dia = 0, _spo2 = 0, _pulse = 0, _resp = 0;
    float _bmi = (float) 0.0;
    float _temp = (float) 0.0;
    String[] COLORCODE = {"#4C8A06", "#FFEF7407", "#E40C0C"};

    public void setFilteredList(ArrayList<ScreeningPickedData> ListToShow) {
        this.recs = ListToShow;
        notifyDataSetChanged();


    }


    public ScreeningPickedAdapter(Context context, ArrayList<ScreeningPickedData> recs) {
        this.recs = recs;
        this.context = context;
    }

    @Override
    public ScreeningPickedHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.screeningpicked_row, parent, false);
        ScreeningPickedHolder viewHolder = new ScreeningPickedHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ScreeningPickedHolder holder, int position) {


        final ScreeningPickedData rec = recs.get(position);

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
        holder.txDate.setText(holder.txDate.getText() + rec.strDate);
        holder.txName.setText(holder.txName.getText() + rec._name);

        if (rec._sex.equalsIgnoreCase("Male")) {
            holder.txSex.setText("");
            holder.txSex.setBackgroundResource(R.drawable.ic_male_18dp);
        } else if (rec._sex.equalsIgnoreCase("Female")) {
            holder.txSex.setText("");
            holder.txSex.setBackgroundResource(R.drawable.ic_female_18dp);
        }

        /*if(rec.strArm.length()>0 || rec.strArm.equalsIgnoreCase("null")==false) {
            holder.txArm.setText(holder.txArm.getText() + rec.strArm);
        }else{
            holder.txArm.setVisibility(View.GONE);
        }*/

        //******************///
        if (rec.strArm.length() > 0) {
            if (!rec.strArm.equalsIgnoreCase("null"))
                holder.txArm.setText(holder.txArm.getText() + rec.strArm);
        } else {
            holder.txArm.setVisibility(View.GONE);
        }
        if (rec.strNotes.length() > 0) {
            holder.txNotes.setText(holder.txNotes.getText() + rec.strNotes);
        } else {
            holder.txNotes.setVisibility(View.GONE);
        }
        Log.e(TAG, "BMI : " + holder.txBmi.getText());
        if (rec.bmi.length() > 0) {
            _bmi = Float.parseFloat(rec.bmi.toString());
        }
        if (rec.bpsys.length() > 0) {
            _sys = Integer.parseInt(rec.bpsys.toString());
        }
        if (rec.bpdia.length() > 0) {
            _dia = Integer.parseInt(rec.bpdia.toString());
        }
        if (rec.spo2.length() > 0) {
            _spo2 = Integer.parseInt(rec.spo2.toString());
        }
        if (rec.temperature.length() > 0) {
            _temp = Float.parseFloat(rec.temperature.toString());
        }
        if (rec.pulse.length() > 0) {
            _pulse = Integer.parseInt(rec.pulse.toString());
        }
        if (rec.respiratory_rate.length() > 0) {
            _resp = Integer.parseInt(rec.respiratory_rate.toString());
        }

        holder.txBpsys.setTextColor(Color.parseColor(COLORCODE[rec.cBP]));
        holder.txBpdia.setTextColor(Color.parseColor(COLORCODE[rec.cBP]));
        holder.txPulse.setTextColor(Color.parseColor(COLORCODE[rec.cPULS]));
        holder.txSpo2.setTextColor(Color.parseColor(COLORCODE[rec.cSPO2]));
        holder.txRes.setTextColor(Color.parseColor(COLORCODE[rec.cRESP]));
        holder.txBmi.setTextColor(Color.parseColor(COLORCODE[rec.cBMI]));
        holder.txTemp.setTextColor(Color.parseColor(COLORCODE[rec.cTMP]));


        //***************************//

        int _status = Integer.parseInt(rec._status);
        holder.btnAdd.setVisibility(View.VISIBLE);

        if (_status == 2) {
            holder.btnPick.setVisibility(View.GONE);
            holder.btnAdd.setVisibility(View.VISIBLE);
        } else {
            holder.btnPick.setVisibility(View.VISIBLE);
            holder.btnAdd.setVisibility(View.GONE);
            holder.btnView.setVisibility(View.GONE);
        }
        if (_status == 3) {
            holder.btnView.setVisibility(View.VISIBLE);
            holder.btnAdd.setVisibility(View.VISIBLE);
            holder.btnPick.setVisibility(View.GONE);
        } else {
            holder.btnView.setVisibility(View.GONE);
        }

        holder.txRes.setVisibility(View.GONE);
        final ScreeningPickedHolder finalHolder = holder;
        holder.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(holder.btnAdd.getContext()
                );
                alertDialog.setMessage("Are you sure want to prescribe !");
                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(context, AddPrescriptionActivity.class);
                        Log.e(TAG, "parameters : " + rec.screenerid);
                        i.putExtra("CitizenId", rec.citizenid);
                        i.putExtra("ScreenerId", rec.screenerid);
                        i.putExtra("doctorId", Config._doctorid);
                        i.putExtra("recordId", "0");
                        i.putExtra("caseId", rec.caseid);

                        Log.e("Response", "checking: " + context);
                        context.startActivity(i);
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

        holder.btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                        /*Intent i = new Intent(context, ViewPrescriptionActivity.class);
                        i.putExtra("caseId",rec.caseid);
                        context.startActivity(i);*/
                MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
//                RequestBody body = RequestBody.create(mediaType, "caseId=" + rec.caseid);
                RequestBody body = RequestBody.create(mediaType, "caseId="+ rec.caseid+"&ngoId=" +Config.NGO_ID+ "&token=dfjkhsdfaksjfh3756237");
                String url = "http://143.244.136.145:3010/api/report/createPrescriptionReport";
//                String url = "http://159.65.148.197:3001/api/report/createPrescriptionReport";
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .addHeader("content-type", "application/x-www-form-urlencoded")
                        .addHeader("cache-control", "no-cache")
                        .addHeader("postman-token", "08b43f25-b3a6-b582-35c8-25cfdad00694")
                        .build();
                System.out.println("OKAY CLIENT");
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        System.out.println("JCANCEL " + e.toString());
                        call.cancel();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        final String myResponse = response.body().string();
                        try {
                            JSONObject jsonObject = new JSONObject(myResponse);
                            if (jsonObject.getInt("status") == 1) {
                                JSONObject data = jsonObject.getJSONObject("data");
                                data = data.getJSONObject("data");
                                String fileName = data.getString("filename");
                                Intent intx = new Intent(Intent.ACTION_VIEW);
                                intx.setData(Uri.parse(fileName));
                                intx.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                MyConfig.CONTEXT.startActivity(intx);
                            }
                        } catch (Exception ee) {
                        }
                    }
                });

            }
        });


        holder.btnPick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(holder.btnAdd.getContext());
                alertDialog.setMessage("Are you sure want to pick case !");
                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                        HashMap<String, String> paramHash = new HashMap<String, String>();
                        Config.tmp_caseId = rec.caseid;
                        paramHash.put("caseId", rec.caseid);
                        paramHash.put("status", "2");
                        paramHash.put("doctorId", Config._doctorid);
                        paramHash.put("ngoId", Config.NGO_ID);
                        Log.d(TAG, "onClick: gh"+paramHash);
                        ScreeningListActivity.RequestUpdate reqUpdate = new ScreeningListActivity.RequestUpdate(finalHolder.txPulse, paramHash);
                        reqUpdate.execute(MyConfig.URL_PICK_CASE);

                        dialog.dismiss();
                        Intent i = new Intent(context, TestListActivity.class);
                        i.putExtra("CitizenId", rec.citizenid);
                        i.putExtra("ScreenerId", rec.screenerid);
                        i.putExtra("doctorId", Config._doctorid);
                        Log.e("Response", "ScreeningPickedActivity: Doctor_id: " + Config._doctorid);
                        i.putExtra("recordId", "0");
                        i.putExtra("caseId", rec.caseid);
                        i.putExtra("flag", "1");
                        Config.tmp_caseId = rec.caseid;
                        Config.tmp_citizenId = rec.citizenid;
                        Config.tmp_screenerid = rec.screenerid;
                        Config.tmp_Pid = "1";
                        Config.recent_investigation = 1;
                        //Toast.makeText(context,Config.tmp_Pid,Toast.LENGTH_LONG).show();
                        context.startActivity(i);
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

        holder.setIsRecyclable(false);

    }

    @Override
    public int getItemCount() {
        return recs.size();
    }
}