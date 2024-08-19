package com.mssv_71dctm22077.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mssv_71dctm22077.R;
import com.mssv_71dctm22077.model.Order;
import com.mssv_71dctm22077.model.OrderStatus;
import com.mssv_71dctm22077.order.CustomStatusAdapter;
import com.mssv_71dctm22077.order.OrderDetailActivity;
import com.mssv_71dctm22077.sqlite.MyDatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class OrderAdminAdapter extends RecyclerView.Adapter<OrderAdminAdapter.OrderViewHolder> {

  private List<Order> orders;
  private List<Order> filteredOrders;
  private Context context;
  private MyDatabaseHelper databaseHelper;

  public OrderAdminAdapter(Context context, List<Order> orders) {
    this.context = context;
    this.orders = orders;
    this.filteredOrders = new ArrayList<>(orders); // Initialize filtered list
    this.databaseHelper = new MyDatabaseHelper(context);
  }

  @NonNull
  @Override
  public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
      .inflate(R.layout.item_order_by_admin, parent, false);
    return new OrderViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
    Order order = filteredOrders.get(position);
    holder.orderIdTextView.setText("MA2024" + order.getOrderId());
    holder.createdAtTextView.setText("Ngày tạo: " + order.getCreatedAt());

    CustomStatusAdapter adapter = new CustomStatusAdapter(
      context,
      OrderStatus.values()
    );
    holder.statusSpinner.setAdapter(adapter);

    // Set the spinner selection based on the order status
    holder.statusSpinner.setSelection(order.getStatus().ordinal());

    // Handle spinner item selection
    holder.statusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        OrderStatus newStatus = OrderStatus.values()[position];
        if (order.getStatus() != newStatus) {
          showConfirmDialog(order, newStatus);
        }
      }

      @Override
      public void onNothingSelected(AdapterView<?> parent) {
        // No action needed
      }
    });

  }

  @Override
  public int getItemCount() {
    return filteredOrders.size();
  }


  public Filter getFilter() {
    return new Filter() {
      @Override
      protected FilterResults performFiltering(CharSequence constraint) {
        String query = constraint.toString().toLowerCase();
        List<Order> filtered = new ArrayList<>();
        if (query.isEmpty()) {
          filtered.addAll(orders);
        } else {
          for (Order order : orders) {
            if (order.getStatus().toString().toLowerCase().contains(query)) {
              filtered.add(order);
            }
          }
        }
        FilterResults results = new FilterResults();
        results.values = filtered;
        return results;
      }

      @SuppressWarnings("unchecked")
      @Override
      protected void publishResults(CharSequence constraint, FilterResults results) {
        filteredOrders.clear();
        filteredOrders.addAll((List<Order>) results.values);
        notifyDataSetChanged();
      }
    };
  }

  private void showConfirmDialog(Order order, OrderStatus newStatus) {
    new AlertDialog.Builder(context)
      .setTitle("Xác nhận thay đổi trạng thái")
      .setMessage("Bạn có chắc chắn muốn thay đổi trạng thái thành " + newStatus.getDisplayName() + "?")
      .setPositiveButton("Đồng ý", (dialog, which) -> {
        // Update the order status in the database
        order.setStatus(newStatus);
        databaseHelper.updateOrderStatus(order.getOrderId(), newStatus);
        // Show confirmation toast message
        Toast.makeText(context, "Trạng thái đơn hàng được cập nhật thành " + newStatus.getDisplayName(), Toast.LENGTH_SHORT).show();
      })
      .setNegativeButton("Không", (dialog, which) -> {
        // Restore the original status in case of cancellation
        dialog.dismiss();
        notifyDataSetChanged();
      })
      .create()
      .show();
  }

  public class OrderViewHolder extends RecyclerView.ViewHolder {

    TextView orderIdTextView;
    TextView createdAtTextView;
    Spinner statusSpinner;

    OrderViewHolder(View itemView) {
      super(itemView);
      orderIdTextView = itemView.findViewById(R.id.order_id);
      createdAtTextView = itemView.findViewById(R.id.created_at);
      statusSpinner = itemView.findViewById(R.id.order_status);
      itemView.setOnClickListener(v -> {
        int position = getAdapterPosition();
        if (position != RecyclerView.NO_POSITION) {
          Order order = filteredOrders.get(position);
          Intent intent = new Intent(context, OrderDetailActivity.class);
          intent.putExtra("orderId", order.getOrderId());
          context.startActivity(intent);
        }
      });
    }
  }
}
