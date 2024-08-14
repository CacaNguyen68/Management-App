package com.mssv_71dctm22077.chart;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.mssv_71dctm22077.R;
import com.mssv_71dctm22077.sqlite.MyDatabaseHelper;

import java.util.ArrayList;


public class ChartActivity extends AppCompatActivity {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_chart);

    Button button1 = findViewById(R.id.button1);
    Button button2 = findViewById(R.id.button2);
    Button button3 = findViewById(R.id.button3);
    Button button4 = findViewById(R.id.button4);
    Button button5 = findViewById(R.id.button5);

    button1.setOnClickListener(v -> {
      Intent intent = new Intent(ChartActivity.this, BarChartCategoryActivity.class);
      startActivity(intent);
    });

    button2.setOnClickListener(v -> {
      Intent intent = new Intent(ChartActivity.this, BarChartOrderByStatusActivity.class);
      startActivity(intent);
    });

    button3.setOnClickListener(v -> {
      Intent intent = new Intent(ChartActivity.this, RevenueOrderActivity.class);
      startActivity(intent);
    });

    button4.setOnClickListener(v -> {
//      Intent intent = new Intent(ChartActivity.this, Button4Activity.class);
//      startActivity(intent);
    });

    button5.setOnClickListener(v -> {
//      Intent intent = new Intent(ChartActivity.this, Button5Activity.class);
//      startActivity(intent);
    });
  }
}
