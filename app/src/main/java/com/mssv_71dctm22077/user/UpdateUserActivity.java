package com.mssv_71dctm22077.user;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.mssv_71dctm22077.R;
import com.mssv_71dctm22077.sqlite.MyDatabaseHelper;

import org.mindrot.jbcrypt.BCrypt;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class UpdateUserActivity extends AppCompatActivity {

    private EditText etName, etPhone, etEmail, etPassword;
    private TextView tvDOB;
    private Spinner spinnerRole;
    private ImageView imageView;
    private Button btnChooseImage, btnUpdate;

    private MyDatabaseHelper dbHelper;

    private static final int PICK_IMAGE_REQUEST = 1;

    private int userId;
    private byte[] userImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_update_user);

      // Initialize UI components
      etName = findViewById(R.id.etName);
      etPhone = findViewById(R.id.etPhone);
      etEmail = findViewById(R.id.etEmail);
      etPassword = findViewById(R.id.etPassword);
      tvDOB = findViewById(R.id.tvDOB);
      spinnerRole = findViewById(R.id.spinnerRole);
      imageView = findViewById(R.id.imageView);
      btnChooseImage = findViewById(R.id.btnChooseImage);
      btnUpdate = findViewById(R.id.btnUpdate);
      Toolbar toolbar = findViewById(R.id.toolbar);

      setSupportActionBar(toolbar);
      if (getSupportActionBar() != null) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
      }

      // Initialize MyDatabaseHelper
      dbHelper = new MyDatabaseHelper(this);

      // Get data from intent
      Intent intent = getIntent();
      if (intent != null) {
        userId = intent.getIntExtra("userId", -1);
        etName.setText(intent.getStringExtra("userName"));
        etPhone.setText(intent.getStringExtra("userPhone"));
        etEmail.setText(intent.getStringExtra("userEmail"));
        tvDOB.setText(intent.getStringExtra("userDob"));

        // Populate the Spinner
        String userType = intent.getStringExtra("userType");
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
          R.array.user_types_full, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRole.setAdapter(adapter);

        if (userType != null) {
          String displayRole;
          if (userType.equals("USER")) {
            displayRole = "Khách hàng";
          } else if (userType.equals("ADMIN")) {
            displayRole = "Quản trị viên";
          } else {
            displayRole = ""; // Optional: Handle other cases or invalid userType
          }

          // Find the position of the role in the spinner and set it
          int spinnerPosition = adapter.getPosition(displayRole);
          if (spinnerPosition >= 0) {
            spinnerRole.setSelection(spinnerPosition);
          }
        }

        // Display the user image if available
        userImage = intent.getByteArrayExtra("userImage");
        if (userImage != null) {
          Bitmap bitmap = BitmapFactory.decodeByteArray(userImage, 0, userImage.length);
          imageView.setImageBitmap(bitmap);
        }
      }

      // Handle the date picker for the date of birth
      tvDOB.setOnClickListener(view -> showDatePicker());

      // Handle choose image button click
      btnChooseImage.setOnClickListener(v -> openImageChooser());

      // Handle update button click
      btnUpdate.setOnClickListener(v -> updateUser());
    }

  private void showDatePicker() {
    final Calendar calendar = Calendar.getInstance();
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH);
    int day = calendar.get(Calendar.DAY_OF_MONTH);

    DatePickerDialog datePickerDialog = new DatePickerDialog(this,
      (view, year1, monthOfYear, dayOfMonth) -> {
        // Format the selected date to dd-MM-yyyy
        String selectedDate = String.format("%02d-%02d-%d", dayOfMonth, monthOfYear + 1, year1);
        tvDOB.setText(selectedDate);
      }, year, month, day);
    datePickerDialog.show();
  }


    private void openImageChooser() {
      Intent intent = new Intent();
      intent.setType("image/*");
      intent.setAction(Intent.ACTION_GET_CONTENT);
      startActivityForResult(Intent.createChooser(intent, "Chọn ảnh"), PICK_IMAGE_REQUEST);
    }

    private void updateUser() {
      String name = etName.getText().toString();
      String dob = tvDOB.getText().toString();
      String phone = etPhone.getText().toString();
      String email = etEmail.getText().toString();
      String userType = spinnerRole.getSelectedItem().toString();

      if (userType.equals("Khách hàng")){
        userType = "USER";
      }else{
        userType = "ADMIN";
      }
      String password = ""; // Giả sử không thay đổi mật khẩu
      if (etPassword != null && !etPassword.getText().toString().isEmpty()){
         password = BCrypt.hashpw(etPassword.getText().toString(), BCrypt.gensalt());
      }

      // Update user information in the database
      dbHelper.updateUser(userId, name, dob, phone, email, password, userType, userImage);
      // Return to the previous screen
      finish();
    }

    // Convert Bitmap to byte[]
    private byte[] convertBitmapToByteArray(Bitmap bitmap) {
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
      return outputStream.toByteArray();
    }

    // Receive result from the image chooser
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      super.onActivityResult(requestCode, resultCode, data);
      if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
        Uri imageUri = data.getData();
        try {
          Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
          imageView.setImageBitmap(bitmap);
          // Convert the bitmap to byte[] and save it to userImage
          userImage = convertBitmapToByteArray(bitmap);
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }
