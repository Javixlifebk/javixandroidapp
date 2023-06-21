package com.sumago.latestjavix;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sumago.latestjavix.Adapters.RecentScreeningAdapter;

public class RecentScreeningActivity extends AppCompatActivity implements View.OnClickListener {

    private String[] data = { "Apple", "Banana", "Orange", "Watermelon",
            "Pear", "Grape", "Pineapple", "Strawberry", "Cherry", "Mango" };


    public static String [] caseidList={"001","002","003","004","005"};
    public static String [] cdateList={"02-07-2021","02-08-2021","02-09-2021","02-10-2021","02-11-2021"};
    public static String [] cstatusList={"Pending-0","Progress-1","Competed-2","Pending-0","Competed-2"};
    public static String [] cnameList={"Rakesh Sinha","Jilani Jio","Somkiran Singh","Rahul Pandey","Late Mehta Shyam Naresh Kumar Sinha"};
    TextView txtcaseid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_screening);
        displayData();
    }


    public void displayData(){
        RecentScreeningAdapter Adp=new RecentScreeningAdapter(this, caseidList,cdateList,cstatusList,cnameList);
        ListView lv=(ListView) findViewById(R.id.list_view);
        lv.setAdapter(Adp);
        /*ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                RecentScreeningActivity.this, android.R.layout.simple_list_item_1, data);
        ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(adapter);*/
    }

    @Override
    public void onClick(View view)
    {

        txtcaseid = (TextView)findViewById(R.id.txtCaseid);
        /*switch (view.getId())
        {
            //handle multiple view click events
        }*/
        Toast.makeText(getApplicationContext(),txtcaseid.getText(),Toast.LENGTH_LONG).show();
    }

}