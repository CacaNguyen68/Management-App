package com.mssv_71dctm22077.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mssv_71dctm22077.R;
import com.mssv_71dctm22077.model.Product;
import com.mssv_71dctm22077.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProductAdapter  extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

  private Context context;
  private List<Product> productList;
  private List<Product> productListFull;

  public ProductAdapter(Context context, List<Product> productList) {
    this.context = context;
    this.productList = productList;
    this.productListFull = new ArrayList<>(productList);
  }

  @NonNull
  @Override
  public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(context).inflate(R.layout.detail_product, parent, false);
    return new ProductViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
    Product product = productList.get(position);
    holder.nameTextView.setText(product.getName());
    holder.priceTextView.setText("Price: " + product.getPrice());
    holder.categoryTextView.setText("Category: " + product.getCategoryId());
    holder.createdTextView.setText("Created Date: " + product.getCreatedAt());
    holder.userCreatedTextView.setText("User Created: " + product.getUserCreatedAt());

    if (product.getImage() != null) {
      Bitmap bitmap = BitmapFactory.decodeByteArray(product.getImage(), 0, product.getImage().length);
      holder.imageView.setImageBitmap(bitmap);
    } else {
      holder.imageView.setImageResource(R.drawable.baseline_yard_24);
    }
  }

  @Override
  public int getItemCount() {
    return productList.size();
  }

  public static class ProductViewHolder extends RecyclerView.ViewHolder {
    TextView nameTextView, priceTextView, categoryTextView, createdTextView, userCreatedTextView;
    ImageView imageView;

    public ProductViewHolder(@NonNull View itemView) {
      super(itemView);
      nameTextView = itemView.findViewById(R.id.name_product);
      priceTextView = itemView.findViewById(R.id.price_product);
      categoryTextView = itemView.findViewById(R.id.category_id);
      createdTextView = itemView.findViewById(R.id.created_product);
      userCreatedTextView = itemView.findViewById(R.id.user_created_product);
      imageView = itemView.findViewById(R.id.product_image);
    }
  }

  public void filter(String text) {
    text = text.toLowerCase(Locale.getDefault());
    productList.clear();
    if (text.length() == 0) {
      productList.addAll(productListFull);
    } else {
      for (Product product : productListFull) {
        if (product.getName().toLowerCase(Locale.getDefault()).contains(text)) {
          productList.add(product);
        }
      }
    }
    notifyDataSetChanged();
  }
}
