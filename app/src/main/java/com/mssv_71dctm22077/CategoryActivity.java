package com.mssv_71dctm22077;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mssv_71dctm22077.adapter.CustomAdapter;
import com.mssv_71dctm22077.sqlite.MyDatabaseHelper;

import java.util.ArrayList;

public class CategoryActivity extends AppCompatActivity {

  RecyclerView recyclerView;
  FloatingActionButton addButton;

  MyDatabaseHelper myDB;
  ArrayList <String> categoryId, categoryName, categoryCreated;
  CustomAdapter customAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_danhmuc);

    recyclerView = findViewById(R.id.recyclerViewDanhMuc);
    addButton = findViewById(R.id.add_button);
    addButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(CategoryActivity.this, AddCategoryActivity.class);
        startActivity(intent);
      }
    });

    myDB = new MyDatabaseHelper(CategoryActivity.this);
    categoryId = new ArrayList<>();
    categoryName = new ArrayList<>();
    categoryCreated = new ArrayList<>();

    storeDataArrays();
    customAdapter = new CustomAdapter(CategoryActivity.this, categoryId,categoryName,categoryCreated);
    recyclerView.setAdapter(customAdapter);
    recyclerView.setLayoutManager(new LinearLayoutManager(CategoryActivity.this));

  }

  void storeDataArrays (){
    Cursor cursor = myDB.getAllDanhMuc();
    if (cursor.getCount() == 0){
      Toast.makeText(this, "Dữ liệu rỗng.", Toast.LENGTH_SHORT).show();
    }else{
      while (cursor.moveToNext()){
        categoryId.add(cursor.getString(0));
        categoryName.add(cursor.getString(1));
        categoryCreated.add(cursor.getString(2));
      }
    }
  }

  @Override
  protected void onResume() {
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
}
