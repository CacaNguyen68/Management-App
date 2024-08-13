package com.mssv_71dctm22077.order;

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
      startActivity(intent);
      return true;
    }
    return super.onOptionsItemSelected(item);
  }
}
