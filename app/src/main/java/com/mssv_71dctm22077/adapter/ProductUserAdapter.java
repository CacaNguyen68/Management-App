package com.mssv_71dctm22077.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mssv_71dctm22077.Product.UpdateProductActivity;
import com.mssv_71dctm22077.R;
import com.mssv_71dctm22077.model.Product;
import com.mssv_71dctm22077.sqlite.MyDatabaseHelper;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ProductUserAdapter extends RecyclerView.Adapter<ProductUserAdapter.MyViewHolder> {

  private Context context;
  private ArrayList<Product> productList;
  private Activity activity;
  private MyDatabaseHelper myDB;

  public ProductUserAdapter(Activity activity, Context context, ArrayList<Product> productList) {
    this.activity = activity;
    this.context = context;
    this.productList = productList;
    this.myDB = new MyDatabaseHelper(context);
  }

  @NonNull
  @Override
  public ProductUserAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(context);
    View view = inflater.inflate(R.layout.details_product_for_user, parent, false);
    return new ProductUserAdapter.MyViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull ProductUserAdapter.MyViewHolder holder, final int position) {
    Product product = productList.get(position);
    holder.productName.setText(product.getName());
    // Sử dụng DecimalFormat để định dạng số tiền
    DecimalFormat decimalFormat = new DecimalFormat("#,###");
    String formattedPrice = decimalFormat.format(product.getPrice());
    holder.productPrice.setText(String.format("Giá: %s VND", formattedPrice));
    String nameCategory = myDB.getCategoryNameById(product.getCategoryId());

    holder.categoryId.setText(String.format("Danh mục: %s", nameCategory));
    holder.productCreated.setText(String.format("Ngày khởi tạo: %s", product.getCreatedAt()));

    if (product.getImage() != null) {
      Bitmap bitmap = BitmapFactory.decodeByteArray(product.getImage(), 0, product.getImage().length);
      holder.imageView.setImageBitmap(bitmap);
    } else {
      holder.imageView.setImageResource(R.drawable.baseline_yard_24);

    }

    holder.floatingAdd.setOnClickListener(v -> {
//      Intent intent = new Intent(context, UpdateProductActivity.class);
//      intent.putExtra("id", String.valueOf(product.getId()));
//      intent.putExtra("name", product.getName());
//      intent.putExtra("price", String.valueOf(product.getPrice()));
//      intent.putExtra("categoryId", String.valueOf(product.getCategoryId()));
//      Log.d("Category AFTER", "categoryId: " + product.getCategoryId());
////      intent.putExtra("createdAt", product.getCreatedAt());
//      if (product.getImage() != null) {
//        intent.putExtra("image", product.getImage());
//      }
//      activity.startActivityForResult(intent, 1);
    });

//    holder.floatingUpdate.setOnClickListener(v -> );
  }

  @Override
  public int getItemCount() {
    return productList.size();
  }

  public class MyViewHolder extends RecyclerView.ViewHolder {
    TextView productName, productPrice, categoryId, productCreated, userCreatedProduct;
    FloatingActionButton floatingAdd;
    ImageView imageView;

    public MyViewHolder(@NonNull View itemView) {
      super(itemView);
      productName = itemView.findViewById(R.id.name_product);
      productPrice = itemView.findViewById(R.id.price_product);
      categoryId = itemView.findViewById(R.id.category_id);
      productCreated = itemView.findViewById(R.id.created_product);
      userCreatedProduct = itemView.findViewById(R.id.user_created_product);
      imageView = itemView.findViewById(R.id.product_image);
      floatingAdd = itemView.findViewById(R.id.floatingAdd);
    }
  }

}
