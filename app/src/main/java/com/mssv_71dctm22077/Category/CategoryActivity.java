package com.mssv_71dctm22077.Category;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mssv_71dctm22077.MenuAdminActivity;
import com.mssv_71dctm22077.MenuUserActivity;
import com.mssv_71dctm22077.R;
import com.mssv_71dctm22077.adapter.CategoryAdapter;
import com.mssv_71dctm22077.sqlite.MyDatabaseHelper;

import java.util.ArrayList;

public class CategoryActivity extends AppCompatActivity {

  RecyclerView recyclerView;
  FloatingActionButton addButton;

  MyDatabaseHelper myDB;
  ArrayList<String> categoryId, categoryName, categoryCreated;
  CategoryAdapter customAdapter;

  SearchView searchView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_category);

    // Khởi tạo RecyclerView và SearchView
    recyclerView = findViewById(R.id.recyclerViewDanhMuc);
    searchView = findViewById(R.id.search_view);

    // Toolbar
    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    // Handle navigation icon click (back button click)
    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        finish();
      }
    });

      // Khởi tạo đối tượng cơ sở dữ liệu
    myDB = new MyDatabaseHelper(CategoryActivity.this);

    // Khởi tạo danh sách để chứa dữ liệu danh mục
    categoryId = new ArrayList<>();
    categoryName = new ArrayList<>();
    categoryCreated = new ArrayList<>();

    // Gọi hàm để nạp dữ liệu từ cơ sở dữ liệu
    storeDataArrays();

    // Thiết lập adapter cho RecyclerView
    customAdapter = new CategoryAdapter(CategoryActivity.this,this, categoryId, categoryName, categoryCreated);
    recyclerView.setAdapter(customAdapter);
    recyclerView.setLayoutManager(new LinearLayoutManager(CategoryActivity.this));

    // Thiết lập sự kiện cho SearchView để lọc dữ liệu theo từ khóa
    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
      @Override
      public boolean onQueryTextSubmit(String query) {
        return false;
      }

      @Override
      public boolean onQueryTextChange(String newText) {
        customAdapter.filter(newText); // Lọc danh sách dựa trên từ khóa nhập vào
        return false;
      }
    });

    addButton = findViewById(R.id.add_button);
    // Handle FloatingActionButton click
    addButton.setOnClickListener(view -> {
      Intent intent = new Intent(CategoryActivity.this, AddCategoryActivity.class);
      startActivity(intent);
    });
  }

  // Hàm để nạp dữ liệu từ cơ sở dữ liệu vào các ArrayList
  void storeDataArrays() {
    Cursor cursor = myDB.getAllDanhMuc(); // Lấy tất cả các danh mục từ cơ sở dữ liệu
    if (cursor.getCount() == 0) {
      // Nếu không có dữ liệu, thêm dữ liệu mẫu vào cơ sở dữ liệu
      myDB.addDanhMuc("Trẻ em");
      myDB.addDanhMuc("Nhân vật");
      myDB.addDanhMuc("Tóc giả");
      myDB.addDanhMuc("Sexy");
      myDB.addDanhMuc("Phụ kiện");
      myDB.addDanhMuc("Cổ trang");
    } else {
      // Nếu có dữ liệu, nạp vào các danh sách
      while (cursor.moveToNext()) {
        categoryId.add(cursor.getString(0));  // ID của danh mục
        categoryName.add(cursor.getString(1)); // Tên danh mục
        categoryCreated.add(cursor.getString(2)); // Ngày tạo
      }
    }
  }

  // Hàm được gọi khi Activity quay trở lại trạng thái foreground (trở lại màn hình)
  @Override
  public void onResume() {
    super.onResume();
    // Xóa dữ liệu cũ trong các danh sách
    categoryId.clear();
    categoryName.clear();
    categoryCreated.clear();
    // Nạp lại dữ liệu từ cơ sở dữ liệu
    storeDataArrays();
    // Thông báo cho adapter rằng dữ liệu đã thay đổi để cập nhật giao diện
    customAdapter.notifyDataSetChanged();
  }
}
