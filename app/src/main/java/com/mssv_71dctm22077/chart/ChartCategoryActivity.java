package com.mssv_71dctm22077.chart;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

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


public class ChartCategoryActivity extends AppCompatActivity {
  MyDatabaseHelper mb;
  PieChart pieChart;
  BarChart barChart;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_chart_category);

    mb = new MyDatabaseHelper(this);
    pieChart = findViewById(R.id.pieChart1);
    barChart = findViewById(R.id.barChart2);

    setupPieChart(pieChart);
    setupBarChart(barChart);

    ArrayList<PieEntry> entries = new ArrayList<>();
    Cursor cursor = mb.getProductCountByCategory();

    if (cursor != null) {
      while (cursor.moveToNext()) {
        String categoryName = cursor.getString(0); // Tên danh mục
        int productCount = cursor.getInt(1); // Số lượng sản phẩm
        entries.add(new PieEntry(productCount, categoryName));
      }
      cursor.close();
    }

    PieDataSet dataSet = new PieDataSet(entries, "");
    dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
    dataSet.setValueTextSize(12f); // Set text size for value

    PieData data = new PieData(dataSet);

    pieChart.setData(data);
    pieChart.invalidate(); // Refresh the chart

    ArrayList<BarEntry> entries1 = new ArrayList<>();
    ArrayList<String> labels = new ArrayList<>();
    Cursor cursor1 = mb.getProductCountByCategory();

    if (cursor1 != null) {
      int index = 0;
      while (cursor1.moveToNext()) {
        String categoryName = cursor1.getString(0); // Tên danh mục
        int productCount = cursor1.getInt(1); // Số lượng sản phẩm

        Log.d("ChartData", "Category: " + categoryName + ", Count: " + productCount); // Log dữ liệu

        entries1.add(new BarEntry(index, productCount));
        labels.add(categoryName);
        index++;
      }
      cursor1.close();
    }
    BarDataSet dataSet1 = new BarDataSet(entries1, "Sản phẩm theo từng danh mục");
    dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
    dataSet.setValueTextSize(12f);
    BarData data1 = new BarData(dataSet1);
    setupChartData(barChart, data1, labels);
  }

  private void setupPieChart(PieChart pieChart) {
    pieChart.setUsePercentValues(true);
    pieChart.getDescription().setEnabled(false);
    pieChart.setDrawHoleEnabled(true);
    pieChart.setHoleColor(android.R.color.transparent);
    pieChart.setTransparentCircleRadius(61f);

    // Thiết lập kích thước chữ cho các nhãn và văn bản trung tâm
    pieChart.setEntryLabelTextSize(12f);  // Kích thước chữ cho nhãn của các phần trong biểu đồ
    pieChart.setCenterTextSize(16f);  // Kích thước chữ cho văn bản trung tâm
    pieChart.setCenterText("Sản phẩm theo từng danh mục");  // Văn bản trung tâm của biểu đồ

    // Thiết lập các thuộc tính khác nếu cần
    pieChart.getLegend().setEnabled(false); // Tắt/ bật phần legend nếu cần
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
