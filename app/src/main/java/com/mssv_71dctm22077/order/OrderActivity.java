package com.mssv_71dctm22077.order;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mssv_71dctm22077.HistoryActivity;
import com.mssv_71dctm22077.MainActivity;
import com.mssv_71dctm22077.MenuUserActivity;
import com.mssv_71dctm22077.R;
import com.mssv_71dctm22077.adapter.OrderAdapter;
import com.mssv_71dctm22077.model.Order;
import com.mssv_71dctm22077.sqlite.MyDatabaseHelper;

import java.util.List;

public class OrderActivity extends AppCompatActivity {

  private RecyclerView recyclerViewOrders;
  private OrderAdapter orderAdapter;
  private MyDatabaseHelper databaseHelper;
  private int userId;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_order);
    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    // Thiết lập sự kiện khi nhấn nút back trên thanh công cụ
    toolbar.setNavigationOnClickListener(view -> finish());


    userId = getIntent().getIntExtra("userId", -1);
    Log.d("cac don cua id ", "ID:" + userId);

    // Initialize UI components
    recyclerViewOrders = findViewById(R.id.recyclerViewOrders);

    // Initialize database helper
    databaseHelper = new MyDatabaseHelper(this);

    // Fetch orders from the database
    List<Order> orderList = databaseHelper.getOrderByUserId(userId);

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
        if (itemId == R.id.navigation_login) {
          // Điều hướng tới Home
          Intent homeIntent = new Intent(OrderActivity.this, MainActivity.class);
          homeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
          startActivity(homeIntent);
          return true;
        }
//        else if (itemId == R.id.navigation_profile) {
//          // Điều hướng tới Profile
//          Intent profileIntent = new Intent(MenuUserActivity.this, CategoryActivity.class);
//          profileIntent.putExtra("userId", user.getId());
//          startActivity(profileIntent);
//          return true;
//        }
        else if (itemId == R.id.navigation_exit) {
          finishAffinity();
          // Kết thúc toàn bộ ứng dụng
          System.exit(0);
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
      // Xử lý khi nhấn vào mục menu
      Intent intent = new Intent(this, MenuUserActivity.class);
      intent.putExtra("userId", userId); // truyen thanh cong
      startActivity(intent);
      return true;
    }
    return super.onOptionsItemSelected(item);
  }
}
