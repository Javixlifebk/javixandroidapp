package com.sumago.latestjavix.Adapters;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sumago.latestjavix.Model.SettingsModel;
import com.sumago.latestjavix.R;

import java.util.ArrayList;


public class PromotionAdapter extends RecyclerView.Adapter<PromotionAdapter.ViewHolder>{

    Context context;
    private ArrayList<SettingsModel> settingsModelArrayList;


    public PromotionAdapter(Context context, ArrayList<SettingsModel> settingsModelArrayList) {
        this.context = context;
        this.settingsModelArrayList = settingsModelArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_promotion,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        Toast.makeText(context, Integer.toString(position), Toast.LENGTH_LONG).show();
        //holder.iv_logo.setImageResource(settingsModelArrayList.get(position).getIv_logo());
        //holder.tv_settings.setText(settingsModelArrayList.get(position).getTv_settings());
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
