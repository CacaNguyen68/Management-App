package com.mssv_71dctm22077.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mssv_71dctm22077.R;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

  private Context context;
  private ArrayList category_id, category_name, caregory_created;

  public CustomAdapter(Context context, ArrayList category_id, ArrayList category_name, ArrayList caregory_created) {
    this.context = context;
    this.category_id = category_id;
    this.category_name = category_name;
    this.caregory_created = caregory_created;
  }

  @NonNull
  @Override
  public CustomAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(context);
    View view = inflater.inflate(R.layout.details_category,parent,false);
    return new MyViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull CustomAdapter.MyViewHolder holder, int position) {
    holder.category_id.setText(String.valueOf(category_id.get(position)));
    holder.category_name.setText(String.valueOf(category_name.get(position)));
    holder.caregory_created.setText(String.valueOf(caregory_created.get(position)));
  }

  @Override
  public int getItemCount() {
    return category_id.size();
  }

  public class MyViewHolder extends RecyclerView.ViewHolder {
    TextView category_id, category_name, caregory_created;
    public MyViewHolder(@NonNull View itemView) {
      super(itemView);
      category_id = itemView.findViewById(R.id.category_id_txt);
      category_name = itemView.findViewById(R.id.category_name_txt);
      caregory_created = itemView.findViewById(R.id.category_created_txt);
    }
  }
}
