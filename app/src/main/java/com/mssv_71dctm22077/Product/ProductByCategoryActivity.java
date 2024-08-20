package com.mssv_71dctm22077.Product;

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
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mssv_71dctm22077.MenuAdminActivity;
import com.mssv_71dctm22077.MenuUserActivity;
import com.mssv_71dctm22077.R;
import com.mssv_71dctm22077.adapter.ProductAdapter;
import com.mssv_71dctm22077.model.Product;
import com.mssv_71dctm22077.sqlite.MyDatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ProductByCategoryActivity extends AppCompatActivity {

  private RecyclerView recyclerView;
  private ProductAdapter productAdapter;
  private ArrayList<Product> productList;
  private MyDatabaseHelper myDB;
  private FloatingActionButton addButton;
  private int categoryId;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_product_by_category);

    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    // Handle navigation icon click (back button click)
    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        finish();
      }
    });

    recyclerView = findViewById(R.id.recyclerViewProduct);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    myDB = new MyDatabaseHelper(this);
    productList = new ArrayList<>();

    // Setup SearchView
    SearchView searchView = findViewById(R.id.search_view);
    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
      @Override
      public boolean onQueryTextSubmit(String query) {
        return false;
      }

      @Override
      public boolean onQueryTextChange(String newText) {
        loadDataSearch(newText);
        return true;
      }
    });

    // Handle FloatingActionButton click
    addButton = findViewById(R.id.add_button);
    addButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(ProductByCategoryActivity.this, AddProductActivity.class);
        startActivity(intent);
      }
    });

    // Get the categoryId from Intent
    Intent intent = getIntent();
    if (intent.hasExtra("categoryId")) {
      categoryId = Integer.parseInt(intent.getStringExtra("categoryId"));
      loadProductsByCategory(categoryId);
      Log.d("ProductByCategory", "Category ID ddf: " + categoryId);
    }
  }

  private void loadProductsByCategory(int id) {
    productList.clear();
    productList.addAll(myDB.getProductsByCategoryId(id));
    productAdapter = new ProductAdapter(this, this, productList);
    recyclerView.setAdapter(productAdapter);
  }

  private void loadDataSearch(String query) {
    productList.clear();
    productList.addAll(myDB.searchProductsWhereCategoryId(query, categoryId));
    productAdapter.notifyDataSetChanged();
  }

  @Override
  public void onResume() {
    super.onResume();
    // Reload data from the database
    if (categoryId > 0) {
      productList.clear();
      productList.addAll(myDB.getProductsByCategoryId(categoryId));
      if (productAdapter != null) {
        productAdapter.notifyDataSetChanged();
      }
    }
  }
}
