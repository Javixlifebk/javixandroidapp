/*
package com.sumago.latestjavix.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.sumago.latestjavix.AddPrescriptionActivity;
import com.sumago.latestjavix.Model.CasesAdapterDoctorModel;
import com.sumago.latestjavix.Model.SevikaReferredToDrModel;
import com.sumago.latestjavix.R;
import com.sumago.latestjavix.ReferredFullDetails;
import com.sumago.latestjavix.ReferredModel;
import com.sumago.latestjavix.TestListActivity;
import com.sumago.latestjavix.Util.Config;

import java.util.ArrayList;

public class SevikaReferredToDrAdapter extends RecyclerView.Adapter<SevikaReferredToDrAdapter.ViewHolder> {

    private static final String TAG = "_msg";
    Context context;
    ArrayList<ReferredModel> List = new ArrayList<ReferredModel>();
    ArrayList<CasesAdapterDoctorModel> ListCase = new ArrayList<CasesAdapterDoctorModel>();

    public SevikaReferredToDrAdapter(Context context, ArrayList<ReferredModel> list) {
        this.context = context;
        List = list;
//        ListCase = listCase;
    }

    public void setFilteredList(ArrayList<ReferredModel> ListToShow) {
        this.List = ListToShow;
        notifyDataSetChanged();
    }


    //
//    public SevikaReferredToDrAdapter(Context context, ArrayList<SevikaReferredToDrModel> list) {
//
//        this.context = context;
//        List = list;
//    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.sevika_referred_to_dr_layout, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

//
//        holder.tv_citizen_id.setText(List.get(position).getCitizenId());
//        holder.tv_name.setText(List.get(position).getFullname());
//        holder.tv_sex.setText(List.get(position).getSex());
//        holder.tv_screener_id.setText(List.get(position).getScreenerId());
//        holder.tv_screener_name.setText(List.get(position).getScreenerfullname());
//        holder.tv_mobile.setText(List.get(position).getMobile());
      //  holder.tv_name.setText(List.get(position).getFullname());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Log.e("adapter", " id: " +List.get(position).get_id());
                Log.e("adapter", " pstatus: " +List.get(position).getPstatus());
                Log.e("adapter", " is instant: " +List.get(position).getIsInstant());
                Log.e("adapter", " is unrefer: " +List.get(position).getIsUnrefer());
                Log.e("adapter", " citizen id: " +List.get(position).getCitizenId());
                Log.e("adapter", " sex: " +List.get(position).getSex());
                Log.e("adapter", "email id: " +List.get(position).getJavixId());
                Log.e("adapter", "citizer login id: " +List.get(position).getCitizenLoginId());
                Log.e("adapter", "aadhaar: " +List.get(position).getAadhaar());
                Log.e("adapter", "screener id: " +List.get(position).getScreenerId());
                Log.e("adapter", "created at: " +List.get(position).getCreatedAt());
                Log.e("adapter", "full name: " +List.get(position).getFullname());
                Log.e("adapter", "mobile: " +List.get(position).getMobile());

            }
        });

//
//        Log.e("adapter", "citizen id: " +List.get(position).getCitizenId());
//        Log.e("adapter", "name: " +List.get(position).getFullname());
//        Log.e("adapter", "sex: " +List.get(position).getSex());
//        Log.e("adapter", "screener id: " +List.get(position).getScreenerId());
//        Log.e("adapter", "mobile: " +List.get(position).getMobile());

        holder.btnadd_refer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(holder.btnadd_refer.getContext());
                alertDialog.setMessage("Are you sure want to pick & prescribe!");
                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                        Intent ii = new Intent(context, AddPrescriptionActivity.class);
                        Log.e(TAG, "parameters : " + List.get(position).getScreenerId());
//                        i.putExtra("CitizenId", rec.citizenid);
//                        i.putExtra("ScreenerId", rec.screenerid);
//                        i.putExtra("doctorId", Config._doctorid);
//                        i.putExtra("recordId", "0");
//                        i.putExtra("caseId", rec.caseid);

                        ii.putExtra("CitizenId", List.get(position).getCitizenId());
                        ii.putExtra("ScreenerId", List.get(position).getScreenerId());
                        ii.putExtra("doctorId", Config._doctorid);
                        ii.putExtra("recordId", "0");
                        ii.putExtra("caseId", ListCase.get(position).getCaseId());
                        Log.e("Response_Check", "caseId: " + ListCase.get(position).getCaseId());
                        context.startActivity(ii);
                        dialogInterface.dismiss();
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


                // Toast.makeText(context, "Button is clicked", Toast.LENGTH_SHORT).show();
            }
        });


//        holder.btn_view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Intent ii = new Intent(context, ReferredFullDetails.class);
//
//                ii.putExtra("name", List.get(position).getFirstName() + " " + List.get(position).getLastName());
//                ii.putExtra("case_id", ListCase.get(position).getCaseId());
//                ii.putExtra("screening_date", ListCase.get(position).getCreatedAt());  // not sure that it is screening date
//                ii.putExtra("height", ListCase.get(position).getHeight());
//                ii.putExtra("weight", ListCase.get(position).getWeight());
//                ii.putExtra("bmi", ListCase.get(position).getBmi());
//                ii.putExtra("bp_sys", ListCase.get(position).getBpsys());
//                ii.putExtra("bp_dia", ListCase.get(position).getBpdia());
//                ii.putExtra("spo2", ListCase.get(position).getSpo2());
//                ii.putExtra("heartRateBgm", ListCase.get(position).getSeverity_pulse()); // Heart rate bgm pending because there is not value in API
//                ii.putExtra("temperature", ListCase.get(position).getTemperature());
//                ii.putExtra("arm", ListCase.get(position).getArm());
//
//                // sending data for picking and prescribing
//
//                ii.putExtra("CitizenId", List.get(position).getCitizenId());
//                ii.putExtra("ScreenerId", List.get(position).getScreenerId());
//                ii.putExtra("doctorId", Config._doctorid);
//                ii.putExtra("recordId", "0");
//                ii.putExtra("caseId", ListCase.get(position).getCaseId());
//
//                Log.e("doctor_id_is", "doctor id is: " + Config._doctorid);
//
//                context.startActivity(ii);
//            }
//        });


        holder.btn_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent ii = new Intent(context, TestListActivity.class);
                ii.putExtra("CitizenId", List.get(position).getCitizenId());
                ii.putExtra("ScreenerId", List.get(position).getScreenerId());
                ii.putExtra("doctorId", Config._doctorid);
                Log.e("Response", "ScreeningPickedActivity: Doctor_id: " +Config._doctorid);
                ii.putExtra("recordId", "0");
//                ii.putExtra("caseId", ListCase.get(position).getCaseId());
                ii.putExtra("flag", "1");


                Log.e("PutExtra", "TestListActivity- CitizenId: " +List.get(position).getCitizenId());
                Log.e("PutExtra", "TestListActivity- ScreenerId: " +List.get(position).getScreenerId());
//                Log.e("PutExtra", "TestListActivity- CaseId: " +ListCase.get(position).getCaseId());

//                        Log.e("PutExtra", "TestListActivity- CitizenId: " +List.get(position).getCitizenId());
//                        Log.e("PutExtra", "TestListActivity- CitizenId: " +List.get(position).getCitizenId());


                Config.tmp_caseId = ListCase.get(position).getCaseId();
                Config.tmp_citizenId = List.get(position).getCitizenId();
                Config.tmp_screenerid = List.get(position).getScreenerId();
                Config.tmp_Pid = "1";
                //Toast.makeText(context,Config.tmp_Pid,Toast.LENGTH_LONG).show();
                context.startActivity(ii);

            }
        });


    }

    @Override
    public int getItemCount() {
        return List.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_citizen_id, tv_name, tv_sex, tv_email, tv_screener_id, tv_mobile, tv_screener_name;
        AppCompatButton btnadd_refer = null, btn_view = null;

        public ViewHolder(@NonNull View itemView) {
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


        }
    }
}
*/
