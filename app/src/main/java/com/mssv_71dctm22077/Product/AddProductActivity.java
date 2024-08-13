package com.mssv_71dctm22077.Product;


import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.mssv_71dctm22077.MenuAdminActivity;
import com.mssv_71dctm22077.R;
import com.mssv_71dctm22077.sqlite.MyDatabaseHelper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddProductActivity extends AppCompatActivity {
  private EditText editTextProductName, editTextProductPrice;
  private Spinner spinnerCategory;
  private ImageView imageViewProduct;
  private Button buttonSelectImage, buttonAddProduct, buttonClearProduct;
  private MyDatabaseHelper db;
  private Bitmap selectedImageBitmap;

  private static final int PICK_IMAGE_REQUEST = 1;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add_product);

    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    toolbar.setNavigationOnClickListener(v -> onBackPressed());

    editTextProductName = findViewById(R.id.editTextProductName);
    editTextProductPrice = findViewById(R.id.editTextProductPrice);
    spinnerCategory = findViewById(R.id.spinnerCategory);
    imageViewProduct = findViewById(R.id.imageViewProduct);
    buttonSelectImage = findViewById(R.id.buttonSelectImage);
    buttonAddProduct = findViewById(R.id.buttonAddProduct);
    buttonClearProduct = findViewById(R.id.buttonClearProduct);

    // Khởi tạo MyDatabaseHelper
    db = new MyDatabaseHelper(this);

    // Lấy danh sách danh mục từ cơ sở dữ liệu
    List<String> categoryList = db.getCategoryList();
    ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, R.layout.spinner_item_list_category, categoryList);
    categoryAdapter.setDropDownViewResource(R.layout.spinner_item_list_category);
    spinnerCategory.setAdapter(categoryAdapter);

    buttonSelectImage.setOnClickListener(view -> openImageChooser());

    buttonAddProduct.setOnClickListener(view -> {
      String productName = editTextProductName.getText().toString().trim();
      String productPriceStr = editTextProductPrice.getText().toString().trim();
      int categoryId = spinnerCategory.getSelectedItemPosition() + 1; // Lấy vị trí đã chọn trong Spinner
      String userCreated = "Admin"; // Đây là giá trị mặc định, bạn có thể cần lấy từ đâu đó khác

      // Kiểm tra và xử lý khi người dùng không nhập đầy đủ thông tin
      if (productName.isEmpty() || productPriceStr.isEmpty()) {
        Toast.makeText(AddProductActivity.this, "Please enter product name and price", Toast.LENGTH_SHORT).show();
        return;
      }

      // Chuyển đổi giá sản phẩm từ String sang số nguyên
      Double productPrice = Double.parseDouble(productPriceStr);

      // Lấy ngày hiện tại và định dạng thành chuỗi "dd-MM-yyyy"
      Date today = new Date();
      SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
      String createdAt = formatter.format(today);

      // Chèn sản phẩm vào cơ sở dữ liệu
      db.addProduct(productName, productPrice, categoryId, createdAt, userCreated, getBitmapAsByteArray(selectedImageBitmap));
      finish();
      // Kiểm tra kết quả và hiển thị thông báo
//      if (result > 0) {
//        Toast.makeText(AddProductActivity.this, "Product added successfully", Toast.LENGTH_SHORT).show();
//        // Xử lý khi thêm sản phẩm thành công, ví dụ: quay về màn hình trước đó
//        finish(); // Kết thúc activity hiện tại sau khi thêm sản phẩm thành công
//      } else {
//        Toast.makeText(AddProductActivity.this, "Failed to add product", Toast.LENGTH_SHORT).show();
//      }
    });

    buttonClearProduct.setOnClickListener(view -> {
      editTextProductName.getText().clear();
      editTextProductPrice.getText().clear();
      imageViewProduct.setImageResource(R.drawable.baseline_yard_24); // Đặt lại ảnh mặc định
      selectedImageBitmap = null; // Đặt lại ảnh đã chọn
    });
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
        imageViewProduct.setImageBitmap(selectedImageBitmap);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  // Chuyển đổi Bitmap thành mảng byte[]
  private byte[] getBitmapAsByteArray(Bitmap bitmap) {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
    return outputStream.toByteArray();
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
      Intent intent = new Intent(this, MenuAdminActivity.class);
      startActivity(intent);
      return true;
    }
    return super.onOptionsItemSelected(item);
  }
}
