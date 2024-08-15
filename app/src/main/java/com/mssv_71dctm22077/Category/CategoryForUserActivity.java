package com.mssv_71dctm22077.Category;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mssv_71dctm22077.MenuUserActivity;
import com.mssv_71dctm22077.R;
import com.mssv_71dctm22077.adapter.CategoryAdapter;
import com.mssv_71dctm22077.adapter.CategoryForUserAdapter;
import com.mssv_71dctm22077.model.User;
import com.mssv_71dctm22077.sqlite.MyDatabaseHelper;

import java.util.ArrayList;

public class CategoryForUserActivity extends AppCompatActivity {

  RecyclerView recyclerView;
  MyDatabaseHelper myDB;
  ArrayList<String> categoryId, categoryName, categoryCreated;
  CategoryForUserAdapter categoryForUserAdapter;
  User user;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_category_for_user);

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

    // Toolbar
    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    // RecyclerView and FloatingActionButton
    recyclerView = findViewById(R.id.recyclerViewDanhMuc);

    // Initialize database and arrays
    myDB = new MyDatabaseHelper(CategoryForUserActivity.this);
    categoryId = new ArrayList<>();
    categoryName = new ArrayList<>();
    categoryCreated = new ArrayList<>();

    // Load data into RecyclerView
    storeDataArrays();
    categoryForUserAdapter = new CategoryForUserAdapter(CategoryForUserActivity.this, this, categoryId, categoryName, categoryCreated, user);
    recyclerView.setAdapter(categoryForUserAdapter);
    recyclerView.setLayoutManager(new LinearLayoutManager(CategoryForUserActivity.this));


    // Setup SearchView
    SearchView searchView = findViewById(R.id.search_view);
    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
      @Override
      public boolean onQueryTextSubmit(String query) {
        return false;
      }

      @Override
      public boolean onQueryTextChange(String newText) {
        categoryForUserAdapter.filter(newText);
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
    categoryForUserAdapter.notifyDataSetChanged();
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
