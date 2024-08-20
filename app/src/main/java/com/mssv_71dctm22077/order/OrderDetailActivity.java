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
import com.mssv_71dctm22077.model.User;
import com.mssv_71dctm22077.sqlite.MyDatabaseHelper;

import java.util.List;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class OrderDetailActivity extends AppCompatActivity {

  private Button confirmButton;
  private RecyclerView recyclerViewOrderDetails;
  private MyDatabaseHelper databaseHelper;
  private int orderId;
  private User user;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_order_detail);
    confirmButton = findViewById(R.id.confirmButton);

    orderId = getIntent().getIntExtra("orderId", -1);
    int statusCode = getIntent().getIntExtra("status", -1);
    int userId = getIntent().getIntExtra("userId", -1);
    Log.d("asad","dadfa"+userId);

    OrderStatus status = OrderStatus.values()[statusCode];

    databaseHelper = new MyDatabaseHelper(this);

    user = databaseHelper.getUserById(userId);
    if (status == OrderStatus.SHIPPED) {
      confirmButton.setVisibility(View.VISIBLE);
    } else {
      confirmButton.setVisibility(View.GONE);
    }

    confirmButton.setOnClickListener(view -> {
      new AlertDialog.Builder(OrderDetailActivity.this)
        .setTitle("Xác nhận")
        .setMessage("Bạn có chắc chắn đã nhận đơn hàng này không?")
        .setPositiveButton("Có", (dialog, which) -> {
          if (databaseHelper != null) {
            // Cập nhật trạng thái đơn hàng
            databaseHelper.updateOrderStatus(orderId, OrderStatus.DELIVERED);

            // Tạo đánh giá mẫu (nếu cần thiết)
            createSampleReviews(orderId);

            // Kiểm tra xem trạng thái có được cập nhật thành công hay không
            boolean isUpdated = databaseHelper.isOrderStatusUpdated(orderId, OrderStatus.DELIVERED);
            if (isUpdated) {
              Toast.makeText(OrderDetailActivity.this, "Đơn hàng đã được xác nhận", Toast.LENGTH_SHORT).show();

              // Khởi động ReviewActivity
              Intent intent = new Intent(OrderDetailActivity.this, ReviewActivity.class);

              intent.putExtra("orderId", orderId);
              intent.putExtra("userId", user.getId());
              startActivity(intent);
            } else {
              Toast.makeText(OrderDetailActivity.this, "Cập nhật trạng thái đơn hàng thất bại", Toast.LENGTH_SHORT).show();
            }
          }
        })
        .setNegativeButton("Không", (dialog, which) -> dialog.dismiss())
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
      boolean result = databaseHelper.addReview(orderId, product.getId(), 0, null,user.getName());

      if (result == true) {
        Log.e("ReviewCreation", "Lỗi khi lưu đánh giá. Order ID = " + orderId + ", Product ID = " + product.getId());
      } else {
        Log.d("ReviewCreation", "Đánh giá đã được lưu. Order ID = " + orderId + ", Product ID = " + product.getId());
      }
    }
  }

}
