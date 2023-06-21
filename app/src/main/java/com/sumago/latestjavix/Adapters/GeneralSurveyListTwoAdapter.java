package com.sumago.latestjavix.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.sumago.latestjavix.Model.GeneralSurveyListTwoModel;
import com.sumago.latestjavix.R;

import java.util.ArrayList;

public class GeneralSurveyListTwoAdapter extends RecyclerView.Adapter<GeneralSurveyListTwoAdapter.ViewHolder> {
    ArrayList<GeneralSurveyListTwoModel> recs=null;
    Context context;
    String _type=null;

    public GeneralSurveyListTwoAdapter(ArrayList<GeneralSurveyListTwoModel> recs, Context context, String _type) {
        this.recs = recs;
        this.context = context;
        this._type = _type;
    }


    public void setFilteredList(ArrayList<GeneralSurveyListTwoModel> ListToShow){
        this.recs= ListToShow;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.generalsurveylist_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.txfamilyId.setText(recs.get(position).getFamilyId());
        holder.txnoOfFamilyMembers.setText(recs.get(position).getNoOfFamilyMembers());
        holder.txnameHead.setText(recs.get(position).getNameHead());
        holder.txageHead.setText(recs.get(position).getAgeHead());
        holder.txNoOfAdultMales.setText(recs.get(position).getNoOfAdultMales());
        holder.txNoOfAdultFemales.setText(recs.get(position).getNoOfAdultFemales());
        holder.txNoOfChildrenMales.setText(recs.get(position).getNoOfChildrenMales());
        holder.txNoOfChildrenFemales.setText(recs.get(position).getNoOfChildrenFemales());


    }

    @Override
    public int getItemCount() {
        return recs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txfamilyId=null;
        TextView txnoOfFamilyMembers=null;
        TextView txnameHead=null;
        TextView txageHead=null;
        TextView txNoOfAdultMales=null;
        TextView txNoOfAdultFemales=null;
        TextView txNoOfChildrenMales=null;
        TextView txNoOfChildrenFemales=null;
        AppCompatButton btnSelect=null;



        public ViewHolder(@NonNull View itemView) {
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

}
