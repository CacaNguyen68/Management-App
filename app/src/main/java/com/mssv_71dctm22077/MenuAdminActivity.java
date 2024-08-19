package com.mssv_71dctm22077;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mssv_71dctm22077.Category.CategoryActivity;
import com.mssv_71dctm22077.Content.ContentActivity;
import com.mssv_71dctm22077.Product.ProductActivity;
import com.mssv_71dctm22077.Review.ReviewByAdminActivity;
import com.mssv_71dctm22077.adapter.ImageSliderAdapter;
import com.mssv_71dctm22077.chart.BarChartCategoryActivity;
import com.mssv_71dctm22077.chart.ChartActivity;
import com.mssv_71dctm22077.order.OrderByAdminActivity;
import com.mssv_71dctm22077.user.UserActivity;

import java.util.ArrayList;
import java.util.List;

public class MenuAdminActivity extends AppCompatActivity {

  private ViewPager2 viewPager;
  private Handler handler = new Handler();
  private Runnable runnable;
  private ImageSliderAdapter adapter;
  private List<Integer> imageList;
  private FloatingActionButton categoryFloating, productFloating, userFloating, reviewsFloating, statisticFloating, notificationFloating, orderFloating;
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
      Intent intent = new Intent(this, ChartActivity.class);
      startActivity(intent);
    });
    reviewsFloating = findViewById(R.id.fabNewReviews);
    reviewsFloating.setOnClickListener(view -> {
      Intent intent = new Intent(this, ReviewByAdminActivity.class);
      startActivity(intent);
    });

    orderFloating = findViewById(R.id.fabInventoryManagement);
    orderFloating.setOnClickListener(view -> {
      Intent intent = new Intent(this, OrderByAdminActivity.class);
      startActivity(intent);
    });


    BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
    bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
      @Override
      public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.navigation_home) {
          // Điều hướng tới Home và dọn sạch back stack
          Intent homeIntent = new Intent(MenuAdminActivity.this, MainActivity.class);
          homeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
          startActivity(homeIntent);
          return true;
        } else if (itemId == R.id.navigation_profile) {
          // Điều hướng tới Profile
          Intent profileIntent = new Intent(MenuAdminActivity.this, CategoryActivity.class);
          profileIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
          startActivity(profileIntent);
          return true;
        } else if (itemId == R.id.navigation_contents) {
          // Điều hướng tới Contents
          Intent contentsIntent = new Intent(MenuAdminActivity.this, ContentActivity.class);
          contentsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
          startActivity(contentsIntent);
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
