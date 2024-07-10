package com.mssv_71dctm22077;

import android.content.Intent;
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

public class MainActivity extends AppCompatActivity {

  Button registerButton, loginButton;

  EditText edPhone, edPassword;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_main);
    MyDatabaseHelper mb = new MyDatabaseHelper(this);
    mb.getAllUser();

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
    if (mb.checkUser(phone,password)){
      Toast.makeText(this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
      // Chuyển sang màn hình chính hoặc làm bất kỳ hành động nào khác sau khi đăng nhập thành công
      Intent intent = new Intent(MainActivity.this, CategoryActivity.class);
      startActivity(intent);
    } else {
      Toast.makeText(this, "Sai tên đăng nhập hoặc mật khẩu", Toast.LENGTH_SHORT).show();
      edPhone.getText().clear();
      edPassword.getText().clear();
    }
  }
}
