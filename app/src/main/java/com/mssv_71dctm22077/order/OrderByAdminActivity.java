package com.mssv_71dctm22077.order;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mssv_71dctm22077.MenuAdminActivity;
import com.mssv_71dctm22077.R;
import com.mssv_71dctm22077.adapter.OrderAdminAdapter;
import com.mssv_71dctm22077.model.Order;
import com.mssv_71dctm22077.model.OrderStatus;
import com.mssv_71dctm22077.sqlite.MyDatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class OrderByAdminActivity extends AppCompatActivity {

  private int userId;
  private MyDatabaseHelper databaseHelper;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    EdgeToEdge.enable(this);
    setContentView(R.layout.activity_order_by_admin);

    // Retrieve userId from Intent
    userId = getIntent().getIntExtra("userId", -1);
    Log.d("OrderByAdminActivity", "User ID: " + userId);

    // Initialize database helper
    databaseHelper = new MyDatabaseHelper(this);

    // Fetch orders from the database
    List<Order> orders = databaseHelper.getAllOrders(); // Fetch all orders

    // Setup RecyclerView
    RecyclerView recyclerView = findViewById(R.id.recyclerViewOrders);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    OrderAdminAdapter adapter = new OrderAdminAdapter(this, orders);
    recyclerView.setAdapter(adapter);
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
      Intent intent = new Intent(this, MenuAdminActivity.class);
      startActivity(intent);
      return true;
    }
    return super.onOptionsItemSelected(item);
  }
}
