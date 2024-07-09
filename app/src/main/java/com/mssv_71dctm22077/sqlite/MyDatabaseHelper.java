package com.mssv_71dctm22077.sqlite;

import static android.content.ContentValues.TAG;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MyDatabaseHelper extends SQLiteOpenHelper {

  private Context context;
  private static final String DATABASE_NAME = "ManagementCosplay.db";
  private static int DATABASE_VERSION = 1;

  //  Khởi tạo cho table của danh mục
  private static final String TABLE_CATEGORY = "table_danhmuc";
  private static final String COLUMN_ID_CATEGORY = "id";
  private static final String COLUMN_NAME_CATEGORY = "ten_danh_muc";
  private static final String COLUMN_CREATED_CATEGORY = "ngay_khoi_tao";

  //  Khởi tạo cho table của sản phẩm
  private static final String TABLE_PRODUCT = "table_sanpham";
  private static final String COLUMN_ID_PRODUCT = "id";
  private static final String COLUMN_NAME_PRODUCT = "ten_san_pham";
  private static final String COLUMN_IMAGE_PRODUCT = "hinh_anh";
  private static final String COLUMN_PRICE_PRODUCT = "gia_san_pham";
  private static final String COLUMN_CATEGORY_ID = "danhmuc_id"; // Đây là khóa ngoại
  private static final String COLUMN_CREATED_PRODUCT = "ngay_khoi_tao";

  //  Khoi tao cho table nguoi dung
  private static final String TABLE_USER = "table_user";
  private static final String COLUMN_ID_USER = "id";
  private static final String COLUMN_NAME_USER = "ten_nguoi_dung";
  private static final String COLUMN_DOB_USER = "ngay_sinh";
  private static final String COLUMN_PHONE_USER = "so_dien_thoai";
  private static final String COLUMN_EMAIL_USER = "email";
  private static final String COLUMN_USERNAME = "username";
  private static final String COLUMN_PASSWORD = "password";
  private static final String COLUMN_USER_TYPE = "loai_nguoi_dung";
  private static final String COLUMN_CREATED_USER = "ngay_khoi_tao";
  private static final String COLUMN_IMAGE_USER = "hinh_anh";


  public MyDatabaseHelper(@Nullable Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
    this.context = context;
  }


  @Override
  public void onCreate(SQLiteDatabase db) {

//    Cau lenh tao bang cho danh muc
    String queryCategory = "CREATE TABLE " + TABLE_CATEGORY + "(" + COLUMN_ID_CATEGORY + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_NAME_CATEGORY + " TEXT, " + COLUMN_CREATED_CATEGORY + " TEXT" + ")";
    db.execSQL(queryCategory);

//    Cau lenh tao bang cho san pham
    String queryProduct = "CREATE TABLE " + TABLE_PRODUCT + "(" + COLUMN_ID_PRODUCT + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_NAME_PRODUCT + " TEXT, " + COLUMN_IMAGE_PRODUCT + " TEXT, " + COLUMN_PRICE_PRODUCT + " DOUBLE, " + COLUMN_CREATED_PRODUCT + " TEXT, " + COLUMN_CATEGORY_ID + " INTEGER, " + "FOREIGN KEY(" + COLUMN_CATEGORY_ID + ") REFERENCES " + TABLE_CATEGORY + "(" + COLUMN_ID_CATEGORY + ")" + ")";
    db.execSQL(queryProduct);

    // Câu lệnh tạo bảng cho bảng "user" với trường hình ảnh
    String queryUser = "CREATE TABLE " + TABLE_USER + "(" + COLUMN_ID_USER + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_NAME_USER + " TEXT, " + COLUMN_DOB_USER + " TEXT, " + COLUMN_PHONE_USER + " TEXT, " + COLUMN_EMAIL_USER + " TEXT, " + COLUMN_USERNAME + " TEXT UNIQUE, " + COLUMN_PASSWORD + " TEXT, " + COLUMN_USER_TYPE + " TEXT, " + COLUMN_CREATED_USER + " TEXT, " + COLUMN_IMAGE_USER + " BLOB" + ")";

// Thực thi câu lệnh SQL để tạo bảng "user" với trường hình ảnh
    db.execSQL(queryUser);

  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT);
    onCreate(db);
  }

  // Phương thức để thêm một người dùng mới vào cơ sở dữ liệu
  public void addUser(String name, String dob, String phone, String email, String username, String password, String userType, String createdDate, byte[] image) {
    SQLiteDatabase db = this.getWritableDatabase();
    ContentValues values = new ContentValues();

    values.put(COLUMN_NAME_USER, name);
    values.put(COLUMN_DOB_USER, dob);
    values.put(COLUMN_PHONE_USER, phone);
    values.put(COLUMN_EMAIL_USER, email);
    values.put(COLUMN_USERNAME, username);
    values.put(COLUMN_PASSWORD, password);
    values.put(COLUMN_USER_TYPE, userType);
    values.put(COLUMN_CREATED_USER, createdDate);
    values.put(COLUMN_IMAGE_USER, image);

    // Thực thi câu lệnh insert vào bảng TABLE_USER
    long result = db.insert(TABLE_USER, null, values);

    // Đóng kết nối đến cơ sở dữ liệu
    db.close();
  }


  //  kiem tra xem ten danh muc do co ton tai hay khong
  public boolean isDanhMucExists(String tenDanhMuc) {
    SQLiteDatabase db = this.getReadableDatabase();
    String query = "SELECT * FROM " + TABLE_CATEGORY + " WHERE " + COLUMN_NAME_CATEGORY + "=?";
    Cursor cursor = db.rawQuery(query, new String[]{tenDanhMuc});
    boolean exists = cursor.getCount() > 0;
    cursor.close();
    return exists;
  }

  // Cau truy van lay so luong danh muc trong table
  private int getDanhMucCount() {
    SQLiteDatabase db = this.getReadableDatabase();
    Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_CATEGORY, null);
    cursor.moveToFirst();
    int count = cursor.getInt(0);
    cursor.close();
    return count;
  }

  //  Cau truy van lay danh sach danh muc
  public Cursor getAllDanhMuc() {
    String query = "SELECT * FROM " + TABLE_CATEGORY;
    SQLiteDatabase db = this.getReadableDatabase();

    Cursor cursor = null;
    if (db != null) {
      cursor = db.rawQuery(query, null);
    }
    return cursor;
  }

  //  Cau truy van them 1 danh muc moi
  public void addDanhMuc(String tenDanhMuc) {
    SQLiteDatabase db = this.getWritableDatabase();
    ContentValues cv = new ContentValues();

    cv.put(COLUMN_NAME_CATEGORY, tenDanhMuc);
    cv.put(COLUMN_CREATED_CATEGORY, getCurrentDate());

    long result = db.insert(TABLE_CATEGORY, null, cv);

  }

  //  Cau truy van update danh muc tu id danh muc
  public void updateCategory(String row_id, String name) {
    if (name.length() > 0) {
      SQLiteDatabase db = this.getWritableDatabase();
      ContentValues cv = new ContentValues();
      cv.put(COLUMN_NAME_CATEGORY, name);
      cv.put(COLUMN_CREATED_CATEGORY, getCurrentDate());

      long result = db.update(TABLE_CATEGORY, cv, COLUMN_ID_CATEGORY + "=?", new String[]{row_id});
      if (result == -1) {
        Toast.makeText(context, "Cập nhật thất bại!", Toast.LENGTH_SHORT).show();
      } else {
        Toast.makeText(context, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
      }
    } else {
      Toast.makeText(context, "Tên danh mục không được rỗng", Toast.LENGTH_SHORT).show();
    }
  }

  //  Cau truy van xoa 1 danh muc tu id danh muc
  public void deleteCategory(String row_id) {
    try {
      SQLiteDatabase db = this.getWritableDatabase();
      int result = db.delete(TABLE_CATEGORY, COLUMN_ID_CATEGORY + "=?", new String[]{row_id});
      if (result > 0) {
        Toast.makeText(context, "Xóa danh mục thành công!", Toast.LENGTH_SHORT).show();
      } else {
        Toast.makeText(context, "Xóa danh mục thất bại!", Toast.LENGTH_SHORT).show();
      }
    } catch (Exception e) {
      Log.e(TAG, "Error deleting category: " + e.getMessage());
      Toast.makeText(context, "Lỗi khi xóa danh mục!", Toast.LENGTH_SHORT).show();
    }
  }

  //  Cau truy van lay danh sach san pham
  public Cursor getAllProducts() {
    String query = "SELECT * FROM " + TABLE_PRODUCT;
    SQLiteDatabase db = this.getReadableDatabase();

    Cursor cursor = null;
    if (db != null) {
      cursor = db.rawQuery(query, null);
    }
    return cursor;
  }

  // Lấy ngày hiện tại dd-MM-yyyy
  private String getCurrentDate() {
    Date today = new Date();
    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
    return formatter.format(today);
  }
}
