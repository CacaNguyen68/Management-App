package com.mssv_71dctm22077;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mssv_71dctm22077.Cart.CartActivity;
import com.mssv_71dctm22077.Category.CategoryActivity;
import com.mssv_71dctm22077.Category.CategoryForUserActivity;
import com.mssv_71dctm22077.Product.ProductActivity;
import com.mssv_71dctm22077.adapter.ImageSliderAdapter;
import com.mssv_71dctm22077.chart.BarChartCategoryActivity;
import com.mssv_71dctm22077.model.User;
import com.mssv_71dctm22077.sqlite.MyDatabaseHelper;
import com.mssv_71dctm22077.user.UserActivity;

import java.util.ArrayList;
import java.util.List;

public class MenuUserActivity extends AppCompatActivity {

  private ViewPager2 viewPager;
  private Handler handler = new Handler();
  private Runnable runnable;
  private ImageSliderAdapter adapter;
  private List<Integer> imageList;
  private FloatingActionButton categoryFloating, cartFloating, userFloating, notifiactionFloating, statisticFloating, notificationFloating, orderFloating;
  private CoordinatorLayout coordinatorLayout;
  private String userId;
  private MyDatabaseHelper db;
  User user;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_menu_user);

    Intent intentProfile = getIntent();
    String phone = intentProfile.getStringExtra("phone");
    db = new MyDatabaseHelper(this);
    if (phone != null) {
      user = db.getUserByPhone(phone);
      if (user != null) {
        Log.d("Profile user", "User ID: " + user.getId());
      } else {
        Log.d("Profile user", "User not found with phone: " + phone);
      }
    } else {
      Log.d("Profile user", "Phone number is null");
    }

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
      Intent intent = new Intent(MenuUserActivity.this, CategoryForUserActivity.class);
      intent.putExtra("phone",phone);
      startActivity(intent);
    });

    cartFloating = findViewById(R.id.fabPlaceOrder);
    cartFloating.setOnClickListener(view -> {
      Intent intent = new Intent(MenuUserActivity.this, CartActivity.class);
      intent.putExtra("userId", user.getId()); // truyen thanh cong
      startActivity(intent);
    });

    BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
    bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
      @Override
      public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.navigation_home) {
          // Điều hướng tới Home
          Intent homeIntent = new Intent(MenuUserActivity.this, UserActivity.class);
          startActivity(homeIntent);
          return true;
        } else if (itemId == R.id.navigation_profile) {
          // Điều hướng tới Profile
          Intent profileIntent = new Intent(MenuUserActivity.this, CategoryActivity.class);
          startActivity(profileIntent);
          return true;
        } else if (itemId == R.id.navigation_settings) {
          // Điều hướng tới Settings
          Intent settingsIntent = new Intent(MenuUserActivity.this, ProductActivity.class);
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
