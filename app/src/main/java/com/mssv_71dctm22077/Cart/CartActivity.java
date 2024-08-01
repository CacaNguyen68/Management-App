package com.mssv_71dctm22077.Cart;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mssv_71dctm22077.R;
import com.mssv_71dctm22077.adapter.CartAdapter;
import com.mssv_71dctm22077.model.CartItem;
import com.mssv_71dctm22077.model.Product;
import com.mssv_71dctm22077.sqlite.MyDatabaseHelper;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {

  private RecyclerView recyclerViewCart;
  private TextView totalPrice;
  private List<CartItem> cartItemList;
  private CartAdapter cartAdapter;
  private MyDatabaseHelper databaseHelper;
  private int userId;
  private Button buttonPlaceOrder;
  EditText addressInput;
  String address;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_cart);
    addressInput = findViewById(R.id.address_input);
    address = addressInput.getText().toString().trim();

    userId = getIntent().getIntExtra("userId", -1);
    databaseHelper = new MyDatabaseHelper(this);

    recyclerViewCart = findViewById(R.id.recyclerViewCart);
    totalPrice = findViewById(R.id.total_price);
    buttonPlaceOrder = findViewById(R.id.button_place_order);

    cartItemList = databaseHelper.getCartItemsByUserId(userId);

    cartAdapter = new CartAdapter(this, cartItemList, databaseHelper);
    recyclerViewCart.setAdapter(cartAdapter);
    recyclerViewCart.setLayoutManager(new LinearLayoutManager(this));

    updateTotalPrice();

    buttonPlaceOrder.setOnClickListener(v ->

      placeOrder());
  }

  public void updateTotalPrice() {
    double total = 0.0;
    for (CartItem item : cartItemList) {
      total += item.getProductPrice() * item.getQuantity();
    }
    totalPrice.setText("Tổng giá: " + total + " VND");
  }

  private void placeOrder() {
    if (cartItemList.isEmpty()) {
      // Giỏ hàng rỗng, hiển thị thông báo
      Toast.makeText(this, "Giỏ hàng của bạn chưa có sản phẩm nào!", Toast.LENGTH_SHORT).show();
      return;
    }

    if (addressInput.getText().toString().trim().isEmpty()) {
      addressInput.setError("Địa chỉ không được bỏ trống");
      return;
    }
    long orderId = databaseHelper.createOrder(userId, address);

    for (CartItem cartItem : cartItemList) {
      int productId = databaseHelper.getProductIdByName(cartItem.getProductName());
      if (productId != -1) {
        databaseHelper.createOrderItem(orderId, productId, cartItem.getQuantity());
      } else {
        // Xử lý nếu không tìm thấy sản phẩm với tên đó
        Toast.makeText(this, "Không tìm thấy sản phẩm: " + cartItem.getProductName(), Toast.LENGTH_SHORT).show();
      }
    }

    // Sau khi tạo đơn hàng, xóa các mục giỏ hàng hiện tại
    databaseHelper.clearCart(userId);
    cartItemList.clear();
    cartAdapter.notifyDataSetChanged();
    addressInput.setText(""); // Reset the input field to empty
    addressInput.setError(null); // Remove any error messages
    updateTotalPrice();
    Toast.makeText(this, "Đơn hàng đã được đặt thành công!", Toast.LENGTH_SHORT).show();
  }

}
