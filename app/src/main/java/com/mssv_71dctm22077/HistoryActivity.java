package com.mssv_71dctm22077;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mssv_71dctm22077.Category.CategoryActivity;
import com.mssv_71dctm22077.Content.ContentForUserActivity;
import com.mssv_71dctm22077.adapter.OrderAdapter;
import com.mssv_71dctm22077.model.Order;
import com.mssv_71dctm22077.sqlite.MyDatabaseHelper;
import com.mssv_71dctm22077.user.UserActivity;

import java.util.List;

public class HistoryActivity extends AppCompatActivity {

  private RecyclerView recyclerViewOrders;
  private OrderAdapter orderAdapter;
  private MyDatabaseHelper databaseHelper;
  private int userId;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_history);

    userId = getIntent().getIntExtra("userId", -1);
    Log.d("cac don cua id ", "ID:" + userId);

    // Initialize UI components
    recyclerViewOrders = findViewById(R.id.recyclerViewOrders);

    // Initialize database helper
    databaseHelper = new MyDatabaseHelper(this);

    // Fetch orders from the database
    List<Order> orderList = databaseHelper.getOrderByUserIdStatusDeli(userId);

    // Setup RecyclerView
    orderAdapter = new OrderAdapter(this, orderList);
    recyclerViewOrders.setAdapter(orderAdapter);
    recyclerViewOrders.setLayoutManager(new LinearLayoutManager(this));

    // Initialize BottomNavigationView and set listener
    BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
    bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
      @Override
      public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.navigation_return) {
          // Quay trở lại màn hình trước đó
          finish(); // Kết thúc Activity hiện tại và quay lại Activity trước đó
          return true;

        } else if (itemId == R.id.navigation_profile) {
          // Chuyển tới màn hình hồ sơ người dùng
          Intent profileIntent = new Intent(HistoryActivity.this, CategoryActivity.class);
          startActivity(profileIntent);
          return true;

        } else if (itemId == R.id.navigation_exit) {
          // Thực hiện đăng xuất và quay lại màn hình đăng nhập
          logoutAndReturnToLogin();
          return true;
        }

        return false;
      }
    });
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_user, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    if (item.getItemId() == R.id.action_home) {
      // Handle action home click
      Intent intent = new Intent(this, MenuUserActivity.class);
      startActivity(intent);
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  private void logoutAndReturnToLogin() {
    Intent loginIntent = new Intent(HistoryActivity.this, MainActivity.class);
    loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Xóa các Activity trước đó
    startActivity(loginIntent);
    finish(); // Kết thúc Activity hiện tại
  }
}
