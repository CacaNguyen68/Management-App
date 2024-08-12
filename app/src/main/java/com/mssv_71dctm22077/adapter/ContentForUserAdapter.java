package com.mssv_71dctm22077.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
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
import com.mssv_71dctm22077.model.Content;
import com.mssv_71dctm22077.sqlite.MyDatabaseHelper;

import java.util.ArrayList;

public class ContentForUserAdapter extends RecyclerView.Adapter<ContentForUserAdapter.MyViewHolder> {

  private Context context;
  private ArrayList<Content> contentList;
  private Activity activity;
  private MyDatabaseHelper myDB;

  public ContentForUserAdapter(Activity activity, Context context, ArrayList<Content> contentList) {
    this.activity = activity;
    this.context = context;
    this.contentList = contentList;
    this.myDB = new MyDatabaseHelper(context);
  }

  @NonNull
  @Override
  public ContentForUserAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(context);
    View view = inflater.inflate(R.layout.detail_content_for_user, parent, false);
    return new ContentForUserAdapter.MyViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull ContentForUserAdapter.MyViewHolder holder, final int position) {
    Content content = contentList.get(position);
    holder.contentTitle.setText(content.getTitle());
    holder.contentCreatedAt.setText(String.format("Ngày khởi tạo: %s", content.getCreatedAt()));
    holder.contentClick.setText(String.format("Số lượt click: %d", content.getClick()));
    if (content.getImage() != null) {
      Bitmap bitmap = BitmapFactory.decodeByteArray(content.getImage(), 0, content.getImage().length);
      holder.imageView.setImageBitmap(bitmap);
    } else {
      holder.imageView.setImageResource(R.drawable.baseline_yard_24);
    }

  }

  @Override
  public int getItemCount() {
    return contentList.size();
  }

  public class MyViewHolder extends RecyclerView.ViewHolder {
    TextView contentTitle, contentCreatedAt, contentClick;
    ImageView imageView;

    public MyViewHolder(@NonNull View itemView) {
      super(itemView);
      contentTitle = itemView.findViewById(R.id.title_content);
      contentCreatedAt = itemView.findViewById(R.id.created_content);
      contentClick = itemView.findViewById(R.id.click_content);
      imageView = itemView.findViewById(R.id.content_image);
    }
  }
}
