package com.mssv_71dctm22077;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mssv_71dctm22077.sqlite.MyDatabaseHelper;

public class AddCategoryActivity extends AppCompatActivity {

  EditText tendanhmuc_input;

  Button add_button, reset_button;

  FloatingActionButton back_floating_button, menu_floating_button;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_danh_muc_them);

    tendanhmuc_input = findViewById(R.id.tenDanhMuc);
    add_button = findViewById(R.id.buttonAdd);

    add_button.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        MyDatabaseHelper myDB = new MyDatabaseHelper(AddCategoryActivity.this);
        myDB.addDanhMuc(tendanhmuc_input.getText().toString().trim());
        finish();
      }
    });

    reset_button = findViewById(R.id.buttonReset);
    reset_button.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        tendanhmuc_input.getText().clear();
      }
    });

    back_floating_button = findViewById(R.id.back_button);
    back_floating_button.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        finish();
      }
    });

    menu_floating_button = findViewById(R.id.menu_button);
    menu_floating_button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//          Intent intent = new Intent(getApplicationContext(), NavigationActivity.class);
//          intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//          startActivity(intent);
        }
      });
  }
}
