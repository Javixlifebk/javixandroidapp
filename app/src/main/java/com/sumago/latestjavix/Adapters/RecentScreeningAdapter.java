package com.sumago.latestjavix.Adapters;

import android.content.Intent;
import android.widget.ArrayAdapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sumago.latestjavix.R;
import com.sumago.latestjavix.RecentScreeningActivity;

import java.util.ArrayList;
public class RecentScreeningAdapter extends BaseAdapter {
    String [] caseid,cdate,cstatus,cname;
    Context context;


    private static LayoutInflater inflater=null;

    public RecentScreeningAdapter(RecentScreeningActivity mainActivity, String[] caseidList, String[] cdateList, String[] cstatusList, String[] cnameList) {
        // TODO Auto-generated constructor stub
        caseid=caseidList;
        cdate=cdateList;
        cstatus=cstatusList;
        cname=cnameList;
        context=mainActivity;

        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return caseid.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder
    {
        TextView tv,txtCaseid,txtDate,textStatus,txtName;
        ImageView img;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.recent_screener_list, null);

        holder.txtCaseid=(TextView) rowView.findViewById(R.id.txtCaseid);
        holder.txtDate=(TextView) rowView.findViewById(R.id.txtDate);
        holder.textStatus=(TextView) rowView.findViewById(R.id.textStatus);
        holder.txtName=(TextView) rowView.findViewById(R.id.txtName);

        //holder.tv=(TextView) rowView.findViewById(R.id.textView1);
        holder.img=(ImageView) rowView.findViewById(R.id.imageView1);

       // holder.tv.setText(result[position]);

        holder.txtCaseid.setText(caseid[position]);
        holder.txtDate.setText(cdate[position]);
        holder.textStatus.setText(cstatus[position]);
        holder.txtName.setText(cname[position]);

        rowView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub


                /*if(position==0) {

                    context.startActivity(new Intent(context, SearchConsumer.class));

                }
                else if(position==1) {

                    context.startActivity(new Intent(context, BillGenerateStep1.class));



                }
                else   if(position==2) {
                    context.startActivity(new Intent(context, SearchBill.class));
                }
                else   if(position==3) {
                    context.startActivity(new Intent(context, Receipt.class));
                }
                else   if(position==4) {
                    context.startActivity(new Intent(context, Modification.class));
                }
                else   if(position==5) {
                    context.startActivity(new Intent(context, Complain.class));
                }
                else   if(position==6) {
                    context.startActivity(new Intent(context, NewConnection.class));
                }
                else   if(position==7) {
                    context.startActivity(new Intent(context, MisssingActivity.class));
                }
                else   if(position==8) {
                    context.startActivity(new Intent(context, ActivityMeterChange.class));
                }
                else   if(position==9) {
                    context.startActivity(new Intent(context, ListBill.class));
                }*/
                // Toast.makeText(context, "You Clicked "+result[position], Toast.LENGTH_LONG).show();
            }
        });
        return rowView;
    }
}
