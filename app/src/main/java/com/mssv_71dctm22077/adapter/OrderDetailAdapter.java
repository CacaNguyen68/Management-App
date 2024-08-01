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

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.ViewHolder> {

  private Context context;
  private List<Product> productList;

  public OrderDetailAdapter(Context context, List<Product> productList) {
    this.context = context;
    this.productList = productList;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(context).inflate(R.layout.product_in_order_user, parent, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    Product product = productList.get(position);
    holder.productName.setText(product.getName());
    holder.productPrice.setText(String.format("%s VND", product.getPrice())); // Format price with currency
    holder.productQuantity.setText(product.getCreatedAt()); // Assuming quantity is 1 for each product in the order

    // Convert byte array to Bitmap
    byte[] imageBytes = product.getImage();
    if (imageBytes != null && imageBytes.length > 0) {
      Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
      holder.productImage.setImageBitmap(bitmap);
    } else {
      holder.productImage.setImageResource(R.drawable.baseline_yard_24); // Set a placeholder image if bytes are null
    }
  }

  @Override
  public int getItemCount() {
    return productList.size();
  }

  public static class ViewHolder extends RecyclerView.ViewHolder {
    TextView productName, productPrice, productQuantity;
    ImageView productImage;

    public ViewHolder(@NonNull View itemView) {
      super(itemView);
      productName = itemView.findViewById(R.id.name_product);
      productPrice = itemView.findViewById(R.id.price_product);
      productQuantity = itemView.findViewById(R.id.productQuantity); // Adjusted to match XML
      productImage = itemView.findViewById(R.id.product_image);
    }
  }
}
