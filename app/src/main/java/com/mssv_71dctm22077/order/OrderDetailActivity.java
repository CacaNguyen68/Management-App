package com.mssv_71dctm22077.order;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mssv_71dctm22077.R;
import com.mssv_71dctm22077.Review.ReviewActivity;
import com.mssv_71dctm22077.adapter.OrderDetailAdapter;
import com.mssv_71dctm22077.adapter.ProductAdapter;
import com.mssv_71dctm22077.model.OrderStatus;
import com.mssv_71dctm22077.model.Product;
import com.mssv_71dctm22077.sqlite.MyDatabaseHelper;

import java.util.List;

import android.util.Log;
import android.view.View;
import android.widget.Button;

public class OrderDetailActivity extends AppCompatActivity {

  private Button confirmButton;
  private RecyclerView recyclerViewOrderDetails;
  private MyDatabaseHelper databaseHelper;
  private int orderId;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_order_detail);
    confirmButton = findViewById(R.id.confirmButton);

    orderId = getIntent().getIntExtra("orderId", -1);
    int statusCode = getIntent().getIntExtra("status", -1);
    OrderStatus status = OrderStatus.values()[statusCode];

    databaseHelper = new MyDatabaseHelper(this);

    if (status == OrderStatus.SHIPPED) {
      confirmButton.setVisibility(View.VISIBLE);
    } else {
      confirmButton.setVisibility(View.GONE);
    }

    confirmButton.setOnClickListener(view -> {
      // Hiển thị hộp thoại xác nhận
      new AlertDialog.Builder(OrderDetailActivity.this)
        .setTitle("Xác nhận")
        .setMessage("Bạn có chắc chắn đã nhận đơn hàng này không?")
        .setPositiveButton("Có", (dialog, which) -> {
          // Người dùng nhấn "Có", cập nhật trạng thái trong cơ sở dữ liệu
          if (databaseHelper != null) {
            databaseHelper.updateOrderStatus(orderId, OrderStatus.DELIVERED);
            createSampleReviews(orderId);

            // Khởi động ReviewActivity để hiển thị danh sách đánh giá
            Intent intent = new Intent(OrderDetailActivity.this, ReviewActivity.class);
            intent.putExtra("orderId", orderId);
            startActivity(intent);
          }
        })
        .setNegativeButton("Không", (dialog, which) -> {
          // Người dùng nhấn "Không", không làm gì cả
          dialog.dismiss();
        })
        .show();
    });

    recyclerViewOrderDetails = findViewById(R.id.recyclerViewProducts);

    List<Product> productList = databaseHelper.getProductInOrderItemByOrderId(orderId);

    OrderDetailAdapter orderDetailAdapter = new OrderDetailAdapter(this, productList);
    recyclerViewOrderDetails.setAdapter(orderDetailAdapter);
    recyclerViewOrderDetails.setLayoutManager(new LinearLayoutManager(this));

  }

  // Phương thức tạo đánh giá mẫu
  private void createSampleReviews(int orderId) {
    // Giả sử bạn đã có danh sách sản phẩm trong đơn hàng
    List<Product> products = databaseHelper.getProductInOrderItemByOrderId(orderId);

    for (Product product : products) {
      // Tạo đánh giá mẫu
      boolean result = databaseHelper.addReview(orderId, product.getId(), 0, null);

      if (result == true) {
        Log.e("ReviewCreation", "Lỗi khi lưu đánh giá. Order ID = " + orderId + ", Product ID = " + product.getId());
      } else {
        Log.d("ReviewCreation", "Đánh giá đã được lưu. Order ID = " + orderId + ", Product ID = " + product.getId());
      }
    }
  }

}
