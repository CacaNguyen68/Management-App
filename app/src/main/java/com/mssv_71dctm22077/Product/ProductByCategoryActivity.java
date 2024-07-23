package com.mssv_71dctm22077.Product;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
  FloatingActionButton addButton, updateButton, deleteButton;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_product_by_category);

    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    recyclerView = findViewById(R.id.recyclerViewProduct);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    myDB = new MyDatabaseHelper(this);
    productList = new ArrayList<>();

    // Load data into RecyclerView
    loadData();

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

  }

  private void loadData() {
    productList.clear();
    productList.addAll(myDB.getAllProducts());
    productAdapter = new ProductAdapter(this,this, productList);
    recyclerView.setAdapter(productAdapter);
  }

  private void loadDataSearch(String query) {
    productList.clear();
    productList.addAll(myDB.searchProducts(query));
    productAdapter.notifyDataSetChanged();

  }

  @Override
  public void onResume() {
    super.onResume();
    // Clear old data
    productList.clear();

    // Reload data from the database
    productList.addAll(myDB.getAllProducts());

    // Notify the adapter about the data change
    if (productAdapter != null) {
      productAdapter.notifyDataSetChanged();
    }
  }

}
