package com.mssv_71dctm22077;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mssv_71dctm22077.Category.AddCategoryActivity;
import com.mssv_71dctm22077.Category.CategoryActivity;
import com.mssv_71dctm22077.Product.ProductActivity;
import com.mssv_71dctm22077.adapter.ImageSliderAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MenuAdminActivity extends AppCompatActivity {

  private ViewPager2 viewPager;
  private Handler handler = new Handler();
  private Runnable runnable;
  private ImageSliderAdapter adapter;
  private List<Integer> imageList;
  private FloatingActionButton categoryFloating, productFloating, userFloating, notifiactionFloating, statisticFloating, notificationFloating, orderFloating;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_menu_admin);

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
      startActivity(intent);
    });
    productFloating = findViewById(R.id.fabManageProducts);
    userFloating = findViewById(R.id.fabManageUsers);
    statisticFloating = findViewById(R.id.fabStatistics);
    notifiactionFloating = findViewById(R.id.fabNewNotifications);
    orderFloating = findViewById(R.id.fabInventoryManagement);

  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    handler.removeCallbacks(runnable);
  }
}
