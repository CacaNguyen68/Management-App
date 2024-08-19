package com.mssv_71dctm22077.order;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mssv_71dctm22077.MenuAdminActivity;
import com.mssv_71dctm22077.R;
import com.mssv_71dctm22077.adapter.CategoryAdapter;
import com.mssv_71dctm22077.adapter.OrderAdminAdapter;
import com.mssv_71dctm22077.model.Order;
import com.mssv_71dctm22077.model.OrderStatus;
import com.mssv_71dctm22077.sqlite.MyDatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class OrderByAdminActivity extends AppCompatActivity {

  private MyDatabaseHelper databaseHelper;
  private OrderAdminAdapter orderAdminAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_order_by_admin);

    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    toolbar.setNavigationOnClickListener(v -> finish());

    databaseHelper = new MyDatabaseHelper(this);

    List<Order> orders = databaseHelper.getAllOrders(); // Fetch all orders
    RecyclerView recyclerView = findViewById(R.id.recyclerViewOrders);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    orderAdminAdapter = new OrderAdminAdapter(this, orders);
    recyclerView.setAdapter(orderAdminAdapter);

    SearchView searchView = findViewById(R.id.search_view);
    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
      @Override
      public boolean onQueryTextSubmit(String query) {
        return false;
      }

      @Override
      public boolean onQueryTextChange(String newText) {
        orderAdminAdapter.getFilter().filter(newText);
        return true;
      }
    });
  }
}
