package com.mssv_71dctm22077.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mssv_71dctm22077.R;
import com.mssv_71dctm22077.model.Order;
import com.mssv_71dctm22077.order.OrderDetailActivity;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
  private Context context;
  private List<Order> orderList;

  public OrderAdapter(Context context, List<Order> orderList) {
    this.context = context;
    this.orderList = orderList;
  }

  @NonNull
  @Override
  public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
    return new OrderViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
    Order order = orderList.get(position);
    holder.bind(order);
  }

  @Override
  public int getItemCount() {
    return orderList.size();
  }

  public class OrderViewHolder extends RecyclerView.ViewHolder {

    private TextView orderIdTextView;
    private TextView orderDateTextView;

    public OrderViewHolder(@NonNull View itemView) {
      super(itemView);
      orderIdTextView = itemView.findViewById(R.id.order_id);
      orderDateTextView = itemView.findViewById(R.id.order_date);

      itemView.setOnClickListener(v -> {
        int position = getAdapterPosition();
        if (position != RecyclerView.NO_POSITION) {
          Order order = orderList.get(position);
          Intent intent = new Intent(context, OrderDetailActivity.class);
          intent.putExtra("orderId", order.getOrderId());  // Truyền orderId dưới dạng int
          System.out.println("Order ID: " + order.getOrderId());  // In ra để kiểm tra
          context.startActivity(intent);
        }
      });
    }

    public void bind(Order order) {
      orderIdTextView.setText("Order ID: " + order.getOrderId());
      orderDateTextView.setText("Date: " + order.getCreatedAt());
    }
  }
}
