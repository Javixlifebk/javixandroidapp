package com.sumago.latestjavix;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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

public class ViewPrescriptionActivity extends AppCompatActivity {
    RecyclerView prescriptionListRecyclerView;
    Context context;
    private static final String TAG = "_msg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_prescription);
        MyConfig.CONTEXT = getApplicationContext();
        context = this;
        prescriptionListRecyclerView = (RecyclerView) findViewById(R.id.prescriptionListRecyclerView);
        Bundle bundle;
        bundle = getIntent().getExtras();
        HashMap<String, String> paramHash = new HashMap<String, String>();
        paramHash.put("caseId", bundle.getString("caseId"));
        paramHash.put("ngoId", Config.NGO_ID);

        // paramHash.put("status","2");
        RequestScreeningPicked req = new RequestScreeningPicked(this, paramHash);
        req.execute(MyConfig.URL_VIEW_PRESCRIPTION);
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
                JSONObject jsonObject = new JSONObject(result);
                int respStatus = jsonObject.getInt("status");
                if (respStatus == 1) {
                    Toast.makeText(MyConfig.CONTEXT, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    ArrayList<PrescriptiondData> recsArrayList = new ArrayList<PrescriptiondData>();
                    JSONObject recsData = jsonObject.getJSONObject("data");
                    JSONArray recsArray = recsData.getJSONArray("data");

                    int recsLen = recsArray.length();

                    for (int i = 0; i < recsLen; i++) {
                        JSONObject rec = recsArray.getJSONObject(i);
                        PrescriptiondData cdata = new PrescriptiondData();
                        cdata.caseid = rec.getString("caseId");
                        cdata.createdAt = rec.getString("createdAt");
                        cdata.comments = rec.getString("comments");
                        cdata.medicine = rec.getString("medicine");
                        cdata.tests = rec.getString("tests");
                        JSONArray is_online = rec.getJSONArray("citizens");
                        JSONObject is_loggedin = is_online.getJSONObject(0);
                        Log.e(TAG, "isonline : " + is_loggedin.getString("firstName"));
                        cdata.citizen = is_loggedin.getString("firstName") + " " + is_loggedin.getString("lastName");

                        recsArrayList.add(cdata);
                    }

                    PrescriptionAdapter adapter = new PrescriptionAdapter(context, recsArrayList);
                    //ngoListRecyclerView.setHasFixedSize(true);
                    prescriptionListRecyclerView.setLayoutManager(new LinearLayoutManager(ViewPrescriptionActivity.this));
                    prescriptionListRecyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                } else
                    Toast.makeText(MyConfig.CONTEXT, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

            } catch (Exception ee) {
            }

        }

    }
}

class PrescriptiondData {
    String citizen = "";
    String doctors = "";
    String caseid = "";
    String medicine = "";
    String tests = "";
    String comments = "";
    String createdAt = "";

    public PrescriptiondData() {

    }
}

class PrescriptionHolder extends RecyclerView.ViewHolder {
    TextView txCaseid = null;
    TextView txMedicine = null;
    TextView txComments = null;
    TextView txTests = null;
    TextView txCitizen = null;
    TextView txDoctor = null;
    TextView txDate = null;
    AppCompatButton btnDownloads = null;

    public PrescriptionHolder(View itemView) {
        super(itemView);
        txCaseid = (TextView) itemView.findViewById(R.id.txCaseid);
        txComments = (TextView) itemView.findViewById(R.id.txComments);
        txMedicine = (TextView) itemView.findViewById(R.id.txMedicine);
        txTests = (TextView) itemView.findViewById(R.id.txTests);
        txCitizen = (TextView) itemView.findViewById(R.id.txCitizen);
        txDoctor = (TextView) itemView.findViewById(R.id.txDoctor);
        txDate = (TextView) itemView.findViewById(R.id.txDate);
        btnDownloads = (AppCompatButton) itemView.findViewById(R.id.btnDownloads);
    }
}

class PrescriptionAdapter extends RecyclerView.Adapter<PrescriptionHolder> {
    ArrayList<PrescriptiondData> recs = null;
    private static final String TAG = "_msg";
    Context context;

    public PrescriptionAdapter(Context context, ArrayList<PrescriptiondData> recs) {
        this.recs = recs;
        this.context = context;
    }

    @Override
    public PrescriptionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.prescription_row, parent, false);
        PrescriptionHolder viewHolder = new PrescriptionHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(PrescriptionHolder holder, int position) {
        final PrescriptiondData rec = recs.get(position);
        holder.txCaseid.setText(holder.txCaseid.getText() + rec.caseid);
        holder.txComments.setText(holder.txComments.getText() + rec.comments);
        holder.txMedicine.setText(holder.txMedicine.getText() + rec.medicine);
        holder.txTests.setText(holder.txTests.getText() + rec.tests);
        holder.txCitizen.setText(holder.txCitizen.getText() + rec.citizen);
        holder.txDoctor.setText("Prescribed By : Dr." + Config._fname + " " + Config._lname);
        holder.txDate.setText(holder.txDate.getText() + rec.createdAt);
        holder.btnDownloads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                RequestBody body = RequestBody.create(mediaType, "caseId=" + rec.caseid);
                String url = "http://143.244.136.145:3010/api/report/createPrescriptionReport";
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

                //ScreeningViewActivity.DownloadReport.getDownload();
                //ScreeningViewActivity.
               /* Intent i = new Intent(context, ViewDetailedScreening.class);
                i.putExtra("CaseId",rec.caseid);
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