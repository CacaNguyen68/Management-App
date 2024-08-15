package com.mssv_71dctm22077.Cart;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mssv_71dctm22077.Category.CategoryForUserActivity;
import com.mssv_71dctm22077.MenuUserActivity;
import com.mssv_71dctm22077.R;
import com.mssv_71dctm22077.adapter.CartAdapter;
import com.mssv_71dctm22077.adapter.CategoryForUserAdapter;
import com.mssv_71dctm22077.model.CartItem;
import com.mssv_71dctm22077.model.Product;
import com.mssv_71dctm22077.model.User;
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
  User user;
  double tong ;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_cart);

    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    addressInput = findViewById(R.id.address_input);

    userId = getIntent().getIntExtra("userId", -1);
    databaseHelper = new MyDatabaseHelper(this);
    user = databaseHelper.getUserById(userId);
    Log.d("dadasd", "sasad" + user.getEmail());

    address = user.getEmail();  // Lấy địa chỉ từ user
    addressInput.setText(address);

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
    int soluong = 0;
    int total = 0 ;
    for (CartItem item : cartItemList) {
      total += item.getProductPrice() * item.getQuantity();
      soluong++;
    }
    totalPrice.setText("Tổng giá "+soluong+" sản phẩm: " + total + " VND");
    tong = total;
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
    long orderId = databaseHelper.createOrder(userId, address, tong);

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
    addressInput.setText(address = user.getEmail().toString()); // Reset the input field to empty
    updateTotalPrice();
    Toast.makeText(this, "Đơn hàng đã được đặt thành công!", Toast.LENGTH_SHORT).show();
  }


  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_product, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    if (item.getItemId() == R.id.action_category) {
      // Xử lý khi nhấn vào mục menu
      Intent intent = new Intent(this, CategoryForUserActivity.class);
      intent.putExtra("phone", user.getPhone());
      startActivity(intent);
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

}
