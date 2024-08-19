package com.mssv_71dctm22077.Review;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mssv_71dctm22077.R;
import com.mssv_71dctm22077.adapter.ReviewAdapter;
import com.mssv_71dctm22077.adapter.ReviewAdminAdapter;
import com.mssv_71dctm22077.model.Review;
import com.mssv_71dctm22077.sqlite.MyDatabaseHelper;

import java.util.List;

public class ReviewByAdminActivity extends AppCompatActivity {

  private RecyclerView recyclerView;
  private MyDatabaseHelper databaseHelper;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_review_by_admin);

    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    // Handle navigation icon click (back button click)
    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        finish();
      }
    });

    recyclerView = findViewById(R.id.recyclerViewReviews);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));

    databaseHelper = new MyDatabaseHelper(this);

    // Lấy danh sách các đánh giá và sắp xếp theo thứ tự mới nhất
    List<Review> reviewList = databaseHelper.getAllReviewsSortedByDate();
    ReviewAdminAdapter reviewAdminAdapter = new ReviewAdminAdapter(reviewList, databaseHelper);
    recyclerView.setAdapter(reviewAdminAdapter);
  }
}
