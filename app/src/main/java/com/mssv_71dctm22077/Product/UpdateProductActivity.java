package com.mssv_71dctm22077.Product;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.mssv_71dctm22077.R;
import com.mssv_71dctm22077.sqlite.MyDatabaseHelper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class UpdateProductActivity extends AppCompatActivity {

  private EditText editTextProductName, editTextProductPrice;
  private Spinner spinnerCategory;
  private ImageView imageViewProduct;
  private Button buttonSelectImage, buttonUpdateProduct, buttonClearProduct;
  private MyDatabaseHelper db;
  private Bitmap selectedImageBitmap;
  private int productId; // ID sản phẩm

  private static final int PICK_IMAGE_REQUEST = 1;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_update_product);

    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    toolbar.setNavigationOnClickListener(v -> onBackPressed());

    editTextProductName = findViewById(R.id.editTextProductName);
    editTextProductPrice = findViewById(R.id.editTextProductPrice);
    spinnerCategory = findViewById(R.id.spinnerCategory);
    imageViewProduct = findViewById(R.id.imageViewProduct);
    buttonSelectImage = findViewById(R.id.buttonSelectImage);
    buttonUpdateProduct = findViewById(R.id.buttonUpdateProduct);
    buttonClearProduct = findViewById(R.id.buttonClearProduct);

    db = new MyDatabaseHelper(this);

    // Lấy thông tin sản phẩm từ Intent
    Intent intent = getIntent();
    productId = intent.getIntExtra("id", -1);

    if (productId != -1) {
      loadProductDetails(productId);
    }

    // Thiết lập Spinner danh mục
    List<String> categoryList = db.getCategoryList();
    ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, R.layout.spinner_item_list_category, categoryList);
    categoryAdapter.setDropDownViewResource(R.layout.spinner_item_list_category);
    spinnerCategory.setAdapter(categoryAdapter);

    buttonSelectImage.setOnClickListener(view -> openImageChooser());

    buttonUpdateProduct.setOnClickListener(view -> {
      String productName = editTextProductName.getText().toString().trim();
      String productPriceStr = editTextProductPrice.getText().toString().trim();
      int categoryId = spinnerCategory.getSelectedItemPosition() + 1;

      if (productName.isEmpty() || productPriceStr.isEmpty()) {
        Toast.makeText(UpdateProductActivity.this, "Please enter product name and price", Toast.LENGTH_SHORT).show();
        return;
      }

      double productPrice = Double.parseDouble(productPriceStr);

      Date today = new Date();
      SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
      String createdAt = formatter.format(today);
      String userCreated = "Admin";

      db.updateProduct(productId, productName, productPrice, categoryId, createdAt, userCreated, getBitmapAsByteArray(selectedImageBitmap));
      Toast.makeText(UpdateProductActivity.this, "Product updated successfully", Toast.LENGTH_SHORT).show();
      finish();
    });

    buttonClearProduct.setOnClickListener(view -> {
      editTextProductName.getText().clear();
      editTextProductPrice.getText().clear();
      imageViewProduct.setImageResource(R.drawable.baseline_yard_24);
      selectedImageBitmap = null;
    });
  }

  private void loadProductDetails(int productId) {
    Cursor cursor = db.getProductById(productId); // Lấy sản phẩm theo ID
    if (cursor.moveToFirst()) {
      String productName = cursor.getString(cursor.getColumnIndex("productName"));
      double productPrice = cursor.getDouble(cursor.getColumnIndex("productPrice"));
      int categoryId = cursor.getInt(cursor.getColumnIndex("categoryId"));
      String createdAt = cursor.getString(cursor.getColumnIndex("createdAt"));
      String userCreated = cursor.getString(cursor.getColumnIndex("userCreated"));
      byte[] productImage = cursor.getBlob(cursor.getColumnIndex("productImage"));

      editTextProductName.setText(productName);
      editTextProductPrice.setText(String.valueOf(productPrice));
      spinnerCategory.setSelection(categoryId - 1);

      if (productImage != null) {
        selectedImageBitmap = BitmapFactory.decodeByteArray(productImage, 0, productImage.length);
        imageViewProduct.setImageBitmap(selectedImageBitmap);
      }
    }
    cursor.close();
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

  private byte[] getBitmapAsByteArray(Bitmap bitmap) {
    if (bitmap == null) return null;
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
    return outputStream.toByteArray();
  }
}
