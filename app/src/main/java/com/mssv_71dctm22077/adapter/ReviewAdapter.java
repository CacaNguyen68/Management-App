package com.mssv_71dctm22077.adapter;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mssv_71dctm22077.R;
import com.mssv_71dctm22077.model.Product;
import com.mssv_71dctm22077.sqlite.MyDatabaseHelper;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

  private static final String TAG = "ReviewAdapter"; // Thay đổi tên theo ý thích
  private final List<Product> productList;
  private final MyDatabaseHelper databaseHelper;
  private final int orderId;

  // Lưu trữ giá trị rating và review text cho từng sản phẩm
  private final SparseArray<Float> ratingsMap = new SparseArray<>();
  private final SparseArray<String> reviewTextMap = new SparseArray<>();

  public ReviewAdapter(List<Product> productList, MyDatabaseHelper databaseHelper, int orderId) {
    this.productList = productList;
    this.databaseHelper = databaseHelper;
    this.orderId = orderId;
  }

  @NonNull
  @Override
  public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review, parent, false);
    return new ReviewViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
    holder.bind(productList.get(position));
  }

  @Override
  public int getItemCount() {
    return productList.size();
  }

  public void submitReviews() {
    for (Product product : productList) {
      float rating = getRatingForProduct(product.getId());
      String reviewText = getReviewTextForProduct(product.getId());
      // Cập nhật vào database
      databaseHelper.updateReview(orderId, product.getId(),rating, reviewText);

      // Ghi log để kiểm tra giá trị đánh giá và nội dung đánh giá
      Log.d(TAG, "Submitting review for Product ID: " + product.getId() +
        " | Rating: " + rating +
        " | Review: " + reviewText);
    }
  }

  private float getRatingForProduct(int productId) {
    // Lấy giá trị rating từ map hoặc trả về 0 nếu không có
    return ratingsMap.get(productId, 0f);
  }

  private String getReviewTextForProduct(int productId) {
    // Lấy nội dung đánh giá từ map hoặc trả về chuỗi rỗng nếu không có
    return reviewTextMap.get(productId, "");
  }

  class ReviewViewHolder extends RecyclerView.ViewHolder {

    private final TextView productName;
    private final RatingBar ratingBar;
    private final EditText reviewText;

    public ReviewViewHolder(@NonNull View itemView) {
      super(itemView);
      productName = itemView.findViewById(R.id.textViewProductName);
      ratingBar = itemView.findViewById(R.id.ratingBarProduct);
      reviewText = itemView.findViewById(R.id.editTextReviewText);
    }

    public void bind(Product product) {
      productName.setText(product.getName());

      // Cài đặt giá trị ratingBar và reviewText từ map
      ratingBar.setRating(ratingsMap.get(product.getId(), 0f));
      reviewText.setText(reviewTextMap.get(product.getId(), ""));

      ratingBar.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
        // Cập nhật giá trị rating vào map
        ratingsMap.put(product.getId(), rating);

        // Ghi log để kiểm tra giá trị rating
        Log.d(TAG, "Rating changed for Product ID: " + product.getId() + " | Rating: " + rating);
      });

      reviewText.addTextChangedListener(new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override
        public void afterTextChanged(Editable s) {
          // Cập nhật nội dung đánh giá vào map
          reviewTextMap.put(product.getId(), s.toString());

          // Ghi log để kiểm tra nội dung đánh giá
          Log.d(TAG, "Review text changed for Product ID: " + product.getId() + " | Review: " + s.toString());
        }
      });
    }
  }
}
