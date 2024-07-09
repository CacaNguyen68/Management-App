package com.mssv_71dctm22077.Category;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mssv_71dctm22077.R;
import com.mssv_71dctm22077.sqlite.MyDatabaseHelper;

public class UpdateCategoryActivity extends AppCompatActivity {
  EditText name_input;
  Button update_button, reset_button;
  String id, name, originalName;
  FloatingActionButton back_floating_button, menu_floating_button;
  Toolbar toolbar;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_update_category);

    toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    name_input = findViewById(R.id.tenDanhMuc);
    update_button = findViewById(R.id.buttonUpdate);
    reset_button = findViewById(R.id.buttonReset);

    // Lấy dữ liệu intent và hiển thị lên EditText
    getAndSetIntentData();

    // Lưu tên ban đầu của danh mục
    originalName = name_input.getText().toString().trim();

    update_button.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        String newName = name_input.getText().toString().trim();
        // Kiểm tra nếu không có sự thay đổi
        if (newName.isEmpty()) {
          Toast.makeText(UpdateCategoryActivity.this, "Vui lòng nhập tên danh mục", Toast.LENGTH_SHORT).show();
        } else {
          if (newName.equals(originalName)) {
            Toast.makeText(UpdateCategoryActivity.this, "Chưa có sự thay đổi", Toast.LENGTH_SHORT).show();
          } else {
            // Cập nhật danh mục trong cơ sở dữ liệu
            MyDatabaseHelper myDB = new MyDatabaseHelper(UpdateCategoryActivity.this);
            myDB.updateCategory(id, newName);
            finish();
          }
        }
      }
    });

    reset_button.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        name_input.getText().clear();
      }
    });

//    back_floating_button.setOnClickListener(new View.OnClickListener() {
//      @Override
//      public void onClick(View v) {
//        finish();
//      }
//    });

    // Thiết lập sự kiện khi nhấn nút back trên thanh công cụ
    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        finish();
      }
    });
  }

  // Lấy dữ liệu từ Intent và hiển thị lên EditText
  void getAndSetIntentData() {
    if (getIntent().hasExtra("id") && getIntent().hasExtra("name")) {
      id = getIntent().getStringExtra("id");
      name = getIntent().getStringExtra("name");
      name_input.setText(name);
    } else {
      Toast.makeText(this, "Không có dữ liệu", Toast.LENGTH_SHORT).show();
    }
  }

  // Xác nhận khi người dùng muốn xoá danh mục
  void confirmDialog() {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setTitle("Xoá danh mục " + name + " này?");
    builder.setMessage("Bạn có muốn xóa danh mục " + name + " này?");
    builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        MyDatabaseHelper myDB = new MyDatabaseHelper(UpdateCategoryActivity.this);
        myDB.deleteCategory(id);
        finish();
      }
    });
    builder.setNegativeButton("Hủy bỏ", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        // Không làm gì nếu người dùng hủy bỏ
      }
    });
    builder.create().show();
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
