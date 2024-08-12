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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mssv_71dctm22077.Content.UpdateContentActivity;
import com.mssv_71dctm22077.R;
import com.mssv_71dctm22077.model.Content;
import com.mssv_71dctm22077.sqlite.MyDatabaseHelper;

import java.util.ArrayList;

public class ContentAdapter extends RecyclerView.Adapter<ContentAdapter.MyViewHolder> {

  private Context context;
  private ArrayList<Content> contentList;
  private Activity activity;
  private MyDatabaseHelper myDB;

  public ContentAdapter(Activity activity, Context context, ArrayList<Content> contentList) {
    this.activity = activity;
    this.context = context;
    this.contentList = contentList;
    this.myDB = new MyDatabaseHelper(context);
  }

  @NonNull
  @Override
  public ContentAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(context);
    View view = inflater.inflate(R.layout.detail_content, parent, false);
    return new MyViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull ContentAdapter.MyViewHolder holder, final int position) {
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
    holder.floatingDelete.setOnClickListener(v -> confirmDialog(position));

    holder.floatingUpdate.setOnClickListener(v -> {
      Intent intent = new Intent(context, UpdateContentActivity.class);
      intent.putExtra("id", String.valueOf(content.getId()));
      intent.putExtra("title", content.getTitle());
      intent.putExtra("content", content.getContent());
      if (content.getImage() != null) {
        intent.putExtra("image", content.getImage());
      }
      activity.startActivityForResult(intent, 1);
    });
  }

  @Override
  public int getItemCount() {
    return contentList.size();
  }

  public class MyViewHolder extends RecyclerView.ViewHolder {
    TextView contentTitle, contentCreatedAt, contentClick;
    FloatingActionButton floatingDelete, floatingUpdate;
    ImageView imageView;

    public MyViewHolder(@NonNull View itemView) {
      super(itemView);
      contentTitle = itemView.findViewById(R.id.title_content);
      contentCreatedAt = itemView.findViewById(R.id.created_content);
      contentClick = itemView.findViewById(R.id.click_content);
      imageView = itemView.findViewById(R.id.content_image);
      floatingDelete = itemView.findViewById(R.id.floatingDelete);
      floatingUpdate = itemView.findViewById(R.id.floatingUpdate);
    }
  }

  private void confirmDialog(int position) {
    AlertDialog.Builder builder = new AlertDialog.Builder(context);
    builder.setTitle("Xoá bài viết " + contentList.get(position).getTitle() + " này?");
    builder.setMessage("Bạn có muốn xóa bài viết " + contentList.get(position).getTitle() + " này?");
    builder.setPositiveButton("Đồng ý", (dialog, which) -> {
      myDB.deleteContent(contentList.get(position).getId());
      contentList.remove(position);
      notifyItemRemoved(position);
      notifyItemRangeChanged(position, contentList.size());
    });
    builder.setNegativeButton("Hủy bỏ", (dialog, which) -> {
    });
    builder.create().show();
  }
}
