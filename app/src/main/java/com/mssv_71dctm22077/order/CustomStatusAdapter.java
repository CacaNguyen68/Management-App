package com.mssv_71dctm22077.order;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mssv_71dctm22077.model.OrderStatus;

public class CustomStatusAdapter extends ArrayAdapter<OrderStatus> {

  private final Context context;
  private final OrderStatus[] statuses;

  public CustomStatusAdapter(Context context, OrderStatus[] statuses) {
    super(context, android.R.layout.simple_spinner_item, statuses);
    this.context = context;
    this.statuses = statuses;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    if (convertView == null) {
      convertView = LayoutInflater.from(context).inflate(android.R.layout.simple_spinner_item, parent, false);
    }

    TextView textView = convertView.findViewById(android.R.id.text1);
    OrderStatus status = statuses[position];
    textView.setText(status.getDisplayName());
    textView.setTextColor(status.getColor());
    return convertView;
  }

  @Override
  public View getDropDownView(int position, View convertView, ViewGroup parent) {
    if (convertView == null) {
      convertView = LayoutInflater.from(context).inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
    }

    TextView textView = convertView.findViewById(android.R.id.text1);
    OrderStatus status = statuses[position];
    textView.setText(status.getDisplayName());
    textView.setTextColor(status.getColor());
    return convertView;
  }
}
