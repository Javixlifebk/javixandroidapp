package com.sumago.latestjavix;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.sumago.latestjavix.Model.CasesAdapterDoctorModel;
import com.sumago.latestjavix.Model.SevikaReferredToDrModel;
import com.sumago.latestjavix.Util.Config;
import com.sumago.latestjavix.WebService.ApiInterface;
import com.sumago.latestjavix.WebService.MyNewConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReferredListBySevikaInDrDashboard extends AppCompatActivity {

    String token = "dfjkhsdfaksjfh375623";
    RecyclerView rec_showReferredList;
    String isUnrefer = "true";
    private SearchView searchView;

    ProgressDialog progressDialog;

    ArrayList<ReferredModel> List = null;
    ArrayList<CasesAdapterDoctorModel> ListCases = null;
    ReferredAdapter sevikaReferredToDrAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_referred_list_by_sevika_in_dr_dashboard);

        List = new ArrayList<ReferredModel>();
        ListCases = new ArrayList<CasesAdapterDoctorModel>();
        progressDialog = new ProgressDialog(ReferredListBySevikaInDrDashboard.this);
        progressDialog.setMessage("loading");
        progressDialog.show();

        searchView = findViewById(R.id.ReferredSearchView);
        searchView.clearFocus();

        rec_showReferredList = findViewById(R.id.rec_showReferredList);

        Log.d("token", "onCreate: " + token);
        Log.d("isUnrefer", "onCreate: " + isUnrefer);
        Log.d("Config.NGO_ID", "onCreate: " + Config.NGO_ID);

        ApiInterface apiInterface = MyNewConfig.getRetrofit().create(ApiInterface.class);
        final Call<ResponseBody> result = (Call<ResponseBody>) apiInterface.ReferBySevikaToDr(token, isUnrefer, Config.NGO_ID);

     /*   result.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                Log.e("Respponse", "Response is success: ");

                progressDialog.dismiss();
                try {
                    if (response.isSuccessful()) {

                        String output = response.body().string();
                        Log.e("Response", "output: " + output);

                        JSONObject object = new JSONObject(output);

                        Log.e("Response", "status: " + object.getString("status"));


                        if (object.getString("status").equals("1")) {

                            Toast.makeText(ReferredListBySevikaInDrDashboard.this, "" + object.getString("message"), Toast.LENGTH_SHORT).show();

                            JSONObject jsonObject = object.getJSONObject("data");
                            Log.e("Cello", "jsonObject: " + jsonObject);
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            Log.e("Cello", "jsonArray: " + jsonArray);


//                            for (int i = 0; i <jsonArray.length(); i++){
//                                JSONObject rec = jsonArray.getJSONObject(i).getJSONObject("cases");
////
//
////                                JSONArray abcd = jsonArray.getJSONArray(i).getJSONArray(0);
//                                Log.e("Cello", "abc: " +rec);
//                            }


                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject rec = jsonArray.getJSONObject(i);
                                ReferredModel cdata = new ReferredModel();
                                cdata._id = rec.getString("_id");
                                cdata.fullname = rec.getString("fullname");
                                cdata.citizenId = rec.getString("citizenId");
                                cdata.sex = rec.getString("sex");
                                cdata.screenerId = rec.getString("screenerId");
                                cdata.screenerfullname = rec.getString("screenerfullname");
                                cdata.mobile = rec.getString("mobile");


                                //  JSONObject recTwo = jsonArray.getJSONObject(i).getJSONObject("screener");

                                //List.add(new SevikaReferredToDrModel(rec.getJSONObject("caseId")));

//
//                                JSONArray jsonArray1 = rec.getJSONArray("cases");
//                                if (jsonArray1.isNull(i)){
//                                    JSONObject object1 = rec.getJSONObject("cases");
////
////                                Log.e("Checking", "kuch to: " +object1);
//                                }
//                                Log.e("Checking", "jsonArray1: " +jsonArray1);


                                // commenting for temporary


                                JSONObject object1 = rec.getJSONObject("cases");

//                                JSONObject object1 =
                                Log.e("Checking", "object1: " + object1);

                                cdata.case_id = object1.getString("caseId");
                                Log.e("case_id", "case id is: " + object1.getString("caseId"));
//                                ListCases.add(new CasesAdapterDoctorModel(object1));


                                List.add(cdata);

//                                JSONArray screeners = rec.getJSONArray("screeners");
//                                Log.e("Response", "Screeners Array: " +screeners);
//                                JSONObject screenersObject = screeners.getJSONObject(i);
//                                Log.e("Response", "screeners object: " +screenersObject);
//                                List.add(new SevikaReferredToDrModel(screenersObject));

                            }

//                            Toast.makeText(ReferredListBySevikaInDrDashboard.this, "Size of list case: " +ListCases.size(), Toast.LENGTH_SHORT).show();


                            // commenting for temporary

*//*

                            for (int i = 0; i < ListCases.size(); i++) {

                                Log.e("Atul", "_id: " + ListCases.get(i).get_id());
//                                Log.e("Atul", "" +ListCases.toString());

                            }
*//*


                            //  Toast.makeText(ReferredListBySevikaInDrDashboard.this, "Size of list is: " +List.size(), Toast.LENGTH_SHORT).show();


                            rec_showReferredList.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
                            sevikaReferredToDrAdapter = new ReferredAdapter(ReferredListBySevikaInDrDashboard.this, List);
                            rec_showReferredList.setAdapter(sevikaReferredToDrAdapter);

//                            Toast.makeText(ReferredListBySevikaInDrDashboard.this, "Referred successfully", Toast.LENGTH_SHORT).show();
//                            Intent ii = new Intent(ReferredListBySevikaInDrDashboard.this, DoctorActivity.class);
//                            startActivity(ii);
//                            finish();
                        } else if (object.getString("status").equals("0")) {
                            Toast.makeText(ReferredListBySevikaInDrDashboard.this, "" + object.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    }


                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                Log.e("Respponse", "Response is Failure: ");
                progressDialog.dismiss();
            }
        });*/


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

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        GetDataFromServer();
    }

    private void GetDataFromServer() {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
//        RequestBody body = RequestBody.create(mediaType, "token=dfjkhsdfaksjfh375623&isUnrefer=true&ngoId=javixngo31");
        RequestBody body = RequestBody.create(mediaType, "token=dfjkhsdfaksjfh375623&isUnrefer=" + 1 + "&ngoId=" + Config.NGO_ID);
        Request request = new Request.Builder()
                .url("http://143.244.136.145:3010/api/citizen/citizenrefer")
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();
        try {
            okhttp3.Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                progressDialog.dismiss();

                String output = response.body().string();
                Log.e("Response", "output: " + output);

                JSONObject object = new JSONObject(output);

                Log.e("Response", "status: " + object.getString("status"));


                if (object.getString("status").equals("1")) {

                    Toast.makeText(ReferredListBySevikaInDrDashboard.this, "" + object.getString("message"), Toast.LENGTH_SHORT).show();

                    JSONObject jsonObject = object.getJSONObject("data");
                    Log.e("Cello", "jsonObject: " + jsonObject);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    Log.e("Cello", "jsonArray: " + jsonArray);

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject rec = jsonArray.getJSONObject(i);
                        ReferredModel cdata = new ReferredModel();
                        cdata._id = rec.getString("_id");
                        cdata.fullname = rec.getString("fullname");
                        cdata.citizenId = rec.getString("citizenId");
                        cdata.sex = rec.getString("sex");
                        cdata.screenerId = rec.getString("screenerId");
                        cdata.screenerfullname = rec.getString("screenerfullname");
                        cdata.mobile = rec.getString("mobile");


                        JSONArray jsonArray1 = rec.getJSONArray("cases");
                        if (!jsonArray1.equals("[]")) {
                            for (int j = 0; j < jsonArray1.length(); j++) {


                                JSONObject object1 = jsonArray1.getJSONObject(j);
                                Log.e("Checking", "object1: " + object1);
                                cdata.case_id = object1.getString("caseId");
                                Log.e("case_id", "case id is: " + object1.getString("caseId"));
//                                ListCases.add(new CasesAdapterDoctorModel(object1));
                                List.add(cdata);
                            }
                        }

                    }

//                            Toast.makeText(ReferredListBySevikaInDrDashboard.this, "Size of list case: " +ListCases.size(), Toast.LENGTH_SHORT).show();


                    // commenting for temporary


                    //  Toast.makeText(ReferredListBySevikaInDrDashboard.this, "Size of list is: " +List.size(), Toast.LENGTH_SHORT).show();


                    rec_showReferredList.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
                    sevikaReferredToDrAdapter = new ReferredAdapter(ReferredListBySevikaInDrDashboard.this, List);
                    rec_showReferredList.setAdapter(sevikaReferredToDrAdapter);

//                            Toast.makeText(ReferredListBySevikaInDrDashboard.this, "Referred successfully", Toast.LENGTH_SHORT).show();
//                            Intent ii = new Intent(ReferredListBySevikaInDrDashboard.this, DoctorActivity.class);
//                            startActivity(ii);
//                            finish();
                } else if (object.getString("status").equals("0")) {
                    Toast.makeText(ReferredListBySevikaInDrDashboard.this, "" + object.getString("message"), Toast.LENGTH_SHORT).show();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void filterList(String text) {

        ArrayList<ReferredModel> filteredList = new ArrayList<ReferredModel>();
        for (ReferredModel item : List) {
            Log.e("item", "item is: " + item.fullname + "  \n\n");
            String name = item.fullname;

            if (item.fullname.toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        if (filteredList.isEmpty()) {
            //Toast.makeText(ReferredListBySevikaInDrDashboard.this, "No Data Found", Toast.LENGTH_SHORT).show();
        } else {
            sevikaReferredToDrAdapter.setFilteredList(filteredList);
        }


    }


}


//class ReferredModel {
//
//
//    String _id = "";
//    String pstatus = "";
//    String isInstant = "";
//    String isUnrefer = "";
//    String citizenId = "";
//    String sex = "";
//    String email = "";
//    String javixId = "";
//    String citizenLoginId = "";
//    String aadhaar = "";
//    String screenerId = "";
//    String createdAt = "";
//    String fullname = "";
//    String screenerfullname = "";
//    String mobile = "";
//    String case_id = "";
//
//    public ReferredModel() {
//    }
//}

class ReferredHolder extends RecyclerView.ViewHolder {

    TextView tv_citizen_id, tv_name, tv_sex, tv_email, tv_screener_id, tv_mobile, tv_screener_name;
    AppCompatButton btnadd_refer = null, btn_view = null, view_all_case = null;

    public ReferredHolder(View itemView) {
        super(itemView);
        tv_citizen_id = itemView.findViewById(R.id.tv_citizen_id);
        tv_name = itemView.findViewById(R.id.tv_name);
        tv_email = itemView.findViewById(R.id.tv_email);
        tv_sex = itemView.findViewById(R.id.tv_sex);
        tv_screener_id = itemView.findViewById(R.id.tv_screener_id);
        tv_mobile = itemView.findViewById(R.id.tv_mobile);
        btnadd_refer = itemView.findViewById(R.id.btnadd_refer);
        tv_screener_name = itemView.findViewById(R.id.tv_screener_name);
        btn_view = itemView.findViewById(R.id.btn_view);

        view_all_case = itemView.findViewById(R.id.view_all_case);
    }
}


class ReferredAdapter extends RecyclerView.Adapter<ReferredHolder> {

    private static final String TAG = "_msg";
    Context context;
    ArrayList<ReferredModel> List = new ArrayList<ReferredModel>();
    ArrayList<CasesAdapterDoctorModel> ListCase = new ArrayList<CasesAdapterDoctorModel>();

    public ReferredAdapter(Context context, ArrayList<ReferredModel> list) {
        this.context = context;
        List = list;
//        ListCase = listCase;
    }

    public void setFilteredList(ArrayList<ReferredModel> ListToShow) {
        this.List = ListToShow;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ReferredHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.sevika_referred_to_dr_layout, parent, false);
        ReferredHolder viewHolder = new ReferredHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ReferredHolder holder, int position) {
        holder.tv_citizen_id.setText(List.get(position).citizenId);
        holder.tv_name.setText(List.get(position).fullname);
        holder.tv_sex.setText(List.get(position).sex);
        holder.tv_screener_id.setText(List.get(position).screenerId);
        holder.tv_screener_name.setText(List.get(position).screenerfullname);
        holder.tv_mobile.setText(List.get(position).mobile);
        Log.e("check", "Mobile No: " + List.get(position).mobile);

        holder.btn_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent ii = new Intent(context, TestListActivity.class);
                ii.putExtra("CitizenId", List.get(position).citizenId);
                ii.putExtra("ScreenerId", List.get(position).screenerId);
                ii.putExtra("doctorId", Config._doctorid);
                Log.e("Response", "ScreeningPickedActivity: Doctor_id: " + Config._doctorid);
                ii.putExtra("recordId", "0");
//                ii.putExtra("caseId", ListCase.get(position).getCaseId());
                ii.putExtra("flag", "1");


                Log.e("PutExtra", "TestListActivity- CitizenId: " + List.get(position).citizenId);
                Log.e("PutExtra", "TestListActivity- ScreenerId: " + List.get(position).screenerId);
//                Log.e("PutExtra", "TestListActivity- CaseId: " +ListCase.get(position).getCaseId());

//                        Log.e("PutExtra", "TestListActivity- CitizenId: " +List.get(position).getCitizenId());
//                        Log.e("PutExtra", "TestListActivity- CitizenId: " +List.get(position).getCitizenId());


                Config.tmp_caseId = List.get(position).case_id;
                Config.tmp_citizenId = List.get(position).citizenId;
                Config.tmp_screenerid = List.get(position).screenerId;
                Config.tmp_Pid = "1";
                //Toast.makeText(context,Config.tmp_Pid,Toast.LENGTH_LONG).show();

                Config.referredClick = 1;

                Log.e("Response", "referredClick: " + Config.referredClick);

                context.startActivity(ii);

            }
        });


        // view_all_cases is commented

        holder.view_all_case.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show();

                Intent i = new Intent(context, ScreeningViewActivity.class);
                i.putExtra("CitizenId", List.get(position).citizenId);
                i.putExtra("ScreenerId", List.get(position).case_id);

                Log.e("Response", "CitizenId: " + List.get(position).citizenId);
                Log.e("Response", "CaseId: " + List.get(position).case_id);
                context.startActivity(i);
                //Toast.makeText(MyConfig.CONTEXT, holder.textViewNgoId.getText().toString(), Toast.LENGTH_SHORT).show();


            }
        });


    }

    @Override
    public int getItemCount() {
        return List.size();
    }
}
