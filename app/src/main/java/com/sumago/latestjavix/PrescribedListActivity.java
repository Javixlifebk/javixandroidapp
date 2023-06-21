package com.sumago.latestjavix;

import static com.sumago.latestjavix.MyConfig.URL_PRESCRIBED_LIST;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sumago.latestjavix.Util.Config;

import org.json.JSONArray;
import org.json.JSONException;
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

public class PrescribedListActivity extends AppCompatActivity {

    RecyclerView rec_showPrescribedList;

    ArrayList<NewPrescribedModel> List = new ArrayList<NewPrescribedModel>();
    PrescribeAdapter prescribeAdapter;
    Context context;
    private SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescribed_list);
        rec_showPrescribedList = findViewById(R.id.rec_showPrescribedList);
        MyConfig.CONTEXT = getApplicationContext();
        searchView = findViewById(R.id.PrescribedSearchView);
        searchView.clearFocus();


        rec_showPrescribedList.removeAllViews();
        HashMap<String, String> ParamHash = new HashMap<String, String>();
//        ParamHash.put("token", "dfjkhsdfaksjfh3756237");
        ParamHash.put("ngoId", Config.NGO_ID);
//        ParamHash.put("doctorId", Config.javixid);
        ParamHash.put("status", "3");


        Config.hidePick = 1;
        Config.hideAdd = 1;
        Config.hideView = 1;

        RequestPrescribedList prescribedList = new RequestPrescribedList(this, ParamHash);
        Log.d("ParamHash", "onCreate: "+ParamHash);
//        prescribedList.execute(MyConfig.URL_PRESCRIBED_LIST);
//        prescribedList.execute("http://192.168.1.10:3010/api/citizen/CitizenPrescribe");
//        prescribedList.execute("http://143.244.136.145:3010/api/citizen/CitizenPrescribe");
        prescribedList.execute(URL_PRESCRIBED_LIST);


        searching();
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


        ArrayList<NewPrescribedModel> filteredList = new ArrayList<NewPrescribedModel>();
        for (NewPrescribedModel item : List) {
            Log.e("item", "item is: " + item._name + "  \n\n");
            String name = item._name;

            if (item._name.toLowerCase().contains(text.toLowerCase()) || item.caseid.toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        if (filteredList.isEmpty()) {
            //Toast.makeText(PrescribedListActivity.this, "No Data Found", Toast.LENGTH_SHORT).show();
            prescribeAdapter.setFilteredList(filteredList);
        } else {
            prescribeAdapter.setFilteredList(filteredList);
        }


    }

    class RequestPrescribedList extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        Activity activity;
        HashMap<String, String> paramHash = null;
        RequestPipe requestPipe = new RequestPipe();

        public RequestPrescribedList(Activity activity, HashMap<String, String> paramHash) {
            this.activity = activity;
            this.paramHash = paramHash;

            progressDialog = new ProgressDialog(PrescribedListActivity.this);
            progressDialog.setMessage("Loading...");
        }

        @Override
        protected void onPreExecute() {
            progressDialog.show();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }


        @Override
        protected String doInBackground(String... params) {
            return requestPipe.requestForm(params[0], paramHash);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.cancel();

            try {

                JSONObject object = new JSONObject(result);
                int respStatus = object.getInt("status");
//                ArrayList<PrescribedModel> List = new ArrayList<PrescribedModel>();

                if (respStatus == 1) {

                    JSONObject jsonObject = object.getJSONObject("data");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    int recslen = jsonArray.length();

                    for (int i = 0; i < recslen; i++) {

                        try {
                            JSONObject rec = jsonArray.getJSONObject(i);
                            NewPrescribedModel cdata = new NewPrescribedModel();


                            if (rec.has("height")) {

                                cdata.height = rec.getString("height");

                            }

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

                            if (rec.has("arm")) {
                                cdata.strArm = rec.getString("arm");
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

                            if (rec.has("caseId")) {
                                cdata.caseid = rec.getString("caseId");
                            }
                            if (rec.has("screenerId")) {
                                cdata.screenerId = rec.getString("screenerId");
                            }

                            if (rec.has("citizenId")) {
                                cdata.citizenid = rec.getString("citizenId");
                            }

                            if (rec.has("createdAt")) {
                                cdata.createdAt = rec.getString("createdAt");
                            }


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





                            JSONArray citizenArray = rec.getJSONArray("citizens");

                            JSONObject citizenObject = citizenArray.getJSONObject(0);
                            if (citizenObject.has("firstName") || citizenObject.has("lastName")) {

                                cdata._name = citizenObject.getString("firstName") + " " + citizenObject.getString("lastName");
                                cdata._sex = citizenObject.getString("sex");


                            }


//                            cdata.citizenid = rec.getString("citizenId");
//                            cdata.screenerid = rec.getString("screenerId");
////                            cdata._sex = rec.getString("sex");
//                            //cdata._name = rec.getString("fullname");
//                            cdata.createdAt = rec.getString("createdAt");
//
////                            cdata.caseid = rec.getString("caseId");
////                            cdata._status = rec.getString("status");
////
//                            JSONObject casesObject = rec.getJSONObject("cases");
//                            Log.e("Response", "casesResponse: " + casesObject);
////
//                            if (casesObject.has("height")) {
//
//                                cdata.height = casesObject.getString("height");
//
//                            }
//
//                            if (casesObject.has("height")) {
//
//                                cdata.height = casesObject.getString("height");
//
//                            }
//
//                            if (casesObject.has("weight")) {
//                                cdata.weight = casesObject.getString("weight");
//                            }
//
//                            if (casesObject.has("bmi")) {
//                                cdata.bmi = casesObject.getString("bmi");
//                            }
//
//                            if (casesObject.has("bpsys")) {
//                                cdata.bpsys = casesObject.getString("bpsys");
//                            }
//
//                            if (casesObject.has("bpdia")) {
//                                cdata.bpdia = casesObject.getString("bpdia");
//                            }
//
//                            if (casesObject.has("arm")) {
//                                cdata.strArm = casesObject.getString("arm");
//                            }
//                            if (casesObject.has("spo2")) {
//                                cdata.spo2 = casesObject.getString("spo2");
//                            }
//
//                                if (casesObject.has("pulse")) {
//                                cdata.pulse = casesObject.getString("pulse");
//                            }
//
//                            if (casesObject.has("respiratory_rate")) {
//                                cdata.respiratory_rate = casesObject.getString("respiratory_rate");
//                            }
//
//                            if (casesObject.has("temperature")) {
//                                cdata.temperature = casesObject.getString("temperature");
//                            }
//
//                            if (casesObject.has("caseId")) {
//                                cdata.caseid = casesObject.getString("caseId");
//                            }


                            List.add(cdata);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                    //Toast.makeText(activity, "Size of list is2: " + List.size(), Toast.LENGTH_SHORT).show();

                    prescribeAdapter = new PrescribeAdapter(List, PrescribedListActivity.this);
                    rec_showPrescribedList.setLayoutManager(new LinearLayoutManager(PrescribedListActivity.this));
                    rec_showPrescribedList.setAdapter(prescribeAdapter);
                    rec_showPrescribedList.setHasFixedSize(true);
                    prescribeAdapter.notifyDataSetChanged();

//                    PrescribeAdapter prescribeAdapter = new PrescribeAdapter(List, context);
//                    rec_showPrescribedList.setLayoutManager(new LinearLayoutManager(PrescribedListActivity.this, RecyclerView.VERTICAL, false));
//                    rec_showPrescribedList.setAdapter(prescribeAdapter);
//                    Toast.makeText(activity, "Run", Toast.LENGTH_SHORT).show();

//                    Toast.makeText(activity, "Size of list is: " + List.size(), Toast.LENGTH_SHORT).show();
//
                }

            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }
}


class NewPrescribedModel {

    String citizenid = "";
    String screenerId = "";
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

    String createdAt = "";

    int cBP;
    int cSPO2;
    int cTMP;
    int cPULS;
    int cBMI;
    int cRESP;
    int cOVERALL;

    public NewPrescribedModel() {
    }


}


class PrescribedHolder extends RecyclerView.ViewHolder {
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

    public PrescribedHolder(@NonNull View itemView) {
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


class PrescribeAdapter extends RecyclerView.Adapter<PrescribedHolder> {

    private static final String TAG = "_msg";
    ArrayList<NewPrescribedModel> recs = null;
    Context context;

    int _sys = 0, _dia = 0, _spo2 = 0, _pulse = 0, _resp = 0;
    float _bmi = (float) 0.0;
    float _temp = (float) 0.0;
    String[] COLORCODE = {"#4C8A06", "#FFEF7407", "#E40C0C"};


    public PrescribeAdapter(ArrayList<NewPrescribedModel> recs, Context context) {
        this.recs = recs;
        this.context = context;
    }

    public void setFilteredList(ArrayList<NewPrescribedModel> ListToShow) {
        this.recs = ListToShow;
        notifyDataSetChanged();


    }

    @NonNull
    @Override
    public PrescribedHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
//        View listItem= layoutInflater.inflate(R.layout.screeningpicked_row, parent, false);
//        PrescribedHolder viewHolder = new PrescribedHolder(listItem);


        return new PrescribedHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.screeningpicked_row, parent, false));
    }


    @Override
    public void onBindViewHolder(@NonNull PrescribedHolder holder, int position) {

        final NewPrescribedModel rec = recs.get(position);


//        holder.txCaseid.setText(holder.txCaseid.getText() + rec.caseid);
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
        holder.txSex.setText(holder.txSex.getText() + rec._sex);
        holder.txDate.setText(holder.txDate.getText() + rec.createdAt);
        holder.txCaseid.setText(holder.txCaseid.getText() + rec.caseid);


        Log.e("Response", "btn_view: " + Config.hideView + " btn_pick: " + Config.hidePick + " btn_add: " + Config.hideAdd);
        if (Config.hideView == 1 || Config.hidePick == 1 || Config.hideAdd == 1) {
            //holder.btnAdd.setVisibility(View.GONE);
            // holder.btnView.setVisibility(View.GONE);
            holder.btnPick.setVisibility(View.GONE);
        }


//        if (rec._sex.equalsIgnoreCase("Male")) {
//            holder.txSex.setText("");
//            holder.txSex.setBackgroundResource(R.drawable.ic_male_18dp);
//        } else if (rec._sex.equalsIgnoreCase("Female")) {
//            holder.txSex.setText("");
//            holder.txSex.setBackgroundResource(R.drawable.ic_female_18dp);
//        }


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


        holder.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(holder.btnAdd.getContext()
                );
                alertDialog.setMessage("Are you sure want to prescribe !");
                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(context, AddPrescriptionActivity.class);
                        Log.e(TAG, "parameters : " + rec.screenerId);
                        i.putExtra("CitizenId", rec.citizenid);
                        i.putExtra("ScreenerId", rec.screenerId);
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
//                RequestBody body = RequestBody.create(mediaType, "caseId=" + rec.caseid  +"&isUnrefer=" + 2 + "&ngoId=" + Config.NGO_ID);
                RequestBody body = RequestBody.create(mediaType, "caseId=" + rec.caseid + "&ngoId=" + Config.NGO_ID);
                Log.e("Response", "CaseId: " + rec.caseid);
                String url = "http://143.244.136.145:3010/api/report/createPrescriptionReport";
//                String url = "http://192.168.1.195:3010/api/report/createPrescriptionReport";
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

                            Log.e("Response", "jsonObject: " + jsonObject);

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


        holder.setIsRecyclable(false);

    }

    @Override
    public int getItemCount() {
        return recs.size();
    }


}