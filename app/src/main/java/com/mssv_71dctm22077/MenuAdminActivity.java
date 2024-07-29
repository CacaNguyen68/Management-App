package com.mssv_71dctm22077;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mssv_71dctm22077.Category.CategoryActivity;
import com.mssv_71dctm22077.Product.ProductActivity;
import com.mssv_71dctm22077.adapter.ImageSliderAdapter;
import com.mssv_71dctm22077.chart.BarChartCategoryActivity;
import com.mssv_71dctm22077.chart.ChartCategoryActivity;
import com.mssv_71dctm22077.user.UserActivity;

import java.util.ArrayList;
import java.util.List;

public class MenuAdminActivity extends AppCompatActivity {

  private ViewPager2 viewPager;
  private Handler handler = new Handler();
  private Runnable runnable;
  private ImageSliderAdapter adapter;
  private List<Integer> imageList;
  private FloatingActionButton categoryFloating, productFloating, userFloating, notifiactionFloating, statisticFloating, notificationFloating, orderFloating;
  private CoordinatorLayout coordinatorLayout;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_menu_admin);

    //lay phone ma nhap vao
    Intent intentProfile = getIntent();
    String phone = intentProfile.getStringExtra("phone");


    viewPager = findViewById(R.id.viewPager);

    // Thêm các ảnh vào danh sách
    imageList = new ArrayList<>();
    imageList.add(R.drawable.header1);
    imageList.add(R.drawable.header2);
    imageList.add(R.drawable.header3);

    adapter = new ImageSliderAdapter(this, imageList);
    viewPager.setAdapter(adapter);

    // Cài đặt CompositePageTransformer để tạo hiệu ứng chuyển ảnh
    CompositePageTransformer transformer = new CompositePageTransformer();
    transformer.addTransformer(new MarginPageTransformer(40));
    transformer.addTransformer((page, position) -> {
      float r = 1 - Math.abs(position);
      page.setScaleY(0.85f + r * 0.15f);
    });
    viewPager.setPageTransformer(transformer);

    // Tự động chuyển ảnh
    runnable = new Runnable() {
      @Override
      public void run() {
        int currentItem = viewPager.getCurrentItem();
        int nextItem = currentItem + 1;
        if (nextItem >= adapter.getItemCount()) {
          nextItem = 0;
        }
        viewPager.setCurrentItem(nextItem, true);
        handler.postDelayed(this, 1000);
      }
    };
    handler.postDelayed(runnable, 1000);


    categoryFloating = findViewById(R.id.fabManageCategories);
    categoryFloating.setOnClickListener(view -> {
      Intent intent = new Intent(MenuAdminActivity.this, CategoryActivity.class);
      intent.putExtra("phone", phone);
      startActivity(intent);
    });
    productFloating = findViewById(R.id.fabManageProducts);
    productFloating.setOnClickListener(view -> {
      Intent intent = new Intent(this, ProductActivity.class);
      intent.putExtra("phone", phone);
      startActivity(intent);
    });
    userFloating = findViewById(R.id.fabManageUsers);
    userFloating.setOnClickListener(view -> {
      Intent intent = new Intent(this, UserActivity.class);
      startActivity(intent);
    });
    statisticFloating = findViewById(R.id.fabStatistics);
    statisticFloating.setOnClickListener(view -> {
      Intent intent = new Intent(this, BarChartCategoryActivity.class);
      startActivity(intent);
    });
    notifiactionFloating = findViewById(R.id.fabNewNotifications);
    orderFloating = findViewById(R.id.fabInventoryManagement);


    BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
    bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
      @Override
      public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.navigation_home) {
          // Điều hướng tới Home
          Intent homeIntent = new Intent(MenuAdminActivity.this, UserActivity.class);
          startActivity(homeIntent);
          return true;
        } else if (itemId == R.id.navigation_profile) {
          // Điều hướng tới Profile
          Intent profileIntent = new Intent(MenuAdminActivity.this, CategoryActivity.class);
          startActivity(profileIntent);
          return true;
        } else if (itemId == R.id.navigation_settings) {
          // Điều hướng tới Settings
          Intent settingsIntent = new Intent(MenuAdminActivity.this, ProductActivity.class);
          startActivity(settingsIntent);
          return true;
        }
        return false;
      }
    });
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    handler.removeCallbacks(runnable);
  }
}
