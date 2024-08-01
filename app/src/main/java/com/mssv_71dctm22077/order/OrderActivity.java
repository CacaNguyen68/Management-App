package com.mssv_71dctm22077.order;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mssv_71dctm22077.R;
import com.mssv_71dctm22077.adapter.OrderAdapter;
import com.mssv_71dctm22077.model.Order;
import com.mssv_71dctm22077.sqlite.MyDatabaseHelper;

import java.util.List;

public class OrderActivity extends AppCompatActivity {

  private RecyclerView recyclerViewOrders;
  private OrderAdapter orderAdapter;
  private MyDatabaseHelper databaseHelper;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_order);

    // Initialize UI components
    recyclerViewOrders = findViewById(R.id.recyclerViewOrders);

    // Initialize database helper
    databaseHelper = new MyDatabaseHelper(this);

    // Fetch orders from the database
    List<Order> orderList = databaseHelper.getAllOrders();

    // Setup RecyclerView
    orderAdapter = new OrderAdapter(this, orderList);
    recyclerViewOrders.setAdapter(orderAdapter);
    recyclerViewOrders.setLayoutManager(new LinearLayoutManager(this));
  }
}
