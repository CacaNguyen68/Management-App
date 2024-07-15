package com.mssv_71dctm22077.Product;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.service.controls.actions.FloatAction;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mssv_71dctm22077.MainActivity;
import com.mssv_71dctm22077.R;
import com.mssv_71dctm22077.adapter.ProductAdapter;
import com.mssv_71dctm22077.model.Product;
import com.mssv_71dctm22077.sqlite.MyDatabaseHelper;
import com.mssv_71dctm22077.user.RegisterActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ProductActivity extends AppCompatActivity {

  private RecyclerView recyclerView;
  private ProductAdapter productAdapter;
  private List<Product> productList;
  private MyDatabaseHelper myDB;
  FloatingActionButton addButton;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_product);

    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    recyclerView = findViewById(R.id.recyclerViewProduct);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    myDB = new MyDatabaseHelper(this);
    productList = new ArrayList<>();

    // Initialize data if necessary
    storeDataArrays();

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

    addButton = findViewById(R.id.add_button);
    addButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(ProductActivity.this, AddProductActivity.class);
        startActivity(intent);
      }
    });
  }

  private void loadData() {
    productList.clear();
    productList.addAll(myDB.getAllProducts());
    productAdapter = new ProductAdapter(this, productList);
    recyclerView.setAdapter(productAdapter);
  }

  private void loadDataSearch(String query) {
    productList.clear();
    productList.addAll(myDB.searchProducts(query));
    productAdapter.notifyDataSetChanged();

  }

  private void storeDataArrays() {
    if (myDB.getAllProducts().isEmpty()) {
      Date today = new Date();
      SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

      myDB.addProduct("Product 1", 100.0, 1, formatter.format(today), "Admin", null);
      myDB.addProduct("Product 2", 120.0, 2, formatter.format(today), "Admin", null);
    }
  }
}
