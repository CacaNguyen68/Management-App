package com.mssv_71dctm22077.Content;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.mssv_71dctm22077.R;
import com.mssv_71dctm22077.sqlite.MyDatabaseHelper;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class UpdateContentActivity extends AppCompatActivity {

  private static final int PICK_IMAGE_REQUEST = 1;

  private EditText etTitle;
  private EditText etContent;
  private ImageView ivImage;
  private Button btnChooseImage;
  private Button btnReset;
  private Button btnSave;
  private MyDatabaseHelper myDatabaseHelper;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_update_content);

    Toolbar toolbar = findViewById(R.id.toolbar_update_content);
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    etTitle = findViewById(R.id.et_update_title);
    etContent = findViewById(R.id.et_update_content);
    ivImage = findViewById(R.id.iv_update_image);
    btnChooseImage = findViewById(R.id.btn_choose_update_image);
    btnReset = findViewById(R.id.btn_reset_update_content);
    btnSave = findViewById(R.id.btn_save_update_content);

    myDatabaseHelper = new MyDatabaseHelper(this);

    Intent intent = getIntent();
    String id = intent.getStringExtra("id");
    String title = intent.getStringExtra("title");
    String content = intent.getStringExtra("content");
    byte[] image = intent.getByteArrayExtra("image");

    etTitle.setText(title);
    etContent.setText(content);

    if (image != null) {
      Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
      ivImage.setImageBitmap(bitmap);
    }

    btnChooseImage.setOnClickListener(v -> {
      Intent pickPhoto = new Intent(Intent.ACTION_PICK,
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
      startActivityForResult(pickPhoto, PICK_IMAGE_REQUEST);
    });

    btnReset.setOnClickListener(v -> {
      etTitle.setText(title);
      etContent.setText(content);
      if (image != null) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
        ivImage.setImageBitmap(bitmap);
      }
    });

    btnSave.setOnClickListener(v -> {
      String newTitle = etTitle.getText().toString();
      String newContent = etContent.getText().toString();

      Bitmap bitmap = ((BitmapDrawable) ivImage.getDrawable()).getBitmap();
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
      byte[] updatedImage = baos.toByteArray();

      boolean isUpdated = myDatabaseHelper.updateContent(Integer.parseInt(id), newTitle, newContent, updatedImage);

      if (isUpdated) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("id", id);
        resultIntent.putExtra("title", newTitle);
        resultIntent.putExtra("content", newContent);
        resultIntent.putExtra("image", updatedImage);
        setResult(RESULT_OK, resultIntent);
        finish();
        Toast.makeText(this, "Content updated successfully!", Toast.LENGTH_SHORT).show();
      } else {
        Toast.makeText(this, "Failed to update content.", Toast.LENGTH_SHORT).show();
      }
    });
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
      Uri imageUri = data.getData();
      try {
        InputStream inputStream = getContentResolver().openInputStream(imageUri);
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
        ivImage.setImageBitmap(bitmap);

        // Convert the selected image to byte[]
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] imageBytes = baos.toByteArray();

        // Save imageBytes if needed for further use
        // For now, it is just displayed on the ImageView

      } catch (FileNotFoundException e) {
        e.printStackTrace();
      }
    }
  }
}
