package com.mssv_71dctm22077.Product;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mssv_71dctm22077.R;
import com.mssv_71dctm22077.adapter.ProductUserAdapter;
import com.mssv_71dctm22077.model.Product;
import com.mssv_71dctm22077.model.User;
import com.mssv_71dctm22077.sqlite.MyDatabaseHelper;

import java.util.ArrayList;

public class FavoriteProductActivity extends AppCompatActivity {

  private RecyclerView recyclerView;
  private ProductUserAdapter productAdapter;
  private ArrayList<Product> productList;
  private MyDatabaseHelper myDB;
  private User user;
  private int userId;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    EdgeToEdge.enable(this);
    setContentView(R.layout.activity_favorite_product);

    userId = getIntent().getIntExtra("userId", -1);
    Log.d("cac don cua id ", "ID:" + userId);


    Intent intentProfile = getIntent();
    String phone = intentProfile.getStringExtra("phone");//lay thanh cong
    myDB = new MyDatabaseHelper(this);
    if (phone != null) {
      user = myDB.getUserByPhone(phone);
      if (user != null) {
        Log.d("Profile user", "User ID: " + user.getId());
      } else {
        Log.d("Profile user", "User not found with phone: " + phone);
      }
    } else {
      Log.d("Profile user", "Phone number is null");
    }


    // Set up padding for system bars
    ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
      Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
      v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
      return insets;
    });

    // Initialize database helper and user
//    user = myDB.getUser(Long.parseLong(String.valueOf(userId))); // Replace with actual user data if available
    // Set up RecyclerView
    recyclerView = findViewById(R.id.recyclerViewProduct);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));

    // Load products and set adapter
    loadProducts();
  }

  private void loadProducts() {
    productList = new ArrayList<>();
    productList.addAll(myDB.getTop5ProductsSold());

    productAdapter = new ProductUserAdapter(this, this, productList, user);
    recyclerView.setAdapter(productAdapter);

    Log.d("FavoriteProductActivity", "Loaded products: " + productList.size());
  }

  @Override
  public void onResume() {
    super.onResume();
    // Reload data from the database
    loadProducts();
  }
}
