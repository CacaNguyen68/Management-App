package com.mssv_71dctm22077;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mssv_71dctm22077.sqlite.MyDatabaseHelper;
import com.mssv_71dctm22077.user.RegisterActivity;

import org.mindrot.jbcrypt.BCrypt;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

  Button registerButton, loginButton;

  EditText edPhone, edPassword;

  MyDatabaseHelper myDB;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_main);
    MyDatabaseHelper myDB = new MyDatabaseHelper(this);
    storeDataArrays();
//    storeDataArraysCategory();
//    storeDataArraysProduct();

    edPhone = findViewById(R.id.edittext_phone);
    edPassword = findViewById(R.id.edittext_password);

    registerButton = findViewById(R.id.button_register);
    registerButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
        startActivity(intent);
      }
    });


    loginButton = findViewById(R.id.button_login);
    loginButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        login();
      }
    });

  }

  private void login() {
    String phone = edPhone.getText().toString().trim();
    String password = edPassword.getText().toString().trim();
//    Log.d("pass",hashedPassword);

    MyDatabaseHelper mb = new MyDatabaseHelper(MainActivity.this);
    mb.getAllUser();

    if (mb.checkUser(phone, password)) {
      Toast.makeText(this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
      String typeUser = mb.getTypeUserByPhone(phone);
      Log.d("Type User", typeUser);
      if (typeUser.equals("ADMIN")) {
        // Chuyển sang màn hình chính hoặc làm bất kỳ hành động nào khác sau khi đăng nhập thành công
        Intent intent = new Intent(MainActivity.this, MenuAdminActivity.class);
        intent.putExtra("phone", phone);
        startActivity(intent);
      } else {
// Chuyển sang màn hình chính hoặc làm bất kỳ hành động nào khác sau khi đăng nhập thành công
        Intent intent = new Intent(MainActivity.this, MenuUserActivity.class);
        intent.putExtra("phone", phone);
        startActivity(intent);
      }

    } else {
      Toast.makeText(this, "Sai tên đăng nhập hoặc mật khẩu", Toast.LENGTH_SHORT).show();
      edPhone.getText().clear();
      edPassword.getText().clear();
    }
  }

  @Override
  public void onResume() {
    super.onResume();
    // Clear old data
    edPhone.getText().clear();
    edPassword.getText().clear();

  }


  void storeDataArrays() {
    MyDatabaseHelper myDB = new MyDatabaseHelper(this);
    Cursor cursor = myDB.getAllUser();

    if (cursor.getCount() == 0) {
      Date today = new Date();
      SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

      myDB.addUser("Super Admin", formatter.format(today), "012345678", "thubackend2022@gmail.com", BCrypt.hashpw("superadmin", BCrypt.gensalt()), "ADMIN", null);
      myDB.addUser("Join User", formatter.format(today), "88888888", "john.doe@example.com", BCrypt.hashpw("88888888", BCrypt.gensalt()), "USER", null);
      myDB.addUser("Kate Admin", formatter.format(today), "66666666", "kate.doe@example.com", BCrypt.hashpw("66666666", BCrypt.gensalt()), "ADMIN", null);

      myDB.addDanhMuc("Trẻ em");
      myDB.addDanhMuc("Nhân vật");
      myDB.addDanhMuc("Tóc giả");
      myDB.addDanhMuc("Sexy");
      myDB.addDanhMuc("Phụ kiện");
      myDB.addDanhMuc("Cổ trang");

      myDB.addProduct("[Kuroko no Basket] Đồng phục trường TEIKO", 650000, 1, formatter.format(today), "Super Admin", null);
      myDB.addProduct("Đồng phục học sinh Nhật Bản – Màu đen", 150000, 1, formatter.format(today), "Super Admin", null);
      myDB.addProduct("Hán Phục HPW26", 180000, 6, formatter.format(today), "Super Admin", null);
      myDB.addProduct("Hán Phục HPW20", 180000, 6, formatter.format(today), "Super Admin", null);
      myDB.addProduct("[JUJUTSU KAISEN] Tóc giả Gojo Satoru", 330000, 3, formatter.format(today), "Super Admin", null);
      myDB.addProduct("[Jigoku Shōjo] Hone Onna", 1000000, 4, formatter.format(today), "Super Admin", null);
      myDB.addProduct("Vương miện yêu tinh", 40000, 5, formatter.format(today), "Super Admin", null);
      myDB.addProduct("Cà Vạt (Caravat) Harry Potter", 30000, 5, formatter.format(today), "Super Admin", null);
      myDB.addProduct("[NARUTO] Costume Naruto", 220000, 2, formatter.format(today), "Super Admin", null);
      myDB.addProduct("[Naruto] Áo khoác Hokage đệ tứ – Minato", 250000, 2, formatter.format(today), "Super Admin", null);


    }
  }
}
