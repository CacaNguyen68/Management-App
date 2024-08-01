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
import com.mssv_71dctm22077.adapter.OrderDetailAdapter;
import com.mssv_71dctm22077.adapter.ProductAdapter;
import com.mssv_71dctm22077.model.Product;
import com.mssv_71dctm22077.sqlite.MyDatabaseHelper;

import java.util.List;

public class OrderDetailActivity extends AppCompatActivity {

  private RecyclerView recyclerViewOrderDetails;
  private MyDatabaseHelper databaseHelper;
  private int orderId;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_order_detail);

    orderId = getIntent().getIntExtra("orderId", -1);
    databaseHelper = new MyDatabaseHelper(this);

    recyclerViewOrderDetails = findViewById(R.id.recyclerViewProducts);

    List<Product> productList = databaseHelper.getProductInOrderItemByOrderId(orderId);

    OrderDetailAdapter orderDetailAdapter = new OrderDetailAdapter(this, productList);
    recyclerViewOrderDetails.setAdapter(orderDetailAdapter);
    recyclerViewOrderDetails.setLayoutManager(new LinearLayoutManager(this));
  }
}
