package com.mssv_71dctm22077;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.mssv_71dctm22077.Category.UpdateCategoryActivity;
import com.mssv_71dctm22077.sqlite.MyDatabaseHelper;

import org.mindrot.jbcrypt.BCrypt;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class RegisterActivity extends AppCompatActivity {

  private EditText etName, etPhone, etEmail, etUsername, etPassword, etConfirmPassword;
  private TextView tvDOB;
  private Spinner spinnerRole;
  private ImageView imageView;
  private Button btnChooseImage, btnRegister;
  private Bitmap selectedImageBitmap;

  private static final int PICK_IMAGE_REQUEST = 1;
  private Calendar myCalendar = Calendar.getInstance();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_register);

    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    toolbar.setNavigationOnClickListener(view -> onBackPressed());

    etName = findViewById(R.id.etName);
    tvDOB = findViewById(R.id.tvDOB);
    etPhone = findViewById(R.id.etPhone);
    etEmail = findViewById(R.id.etEmail);
    etPassword = findViewById(R.id.etPassword);
    etConfirmPassword = findViewById(R.id.etConfirmPassword);
    spinnerRole = findViewById(R.id.spinnerRole);
    // Tạo Adapter cho Spinner
    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
      R.array.user_types, android.R.layout.simple_spinner_item);
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    spinnerRole.setAdapter(adapter);
    imageView = findViewById(R.id.imageView);
    btnChooseImage = findViewById(R.id.btnChooseImage);
    btnRegister = findViewById(R.id.btnRegister);

    tvDOB.setOnClickListener(view -> showDatePickerDialog());

    btnChooseImage.setOnClickListener(view -> openImageChooser());

    btnRegister.setOnClickListener(view -> registerUser());
  }

  private void showDatePickerDialog() {
    DatePickerDialog datePickerDialog = new DatePickerDialog(RegisterActivity.this, (view, year, month, day) -> {
      myCalendar.set(Calendar.YEAR, year);
      myCalendar.set(Calendar.MONTH, month);
      myCalendar.set(Calendar.DAY_OF_MONTH, day);
      updateLabel();
    }, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
    datePickerDialog.show();
  }

  private void updateLabel() {
    String myFormat = "dd-MM-yyyy";
    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
    tvDOB.setText(sdf.format(myCalendar.getTime()));
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
        imageView.setImageBitmap(selectedImageBitmap);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  private void registerUser() {
    String name = etName.getText().toString().trim();
    String dob = tvDOB.getText().toString().trim();
    String phone = etPhone.getText().toString().trim();
    String email = etEmail.getText().toString().trim();
    String password = etPassword.getText().toString().trim();
    String confirmPassword = etConfirmPassword.getText().toString().trim();
    String userType = spinnerRole.getSelectedItem().toString();

    if (!password.equals(confirmPassword)) {
      Toast.makeText(this, "Mật khẩu và xác nhận mật khẩu không khớp", Toast.LENGTH_SHORT).show();
      return;
    }

    // Mã hóa mật khẩu
    String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

    if (userType.equals("Khách hàng")){
      userType = "USER";
    }else{
      userType = "ADMIN";
    }

    byte[] image = getBitmapAsByteArray(selectedImageBitmap);

    MyDatabaseHelper mb = new MyDatabaseHelper(this);
    mb.addUser(name, dob, phone, email, hashedPassword, userType, image);

    Toast.makeText(this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
    finish();
  }

  private byte[] getBitmapAsByteArray(Bitmap bitmap) {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
    return outputStream.toByteArray();
  }

  public void togglePasswordVisibility(View view) {
    ImageView imageView = (ImageView) view;
    TextInputEditText passwordEditText = findViewById(R.id.etPassword);

    if (passwordEditText.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
      // Hide the password
      passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
      imageView.setImageResource(R.drawable.ic_visibility_off); // Set icon to hide
    } else {
      // Show the password
      passwordEditText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
      imageView.setImageResource(R.drawable.ic_visibility); // Set icon to show
    }

    // Move cursor to the end of text
    passwordEditText.setSelection(passwordEditText.getText().length());
  }

  public void toggleConfirmPasswordVisibility(View view) {
    ImageView imageView = (ImageView) view;
    TextInputEditText confirmPasswordEditText = findViewById(R.id.etConfirmPassword);

    if (confirmPasswordEditText.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
      // Hide the password
      confirmPasswordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
      imageView.setImageResource(R.drawable.ic_visibility_off); // Set icon to hide
    } else {
      // Show the password
      confirmPasswordEditText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
      imageView.setImageResource(R.drawable.ic_visibility); // Set icon to show
    }

    // Move cursor to the end of text
    confirmPasswordEditText.setSelection(confirmPasswordEditText.getText().length());
  }

}
