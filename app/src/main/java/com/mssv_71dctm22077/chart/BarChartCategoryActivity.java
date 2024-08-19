package com.mssv_71dctm22077.chart;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.mssv_71dctm22077.R;
import com.mssv_71dctm22077.sqlite.MyDatabaseHelper;

import java.util.ArrayList;

public class BarChartCategoryActivity extends AppCompatActivity {
  MyDatabaseHelper mb;
  BarChart barChart;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_bar_chart_category);

    mb = new MyDatabaseHelper(this);
    barChart = findViewById(R.id.barChart);

    setupBarChart(barChart);

    // Toolbar
    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    // Handle navigation icon click (back button click)
    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        finish();
      }
    });

    ArrayList<BarEntry> entries = new ArrayList<>();
    ArrayList<String> labels = new ArrayList<>();
    Cursor cursor = mb.getProductCountByCategory();

    if (cursor != null) {
      int index = 0;
      while (cursor.moveToNext()) {
        String categoryName = cursor.getString(0); // Tên danh mục
        int productCount = cursor.getInt(1); // Số lượng sản phẩm

        Log.d("ChartData", "Category: " + categoryName + ", Count: " + productCount); // Log dữ liệu

        entries.add(new BarEntry(index, productCount));
        labels.add(categoryName);
        index++;
      }
      cursor.close();
    }

    BarDataSet dataSet = new BarDataSet(entries, "Sản phẩm theo từng danh mục");
    dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
    dataSet.setValueTextSize(12f);

    BarData data = new BarData(dataSet);

    // Đặt data cho từng biểu đồ cột và thiết lập IndexAxisValueFormatter cho trục X
    setupChartData(barChart, data, labels);

  }
  private void setupBarChart(BarChart barChart) {
    barChart.getDescription().setEnabled(false);
    barChart.setFitBars(true);
    barChart.getXAxis().setDrawGridLines(false);
    barChart.getAxisLeft().setDrawGridLines(false);
    barChart.getAxisRight().setDrawGridLines(false);

    // Thiết lập kích thước chữ cho các nhãn và giá trị
    barChart.getXAxis().setTextSize(12f);
    barChart.getAxisLeft().setTextSize(12f);
    barChart.getAxisRight().setTextSize(12f);

    // Tạo và thiết lập ValueFormatter để hiển thị số nguyên trên trục Y
    ValueFormatter integerValueFormatter = new ValueFormatter() {
      @Override
      public String getFormattedValue(float value) {
        return String.valueOf((int) value);
      }
    };

    barChart.getAxisLeft().setValueFormatter(integerValueFormatter);
    barChart.getAxisRight().setValueFormatter(integerValueFormatter);
  }

  private void setupChartData(BarChart barChart, BarData data, ArrayList<String> labels) {
    barChart.setData(data);
    barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
    barChart.invalidate(); // Refresh the chart
  }
}
