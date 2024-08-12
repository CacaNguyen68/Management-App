package com.mssv_71dctm22077.Content;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.mssv_71dctm22077.R;
import com.mssv_71dctm22077.sqlite.MyDatabaseHelper;

import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class AddContentActivity extends AppCompatActivity {

  private static final int PICK_IMAGE_REQUEST = 1;
  private TextInputEditText titleContent, contentText;
  private ImageView imagePreview;
  private byte[] selectedImage;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add_content);

    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setDisplayShowHomeEnabled(true);

    titleContent = findViewById(R.id.titleContent);
    contentText = findViewById(R.id.contentText);
    imagePreview = findViewById(R.id.imagePreview);
    Button buttonSelectImage = findViewById(R.id.buttonSelectImage);
    Button buttonAdd = findViewById(R.id.buttonAdd);
    Button buttonReset = findViewById(R.id.buttonReset);

    buttonSelectImage.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        openImageChooser();
      }
    });

    buttonAdd.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        addContent();
      }
    });

    buttonReset.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        resetForm();
      }
    });
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
      onBackPressed();
      return true;
    }
    return super.onOptionsItemSelected(item);
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
        InputStream inputStream = getContentResolver().openInputStream(imageUri);
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
        imagePreview.setImageBitmap(bitmap);
        selectedImage = getBytesFromBitmap(bitmap);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  private byte[] getBytesFromBitmap(Bitmap bitmap) {
    ByteArrayOutputStream stream = new ByteArrayOutputStream();
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
    return stream.toByteArray();
  }

  private void addContent() {
    String title = titleContent.getText().toString().trim();
    String content = contentText.getText().toString().trim();

    if (title.isEmpty() || content.isEmpty() || selectedImage == null) {
      Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin và chọn hình ảnh.", Toast.LENGTH_SHORT).show();
      return;
    }

    // Insert content into the database using MyDatabaseHelper
    MyDatabaseHelper dbHelper = new MyDatabaseHelper(this);
    boolean isInserted = dbHelper.addContent(title,content,selectedImage);

    if (isInserted) {
      Toast.makeText(this, "Bài viết đã được thêm thành công.", Toast.LENGTH_SHORT).show();
      finish(); // Close the activity
    } else {
      Toast.makeText(this, "Có lỗi xảy ra, vui lòng thử lại.", Toast.LENGTH_SHORT).show();
    }
  }

  private void resetForm() {
    titleContent.setText("");
    contentText.setText("");
    imagePreview.setImageResource(R.drawable.baseline_yard_24);
    selectedImage = null;
  }
}
