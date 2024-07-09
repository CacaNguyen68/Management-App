package com.mssv_71dctm22077.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mssv_71dctm22077.R;
import com.mssv_71dctm22077.Category.UpdateCategoryActivity;
import com.mssv_71dctm22077.sqlite.MyDatabaseHelper;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

  private Context context;
  private ArrayList category_id, category_name, caregory_created;
  Activity activity;
  MyDatabaseHelper myDB;

  public CustomAdapter(Activity activity, Context context, ArrayList category_id, ArrayList category_name, ArrayList caregory_created) {
    this.activity = activity;
    this.context = context;
    this.category_id = category_id;
    this.category_name = category_name;
    this.caregory_created = caregory_created;
  }

  @NonNull
  @Override
  public CustomAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(context);
    View view = inflater.inflate(R.layout.details_category, parent, false);
    return new MyViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull CustomAdapter.MyViewHolder holder, final int position) {
    holder.category_id.setText(String.valueOf(category_id.get(position)));
    holder.category_name.setText(String.valueOf(category_name.get(position)));
    holder.caregory_created.setText(String.valueOf(caregory_created.get(position)));
    holder.mainLayout.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
//        Intent intent = new Intent(context, UpdateCategoryActivity.class);
//        intent.putExtra("id", String.valueOf(category_id.get(position)));
//        intent.putExtra("name", String.valueOf(category_name.get(position)));
//        intent.putExtra("created", String.valueOf(caregory_created.get(position)));
//
//        activity.startActivityForResult(intent, 1);
      }
    });

    holder.floatingDelete.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        confirmDialog(position);
      }
    });

    holder.floatingUpdate.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(context, UpdateCategoryActivity.class);
        intent.putExtra("id", String.valueOf(category_id.get(position)));
        intent.putExtra("name", String.valueOf(category_name.get(position)));
        intent.putExtra("created", String.valueOf(caregory_created.get(position)));
        activity.startActivityForResult(intent, 1);
      }
    });
  }

  @Override
  public int getItemCount() {
    return category_id.size();
  }

  public class MyViewHolder extends RecyclerView.ViewHolder {
    FloatingActionButton floatingDelete, floatingUpdate;
    TextView category_id, category_name, caregory_created;
    LinearLayout mainLayout;

    public MyViewHolder(@NonNull View itemView) {
      super(itemView);
      category_id = itemView.findViewById(R.id.category_id_txt);
      category_name = itemView.findViewById(R.id.category_name_txt);
      caregory_created = itemView.findViewById(R.id.category_created_txt);
      mainLayout = itemView.findViewById(R.id.mainLayout);
      floatingDelete = itemView.findViewById(R.id.floatingDelete); // Initialize floatingDelete here
      floatingUpdate = itemView.findViewById(R.id.floatingUpdate);
      //Animate Recyclerview
    }
  }

  void confirmDialog(int position){
    AlertDialog.Builder builder = new AlertDialog.Builder(context);
    builder.setTitle("Xoá danh mục "+category_name.get(position).toString()+" này?");
    builder.setMessage("Bạn có muốn xóa danh mục "+category_name.get(position).toString()+" này?");
    builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        MyDatabaseHelper myDB = new MyDatabaseHelper(context);
        myDB.deleteCategory(category_id.get(position).toString());
        // Xóa category khỏi ArrayList
        category_id.remove(position);
        category_name.remove(position);
        caregory_created.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, category_id.size());
      }
    });
    builder.setNegativeButton("Hủy bỏ", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
      }
    });
    builder.create().show();

  }

}
