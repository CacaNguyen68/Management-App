package com.mssv_71dctm22077.adapter;

import android.content.Context;
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
import com.mssv_71dctm22077.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
  private Context context;
  private List<User> userList;
  private List<User> userListFull; // Danh sách người dùng đầy đủ

  public UserAdapter(Context context, List<User> userList) {
    this.context = context;
    this.userList = userList;
    this.userListFull = new ArrayList<>(userList); // Sao chép danh sách người dùng gốc
  }

  @NonNull
  @Override
  public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(context).inflate(R.layout.detail_user, parent, false);
    return new UserViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
    User user = userList.get(position);
    holder.nameTextView.setText(user.getName());
    holder.phoneTextView.setText("Số điện thoại: "+user.getPhone());
    holder.emailTextView.setText("Email: "+user.getEmail());
    holder.userTypeTextView.setText("Loại tài khoản: "+user.getUserType());
    // Hiển thị ngày sinh
    holder.birthTextView.setText("Ngày sinh: "+ user.getDob());

    // Hiển thị ngày khởi tạo
    holder.createdTextView.setText("Ngày khởi tạo: "+user.getCreatedAt());
    // Display user image
    if (user.getImage() != null) {
      Bitmap bitmap = BitmapFactory.decodeByteArray(user.getImage(), 0, user.getImage().length);
      holder.imageView.setImageBitmap(bitmap);
    } else {
      holder.imageView.setImageResource(R.drawable.ic_user); // Placeholder image
    }
  }

  // Filter method to perform search
  public void filter(String text) {
    text = text.toLowerCase(Locale.getDefault());
    userList.clear();
    if (text.length() == 0) {
      userList.addAll(userListFull);
    } else {
      for (User user : userListFull) {
        if (user.getName().toLowerCase(Locale.getDefault()).contains(text)) {
          userList.add(user);
        }
      }
    }
    notifyDataSetChanged();
  }

  @Override
  public int getItemCount() {
    return userList.size();
  }

  public static class UserViewHolder extends RecyclerView.ViewHolder {
    TextView nameTextView, phoneTextView, emailTextView, userTypeTextView, birthTextView, createdTextView, userCreatedTextView;
    ImageView imageView;

    public UserViewHolder(@NonNull View itemView) {
      super(itemView);
      nameTextView = itemView.findViewById(R.id.user_name);
      phoneTextView = itemView.findViewById(R.id.user_phone);
      emailTextView = itemView.findViewById(R.id.user_email);
      userTypeTextView = itemView.findViewById(R.id.user_type);
      birthTextView = itemView.findViewById(R.id.user_dob);
      createdTextView = itemView.findViewById(R.id.user_created);
      imageView = itemView.findViewById(R.id.user_image);
    }
  }
}
