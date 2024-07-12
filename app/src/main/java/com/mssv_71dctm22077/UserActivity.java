package com.mssv_71dctm22077;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mssv_71dctm22077.adapter.UserAdapter;
import com.mssv_71dctm22077.model.User;
import com.mssv_71dctm22077.sqlite.MyDatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class UserActivity extends AppCompatActivity {

  private RecyclerView recyclerView;
  private UserAdapter userAdapter;
  private List<User> userList;
  private MyDatabaseHelper myDB;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    EdgeToEdge.enable(this);
    setContentView(R.layout.activity_user);
    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    recyclerView = findViewById(R.id.recyclerViewUser);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));

    myDB = new MyDatabaseHelper(this);
    userList = new ArrayList<>();

    loadData();

    userAdapter = new UserAdapter(this, userList);
    recyclerView.setAdapter(userAdapter);

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
    Cursor cursor = myDB.getAllUser();
    if (cursor.getCount() > 0) {
      while (cursor.moveToNext()) {
        int id = cursor.getInt(cursor.getColumnIndex("id"));
        String name = cursor.getString(cursor.getColumnIndex("ten_nguoi_dung"));
        String dob = cursor.getString(cursor.getColumnIndex("ngay_sinh"));
        String phone = cursor.getString(cursor.getColumnIndex("so_dien_thoai"));
        String email = cursor.getString(cursor.getColumnIndex("email"));
        String userType = cursor.getString(cursor.getColumnIndex("loai_nguoi_dung"));
        String created = cursor.getString(cursor.getColumnIndex("ngay_khoi_tao"));
        String userCreated = cursor.getString(cursor.getColumnIndex("nguoi_khoi_tao"));
        byte[] image = cursor.getBlob(cursor.getColumnIndex("hinh_anh"));

        userList.add(new User(id, name, dob, phone, email, userType, created, userCreated, image));
        // In ra thông tin từng người dùng
        Log.d(TAG, "User ID: " + id);
        Log.d(TAG, "Name: " + name);
        Log.d(TAG, "Date of Birth: " + dob);
        Log.d(TAG, "Phone: " + phone);
        Log.d(TAG, "Email: " + email);
        Log.d(TAG, "User Type: " + userType);
        Log.d(TAG, "Created Date: " + created);
        // Ví dụ in ra kích thước của hình ảnh (nếu cần)
        Log.d(TAG, "Image Size: " + (image != null ? image.length : 0));
        Log.d(TAG, "---------------------------------");
      }
    }
  }

//  // Load dữ liệu từ SQLite và cập nhật RecyclerView
  private void loadDataSearch(String query) {
    if (TextUtils.isEmpty(query)) {
      userList.clear();
      userList.addAll(myDB.getAllUsers());
    } else {
      userList.clear();
      userList.addAll(myDB.searchUsers(query));
    }
    userAdapter.notifyDataSetChanged();
  }
}
