package com.mssv_71dctm22077.chart;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.mssv_71dctm22077.R;
import com.mssv_71dctm22077.sqlite.MyDatabaseHelper;

import java.util.ArrayList;

public class BarChartOrderByStatusActivity extends AppCompatActivity {

  MyDatabaseHelper mb;
  BarChart barChart;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_bar_chart_order_by_status);

    mb = new MyDatabaseHelper(this);
    barChart = findViewById(R.id.barChart);

    setupBarChart(barChart);

    ArrayList<BarEntry> entries = new ArrayList<>();
    ArrayList<String> labels = new ArrayList<>();
    Cursor cursor = mb.getOrderCountByStatus();

    if (cursor != null) {
      int index = 0;
      while (cursor.moveToNext()) {
        String statusName = cursor.getString(0); // Tên trạng thái
        int orderCount = cursor.getInt(1); // Số lượng đơn hàng

        Log.d("ChartData", "Status: " + statusName + ", Count: " + orderCount); // Log dữ liệu

        entries.add(new BarEntry(index, orderCount));
        labels.add(statusName);
        index++;
      }
      cursor.close();
    }

    BarDataSet dataSet = new BarDataSet(entries, "Số lượng đơn theo từng trạng thái");
    dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
    dataSet.setValueTextSize(12f);

    BarData data = new BarData(dataSet);

    // Đặt data cho biểu đồ cột và thiết lập IndexAxisValueFormatter cho trục X
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

    // Thiết lập khoảng cách giữa các cột
    barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
    barChart.getXAxis().setGranularity(1f); // Đảm bảo các nhãn trục X không bị gộp lại
    barChart.getXAxis().setLabelRotationAngle(-45f); // Xoay các nhãn trục X để tránh bị chồng lên nhau
    barChart.getXAxis().setAvoidFirstLastClipping(true); // Tránh việc các nhãn bị cắt
  }

  private void setupChartData(BarChart barChart, BarData data, ArrayList<String> labels) {
    barChart.setData(data);
    barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
    barChart.invalidate(); // Refresh the chart
  }

}
