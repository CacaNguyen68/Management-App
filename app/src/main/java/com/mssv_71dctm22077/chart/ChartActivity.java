package com.mssv_71dctm22077.chart;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mssv_71dctm22077.Category.CategoryActivity;
import com.mssv_71dctm22077.Content.ContentActivity;
import com.mssv_71dctm22077.MainActivity;
import com.mssv_71dctm22077.MenuAdminActivity;
import com.mssv_71dctm22077.R;
import com.mssv_71dctm22077.order.OrderByAdminActivity;
import com.mssv_71dctm22077.sqlite.MyDatabaseHelper;

import java.util.ArrayList;


public class ChartActivity extends AppCompatActivity {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_chart);

    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    // Handle navigation icon click (back button click)
    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        finish();
      }
    });

    Button button1 = findViewById(R.id.button1);
    Button button2 = findViewById(R.id.button2);
    Button button3 = findViewById(R.id.button3);

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

    BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
    bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
      @Override
      public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.navigation_danhmuc) {
          // Điều hướng tới Home và dọn sạch back stack
          Intent homeIntent = new Intent(ChartActivity.this, CategoryActivity.class);
          startActivity(homeIntent);
          return true;
        } else if (itemId == R.id.navigation_donhang) {
          // Điều hướng tới Contents
          Intent contentsIntent = new Intent(ChartActivity.this, OrderByAdminActivity.class);
          startActivity(contentsIntent);
          finish();
          return true;
        } else if (itemId == R.id.navigation_logout) {
          // Thoát tất cả các Activity và thoát ứng dụng
          finishAffinity();
          // Kết thúc toàn bộ ứng dụng
          System.exit(0);
          return true;
        }
        return false;
      }
    });
  }
}
