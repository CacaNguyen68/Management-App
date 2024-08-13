package com.mssv_71dctm22077.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mssv_71dctm22077.R;
import com.mssv_71dctm22077.model.Product;
import com.mssv_71dctm22077.model.Review;
import com.mssv_71dctm22077.sqlite.MyDatabaseHelper;

import java.util.List;

public class ReviewAdminAdapter extends RecyclerView.Adapter<ReviewAdminAdapter.ReviewViewHolder> {

  private List<Review> reviewList;
  private MyDatabaseHelper databaseHelper;

  public ReviewAdminAdapter(List<Review> reviewList, MyDatabaseHelper databaseHelper) {
    this.reviewList = reviewList;
    this.databaseHelper = databaseHelper;
  }

  @NonNull
  @Override
  public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
      .inflate(R.layout.item_review_admin, parent, false);
    return new ReviewViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
    Review review = reviewList.get(position);
    String nameProduct = databaseHelper.getProductNameById(review.getProductId());
    if (nameProduct != null) {
      holder.productNameTextView.setText(nameProduct);
    } else {
      holder.productNameTextView.setText("Unknown Product"); // Xử lý trường hợp sản phẩm không tồn tại
    }
    holder.ratingBar.setRating(review.getRating());
    holder.reviewTextView.setText(review.getReviewText());
    holder.reviewDateTextView.setText(review.getCreatedAt());
    holder.reviewByTextView.setText(review.getCreatedBy());
  }

  @Override
  public int getItemCount() {
    return reviewList.size();
  }

  public static class ReviewViewHolder extends RecyclerView.ViewHolder {
    TextView productNameTextView;
    RatingBar ratingBar;
    TextView reviewTextView;
    TextView reviewDateTextView, reviewByTextView;

    public ReviewViewHolder(@NonNull View itemView) {
      super(itemView);
      productNameTextView = itemView.findViewById(R.id.textViewProductName);
      ratingBar = itemView.findViewById(R.id.ratingBarProduct);
      reviewTextView = itemView.findViewById(R.id.textViewReviewText);
      reviewDateTextView = itemView.findViewById(R.id.textViewReviewDate);
      reviewByTextView = itemView.findViewById(R.id.textViewReviewBy);
    }
  }
}
