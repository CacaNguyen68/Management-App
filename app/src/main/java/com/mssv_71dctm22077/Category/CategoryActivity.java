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

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_category);

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

    // RecyclerView and FloatingActionButton
    recyclerView = findViewById(R.id.recyclerViewDanhMuc);
    addButton = findViewById(R.id.add_button);

    // Initialize database and arrays
    myDB = new MyDatabaseHelper(CategoryActivity.this);
    categoryId = new ArrayList<>();
    categoryName = new ArrayList<>();
    categoryCreated = new ArrayList<>();

    // Load data into RecyclerView
//    storeDataArrays();
    customAdapter = new CategoryAdapter(CategoryActivity.this, this, categoryId, categoryName, categoryCreated);
    recyclerView.setAdapter(customAdapter);
    recyclerView.setLayoutManager(new LinearLayoutManager(CategoryActivity.this));

    // Handle FloatingActionButton click
    addButton.setOnClickListener(view -> {
      Intent intent = new Intent(CategoryActivity.this, AddCategoryActivity.class);
      startActivity(intent);
    });

    // Setup SearchView
    SearchView searchView = findViewById(R.id.search_view);
    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
      @Override
      public boolean onQueryTextSubmit(String query) {
        return false;
      }

      @Override
      public boolean onQueryTextChange(String newText) {
        customAdapter.filter(newText);
        return true;
      }
    });
  }

  void storeDataArrays() {
    Cursor cursor = myDB.getAllDanhMuc();
    if (cursor.getCount() == 0) {
      myDB.addDanhMuc("Trẻ em");
      myDB.addDanhMuc("Nhân vật");
      myDB.addDanhMuc("Tóc giả");
      myDB.addDanhMuc("Sexy");
      myDB.addDanhMuc("Phụ kiện");
      myDB.addDanhMuc("Cổ trang");
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
    categoryId.clear();
    categoryName.clear();
    categoryCreated.clear();
    storeDataArrays();
    customAdapter.notifyDataSetChanged();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_user, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    if (item.getItemId() == R.id.action_home) {
      // Xử lý khi nhấn vào mục menu
      Intent intent = new Intent(this, MenuUserActivity.class);
      startActivity(intent);
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

}
