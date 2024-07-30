package com.mssv_71dctm22077.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mssv_71dctm22077.Cart.CartActivity;
import com.mssv_71dctm22077.R;
import com.mssv_71dctm22077.model.CartItem;
import com.mssv_71dctm22077.sqlite.MyDatabaseHelper;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

  private Context context;
  private List<CartItem> cartItemList;
  private MyDatabaseHelper databaseHelper;

  public CartAdapter(Context context, List<CartItem> cartItemList, MyDatabaseHelper databaseHelper) {
    this.context = context;
    this.cartItemList = cartItemList;
    this.databaseHelper = databaseHelper;
  }

  @NonNull
  @Override
  public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
    return new CartViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
    CartItem cartItem = cartItemList.get(position);
    holder.bind(cartItem);
  }

  @Override
  public int getItemCount() {
    return cartItemList.size();
  }

  public class CartViewHolder extends RecyclerView.ViewHolder {
    private ImageView productImage;
    private TextView productName, productPrice, productQuantity;
    private ImageButton buttonDecrease, buttonIncrease;

    public CartViewHolder(@NonNull View itemView) {
      super(itemView);
      productImage = itemView.findViewById(R.id.product_image);
      productName = itemView.findViewById(R.id.name_product);
      productPrice = itemView.findViewById(R.id.price_product);
      productQuantity = itemView.findViewById(R.id.productQuantity);
      buttonDecrease = itemView.findViewById(R.id.buttonDecrease);
      buttonIncrease = itemView.findViewById(R.id.buttonIncrease);

      buttonDecrease.setOnClickListener(v -> {
        int position = getAdapterPosition();
        if (position != RecyclerView.NO_POSITION) {
          CartItem cartItem = cartItemList.get(position);
          if (cartItem.getQuantity() > 1) {
            cartItem.setQuantity(cartItem.getQuantity() - 1);
            databaseHelper.updateCartItem(cartItem);
            notifyItemChanged(position);
            ((CartActivity) context).updateTotalPrice(); // Cập nhật tổng giá
          }
        }
      });

      buttonIncrease.setOnClickListener(v -> {
        int position = getAdapterPosition();
        if (position != RecyclerView.NO_POSITION) {
          CartItem cartItem = cartItemList.get(position);
          cartItem.setQuantity(cartItem.getQuantity() + 1);
          databaseHelper.updateCartItem(cartItem);
          notifyItemChanged(position);
          ((CartActivity) context).updateTotalPrice(); // Cập nhật tổng giá
        }
      });
    }

    public void bind(CartItem cartItem) {
      productName.setText(cartItem.getProductName());
      productPrice.setText(String.valueOf(cartItem.getProductPrice()));
      productQuantity.setText(String.valueOf(cartItem.getQuantity()));

      if (cartItem.getProductImage() != null) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(cartItem.getProductImage(), 0, cartItem.getProductImage().length);
        productImage.setImageBitmap(bitmap);
      } else {
        productImage.setImageResource(R.drawable.baseline_yard_24);
      }
    }
  }
}
