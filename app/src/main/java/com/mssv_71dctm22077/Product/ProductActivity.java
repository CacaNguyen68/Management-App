package com.mssv_71dctm22077.Product;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
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
import java.util.List;
import java.util.Locale;

public class ProductActivity extends AppCompatActivity {

  private RecyclerView recyclerView;
  private ProductAdapter productAdapter;
  private ArrayList<Product> productList;
  private MyDatabaseHelper myDB;
  FloatingActionButton addButton, updateButton, deleteButton;

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
    // Nhận Intent
//    Intent intent = getIntent();
//    if (intent.hasExtra("categoryId")) {
//      int categoryId = Integer.parseInt(intent.getStringExtra("categoryId"));
//      loadProductsByCategory(categoryId);
//      Log.d("vao ham con","id: "+categoryId);
//    } else {
      loadData();
//      Log.d("ham lon", "khong chinh xac");
//    }

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
    productAdapter = new ProductAdapter(this, this, productList);
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

      myDB.addProduct("[Kuroko no Basket] Đồng phục trường TEIKO", 650000, 1, formatter.format(today), "Super Admin", null);
      myDB.addProduct("Đồng phục học sinh Nhật Bản – Màu đen", 150000, 1, formatter.format(today), "Super Admin", null);
      myDB.addProduct("Hán Phục HPW26", 180000, 6, formatter.format(today), "Super Admin", null);
      myDB.addProduct("Hán Phục HPW20", 180000, 6, formatter.format(today), "Super Admin", null);
      myDB.addProduct("[JUJUTSU KAISEN] Tóc giả Gojo Satoru", 330, 3, formatter.format(today), "Super Admin", null);
      myDB.addProduct("[Jigoku Shōjo] Hone Onna", 1000000, 4, formatter.format(today), "Super Admin", null);
      myDB.addProduct("Vương miện yêu tinh", 40000, 5, formatter.format(today), "Super Admin", null);
      myDB.addProduct("Cà Vạt (Caravat) Harry Potter", 30000, 5, formatter.format(today), "Super Admin", null);
      myDB.addProduct("[NARUTO] Costume Naruto", 220000, 2, formatter.format(today), "Super Admin", null);
      myDB.addProduct("[Naruto] Áo khoác Hokage đệ tứ – Minato", 250000, 2, formatter.format(today), "Super Admin", null);

    }
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
