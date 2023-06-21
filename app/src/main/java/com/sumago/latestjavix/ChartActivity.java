package com.sumago.latestjavix;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class ChartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        BarChart barChart=findViewById(R.id.barChart);
        ArrayList<BarEntry> visitors=new ArrayList<>();
        visitors.add(new BarEntry(2014,500));
        visitors.add(new BarEntry(2015,600));
        BarDataSet barDataSet=new BarDataSet(visitors,"Visitors");
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(16f);
        BarData barData=new BarData(barDataSet);
        barChart.setFitBars(true);
        barChart.setData(barData);
        barChart.getDescription().setText("Bar Chart Example");
        barChart.animateY(2000);

        PieChart pieChart=findViewById(R.id.pieChart);
        ArrayList<PieEntry> visitorss=new ArrayList<>();
        visitorss.add(new PieEntry(2014,"500"));
        visitorss.add(new PieEntry(2015,"600"));

        PieDataSet pieDataSet=new PieDataSet(visitorss,"Visitors");
        pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        pieDataSet.setValueTextColor(Color.BLACK);
        pieDataSet.setValueTextSize(16f);
        PieData pieData=new PieData(pieDataSet);
        //pieChart.setFitBars(true);
        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(true);
        pieChart.setCenterText("Hello");
        pieChart.animate();

    }
}