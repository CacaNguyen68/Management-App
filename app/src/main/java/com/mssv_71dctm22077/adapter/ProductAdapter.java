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

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
  private Context context;
  private List<Product> productList;

  public ProductAdapter(Context context, List<Product> productList) {
    this.context = context;
    this.productList = productList;
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
    holder.priceTextView.setText(String.valueOf(product.getPrice()));
    holder.categoryIdTextView.setText(String.valueOf(product.getCategoryId()));
    holder.createdAtTextView.setText(product.getCreatedAt());
    holder.userCreatedTextView.setText(product.getUserCreatedAt());

    if (product.getImage() != null) {
      Bitmap bitmap = BitmapFactory.decodeByteArray(product.getImage(), 0, product.getImage().length);
      holder.imageView.setImageBitmap(bitmap);
    } else {
      holder.imageView.setImageResource(R.drawable.baseline_yard_24); // Placeholder image
    }
  }

  @Override
  public int getItemCount() {
    return productList.size();
  }

  public static class ProductViewHolder extends RecyclerView.ViewHolder {
    TextView nameTextView, priceTextView, categoryIdTextView, createdAtTextView, userCreatedTextView;
    ImageView imageView;

    public ProductViewHolder(@NonNull View itemView) {
      super(itemView);
      nameTextView = itemView.findViewById(R.id.name_product);
      priceTextView = itemView.findViewById(R.id.price_product);
      categoryIdTextView = itemView.findViewById(R.id.category_id);
      createdAtTextView = itemView.findViewById(R.id.created_product);
      userCreatedTextView = itemView.findViewById(R.id.user_created_product);
      imageView = itemView.findViewById(R.id.product_image);
    }
  }
}
