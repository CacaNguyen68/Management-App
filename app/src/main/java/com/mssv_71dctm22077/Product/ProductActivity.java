package com.mssv_71dctm22077.Product;

import android.os.Bundle;
import android.text.TextUtils;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mssv_71dctm22077.R;
import com.mssv_71dctm22077.adapter.ProductAdapter;
import com.mssv_71dctm22077.model.Product;
import com.mssv_71dctm22077.sqlite.MyDatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class ProductActivity extends AppCompatActivity {

  private RecyclerView recyclerView;
  private ProductAdapter productAdapter;
  private List<Product> productList;
  private MyDatabaseHelper myDB;

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

    loadData();

    productAdapter = new ProductAdapter(this, productList);
    recyclerView.setAdapter(productAdapter);

    // Load dữ liệu ban đầu
    // Thiết lập SearchView và bắt sự kiện tìm kiếm
    SearchView searchView = findViewById(R.id.search_view);
    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
      @Override
      public boolean onQueryTextSubmit(String query) {
        return false;
      }

      @Override
      public boolean onQueryTextChange(String newText) {
        // Thực hiện tìm kiếm khi người dùng nhập text vào SearchView
        loadDataSearch(newText);
        return true;
      }
    });

  }

  private void loadData() {
    productList.clear();
    productList.addAll(myDB.getAllProducts());
    productAdapter.notifyDataSetChanged();
  }

  private void loadDataSearch(String query) {
    if (TextUtils.isEmpty(query)) {
      loadData();
    } else {
      productList.clear();
      productList.addAll(myDB.searchProducts(query));
      productAdapter.notifyDataSetChanged();
    }
  }
}
