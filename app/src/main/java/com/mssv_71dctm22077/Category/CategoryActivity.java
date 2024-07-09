package com.mssv_71dctm22077.Category;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mssv_71dctm22077.R;
import com.mssv_71dctm22077.adapter.CustomAdapter;
import com.mssv_71dctm22077.sqlite.MyDatabaseHelper;

import java.util.ArrayList;

public class CategoryActivity extends AppCompatActivity {

  RecyclerView recyclerView;
  FloatingActionButton addButton;

  MyDatabaseHelper myDB;
  ArrayList<String> categoryId, categoryName, categoryCreated;
  CustomAdapter customAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_danhmuc);

    // Toolbar
    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    // RecyclerView and FloatingActionButton
    recyclerView = findViewById(R.id.recyclerViewDanhMuc);
    addButton = findViewById(R.id.add_button);

    // Initialize database and arrays
    myDB = new MyDatabaseHelper(CategoryActivity.this);
    categoryId = new ArrayList<>();
    categoryName = new ArrayList<>();
    categoryCreated = new ArrayList<>();

    // Load data into RecyclerView
    storeDataArrays();
    customAdapter = new CustomAdapter(CategoryActivity.this, this, categoryId, categoryName, categoryCreated);
    recyclerView.setAdapter(customAdapter);
    recyclerView.setLayoutManager(new LinearLayoutManager(CategoryActivity.this));

    // Handle FloatingActionButton click
    addButton.setOnClickListener(view -> {
      Intent intent = new Intent(CategoryActivity.this, AddCategoryActivity.class);
      startActivity(intent);
    });
  }

  void storeDataArrays() {
    Cursor cursor = myDB.getAllDanhMuc();
    if (cursor.getCount() == 0) {
      MyDatabaseHelper myDB = new MyDatabaseHelper(this);

      myDB.addDanhMuc("Trẻ em");
      myDB.addDanhMuc("Nhân vật");
      myDB.addDanhMuc("Tóc giả");
      myDB.addDanhMuc("Sexy");
      myDB.addDanhMuc("Phụ kiện");
      myDB.addDanhMuc("Cổ trang");

      Toast.makeText(this, "Dữ liệu rỗng.", Toast.LENGTH_SHORT).show();
    } else {
      while (cursor.moveToNext()) {
        categoryId.add(cursor.getString(0));
        categoryName.add(cursor.getString(1));
        categoryCreated.add(cursor.getString(2));
      }
    }
  }

  @Override
  public void onResume() {
    super.onResume();
    // Clear old data
    categoryId.clear();
    categoryName.clear();
    categoryCreated.clear();

    // Reload data
    storeDataArrays();

    // Notify adapter about data change
    customAdapter.notifyDataSetChanged();
  }

  //My menu

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.my_menu, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    int id = item.getItemId();
    if (id == R.id.item1) {
      // Xử lý khi người dùng chọn mục "Item 1"
      Toast.makeText(this, "Item 1 selected", Toast.LENGTH_SHORT).show();
      return true;
    } else if (id == R.id.item2) {
      // Xử lý khi người dùng chọn mục "Item 2"
      Toast.makeText(this, "Item 2 selected", Toast.LENGTH_SHORT).show();
      return true;
    } else if (id == R.id.item3) {
      // Xử lý khi người dùng chọn mục "Item 3"
      Toast.makeText(this, "Item 3 selected", Toast.LENGTH_SHORT).show();
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

}
