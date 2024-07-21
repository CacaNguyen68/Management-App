package com.mssv_71dctm22077.chart;

import android.database.Cursor;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import com.github.mikephil.charting.charts.PieChart;

import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.mssv_71dctm22077.R;
import com.mssv_71dctm22077.sqlite.MyDatabaseHelper;

import java.util.ArrayList;


public class ChartCategoryActivity extends AppCompatActivity {
  MyDatabaseHelper mb;
  PieChart pieChart;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_chart_category);

    mb = new MyDatabaseHelper(this);
    pieChart = findViewById(R.id.pieChart1);
    setupPieChart(pieChart);
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
    PieData data = new PieData(dataSet);

    pieChart.setData(data);
    pieChart.invalidate(); // Refresh the chart
  }

  private void setupPieChart(PieChart pieChart) {
    pieChart.setUsePercentValues(true);
    pieChart.getDescription().setEnabled(false);
    pieChart.setDrawHoleEnabled(true);
    pieChart.setHoleColor(android.R.color.transparent);
    pieChart.setTransparentCircleRadius(61f);

    // Thiết lập kích thước chữ cho các nhãn và văn bản trung tâm
    pieChart.setEntryLabelTextSize(12f);  // Kích thước chữ cho nhãn của các phần trong biểu đồ
    pieChart.setCenterTextSize(12f);  // Kích thước chữ cho văn bản trung tâm
    pieChart.setCenterText("Sản phẩm theo từng danh mục");  // Văn bản trung tâm của biểu đồ
  }
}
