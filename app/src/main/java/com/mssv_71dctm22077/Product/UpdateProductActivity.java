package com.mssv_71dctm22077.Product;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mssv_71dctm22077.Category.UpdateCategoryActivity;
import com.mssv_71dctm22077.R;
import com.mssv_71dctm22077.sqlite.MyDatabaseHelper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class UpdateProductActivity extends AppCompatActivity {
  EditText name_input, price_input;
  Button update_button, reset_button, image_button;
  String id, name, price, categoryId;
  int idProduct;
  ImageView product_image;
  Toolbar toolbar;
  Spinner spinnerCategory;
  private MyDatabaseHelper mb;

  private static final int PICK_IMAGE_REQUEST = 1;
  private Bitmap selectedImageBitmap;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_update_product);

    toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    name_input = findViewById(R.id.editTextProductName);
    price_input = findViewById(R.id.editTextProductPrice);
    spinnerCategory = findViewById(R.id.spinnerCategory);
    update_button = findViewById(R.id.buttonUpdateProduct);
    reset_button = findViewById(R.id.buttonClearProduct);
    image_button = findViewById(R.id.buttonSelectImage);
    product_image = findViewById(R.id.imageViewProduct);

    mb = new MyDatabaseHelper(this);

    image_button.setOnClickListener(view -> openImageChooser());

    // Lấy danh sách danh mục từ cơ sở dữ liệu
    List<String> categoryList = mb.getCategoryList();
    ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, R.layout.spinner_item_list_category, categoryList);
    categoryAdapter.setDropDownViewResource(R.layout.spinner_item_list_category);
    spinnerCategory.setAdapter(categoryAdapter);

    // Lấy dữ liệu intent và hiển thị lên EditText
    getAndSetIntentData();

    // Lưu tên ban đầu của sản phẩm


    update_button.setOnClickListener(view -> {
      String newName = name_input.getText().toString().trim();
      String newPrice = price_input.getText().toString().trim();
      idProduct = Integer.parseInt(id);
      int newCategoryId = spinnerCategory.getSelectedItemPosition() + 1;

      if (newName.isEmpty() || newPrice.isEmpty()) {
        Toast.makeText(UpdateProductActivity.this, "Vui lòng nhập đầy đủ thông tin sản phẩm", Toast.LENGTH_SHORT).show();
      } else {
        // Cập nhật sản phẩm trong cơ sở dữ liệu
        Date today = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String createdAt = formatter.format(today);
        Log.d("ID UPDATE","ID = "+id);
        mb.updateProduct(idProduct, newName, Double.parseDouble(newPrice), newCategoryId, createdAt, "Admin", getBitmapAsByteArray(selectedImageBitmap));
        finish();
      }
    });

    reset_button.setOnClickListener(view -> {
      name_input.getText().clear();
      price_input.getText().clear();
      product_image.setImageResource(R.drawable.baseline_yard_24);
      selectedImageBitmap = null;
    });

    // Thiết lập sự kiện khi nhấn nút back trên thanh công cụ
    toolbar.setNavigationOnClickListener(view -> finish());
  }

  // Lấy dữ liệu từ Intent và hiển thị lên EditText
  void getAndSetIntentData() {
    if (getIntent().hasExtra("id") && getIntent().hasExtra("name") && getIntent().hasExtra("price") && getIntent().hasExtra("categoryId")) {
      id = getIntent().getStringExtra("id");
      name = getIntent().getStringExtra("name");
      price = getIntent().getStringExtra("price");
      categoryId = getIntent().getStringExtra("categoryId");

//      idProduct = Integer.parseInt(id);
      name_input.setText(name);
      price_input.setText(price);

      // Set selected category in spinner
      List<String> categoryList = mb.getCategoryList();
      ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, R.layout.spinner_item_list_category, categoryList);
      categoryAdapter.setDropDownViewResource(R.layout.spinner_item_list_category);
      spinnerCategory.setAdapter(categoryAdapter);

      if (categoryId != null) {
        int categoryIntId = Integer.parseInt(categoryId);
        Log.d("id lay category ", "lay ra: " + categoryId);
//        String nameCategory = mb.getCategoryNameById(product.getCategoryId());
        spinnerCategory.setSelection(categoryIntId - 1);
      }

      if (getIntent().hasExtra("image")) {
        byte[] byteArray = getIntent().getByteArrayExtra("image");
        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        product_image.setImageBitmap(bmp);
      } else {
        product_image.setImageResource(R.drawable.baseline_yard_24);
      }
    } else {
      Toast.makeText(this, "Không có dữ liệu", Toast.LENGTH_SHORT).show();
    }
  }

  private void openImageChooser() {
    Intent intent = new Intent();
    intent.setType("image/*");
    intent.setAction(Intent.ACTION_GET_CONTENT);
    startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
      Uri imageUri = data.getData();
      try {
        selectedImageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
        product_image.setImageBitmap(selectedImageBitmap);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  private byte[] getBitmapAsByteArray(Bitmap bitmap) {
    if (bitmap == null) return null;
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
    return outputStream.toByteArray();
  }
}

