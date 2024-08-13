package com.mssv_71dctm22077.Content;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.mssv_71dctm22077.MenuAdminActivity;
import com.mssv_71dctm22077.MenuUserActivity;
import com.mssv_71dctm22077.R;
import com.mssv_71dctm22077.model.Content;
import com.mssv_71dctm22077.sqlite.MyDatabaseHelper;

public class ContentDetailActivity extends AppCompatActivity {

  private TextView titleTextView, createdAtTextView, clickTextView, contentTextView;
  private ImageView contentImageView;
  private MyDatabaseHelper myDB;
  private int contentId;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_content_detail);

    titleTextView = findViewById(R.id.detail_title);
    createdAtTextView = findViewById(R.id.detail_created_at);
    clickTextView = findViewById(R.id.detail_click);
    contentImageView = findViewById(R.id.detail_image);
    contentTextView = findViewById(R.id.detail_content);

    myDB = new MyDatabaseHelper(this);

    // Nhận dữ liệu từ Intent
    contentId = getIntent().getIntExtra("contentId", -1);
    if (contentId != -1) {
      myDB.incrementClickCount(contentId);
      loadContentDetails(contentId);
    }
  }


  private void loadContentDetails(int id) {
    Content content = myDB.getContentById(id); // Implement this method in MyDatabaseHelper
    if (content != null) {
      titleTextView.setText(content.getTitle());
      createdAtTextView.setText(String.format("Ngày khởi tạo: %s", content.getCreatedAt()));
      clickTextView.setText(String.format("Số lượt click: %d", content.getClick()));
      contentTextView.setText(String.format(content.getContent()));
      if (content.getImage() != null) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(content.getImage(), 0, content.getImage().length);
        contentImageView.setImageBitmap(bitmap);
      } else {
        contentImageView.setImageResource(R.drawable.baseline_yard_24);
      }
    }
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
      Intent intent = new Intent(this, MenuUserActivity.class);
      startActivity(intent);
      return true;
    }
    return super.onOptionsItemSelected(item);
  }
}
