package com.mssv_71dctm22077.Cart;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mssv_71dctm22077.R;
import com.mssv_71dctm22077.adapter.CartAdapter;
import com.mssv_71dctm22077.model.CartItem;
import com.mssv_71dctm22077.sqlite.MyDatabaseHelper;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {

  private RecyclerView recyclerViewCart;
  private TextView totalPrice;
  private List<CartItem> cartItemList;
  private CartAdapter cartAdapter;
  private MyDatabaseHelper databaseHelper;
  private int userId;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_cart);

    userId = getIntent().getIntExtra("userId", -1);
    databaseHelper = new MyDatabaseHelper(this);

    recyclerViewCart = findViewById(R.id.recyclerViewCart);
    totalPrice = findViewById(R.id.total_price);

    cartItemList = databaseHelper.getCartItemsByUserId(userId);

    cartAdapter = new CartAdapter(this, cartItemList, databaseHelper);
    recyclerViewCart.setAdapter(cartAdapter);
    recyclerViewCart.setLayoutManager(new LinearLayoutManager(this));

    updateTotalPrice();
  }

  public void updateTotalPrice() {
    double total = 0.0;
    for (CartItem item : cartItemList) {
      total += item.getProductPrice() * item.getQuantity();
    }
    totalPrice.setText("Tổng giá: " + total + " VND");
  }

}
