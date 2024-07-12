package com.mssv_71dctm22077.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mssv_71dctm22077.R;

import java.util.List;

public class ImageSliderAdapter extends RecyclerView.Adapter<ImageSliderAdapter.SliderViewHolder> {

  private List<Integer> images;
  private LayoutInflater inflater;

  public ImageSliderAdapter(Context context, List<Integer> images) {
    this.images = images;
    this.inflater = LayoutInflater.from(context);
  }

  @NonNull
  @Override
  public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = inflater.inflate(R.layout.slider_item, parent, false);
    return new SliderViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull SliderViewHolder holder, int position) {
    holder.imageView.setImageResource(images.get(position));
  }

  @Override
  public int getItemCount() {
    return images.size();
  }

  static class SliderViewHolder extends RecyclerView.ViewHolder {
    ImageView imageView;

    public SliderViewHolder(@NonNull View itemView) {
      super(itemView);
      imageView = itemView.findViewById(R.id.imageView);
    }
  }
}
