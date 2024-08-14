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

public class RevenueOrderActivity extends AppCompatActivity {

  MyDatabaseHelper mb;
  BarChart barChart;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_revenue_order);

    mb = new MyDatabaseHelper(this);
    barChart = findViewById(R.id.barChart);

    setupBarChart(barChart);

    ArrayList<BarEntry> entries = new ArrayList<>();
    ArrayList<String> labels = new ArrayList<>();
    Cursor cursor = mb.getRevenueByDate(); // Câu lệnh SQL tính doanh thu theo ngày

    if (cursor != null) {
      int index = 0;
      while (cursor.moveToNext()) {
        String date = cursor.getString(0); // Ngày
        float revenue = cursor.getFloat(1); // Doanh thu

         Log.d("ChartData", "Date: " + date + ", Revenue: " + revenue);

        entries.add(new BarEntry(index, revenue));
        labels.add(date);
        index++;
      }
      cursor.close();
    }

    BarDataSet dataSet = new BarDataSet(entries, "Doanh thu theo ngày");
    dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
    dataSet.setValueTextSize(12f);

    BarData data = new BarData(dataSet);
    setupChartData(barChart, data, labels);
  }

  private void setupBarChart(BarChart barChart) {
    barChart.getDescription().setEnabled(false);
    barChart.setFitBars(true);
    barChart.getXAxis().setDrawGridLines(false);
    barChart.getAxisLeft().setDrawGridLines(false);
    barChart.getAxisRight().setDrawGridLines(false);

    barChart.getXAxis().setTextSize(12f);
    barChart.getAxisLeft().setTextSize(12f);
    barChart.getAxisRight().setTextSize(12f);

    ValueFormatter integerValueFormatter = new ValueFormatter() {
      @Override
      public String getFormattedValue(float value) {
        return String.valueOf((int) value);
      }
    };

    barChart.getAxisLeft().setValueFormatter(integerValueFormatter);
    barChart.getAxisRight().setValueFormatter(integerValueFormatter);

    barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
    barChart.getXAxis().setGranularity(1f);
    barChart.getXAxis().setLabelRotationAngle(-45f);
    barChart.getXAxis().setAvoidFirstLastClipping(true);
  }

  private void setupChartData(BarChart barChart, BarData data, ArrayList<String> labels) {
    barChart.setData(data);
    barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
    barChart.invalidate(); // Refresh the chart
  }
}
