package com.mssv_71dctm22077.Review;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mssv_71dctm22077.R;
import com.mssv_71dctm22077.adapter.ReviewAdapter;
import com.mssv_71dctm22077.model.Product;
import com.mssv_71dctm22077.model.Review;
import com.mssv_71dctm22077.sqlite.MyDatabaseHelper;

import java.util.List;

public class ReviewActivity extends AppCompatActivity {

  private RecyclerView recyclerView;
  private ReviewAdapter reviewAdapter;
  private MyDatabaseHelper databaseHelper;
  private int orderId;
  private Button buttonSubmit;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_review);

    // Khởi tạo các thành phần UI
    recyclerView = findViewById(R.id.recyclerViewReviews);
    buttonSubmit = findViewById(R.id.buttonSubmit);
    databaseHelper = new MyDatabaseHelper(this);

    // Nhận orderId từ Intent
    orderId = getIntent().getIntExtra("orderId", -1);

    // Lấy danh sách sản phẩm trong đơn hàng
    List<Product> productList = databaseHelper.getProductInOrderItemByOrderId(orderId);

    // Thiết lập Adapter cho RecyclerView
    reviewAdapter = new ReviewAdapter(productList, databaseHelper, orderId);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    recyclerView.setAdapter(reviewAdapter);

    // Sự kiện khi người dùng nhấn nút "Gửi Đánh Giá"
    buttonSubmit.setOnClickListener(v -> {
      // Xử lý gửi đánh giá và cập nhật vào cơ sở dữ liệu
      reviewAdapter.submitReviews();
      // Hiển thị thông báo hoặc điều hướng người dùng sang màn hình khác
      Toast.makeText(ReviewActivity.this, "Đánh giá đã được gửi!", Toast.LENGTH_SHORT).show();
      finish(); // Đóng activity sau khi gửi đánh giá
    });

  }
}
