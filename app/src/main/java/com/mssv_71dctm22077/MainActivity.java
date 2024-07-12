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

import com.mssv_71dctm22077.Category.CategoryActivity;
import com.mssv_71dctm22077.sqlite.MyDatabaseHelper;

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
    Cursor cursor = myDB.getAllUser();

    if (cursor.getCount() == 0) {
      Date today = new Date();
      SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

      String hashedPassword = BCrypt.hashpw("superadmin", BCrypt.gensalt());
      myDB.addUser("Super Admin", formatter.format(today), "012345678", "thubackend2022@gmail.com", hashedPassword, "ADMIN", null);

    }

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
        startActivity(intent);
      } else {
// Chuyển sang màn hình chính hoặc làm bất kỳ hành động nào khác sau khi đăng nhập thành công
        Intent intent = new Intent(MainActivity.this, CategoryActivity.class);
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
    Cursor cursor = myDB.getAllUser();
    if (cursor.getCount() == 0) {
      MyDatabaseHelper myDB = new MyDatabaseHelper(this);
      Date today = new Date();
      SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
      String hashedPassword = BCrypt.hashpw("superadmin", BCrypt.gensalt());
      myDB.addUser("Super Admin", formatter.format(today), "012345678", "thubackend2022@gmail.com", hashedPassword, "ADMIN", null);

    }
  }
}
