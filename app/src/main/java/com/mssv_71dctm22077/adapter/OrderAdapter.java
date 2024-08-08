package com.mssv_71dctm22077.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
    private TextView orderStatusTextView;  // Add this line

    public OrderViewHolder(@NonNull View itemView) {
      super(itemView);
      orderIdTextView = itemView.findViewById(R.id.order_id);
      orderDateTextView = itemView.findViewById(R.id.order_date);
      orderStatusTextView = itemView.findViewById(R.id.order_status);  // Add this line

      itemView.setOnClickListener(v -> {
        int position = getAdapterPosition();
        if (position != RecyclerView.NO_POSITION) {
          Order order = orderList.get(position);
          Intent intent = new Intent(context, OrderDetailActivity.class);
          intent.putExtra("orderId", order.getOrderId());
          context.startActivity(intent);
        }
      });
    }

    public void bind(Order order) {
      orderIdTextView.setText("MA2024 " + order.getOrderId());
      orderDateTextView.setText("Ngày tạo: " + order.getCreatedAt());
      orderStatusTextView.setText(order.getStatus().getDisplayName());  // Update status display
      orderStatusTextView.setTextColor(order.getStatus().getColor());  // Set color based on status
    }
  }
}
