package com.mssv_71dctm22077.Category;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.mssv_71dctm22077.MenuAdminActivity;
import com.mssv_71dctm22077.R;
import com.mssv_71dctm22077.sqlite.MyDatabaseHelper;

public class AddCategoryActivity extends AppCompatActivity {

  EditText tendanhmuc_input;

  Button add_button, reset_button;

  Toolbar toolbar;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_category_add);

    toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    tendanhmuc_input = findViewById(R.id.tenDanhMuc);
    add_button = findViewById(R.id.buttonAdd);

    add_button.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        String tenDanhMucText = tendanhmuc_input.getText().toString().trim();
        if (tenDanhMucText.isEmpty()) {
          Toast.makeText(AddCategoryActivity.this, "Vui lòng nhập tên danh mục", Toast.LENGTH_SHORT).show();
        } else {
          MyDatabaseHelper myDB = new MyDatabaseHelper(AddCategoryActivity.this);
          if (myDB.isDanhMucExists(tenDanhMucText)) {
            Toast.makeText(AddCategoryActivity.this, "Danh mục đã tồn tại!", Toast.LENGTH_SHORT).show();
          } else {
            myDB.addDanhMuc(tenDanhMucText);
            Toast.makeText(AddCategoryActivity.this, "Thêm danh mục thành công", Toast.LENGTH_SHORT).show();
            finish();
          }
        }
      }
    });

    reset_button = findViewById(R.id.buttonReset);
    reset_button.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        tendanhmuc_input.getText().clear();
      }
    });

    // Handle navigation icon click (back button click)
    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        finish();
      }
    });
  }

  //My menu
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_user, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    if (item.getItemId() == R.id.action_home) {
      // Xử lý khi nhấn vào mục menu
      Intent intent = new Intent(this, MenuAdminActivity.class);
      startActivity(intent);
      return true;
    }
    return super.onOptionsItemSelected(item);
  }


}
