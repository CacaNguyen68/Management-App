package com.mssv_71dctm22077.Content;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mssv_71dctm22077.R;
import com.mssv_71dctm22077.adapter.ContentAdapter;
import com.mssv_71dctm22077.adapter.ContentForUserAdapter;
import com.mssv_71dctm22077.model.Content;
import com.mssv_71dctm22077.sqlite.MyDatabaseHelper;

import java.util.ArrayList;

public class ContentForUserActivity extends AppCompatActivity {

  private RecyclerView recyclerView;
  private ContentForUserAdapter contentAdapter;
  private ArrayList<Content> contentList;
  private MyDatabaseHelper myDB;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_content_for_user);

    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    recyclerView = findViewById(R.id.recyclerViewContent);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    myDB = new MyDatabaseHelper(this);
    contentList = new ArrayList<>();

    // Load data into RecyclerView
    loadData();

    // Setup SearchView
    SearchView searchView = findViewById(R.id.search_view);
    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
      @Override
      public boolean onQueryTextSubmit(String query) {
        return false;
      }

      @Override
      public boolean onQueryTextChange(String newText) {
        loadDataSearch(newText);
        return true;
      }
    });


  }

  private void loadData() {
    contentList.clear();
    contentList.addAll(myDB.getAllContents());
    contentAdapter = new ContentForUserAdapter(this, this, contentList);
    recyclerView.setAdapter(contentAdapter);
  }

  private void loadDataSearch(String query) {
    contentList.clear();
    contentList.addAll(myDB.searchContents(query));
    contentAdapter.notifyDataSetChanged();
  }


  @Override
  public void onResume() {
    super.onResume();
    // Clear old data
    contentList.clear();

    // Reload data from the database
    contentList.addAll(myDB.getAllContents());

    // Notify the adapter about the data change
    if (contentAdapter != null) {
      contentAdapter.notifyDataSetChanged();
    }
  }
}
