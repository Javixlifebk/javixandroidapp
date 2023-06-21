package com.sumago.latestjavix.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.sumago.latestjavix.AddCitizenActivity;
import com.sumago.latestjavix.AllSurveyActivity;
import com.sumago.latestjavix.CitizenListActivity;
import com.sumago.latestjavix.DirectoryActivity;
import com.sumago.latestjavix.InstantExam;
import com.sumago.latestjavix.IssueListActivity;
import com.sumago.latestjavix.Model.SettingsModel;
import com.sumago.latestjavix.OfflineDataSync;
import com.sumago.latestjavix.R;
import com.sumago.latestjavix.SurveyContinuousForm;
import com.sumago.latestjavix.SurveyMenuActivity;

import java.util.ArrayList;


public class SevikaAdapter extends RecyclerView.Adapter<SevikaAdapter.ViewHolder>{

    Context context;
    private ArrayList<SettingsModel> settingsModelArrayList;

    public SevikaAdapter(Context context, ArrayList<SettingsModel> settingsModelArrayList) {
        this.context = context;
        this.settingsModelArrayList = settingsModelArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home,parent,false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.iv_logo.setImageResource(settingsModelArrayList.get(position).getIv_logo());
        holder.tv_settings.setText(settingsModelArrayList.get(position).getTv_settings());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context,Integer.toString(position),Toast.LENGTH_LONG).show();
                Intent i;
                switch (position){
                    case 0:
                        i = new Intent(context, AddCitizenActivity.class);
                        context.startActivity(i);
                        break;
                    case 1:
                        i = new Intent(context, CitizenListActivity.class);
                        context.startActivity(i);
                        break;
                    case 2:
                        i = new Intent(context, InstantExam.class);
                        context.startActivity(i);
                        break;
                    case 3:
                        i = new Intent(context, IssueListActivity.class);
                        context.startActivity(i);
                        break;
                    case 4:
                        i = new Intent(context, DirectoryActivity.class);
                        context.startActivity(i);
                        break;
                    case 5:
//                        i = new Intent(context, SurveyMenuActivity.class);
//                        i = new Intent(context, SurveyContinuousForm.class);
                        i = new Intent(context, AllSurveyActivity.class);
                        context.startActivity(i);
                        break;
                    case 6:
                        i = new Intent(context, OfflineDataSync.class);
                        context.startActivity(i);
                        break;

                    default:

                }
                notifyDataSetChanged();

            }
        });
    }

    @Override
    public int getItemCount() {
        return settingsModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_logo;
        TextView tv_settings;

        public ViewHolder(View itemView) {

            super(itemView);

            iv_logo=itemView.findViewById(R.id.iv_logo);
            tv_settings=itemView.findViewById(R.id.tv_settings);


        }
    }
}
