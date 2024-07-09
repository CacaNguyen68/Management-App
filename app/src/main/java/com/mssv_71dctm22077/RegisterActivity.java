package com.mssv_71dctm22077;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.mssv_71dctm22077.Category.UpdateCategoryActivity;
import com.mssv_71dctm22077.sqlite.MyDatabaseHelper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class RegisterActivity extends AppCompatActivity {

  private EditText etName, etDOB, etPhone, etEmail, etUsername, etPassword, etConfirmPassword;
  private Button btnRegister, btnChooseImage;
  private ImageView imageView;
  private static final int PICK_IMAGE_REQUEST = 1;
  private Uri imageUri;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    EdgeToEdge.enable(this);
    setContentView(R.layout.activity_register);

    // Ánh xạ các thành phần từ layout
    etName = findViewById(R.id.etName);
    etDOB = findViewById(R.id.etName);
    etPhone = findViewById(R.id.etPhone);
    etEmail = findViewById(R.id.etEmail);
    etUsername = findViewById(R.id.etUsername);
    etPassword = findViewById(R.id.etPassword);
    etConfirmPassword = findViewById(R.id.etConfirmPassword);
    btnRegister = findViewById(R.id.btnRegister);
    btnChooseImage = findViewById(R.id.btnChooseImage);
    imageView = findViewById(R.id.imageView);

    // Xử lý sự kiện click cho nút Đăng ký
    btnRegister.setOnClickListener(v -> registerUser());

    // Xử lý sự kiện click cho nút Chọn hình ảnh
    btnChooseImage.setOnClickListener(v -> openFileChooser());

    // Xử lý padding cho layout khi có system bars
    ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
      Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
      v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
      return insets;
    });
  }

  // Phương thức để đăng ký người dùng
  private void registerUser() {
    String name = etName.getText().toString().trim();
    String dob = etDOB.getText().toString().trim();
    String phone = etPhone.getText().toString().trim();
    String email = etEmail.getText().toString().trim();
    String username = etUsername.getText().toString().trim();
    String password = etPassword.getText().toString();
    String confirmPassword = etConfirmPassword.getText().toString();

    // Kiểm tra các điều kiện
    if (name.isEmpty() || dob.isEmpty() || phone.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
      Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
      return;
    }

    // Kiểm tra định dạng email
    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
      Toast.makeText(this, "Email không hợp lệ", Toast.LENGTH_SHORT).show();
      return;
    }

    // Kiểm tra số điện thoại có 10 số
    if (phone.length() != 10 || !TextUtils.isDigitsOnly(phone)) {
      Toast.makeText(this, "Số điện thoại không hợp lệ", Toast.LENGTH_SHORT).show();
      return;
    }

    // Kiểm tra mật khẩu và xác nhận mật khẩu phải trùng khớp
    if (!password.equals(confirmPassword)) {
      Toast.makeText(this, "Mật khẩu và xác nhận mật khẩu không khớp", Toast.LENGTH_SHORT).show();
      return;
    }

    // Mã hoá mật khẩu trước khi lưu vào cơ sở dữ liệu
    String hashedPassword = hashPassword(password);

    // Chuyển đổi Bitmap thành chuỗi Base64
    String encodedImage = "";
    if (imageUri != null) {
      try {
        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
        encodedImage = bitmapToBase64(bitmap);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    MyDatabaseHelper myDB = new MyDatabaseHelper(RegisterActivity.this);
    // Lưu thông tin người dùng vào cơ sở dữ liệu
//    myDB.addUser(name, dob, phone, email, username, hashedPassword, encodedImage);

    // Hiển thị thông báo hoàn tất đăng ký
    Toast.makeText(this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
    finish(); // Đóng activity sau khi đăng ký thành công
  }

  // Phương thức để mã hoá mật khẩu bằng thuật toán SHA-256
  private String hashPassword(String password) {
    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      byte[] hash = digest.digest(password.getBytes());
      StringBuilder hexString = new StringBuilder();
      for (byte b : hash) {
        String hex = Integer.toHexString(0xff & b);
        if (hex.length() == 1) hexString.append('0');
        hexString.append(hex);
      }
      return hexString.toString();
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
    return null;
  }

  // Phương thức để chuyển đổi Bitmap thành chuỗi Base64
  private String bitmapToBase64(Bitmap bitmap) {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
    byte[] imageBytes = byteArrayOutputStream.toByteArray();
    return Base64.encodeToString(imageBytes, Base64.DEFAULT);
  }

  // Phương thức để mở Gallery và chọn hình ảnh từ bộ nhớ thiết bị
  private void openFileChooser() {
    Intent intent = new Intent();
    intent.setType("image/*");
    intent.setAction(Intent.ACTION_GET_CONTENT);
    startActivityForResult(intent, PICK_IMAGE_REQUEST);
  }

  // Xử lý kết quả khi người dùng chọn hình ảnh từ Gallery
  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
      imageUri = data.getData();
      try {
        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
        imageView.setImageBitmap(bitmap);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
