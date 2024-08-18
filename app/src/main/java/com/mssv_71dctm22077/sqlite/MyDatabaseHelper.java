package com.mssv_71dctm22077.sqlite;

import static android.content.ContentValues.TAG;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.mssv_71dctm22077.model.CartItem;
import com.mssv_71dctm22077.model.Content;
import com.mssv_71dctm22077.model.Order;
import com.mssv_71dctm22077.model.OrderDetail;
import com.mssv_71dctm22077.model.OrderStatus;
import com.mssv_71dctm22077.model.Product;
import com.mssv_71dctm22077.model.Review;
import com.mssv_71dctm22077.model.User;

import org.mindrot.jbcrypt.BCrypt;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
  private static final String COLUMN_USER_CREATED_PRODUCT = "nguoi_khoi_tao";

  //  Khoi tao cho table nguoi dung
  private static final String TABLE_USER = "table_user";
  private static final String COLUMN_ID_USER = "id";
  private static final String COLUMN_NAME_USER = "ten_nguoi_dung";
  private static final String COLUMN_DOB_USER = "ngay_sinh";
  private static final String COLUMN_PHONE_USER = "so_dien_thoai";
  private static final String COLUMN_EMAIL_USER = "email";
  private static final String COLUMN_PASSWORD = "password";
  private static final String COLUMN_USER_TYPE = "loai_nguoi_dung";
  private static final String COLUMN_CREATED_USER = "ngay_khoi_tao";
  private static final String COLUMN_USER_CREATED_USER = "nguoi_khoi_tao";
  private static final String COLUMN_IMAGE_USER = "hinh_anh";


  //Khoi tao cho table cart
  private static final String TABLE_CART = "table_cart";
  private static final String COLUMN_CART_ID = "cart_id";
  private static final String COLUMN_USER_ID_CART = "user_id";
  private static final String COLUMN_CREATED_AT_CART = "created_at";

  //Khoi tao cho tung item trong cart
  private static final String TABLE_CART_ITEM = "table_cart_item";
  private static final String COLUMN_CART_ITEM_ID = "cart_item_id";
  private static final String COLUMN_CART_ID_ITEM = "cart_id";
  private static final String COLUMN_PRODUCT_ID_ITEM = "product_id";
  private static final String COLUMN_QUANTITY_ITEM = "quantity";


  private static final String TABLE_ORDER = "table_order";
  private static final String COLUMN_ORDER_ID = "order_id";
  private static final String COLUMN_USER_ID_ORDER = "user_id";
  private static final String COLUMN_CREATED_AT_ORDER = "created_at";
  private static final String COLUMN_STATUS_ORDER = "status"; // Thêm cột trạng thái vào bảng
  private static final String COLUMN_ADDRESS_ORDER = "address"; // New address column
 private static final String COLUMN_TOTAL_ORDER = "total"; // New address column


  private static final String TABLE_ORDER_ITEM = "table_order_item";
  private static final String COLUMN_ORDER_ITEM_ID = "order_item_id";
  private static final String COLUMN_ORDER_ID_ITEM = "order_id";
  private static final String COLUMN_PRODUCT_ID_ORDER_ITEM = "product_id";
  private static final String COLUMN_QUANTITY_ORDER_ITEM = "quantity";

  // Tên bảng và các cột của bảng review
  private static final String TABLE_REVIEW = "table_review";
  private static final String COLUMN_REVIEW_ID = "review_id";
  private static final String COLUMN_ORDER_ID_REVIEW = "order_id";
  private static final String COLUMN_PRODUCT_ID_REVIEW = "product_id";
  private static final String COLUMN_RATING = "rating";
  private static final String COLUMN_REVIEW_TEXT = "review_text";
  private static final String COLUMN_CREATED_AT_REVIEW = "created_at";
  private static final String COLUMN_CREATED_BY_REVIEW = "created_by";

  private static final String TABLE_CONTENT = "table_content";
  private static final String COLUMN_CONTENT_ID = "id";
  private static final String COLUMN_IMAGE_CONTENT = "image";
  private static final String COLUMN_TEXT_CONTENT = "content";
  private static final String COLUMN_TITLE_CONTENT = "title";
  private static final String COLUMN_CREATED_AT_CONTENT = "createdAt";
  private static final String COLUMN_CLICK_CONTENT = "click";

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
    String queryProduct = "CREATE TABLE " + TABLE_PRODUCT + "(" + COLUMN_ID_PRODUCT + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_NAME_PRODUCT + " TEXT, " + COLUMN_IMAGE_PRODUCT + " TEXT, " + COLUMN_PRICE_PRODUCT + " DOUBLE, " + COLUMN_CREATED_PRODUCT + " TEXT," + COLUMN_USER_CREATED_PRODUCT + " TEXT, " + COLUMN_CATEGORY_ID + " INTEGER, " + "FOREIGN KEY(" + COLUMN_CATEGORY_ID + ") REFERENCES " + TABLE_CATEGORY + "(" + COLUMN_ID_CATEGORY + ")" + ")";
    db.execSQL(queryProduct);

    // Câu lệnh tạo bảng cho bảng "user" với trường hình ảnh
    String queryUser = "CREATE TABLE " + TABLE_USER + "(" + COLUMN_ID_USER + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_NAME_USER + " TEXT, " + COLUMN_DOB_USER + " TEXT, " + COLUMN_PHONE_USER + " TEXT UNIQUE, " + COLUMN_EMAIL_USER + " TEXT, " + COLUMN_PASSWORD + " TEXT, " + COLUMN_USER_TYPE + " TEXT," + COLUMN_USER_CREATED_USER + " TEXT, " + COLUMN_CREATED_USER + " TEXT, " + COLUMN_IMAGE_USER + " BLOB" + ")";

// Thực thi câu lệnh SQL để tạo bảng "user" với trường hình ảnh
    db.execSQL(queryUser);

    String queryCart = "CREATE TABLE " + TABLE_CART + " (" + COLUMN_CART_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_USER_ID_CART + " INTEGER, " + COLUMN_CREATED_AT_CART + " TEXT)";
    db.execSQL(queryCart);

    String queryCartItem = "CREATE TABLE " + TABLE_CART_ITEM + " (" + COLUMN_CART_ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_CART_ID_ITEM + " INTEGER, " + COLUMN_PRODUCT_ID_ITEM + " INTEGER, " + COLUMN_QUANTITY_ITEM + " INTEGER, " + "FOREIGN KEY(" + COLUMN_CART_ID_ITEM + ") REFERENCES " + TABLE_CART + "(" + COLUMN_CART_ID + "))";
    db.execSQL(queryCartItem);

    String queryOrder = "CREATE TABLE " + TABLE_ORDER + " ("
      + COLUMN_ORDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
      + COLUMN_USER_ID_ORDER + " INTEGER, "
      + COLUMN_CREATED_AT_ORDER + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
      + COLUMN_STATUS_ORDER + " TEXT, "
      + COLUMN_ADDRESS_ORDER + " TEXT, "
      + COLUMN_TOTAL_ORDER + " DOUBLE, "
      + "FOREIGN KEY(" + COLUMN_USER_ID_ORDER + ") REFERENCES " + TABLE_USER + "(" + COLUMN_ID_USER + "))";
    db.execSQL(queryOrder);

    String queryOrderItem = "CREATE TABLE " + TABLE_ORDER_ITEM + " (" + COLUMN_ORDER_ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_ORDER_ID_ITEM + " INTEGER, " + COLUMN_PRODUCT_ID_ORDER_ITEM + " INTEGER, " + COLUMN_QUANTITY_ORDER_ITEM + " INTEGER, " + "FOREIGN KEY(" + COLUMN_ORDER_ID_ITEM + ") REFERENCES " + TABLE_ORDER + "(" + COLUMN_ORDER_ID + "), " + "FOREIGN KEY(" + COLUMN_PRODUCT_ID_ORDER_ITEM + ") REFERENCES " + TABLE_PRODUCT + "(" + COLUMN_ID_PRODUCT + ")" + ")";
    db.execSQL(queryOrderItem);


    String queryReview = "CREATE TABLE " + TABLE_REVIEW + " (" + COLUMN_REVIEW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_ORDER_ID_REVIEW + " INTEGER NOT NULL, " + COLUMN_PRODUCT_ID_REVIEW + " INTEGER NOT NULL, " + COLUMN_RATING + " REAL NOT NULL, " + COLUMN_REVIEW_TEXT + " TEXT, " + COLUMN_CREATED_BY_REVIEW + " TEXT, " + COLUMN_CREATED_AT_REVIEW + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" + ");";

    db.execSQL(queryReview);

    String queryContent = "CREATE TABLE " + TABLE_CONTENT + "(" + COLUMN_CONTENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_IMAGE_CONTENT + " BLOB, " + COLUMN_TEXT_CONTENT + " TEXT NOT NULL, " + COLUMN_TITLE_CONTENT + " TEXT NOT NULL, " + COLUMN_CREATED_AT_CONTENT + " TEXT, " + COLUMN_CLICK_CONTENT + " INTEGER" + ")";

    db.execSQL(queryContent);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT);
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART_ITEM);
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDER);
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDER_ITEM);
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_REVIEW);
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTENT);

    onCreate(db);
  }
  // Phương thức thêm cột total_amount

  //  Câu truy vấn lay type user tu phone
  public String getTypeUserByPhone(String phone) {
    SQLiteDatabase db = this.getReadableDatabase();
    String query = "SELECT " + COLUMN_USER_TYPE + " FROM " + TABLE_USER + " WHERE " + COLUMN_PHONE_USER + "=?";
    Cursor cursor = db.rawQuery(query, new String[]{phone});
    String type = null;

    cursor.moveToFirst();
    type = cursor.getString(cursor.getColumnIndex(COLUMN_USER_TYPE));

    return type;
  }

  //    Câu truy vấn cho đăng nhập
  public boolean checkUser(String phone, String password) {
    SQLiteDatabase db = this.getReadableDatabase();

    // Xác định các cột cần truy vấn
    String query = "SELECT " + COLUMN_PASSWORD + " FROM " + TABLE_USER + " WHERE " + COLUMN_PHONE_USER + "=?";
    Cursor cursor = db.rawQuery(query, new String[]{phone});

    boolean userExists = false;
    if (cursor.moveToFirst()) {
      String storedPasswordHash = cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD));
      // Kiểm tra mật khẩu đã nhập với mật khẩu đã mã hóa trong cơ sở dữ liệu
      if (BCrypt.checkpw(password, storedPasswordHash)) {
        userExists = true;
      }
    }
    // Đóng cursor và cơ sở dữ liệu
    cursor.close();
    db.close();

    return userExists;
  }

  //  kiem tra xem phone do co ton tai hay khong
  public boolean isPhoneExists(String phone) {
    SQLiteDatabase db = this.getReadableDatabase();
    String query = "SELECT * FROM " + TABLE_USER + " WHERE " + COLUMN_PHONE_USER + "=?";
    Cursor cursor = db.rawQuery(query, new String[]{phone});
    boolean exists = cursor.getCount() > 0;
    cursor.close();
    return exists;
  }

  // Thêm phương thức để lấy thông tin người dùng từ số điện thoại
  public User getUserByPhone(String phone) {
    SQLiteDatabase db = this.getReadableDatabase();
    String query = "SELECT * FROM " + TABLE_USER + " WHERE " + COLUMN_PHONE_USER + "=?";
    Cursor cursor = null;
    User user = null;

    try {
      cursor = db.rawQuery(query, new String[]{phone});
      if (cursor != null && cursor.moveToFirst()) {
        int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID_USER));
        String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_USER));
        String dateOfBirth = cursor.getString(cursor.getColumnIndex(COLUMN_DOB_USER));
        String email = cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL_USER));
        String userType = cursor.getString(cursor.getColumnIndex(COLUMN_USER_TYPE));
        String createdAt = cursor.getString(cursor.getColumnIndex(COLUMN_CREATED_USER));
        String createdBy = cursor.getString(cursor.getColumnIndex(COLUMN_USER_CREATED_USER));
        byte[] image = cursor.getBlob(cursor.getColumnIndex(COLUMN_IMAGE_USER));

        user = new User(id, name, dateOfBirth, phone, email, userType, createdAt, createdBy, image);
      }
    } catch (Exception e) {
      Log.e("Database Error", "Error while getting user by phone", e);
    } finally {
      if (cursor != null) {
        cursor.close();
      }
    }

    return user;
  }


  // Phương thức để thêm một người dùng mới vào cơ sở dữ liệu
  public void addUser(String name, String dob, String phone, String email, String password, String userType, byte[] image) {
    SQLiteDatabase db = this.getWritableDatabase();
    ContentValues values = new ContentValues();

    values.put(COLUMN_NAME_USER, name);
    values.put(COLUMN_DOB_USER, dob);
    values.put(COLUMN_PHONE_USER, phone);
    values.put(COLUMN_EMAIL_USER, email);
    values.put(COLUMN_PASSWORD, password);
    values.put(COLUMN_USER_TYPE, userType);
    values.put(COLUMN_CREATED_USER, getCurrentDate());
    values.put(COLUMN_IMAGE_USER, image);

    try {
      if (isPhoneExists(phone)) {
        Toast.makeText(context, "Số điện thoại đã được đăng ký!", Toast.LENGTH_SHORT).show();
      } else {
        // Thực thi câu lệnh insert vào bảng TABLE_USER
        long result = db.insertOrThrow(TABLE_USER, null, values);
        if (result == -1) {
          Toast.makeText(context, "Thêm thất bại!", Toast.LENGTH_SHORT).show();
        } else {
          Toast.makeText(context, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
        }
      }
    } catch (SQLiteConstraintException e) {
      // Lỗi trùng lặp số điện thoại
      Toast.makeText(context, "Số điện thoại đã tồn tại", Toast.LENGTH_SHORT).show();
    } finally {
      // Đóng kết nối đến cơ sở dữ liệu
      db.close();
    }
  }

  //UPDATE USER
  public void updateUser(int userId, String name, String dob, String phone, String email, String password, String userType, byte[] image) {
    SQLiteDatabase db = this.getWritableDatabase();
    ContentValues values = new ContentValues();

    values.put(COLUMN_NAME_USER, name);
    values.put(COLUMN_DOB_USER, dob);
    values.put(COLUMN_PHONE_USER, phone);
    values.put(COLUMN_EMAIL_USER, email);
    if (password != null && !password.isEmpty()) {
      values.put(COLUMN_PASSWORD, password);
    }
    values.put(COLUMN_USER_TYPE, userType);
    values.put(COLUMN_IMAGE_USER, image);

    try {
      // Cập nhật thông tin người dùng dựa trên ID người dùng
      int rowsAffected = db.update(TABLE_USER, values, COLUMN_ID_USER + " = ?", new String[]{String.valueOf(userId)});
      if (rowsAffected == 0) {
        Toast.makeText(context, "Cập nhật thất bại!", Toast.LENGTH_SHORT).show();
      } else {
        Toast.makeText(context, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
      }
    } catch (SQLiteConstraintException e) {
      // Lỗi trùng lặp số điện thoại hoặc email
      Toast.makeText(context, "Lỗi cập nhật thông tin", Toast.LENGTH_SHORT).show();
    } finally {
      // Đóng kết nối đến cơ sở dữ liệu
      db.close();
    }
  }

  // Câu truy vấn lấy danh sách người dùng mới vào cơ sở dữ liệu
  public Cursor getAllUser() {
    String query = "SELECT * FROM " + TABLE_USER;
    SQLiteDatabase db = this.getReadableDatabase();

    Cursor cursor = null;
    if (db != null) {
      cursor = db.rawQuery(query, null);
    }
    Log.d("List user", "cout = " + cursor.getCount());
    return cursor;
  }

  // Lấy user theo ID
  public User getUserById(int userId) {
    SQLiteDatabase db = this.getReadableDatabase();
    String query = "SELECT * FROM " + TABLE_USER + " WHERE " + COLUMN_ID_USER + "=?";
    Cursor cursor = null;
    User user = null;

    try {
      cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});
      if (cursor != null && cursor.moveToFirst()) {
        int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID_USER));
        String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_USER));
        String dateOfBirth = cursor.getString(cursor.getColumnIndex(COLUMN_DOB_USER));
        String phone = cursor.getString(cursor.getColumnIndex(COLUMN_PHONE_USER));
        String email = cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL_USER));
        String userType = cursor.getString(cursor.getColumnIndex(COLUMN_USER_TYPE));
        String createdAt = cursor.getString(cursor.getColumnIndex(COLUMN_CREATED_USER));
        String createdBy = cursor.getString(cursor.getColumnIndex(COLUMN_USER_CREATED_USER));
        byte[] image = cursor.getBlob(cursor.getColumnIndex(COLUMN_IMAGE_USER));

        user = new User(id, name, dateOfBirth, phone, email, userType, createdAt, createdBy, image);
      }
    } catch (Exception e) {
      Log.e("Database Error", "Error while getting user by ID", e);
    } finally {
      if (cursor != null) {
        cursor.close();
      }
    }

    return user;
  }

  // Lấy danh sách tất cả users
  public List<User> getAllUsers() {
    List<User> userList = new ArrayList<>();
    SQLiteDatabase db = this.getWritableDatabase();
    Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USER, null);

    if (cursor.moveToFirst()) {
      do {
        User user = new User();
        user.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID_USER)));
        user.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_USER)));
        user.setDob(cursor.getString(cursor.getColumnIndex(COLUMN_DOB_USER)));
        user.setPhone(cursor.getString(cursor.getColumnIndex(COLUMN_PHONE_USER)));
        user.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL_USER)));
        user.setUserType(cursor.getString(cursor.getColumnIndex(COLUMN_USER_TYPE)));
        user.setCreatedAt(cursor.getString(cursor.getColumnIndex(COLUMN_CREATED_USER)));
        user.setUserCreatedAt(cursor.getString(cursor.getColumnIndex(COLUMN_USER_CREATED_USER)));
        user.setImage(cursor.getBlob(cursor.getColumnIndex(COLUMN_IMAGE_USER)));
        userList.add(user);
      } while (cursor.moveToNext());
    }
    cursor.close();
    return userList;
  }

  // Tìm kiếm user theo tên hoặc email, sdt
  public List<User> searchUsers(String query) {
    List<User> userList = new ArrayList<>();
    SQLiteDatabase db = this.getWritableDatabase();
    String[] columns = {COLUMN_ID_USER, COLUMN_NAME_USER, COLUMN_DOB_USER, COLUMN_PHONE_USER, COLUMN_EMAIL_USER, COLUMN_USER_TYPE, COLUMN_CREATED_USER, COLUMN_USER_CREATED_USER, COLUMN_IMAGE_USER};
    String selection = COLUMN_NAME_USER + " LIKE ? OR " + COLUMN_EMAIL_USER + " LIKE ? OR " + COLUMN_PHONE_USER + " LIKE ?";
    String[] selectionArgs = {"%" + query + "%", "%" + query + "%", "%" + query + "%"};
    Cursor cursor = db.query(TABLE_USER, columns, selection, selectionArgs, null, null, null);

    if (cursor.moveToFirst()) {
      do {
        User user = new User();
        user.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID_USER)));
        user.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_USER)));
        user.setDob(cursor.getString(cursor.getColumnIndex(COLUMN_DOB_USER)));
        user.setPhone(cursor.getString(cursor.getColumnIndex(COLUMN_PHONE_USER)));
        user.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL_USER)));
        user.setUserType(cursor.getString(cursor.getColumnIndex(COLUMN_USER_TYPE)));
        user.setCreatedAt(cursor.getString(cursor.getColumnIndex(COLUMN_CREATED_USER)));
        user.setUserCreatedAt(cursor.getString(cursor.getColumnIndex(COLUMN_USER_CREATED_USER)));
        user.setImage(cursor.getBlob(cursor.getColumnIndex(COLUMN_IMAGE_USER)));
        userList.add(user);
      } while (cursor.moveToNext());
    }
    cursor.close();
    return userList;
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

  public int getDanhMucCountById(int categoryId) {
    SQLiteDatabase db = this.getReadableDatabase();
    // Thêm điều kiện WHERE để lọc theo ID của danh mục
    Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_CATEGORY + " WHERE id = ?", new String[]{String.valueOf(categoryId)});
    cursor.moveToFirst();
    int count = cursor.getInt(0);
    cursor.close();
    return count;
  }

  //
  public List<String> getCategoryList() {
    List<String> categories = new ArrayList<>();
    Cursor cursor = getAllDanhMuc();

    if (cursor != null && cursor.moveToFirst()) {
      int columnIndex = cursor.getColumnIndex(COLUMN_NAME_CATEGORY); // Tên cột của danh mục trong cơ sở dữ liệu
      do {
        String categoryName = cursor.getString(columnIndex);
        categories.add(categoryName);
      } while (cursor.moveToNext());
      cursor.close();
    }

    return categories;
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

  //  Câu truy vấn lấy tên danh mục từ id danh mục
  public String getCategoryNameById(int categoryId) {
    String categoryName = null;
    SQLiteDatabase db = this.getReadableDatabase();
    Cursor cursor = db.query(TABLE_CATEGORY, new String[]{COLUMN_NAME_CATEGORY}, COLUMN_ID_CATEGORY + "=?", new String[]{String.valueOf(categoryId)}, null, null, null, null);
    if (cursor != null) {
      if (cursor.moveToFirst()) {
        categoryName = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_CATEGORY));
      }
      cursor.close();
    }

    Log.d("Danh muc:", categoryName);
    return categoryName;
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

  // Câu truy vấn lấy danh sách người dùng mới vào cơ sở dữ liệu
  public Cursor getAllProduct() {
    String query = "SELECT * FROM " + TABLE_PRODUCT;
    SQLiteDatabase db = this.getReadableDatabase();

    Cursor cursor = null;
    if (db != null) {
      cursor = db.rawQuery(query, null);
    }
    Log.d("List product", "cout = " + cursor.getCount());
    return cursor;
  }

  //  Cau truy van lay danh sach san pham
  public List<Product> getAllProducts() {
    List<Product> productList = new ArrayList<>();
    String selectQuery = "SELECT * FROM " + TABLE_PRODUCT;
    SQLiteDatabase db = this.getWritableDatabase();
    Cursor cursor = db.rawQuery(selectQuery, null);
    if (cursor.moveToFirst()) {
      do {
        int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID_PRODUCT));
        String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_PRODUCT));
        byte[] image = cursor.getBlob(cursor.getColumnIndex(COLUMN_IMAGE_PRODUCT));
        double price = cursor.getDouble(cursor.getColumnIndex(COLUMN_PRICE_PRODUCT));
        int categoryId = cursor.getInt(cursor.getColumnIndex(COLUMN_CATEGORY_ID));
        String createdAt = cursor.getString(cursor.getColumnIndex(COLUMN_CREATED_PRODUCT));
        String userCreated = cursor.getString(cursor.getColumnIndex(COLUMN_USER_CREATED_PRODUCT));

        productList.add(new Product(id, name, price, categoryId, createdAt, userCreated, image));
      } while (cursor.moveToNext());
    }
    cursor.close();
    return productList;
  }

  // câu truy vấn lấy các sản phẩm theo điều kiện categoryId
  public List<Product> getProductsByCategoryId(int categoryId) {
    List<Product> products = new ArrayList<>();
    String query = "SELECT * FROM " + TABLE_PRODUCT + " WHERE " + COLUMN_CATEGORY_ID + " = ?";

    try (SQLiteDatabase db = this.getReadableDatabase(); Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(categoryId)})) {

      if (cursor.moveToFirst()) {
        do {
          Product product = new Product();
          product.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID_PRODUCT)));
          product.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_PRODUCT)));
          product.setPrice(cursor.getDouble(cursor.getColumnIndex(COLUMN_PRICE_PRODUCT)));
          product.setCategoryId(cursor.getInt(cursor.getColumnIndex(COLUMN_CATEGORY_ID)));
          product.setCreatedAt(cursor.getString(cursor.getColumnIndex(COLUMN_CREATED_PRODUCT)));
          product.setUserCreatedAt(cursor.getString(cursor.getColumnIndex(COLUMN_USER_CREATED_PRODUCT)));
          product.setImage(cursor.getBlob(cursor.getColumnIndex(COLUMN_IMAGE_PRODUCT)));
          products.add(product);
        } while (cursor.moveToNext());
      }
    } catch (Exception e) {
      e.printStackTrace();
      // Có thể thêm thông báo lỗi cho người dùng hoặc ghi log ở đây nếu cần
    }

    return products;
  }

  // Phương thức để thêm một sản phẩm mới vào cơ sở dữ liệu
  public void addProduct(String name, double price, int categoryId, String createdAt, String userCreated, byte[] image) {
    SQLiteDatabase db = this.getWritableDatabase();
    ContentValues values = new ContentValues();

    values.put(COLUMN_NAME_PRODUCT, name);
    values.put(COLUMN_PRICE_PRODUCT, price);
    values.put(COLUMN_CATEGORY_ID, categoryId);
    values.put(COLUMN_CREATED_PRODUCT, createdAt);
    values.put(COLUMN_USER_CREATED_PRODUCT, userCreated);
    values.put(COLUMN_IMAGE_PRODUCT, image);

    try {
      long result = db.insertOrThrow(TABLE_PRODUCT, null, values);
      if (result == -1) {
        Toast.makeText(context, "Thêm sản phẩm thất bại!", Toast.LENGTH_SHORT).show();
      } else {
        Toast.makeText(context, "Thêm sản phẩm thành công", Toast.LENGTH_SHORT).show();
      }
    } catch (SQLiteConstraintException e) {
      // Xử lý các trường hợp lỗi
      Toast.makeText(context, "Lỗi khi thêm sản phẩm: " + e.getMessage(), Toast.LENGTH_SHORT).show();
    } finally {
      db.close();
    }
  }

  //câu truy vấn cập nhật sản phẩm
  public void updateProduct(int id, String name, double price, int categoryId, String createdAt, String userCreated, byte[] image) {
    SQLiteDatabase db = this.getWritableDatabase();
    ContentValues contentValues = new ContentValues();
    contentValues.put(COLUMN_NAME_PRODUCT, name);
    contentValues.put(COLUMN_PRICE_PRODUCT, price);
    contentValues.put(COLUMN_CATEGORY_ID, categoryId);
    contentValues.put(COLUMN_CREATED_PRODUCT, createdAt);
    contentValues.put(COLUMN_USER_CREATED_PRODUCT, userCreated);
    contentValues.put(COLUMN_IMAGE_PRODUCT, image);

    db.update(TABLE_PRODUCT, contentValues, COLUMN_ID_PRODUCT + " = ?", new String[]{String.valueOf(id)});
  }

  // cau truy van search tat ca san pham theo ten san pham
  public List<Product> searchProducts(String query) {
    List<Product> productList = new ArrayList<>();
    String selectQuery = "SELECT * FROM " + TABLE_PRODUCT + " WHERE " + COLUMN_NAME_PRODUCT + " LIKE ?";
    SQLiteDatabase db = this.getWritableDatabase();
    Cursor cursor = db.rawQuery(selectQuery, new String[]{"%" + query + "%"});
    if (cursor.moveToFirst()) {
      do {
        int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID_PRODUCT));
        String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_PRODUCT));
        byte[] image = cursor.getBlob(cursor.getColumnIndex(COLUMN_IMAGE_PRODUCT));
        double price = cursor.getDouble(cursor.getColumnIndex(COLUMN_PRICE_PRODUCT));
        int categoryId = cursor.getInt(cursor.getColumnIndex(COLUMN_CATEGORY_ID));
        String createdAt = cursor.getString(cursor.getColumnIndex(COLUMN_CREATED_PRODUCT));
        String userCreated = cursor.getString(cursor.getColumnIndex(COLUMN_USER_CREATED_PRODUCT));

        productList.add(new Product(id, name, price, categoryId, createdAt, userCreated, image));
      } while (cursor.moveToNext());
    }
    cursor.close();
    return productList;
  }

  public String getProductNameById(int idProduct) {
    String productName = null;
    String query = "SELECT " + COLUMN_NAME_PRODUCT + " FROM " + TABLE_PRODUCT + " WHERE " + COLUMN_ID_PRODUCT + " = ?";

    try (SQLiteDatabase db = this.getReadableDatabase(); Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(idProduct)})) {

      if (cursor.moveToFirst()) {
        productName = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_PRODUCT));
      }
    } catch (Exception e) {
      e.printStackTrace();
      // Có thể thêm thông báo lỗi cho người dùng hoặc ghi log ở đây nếu cần
    }

    return productName;
  }


  public List<Product> searchProductsWhereCategoryId(String query, int categoryId) {
    List<Product> products = new ArrayList<>();
    SQLiteDatabase db = this.getReadableDatabase();
    String sql = "SELECT * FROM " + TABLE_PRODUCT + " WHERE " + COLUMN_CATEGORY_ID + " = ? AND " + COLUMN_NAME_PRODUCT + " LIKE ?";
    Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(categoryId), "%" + query + "%"});

    if (cursor.moveToFirst()) {
      int idIndex = cursor.getColumnIndexOrThrow(COLUMN_ID_PRODUCT);
      int nameIndex = cursor.getColumnIndexOrThrow(COLUMN_NAME_PRODUCT);
      int priceIndex = cursor.getColumnIndexOrThrow(COLUMN_PRICE_PRODUCT);
      int categoryIdIndex = cursor.getColumnIndexOrThrow(COLUMN_CATEGORY_ID);
      int createdAtIndex = cursor.getColumnIndexOrThrow(COLUMN_CREATED_PRODUCT);
      int userCreatedAtIndex = cursor.getColumnIndexOrThrow(COLUMN_USER_CREATED_PRODUCT);
      int imageIndex = cursor.getColumnIndexOrThrow(COLUMN_IMAGE_PRODUCT);

      do {
        Product product = new Product();
        product.setId(cursor.getInt(idIndex));
        product.setName(cursor.getString(nameIndex));
        product.setPrice(cursor.getDouble(priceIndex));
        product.setCategoryId(cursor.getInt(categoryIdIndex));
        product.setCreatedAt(cursor.getString(createdAtIndex));
        product.setUserCreatedAt(cursor.getString(userCreatedAtIndex));
        product.setImage(cursor.getBlob(imageIndex));
        products.add(product);
      } while (cursor.moveToNext());
    }
    cursor.close();
    db.close();
    return products;
  }

  //  Cau truy van xoa 1 san pham tu id danh muc
  public void deleteProduct(String row_id) {
    try {
      SQLiteDatabase db = this.getWritableDatabase();
      int result = db.delete(TABLE_PRODUCT, COLUMN_ID_PRODUCT + "=?", new String[]{row_id});
      if (result > 0) {
        Toast.makeText(context, "Xóa sản phẩm thành công!", Toast.LENGTH_SHORT).show();
      } else {
        Toast.makeText(context, "Xóa sản phẩm thất bại!", Toast.LENGTH_SHORT).show();
      }
    } catch (Exception e) {
      Log.e(TAG, "Error deleting product: " + e.getMessage());
      Toast.makeText(context, "Lỗi khi xóa sản phẩm!", Toast.LENGTH_SHORT).show();
    }
  }

  //  câu truy vấn vẽ chart pei
  public Cursor getProductCountByCategory() {
    String query = "SELECT " + COLUMN_NAME_CATEGORY + ", COUNT(" + TABLE_PRODUCT + "." + COLUMN_ID_PRODUCT + ") AS product_count " + "FROM " + TABLE_CATEGORY + " LEFT JOIN " + TABLE_PRODUCT + " ON " + TABLE_PRODUCT + "." + COLUMN_CATEGORY_ID + " = " + TABLE_CATEGORY + "." + COLUMN_ID_CATEGORY + " GROUP BY " + COLUMN_NAME_CATEGORY;
    SQLiteDatabase db = this.getReadableDatabase();

    Cursor cursor = null;
    if (db != null) {
      cursor = db.rawQuery(query, null);
    }
    Log.d("Product count by category", "count = " + cursor.getCount());
    return cursor;
  }

  //them san pham vao gio hang
//  public void addProductToCart(int userId, int productId, int quantity) {
//    SQLiteDatabase db = this.getWritableDatabase();
//
//    // Kiểm tra nếu giỏ hàng đã tồn tại cho người dùng này
//    String selectQuery = "SELECT * FROM " + TABLE_CART + " WHERE " + COLUMN_USER_ID_CART + " = ?";
//    Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(userId)});
//    int cartId;
//    if (cursor.moveToFirst()) {
//      cartId = cursor.getInt(cursor.getColumnIndex(COLUMN_CART_ID));
//    } else {
//      // Tạo giỏ hàng mới nếu chưa tồn tại
//      ContentValues cartValues = new ContentValues();
//      cartValues.put(COLUMN_USER_ID_CART, userId);
//      cartValues.put(COLUMN_CREATED_AT_CART, System.currentTimeMillis());
//      cartId = (int) db.insert(TABLE_CART, null, cartValues);
//    }
//    cursor.close();
//
//    // Thêm sản phẩm vào giỏ hàng
//    ContentValues values = new ContentValues();
//    values.put(COLUMN_CART_ID_ITEM, cartId);
//    values.put(COLUMN_PRODUCT_ID_ITEM, productId);
//    values.put(COLUMN_QUANTITY_ITEM, quantity);
//
//    db.insert(TABLE_CART_ITEM, null, values);
//    db.close();
//  }

  public void addProductToCart(int userId, int productId, int quantity) {
    SQLiteDatabase db = this.getWritableDatabase();

    // Kiểm tra nếu giỏ hàng đã tồn tại cho người dùng này
    String selectQuery = "SELECT * FROM " + TABLE_CART + " WHERE " + COLUMN_USER_ID_CART + " = ?";
    Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(userId)});
    int cartId;
    if (cursor.moveToFirst()) {
      cartId = cursor.getInt(cursor.getColumnIndex(COLUMN_CART_ID));
    } else {
      // Tạo giỏ hàng mới nếu chưa tồn tại
      ContentValues cartValues = new ContentValues();
      cartValues.put(COLUMN_USER_ID_CART, userId);
      cartValues.put(COLUMN_CREATED_AT_CART, System.currentTimeMillis());
      cartId = (int) db.insert(TABLE_CART, null, cartValues);
    }
    cursor.close();

    // Kiểm tra nếu sản phẩm đã tồn tại trong giỏ hàng
    String checkProductQuery = "SELECT * FROM " + TABLE_CART_ITEM + " WHERE " + COLUMN_CART_ID_ITEM + " = ? AND " + COLUMN_PRODUCT_ID_ITEM + " = ?";
    Cursor productCursor = db.rawQuery(checkProductQuery, new String[]{String.valueOf(cartId), String.valueOf(productId)});

    if (productCursor.moveToFirst()) {
      // Sản phẩm đã tồn tại, cập nhật số lượng
      int currentQuantity = productCursor.getInt(productCursor.getColumnIndex(COLUMN_QUANTITY_ITEM));
      int newQuantity = currentQuantity + quantity;

      ContentValues updateValues = new ContentValues();
      updateValues.put(COLUMN_QUANTITY_ITEM, newQuantity);

      db.update(TABLE_CART_ITEM, updateValues, COLUMN_CART_ID_ITEM + " = ? AND " + COLUMN_PRODUCT_ID_ITEM + " = ?", new String[]{String.valueOf(cartId), String.valueOf(productId)});
    } else {
      // Sản phẩm chưa tồn tại, thêm mới vào giỏ hàng
      ContentValues values = new ContentValues();
      values.put(COLUMN_CART_ID_ITEM, cartId);
      values.put(COLUMN_PRODUCT_ID_ITEM, productId);
      values.put(COLUMN_QUANTITY_ITEM, quantity);

      db.insert(TABLE_CART_ITEM, null, values);
    }
    productCursor.close();
    db.close();
  }

  public void updateCartItem(CartItem cartItem) {
    SQLiteDatabase db = this.getWritableDatabase();

    ContentValues values = new ContentValues();
    values.put(COLUMN_QUANTITY_ITEM, cartItem.getQuantity());

    db.update(TABLE_CART_ITEM, values, COLUMN_CART_ITEM_ID + " = ?", new String[]{String.valueOf(cartItem.getCartItemId())});
    db.close();
  }

  public ArrayList<CartItem> getAllCartItems() {
    ArrayList<CartItem> cartItemList = new ArrayList<>();
    SQLiteDatabase db = this.getReadableDatabase();

    // Truy vấn để lấy thông tin sản phẩm từ giỏ hàng
    String query = "SELECT " + "ci." + COLUMN_CART_ITEM_ID + ", " + "ci." + COLUMN_PRODUCT_ID_ITEM + ", " + "ci." + COLUMN_QUANTITY_ITEM + ", " + "p." + COLUMN_NAME_PRODUCT + ", " + "p." + COLUMN_IMAGE_PRODUCT + ", " + "p." + COLUMN_PRICE_PRODUCT + " FROM " + TABLE_CART_ITEM + " ci " + " JOIN " + TABLE_PRODUCT + " p ON ci." + COLUMN_PRODUCT_ID_ITEM + " = p." + COLUMN_CATEGORY_ID;

    Cursor cursor = db.rawQuery(query, null);

    if (cursor.moveToFirst()) {
      do {
        int cartItemId = cursor.getInt(cursor.getColumnIndex(COLUMN_CART_ITEM_ID));
        int productId = cursor.getInt(cursor.getColumnIndex(COLUMN_PRODUCT_ID_ITEM));
        int quantity = cursor.getInt(cursor.getColumnIndex(COLUMN_QUANTITY_ITEM));
        String productName = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_PRODUCT));
        byte[] productImage = cursor.getBlob(cursor.getColumnIndex(COLUMN_IMAGE_PRODUCT));
        double productPrice = cursor.getDouble(cursor.getColumnIndex(COLUMN_PRICE_PRODUCT));

        CartItem cartItem = new CartItem(cartItemId, productName, productImage, productPrice, quantity);
        cartItemList.add(cartItem);
      } while (cursor.moveToNext());
    }

    cursor.close();
    db.close();
    return cartItemList;
  }


  public void updateCartItemQuantity(int cartItemId, int quantity) {
    SQLiteDatabase db = this.getWritableDatabase();
    ContentValues contentValues = new ContentValues();
    contentValues.put(COLUMN_QUANTITY_ITEM, quantity);

    db.update(TABLE_CART_ITEM, contentValues, COLUMN_CART_ITEM_ID + " = ?", new String[]{String.valueOf(cartItemId)});
    db.close();
  }

  public List<CartItem> getCartItemsByUserId(int userId) {
    SQLiteDatabase db = this.getReadableDatabase();
    List<CartItem> cartItemList = new ArrayList<>();

    String query = "SELECT ci." + COLUMN_CART_ITEM_ID + ", p." + COLUMN_NAME_PRODUCT + ", p." + COLUMN_IMAGE_PRODUCT + ", p." + COLUMN_PRICE_PRODUCT + ", ci." + COLUMN_QUANTITY_ITEM + " FROM " + TABLE_CART_ITEM + " ci " + "JOIN " + TABLE_PRODUCT + " p ON ci." + COLUMN_PRODUCT_ID_ITEM + " = p." + COLUMN_ID_PRODUCT + " JOIN " + TABLE_CART + " c ON ci." + COLUMN_CART_ID_ITEM + " = c." + COLUMN_CART_ID + " WHERE c." + COLUMN_USER_ID_CART + " = ?";

    Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});
    if (cursor != null) {
      while (cursor.moveToNext()) {
        int cartItemId = cursor.getInt(cursor.getColumnIndex(COLUMN_CART_ITEM_ID));
        String productName = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_PRODUCT));
        byte[] productImage = cursor.getBlob(cursor.getColumnIndex(COLUMN_IMAGE_PRODUCT));
        double productPrice = cursor.getDouble(cursor.getColumnIndex(COLUMN_PRICE_PRODUCT));
        int quantity = cursor.getInt(cursor.getColumnIndex(COLUMN_QUANTITY_ITEM));

        CartItem cartItem = new CartItem(cartItemId, productName, productImage, productPrice, quantity);
        cartItemList.add(cartItem);
      }
      cursor.close();
    }

    return cartItemList;
  }

  public void deleteCartItem(int cartItemId) {
    SQLiteDatabase db = this.getWritableDatabase();
    db.delete(TABLE_CART_ITEM, COLUMN_CART_ITEM_ID + " = ?", new String[]{String.valueOf(cartItemId)});
    db.close();
  }
  // Hàm tạo đơn hàng
  public long createOrder(int userId, String address, double total) {
    SQLiteDatabase db = this.getWritableDatabase();
    ContentValues values = new ContentValues();
    values.put(COLUMN_USER_ID_ORDER, userId);
    values.put(COLUMN_STATUS_ORDER, OrderStatus.PLACED.toString()); // Trạng thái đơn hàng là "Đơn hàng đã đặt"
    values.put(COLUMN_ADDRESS_ORDER, address); // Include address
    values.put(COLUMN_TOTAL_ORDER, total);


    // Chèn dòng dữ liệu vào bảng order
    long orderId = db.insert(TABLE_ORDER, null, values);
    db.close();
    return orderId;
  }

  // Hàm tạo mục đơn hàng
  public void createOrderItem(long orderId, int productId, int quantity) {
    SQLiteDatabase db = this.getWritableDatabase();
    ContentValues values = new ContentValues();
    values.put(COLUMN_ORDER_ID_ITEM, orderId);
    values.put(COLUMN_PRODUCT_ID_ORDER_ITEM, productId);
    values.put(COLUMN_QUANTITY_ORDER_ITEM, quantity);

    // Chèn dòng dữ liệu vào bảng order_item
    db.insert(TABLE_ORDER_ITEM, null, values);
    db.close();
  }

  public int getProductIdByName(String productName) {
    SQLiteDatabase db = this.getReadableDatabase();
    String query = "SELECT " + COLUMN_ID_PRODUCT + " FROM " + TABLE_PRODUCT + " WHERE " + COLUMN_NAME_PRODUCT + " = ?";
    Cursor cursor = db.rawQuery(query, new String[]{productName});

    int productId = -1;
    if (cursor.moveToFirst()) {
      productId = cursor.getInt(cursor.getColumnIndex(COLUMN_ID_PRODUCT));
    }
    cursor.close();
    db.close();

    return productId;
  }

//  public List<Order> getAllOrders() {
//    List<Order> orderList = new ArrayList<>();
//    SQLiteDatabase db = this.getReadableDatabase();
//
//    // Add the status column to your query and sort by orderId in descending order
//    Cursor cursor = db.query(TABLE_ORDER, new String[]{COLUMN_ORDER_ID, COLUMN_USER_ID_ORDER, COLUMN_CREATED_AT_ORDER, COLUMN_STATUS_ORDER // Make sure this column exists
//    }, null, null, null, null, COLUMN_ORDER_ID + " DESC"); // Add the sorting order here
//
//    if (cursor.moveToFirst()) {
//      do {
//        int orderId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ORDER_ID));
//        int userIdFromCursor = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_ID_ORDER));
//        String createdAt = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CREATED_AT_ORDER));
//
//        // Read status from cursor and convert it to OrderStatus
//        String statusString = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STATUS_ORDER));
//        OrderStatus status = OrderStatus.valueOf(statusString);
//        double total = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_TOTAL_ORDER));
//
//        Order order = new Order(orderId, userIdFromCursor, createdAt, status, total);
//        orderList.add(order);
//      } while (cursor.moveToNext());
//    }
//    cursor.close();
//    db.close();
//    return orderList;
//  }
public List<Order> getAllOrders() {
  List<Order> orderList = new ArrayList<>();
  SQLiteDatabase db = this.getReadableDatabase();

  // Define the columns you want to retrieve from the database
  String[] columns = {
    COLUMN_ORDER_ID,
    COLUMN_USER_ID_ORDER,
    COLUMN_CREATED_AT_ORDER,
    COLUMN_STATUS_ORDER,
    COLUMN_TOTAL_ORDER
  };

  // Execute the query, sorting by created_at in descending order
  Cursor cursor = db.query(
    TABLE_ORDER,
    columns,
    null,
    null,
    null,
    null,
    COLUMN_CREATED_AT_ORDER + " DESC" // Sorting by creation date in descending order
  );

  // Check if the cursor has data and loop through each row
  if (cursor.moveToFirst()) {
    do {
      int orderId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ORDER_ID));
      int userIdFromCursor = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_ID_ORDER));
      String createdAt = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CREATED_AT_ORDER));

      // Read the status from the cursor and convert it to OrderStatus enum
      String statusString = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STATUS_ORDER));
      OrderStatus status = OrderStatus.valueOf(statusString);

      // Get the total amount for the order
      double total = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_TOTAL_ORDER));

      // Create an Order object and add it to the list
      Order order = new Order(orderId, userIdFromCursor, createdAt, status, total);
      orderList.add(order);
    } while (cursor.moveToNext());
  }

  // Close the cursor and the database to free resources
  cursor.close();
  db.close();

  // Return the list of orders
  return orderList;
}

  // danh sach cac don hang theo user va chi o trang thai dat hang,xac nhan don hang va cho van chuyen
  public List<Order> getOrderByUserId(int userId) {
    List<Order> orderList = new ArrayList<>();
    SQLiteDatabase db = this.getReadableDatabase();

    // Define the selection criteria and arguments
    String selection = COLUMN_USER_ID_ORDER + " = ? AND " + COLUMN_STATUS_ORDER + " IN (?, ?, ?)";
    String[] selectionArgs = {String.valueOf(userId), OrderStatus.PLACED.name(), OrderStatus.CONFIRMED.name(), OrderStatus.SHIPPED.name()};

// Query the database with the specified criteria
    Cursor cursor = db.query(
      TABLE_ORDER,
      new String[]{COLUMN_ORDER_ID, COLUMN_USER_ID_ORDER, COLUMN_CREATED_AT_ORDER, COLUMN_STATUS_ORDER, COLUMN_TOTAL_ORDER},
      selection,
      selectionArgs,
      null,
      null,
      COLUMN_ORDER_ID + " DESC" // Sort by orderId in descending order
    );
    // Query the database with the specified criteria
    if (cursor.moveToFirst()) {
      do {
        int orderId = cursor.getInt(cursor.getColumnIndex(COLUMN_ORDER_ID));
        int userIdFromCursor = cursor.getInt(cursor.getColumnIndex(COLUMN_USER_ID_ORDER));
        String createdAt = cursor.getString(cursor.getColumnIndex(COLUMN_CREATED_AT_ORDER));

        // Read status from cursor and convert it to OrderStatus
        String statusString = cursor.getString(cursor.getColumnIndex(COLUMN_STATUS_ORDER));
        OrderStatus status = OrderStatus.valueOf(statusString);
        Double total = cursor.getDouble(cursor.getColumnIndex(COLUMN_TOTAL_ORDER));

        Order order = new Order(orderId, userIdFromCursor, createdAt, status,total);
        orderList.add(order);
      } while (cursor.moveToNext());
    }
    cursor.close();
    db.close();
    return orderList;
  }

  public List<Order> getOrderByUserIdStatusDeli(int userId) {
    List<Order> orderList = new ArrayList<>();
    SQLiteDatabase db = this.getReadableDatabase();

    // Define the selection criteria and arguments
    String selection = COLUMN_USER_ID_ORDER + " = ? AND " + COLUMN_STATUS_ORDER + " IN (?, ?, ?)";
    String[] selectionArgs = {String.valueOf(userId), OrderStatus.PLACED.name(), OrderStatus.CONFIRMED.name(), OrderStatus.SHIPPED.name()};

    // Query the database with the specified criteria
    Cursor cursor = db.query(TABLE_ORDER,
      new String[]{COLUMN_ORDER_ID, COLUMN_USER_ID_ORDER, COLUMN_CREATED_AT_ORDER, COLUMN_STATUS_ORDER, COLUMN_TOTAL_ORDER}, // Include COLUMN_TOTAL_ORDER
      selection,
      selectionArgs,
      null,
      null,
      COLUMN_ORDER_ID + " DESC");


    if (cursor.moveToFirst()) {
      do {
        int orderId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ORDER_ID));
        int userIdFromCursor = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_ID_ORDER));
        String createdAt = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CREATED_AT_ORDER));

        // Read status from cursor and convert it to OrderStatus
        String statusString = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STATUS_ORDER));
        OrderStatus status = OrderStatus.valueOf(statusString);
        double total = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_TOTAL_ORDER));

        Order order = new Order(orderId, userIdFromCursor, createdAt, status, total);
        orderList.add(order);
      } while (cursor.moveToNext());
    }
    cursor.close();
    db.close();
    return orderList;
  }

  public void updateOrderStatus(int orderId, OrderStatus newStatus) {
    SQLiteDatabase db = this.getWritableDatabase();
    ContentValues values = new ContentValues();
    values.put(COLUMN_STATUS_ORDER, newStatus.name());

    db.update(TABLE_ORDER, values, COLUMN_ORDER_ID + " = ?", new String[]{String.valueOf(orderId)});
    db.close();
  }

  public boolean isOrderStatusUpdated(int orderId, OrderStatus expectedStatus) {
    SQLiteDatabase db = this.getReadableDatabase();
    String query = "SELECT " + COLUMN_STATUS_ORDER + " FROM " + TABLE_ORDER + " WHERE " + COLUMN_ORDER_ID + " = ?";
    Cursor cursor = null;
    boolean isUpdated = false;

    try {
      cursor = db.rawQuery(query, new String[]{String.valueOf(orderId)});
      if (cursor != null && cursor.moveToFirst()) {
        String status = cursor.getString(cursor.getColumnIndex(COLUMN_STATUS_ORDER));
        isUpdated = expectedStatus.name().equals(status);
      }
    } catch (Exception e) {
      Log.e("Database Error", "Error checking order status", e);
    } finally {
      if (cursor != null) {
        cursor.close();
      }
    }

    return isUpdated;
  }


  public void clearCart(int userId) {
    SQLiteDatabase db = this.getWritableDatabase();

    // Xóa tất cả các mục trong giỏ hàng của người dùng
    db.delete(TABLE_CART_ITEM, COLUMN_CART_ID_ITEM + " IN (SELECT " + COLUMN_CART_ID + " FROM " + TABLE_CART + " WHERE " + COLUMN_USER_ID_CART + " = ?)", new String[]{String.valueOf(userId)});

    // Xóa giỏ hàng của người dùng
    db.delete(TABLE_CART, COLUMN_USER_ID_CART + " = ?", new String[]{String.valueOf(userId)});

    db.close();
  }

  public List<OrderDetail> getProductsByOrderId(int orderId) {
    List<OrderDetail> products = new ArrayList<>();
    SQLiteDatabase db = this.getReadableDatabase();
    String query = "SELECT p." + COLUMN_NAME_PRODUCT + " AS productName, " + "p." + COLUMN_PRICE_PRODUCT + " AS productPrice, " + "oi." + COLUMN_QUANTITY_ITEM + " AS productQuantity, " + "p." + COLUMN_IMAGE_PRODUCT + " AS productImage " + "FROM " + TABLE_ORDER_ITEM + " oi " + "JOIN " + TABLE_PRODUCT + " p ON oi." + COLUMN_PRODUCT_ID_ITEM + " = p." + COLUMN_ID_PRODUCT + " " + "WHERE oi." + COLUMN_ORDER_ID_ITEM + " = ?";

    Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(orderId)});
    if (cursor.moveToFirst()) {
      do {
        String productName = cursor.getString(cursor.getColumnIndex("productName"));
        double productPrice = cursor.getDouble(cursor.getColumnIndex("productPrice"));
        int productQuantity = cursor.getInt(cursor.getColumnIndex("productQuantity"));
        byte[] productImage = cursor.getBlob(cursor.getColumnIndex("productImage"));

        OrderDetail product = new OrderDetail();
        product.setName(productName);
        product.setPrice(productPrice);
        product.setImage(productImage);
        product.setQuantity(productQuantity); // Ensure Product class has this method

        products.add(product);
      } while (cursor.moveToNext());
    }
    cursor.close();
    db.close();
    return products;
  }


  // Lấy ngày hiện tại dd-MM-yyyy
  private String getCurrentDate() {
    Date today = new Date();
    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
    return formatter.format(today);
  }


  public void getAllOrderItems() {
    SQLiteDatabase db = this.getReadableDatabase();
    String query = "SELECT * FROM " + TABLE_ORDER_ITEM;

    Cursor cursor = db.rawQuery(query, null);
    if (cursor.moveToFirst()) {
      do {
        int orderId = cursor.getInt(cursor.getColumnIndex(COLUMN_ORDER_ID_ITEM));
        int productId = cursor.getInt(cursor.getColumnIndex(COLUMN_PRODUCT_ID_ORDER_ITEM));
        int quantity = cursor.getInt(cursor.getColumnIndex(COLUMN_QUANTITY_ORDER_ITEM));

        // Process each order item as needed
        System.out.println("Order ID: " + orderId + ", Product ID: " + productId + ", Quantity: " + quantity);

      } while (cursor.moveToNext());
    }
    cursor.close();
    db.close();
  }


  public List<Product> getProductInOrderItemByOrderId(int orderId) {
    List<Product> productList = new ArrayList<>();
    SQLiteDatabase db = this.getReadableDatabase();

    // Query to get the product details for a specific orderId
    String query = "SELECT p." + COLUMN_ID_PRODUCT + ", p." + COLUMN_NAME_PRODUCT + ", p." + COLUMN_PRICE_PRODUCT + ", p." + COLUMN_IMAGE_PRODUCT + ", p." + COLUMN_CATEGORY_ID + ", p." + COLUMN_CREATED_PRODUCT + ", p." + COLUMN_USER_CREATED_PRODUCT + " FROM " + TABLE_ORDER_ITEM + " oi" + " JOIN " + TABLE_PRODUCT + " p ON oi." + COLUMN_PRODUCT_ID_ORDER_ITEM + " = p." + COLUMN_ID_PRODUCT + " WHERE oi." + COLUMN_ORDER_ID_ITEM + " = ?";

    Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(orderId)});
    if (cursor.moveToFirst()) {
      do {
        int productId = cursor.getInt(cursor.getColumnIndex(COLUMN_ID_PRODUCT));
        String productName = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_PRODUCT));
        double productPrice = cursor.getDouble(cursor.getColumnIndex(COLUMN_PRICE_PRODUCT));
        byte[] productImage = cursor.getBlob(cursor.getColumnIndex(COLUMN_IMAGE_PRODUCT));
        int productCategoryId = cursor.getInt(cursor.getColumnIndex(COLUMN_CATEGORY_ID));
        String productCreatedAt = cursor.getString(cursor.getColumnIndex(COLUMN_CREATED_PRODUCT));
        String productUserCreatedAt = cursor.getString(cursor.getColumnIndex(COLUMN_USER_CREATED_PRODUCT));

        Product product = new Product(productId, productName, productPrice, productCategoryId, productCreatedAt, productUserCreatedAt, productImage);
        productList.add(product);
      } while (cursor.moveToNext());
    }
    cursor.close();
    db.close();
    return productList;
  }

//  public List<Review> getReviewsByOrderId(int orderId) {
//    List<Review> reviews = new ArrayList<>();
//    SQLiteDatabase db = this.getReadableDatabase();
//    String query = "SELECT * FROM " + TABLE_REVIEW + " WHERE " + COLUMN_ORDER_ID_REVIEW + " = ?";
//    Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(orderId)});
//
//    if (cursor.moveToFirst()) {
//      do {
//        int reviewId = cursor.getInt(cursor.getColumnIndex(COLUMN_REVIEW_ID));
//        int productId = cursor.getInt(cursor.getColumnIndex(COLUMN_PRODUCT_ID_REVIEW));
//        float rating = cursor.getFloat(cursor.getColumnIndex(COLUMN_RATING));
//        String reviewText = cursor.getString(cursor.getColumnIndex(COLUMN_REVIEW_TEXT));
//        String createdAt = cursor.getString(cursor.getColumnIndex(COLUMN_CREATED_AT_REVIEW));
//
//        reviews.add(new Review(reviewId, orderId, productId, rating, reviewText, createdAt));
//      } while (cursor.moveToNext());
//    }
//
//    cursor.close();
//    db.close();
//    return reviews;
//  }


  public boolean addReview(int orderId, int productId, float rating, String reviewText, String reviewCreatedBy) {
    SQLiteDatabase db = this.getWritableDatabase();
    ContentValues values = new ContentValues();
    values.put(COLUMN_ORDER_ID_REVIEW, orderId);
    values.put(COLUMN_PRODUCT_ID_REVIEW, productId);
    values.put(COLUMN_RATING, rating);
    values.put(COLUMN_CREATED_BY_REVIEW, reviewCreatedBy);
    values.put(COLUMN_REVIEW_TEXT, reviewText);

    long result = db.insert(TABLE_REVIEW, null, values);
    db.close();

    if (result == -1) {
      Log.e("DatabaseError", "Failed to insert review. Order ID: " + orderId + ", Product ID: " + productId);
      return false;
    } else {
      Log.d("DatabaseSuccess", "Review inserted successfully. Order ID: " + orderId + ", Product ID: " + productId);
      return true;
    }
  }


  public boolean updateReview(int orderId, int productId, float rating, String reviewText) {
    SQLiteDatabase db = this.getWritableDatabase();
    ContentValues values = new ContentValues();
    values.put(COLUMN_RATING, rating);
    values.put(COLUMN_REVIEW_TEXT, reviewText);

    // Tạo điều kiện để xác định dòng cần cập nhật
    String whereClause = COLUMN_ORDER_ID_REVIEW + " = ? AND " + COLUMN_PRODUCT_ID_REVIEW + " = ?";
    String[] whereArgs = {String.valueOf(orderId), String.valueOf(productId)};

    // Thực hiện cập nhật
    int rowsAffected = db.update(TABLE_REVIEW, values, whereClause, whereArgs);
    db.close();

    if (rowsAffected > 0) {
      Log.d("DatabaseSuccess", "Review updated successfully. Order ID: " + orderId + ", Product ID: " + productId);
      return true;
    } else {
      Log.e("DatabaseError", "Failed to update review. Order ID: " + orderId + ", Product ID: " + productId);
      return false;
    }
  }

  public List<Review> getAllReviewsSortedByDate() {
    List<Review> reviewList = new ArrayList<>();
    SQLiteDatabase db = this.getReadableDatabase();

    String selectQuery = "SELECT * FROM " + TABLE_REVIEW + " ORDER BY " + COLUMN_CREATED_AT_REVIEW + " DESC";
    Cursor cursor = db.rawQuery(selectQuery, null);

    if (cursor.moveToFirst()) {
      do {
        int id = cursor.getInt(cursor.getColumnIndex(COLUMN_REVIEW_ID));
        int orderId = cursor.getInt(cursor.getColumnIndex(COLUMN_ORDER_ID_REVIEW));
        int productId = cursor.getInt(cursor.getColumnIndex(COLUMN_PRODUCT_ID_REVIEW));
        float rating = cursor.getFloat(cursor.getColumnIndex(COLUMN_RATING));
        String reviewText = cursor.getString(cursor.getColumnIndex(COLUMN_REVIEW_TEXT));
        String reviewBy = cursor.getString(cursor.getColumnIndex(COLUMN_CREATED_BY_REVIEW));
        String reviewDate = cursor.getString(cursor.getColumnIndex(COLUMN_CREATED_AT_REVIEW));

        Review review = new Review(id, orderId, productId, rating, reviewText, reviewDate, reviewBy);
        reviewList.add(review);
      } while (cursor.moveToNext());
    }

    cursor.close();
    db.close();

    return reviewList;
  }

  public List<Product> getTop5ProductsSold() {
    List<Product> productList = new ArrayList<>();
    SQLiteDatabase db = this.getReadableDatabase();

    // Câu truy vấn SQL để lấy danh sách top 5 sản phẩm bán chạy nhất với trạng thái DELIVERED
    String query = "SELECT p." + COLUMN_ID_PRODUCT + ", p." + COLUMN_NAME_PRODUCT + ", p." + COLUMN_PRICE_PRODUCT + ", p." + COLUMN_IMAGE_PRODUCT + ", p." + COLUMN_CATEGORY_ID + ", p." + COLUMN_CREATED_PRODUCT + ", p." + COLUMN_USER_CREATED_PRODUCT + ", SUM(oi." + COLUMN_QUANTITY_ORDER_ITEM + ") AS total_sold " + "FROM " + TABLE_ORDER_ITEM + " oi " + "JOIN " + TABLE_ORDER + " o ON oi." + COLUMN_ORDER_ID_ITEM + " = o." + COLUMN_ORDER_ID + " " + "JOIN " + TABLE_PRODUCT + " p ON oi." + COLUMN_PRODUCT_ID_ORDER_ITEM + " = p." + COLUMN_ID_PRODUCT + " " + "WHERE o." + COLUMN_STATUS_ORDER + " = 'DELIVERED' " + "GROUP BY p." + COLUMN_ID_PRODUCT + " " + "ORDER BY total_sold DESC " + "LIMIT 5";

    Cursor cursor = null;
    try {
      cursor = db.rawQuery(query, null);

      if (cursor.moveToFirst()) {
        do {
          int productId = cursor.getInt(cursor.getColumnIndex(COLUMN_ID_PRODUCT));
          String productName = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_PRODUCT));
          double productPrice = cursor.getDouble(cursor.getColumnIndex(COLUMN_PRICE_PRODUCT));
          byte[] productImage = cursor.getBlob(cursor.getColumnIndex(COLUMN_IMAGE_PRODUCT));
          int productCategoryId = cursor.getInt(cursor.getColumnIndex(COLUMN_CATEGORY_ID));
          String productCreatedAt = cursor.getString(cursor.getColumnIndex(COLUMN_CREATED_PRODUCT));
          String productUserCreatedAt = cursor.getString(cursor.getColumnIndex(COLUMN_USER_CREATED_PRODUCT));
          int totalSold = cursor.getInt(cursor.getColumnIndex("total_sold"));

          // Log thông tin sản phẩm để kiểm tra kết quả
          Log.d("Top5ProductsSold", "Product ID: " + productId + ", Name: " + productName + ", Price: " + productPrice + ", Category ID: " + productCategoryId + ", Created At: " + productCreatedAt + ", User Created At: " + productUserCreatedAt + ", Total Sold: " + totalSold);

          Product product = new Product(productId, productName, productPrice, productCategoryId, productCreatedAt, productUserCreatedAt, productImage);
          productList.add(product);
        } while (cursor.moveToNext());
      } else {
        Log.d("Top5ProductsSold", "No products found.");
      }
    } catch (Exception e) {
      Log.e("Top5ProductsSold", "Error retrieving top 5 products sold", e);
    } finally {
      if (cursor != null && !cursor.isClosed()) {
        cursor.close();
      }
      db.close();
    }
    return productList;
  }

  public boolean addContent(String title, String content, byte[] image) {
    SQLiteDatabase db = this.getWritableDatabase();
    ContentValues values = new ContentValues();
    values.put("title", title);
    values.put("content", content);
    values.put("image", image);
    values.put("createdAt", getCurrentDate());
    values.put("click", 0);

    long result = db.insert("table_content", null, values);
    db.close();

    return result != -1;
  }

  public boolean updateContent(int id, String title, String content, byte[] image) {
    SQLiteDatabase db = this.getWritableDatabase();
    ContentValues contentValues = new ContentValues();
    contentValues.put("title", title);
    contentValues.put("content", content);
    contentValues.put("image", image);

    int result = db.update("table_content", contentValues, "id = ?", new String[]{String.valueOf(id)});
    return result > 0;
  }


  public boolean deleteContent(int id) {
    SQLiteDatabase db = this.getWritableDatabase();

    // Deleting row
    int result = db.delete(TABLE_CONTENT, COLUMN_CONTENT_ID + " = ?", new String[]{String.valueOf(id)});
    db.close();

    if (result == 0) {
      Log.e("DatabaseError", "Failed to delete content. ID: " + id);
      return false;
    } else {
      Log.d("DatabaseSuccess", "Content deleted successfully. ID: " + id);
      return true;
    }
  }

  public List<Content> getAllContents() {
    List<Content> contentList = new ArrayList<>();
    SQLiteDatabase db = this.getReadableDatabase();

    // Select All Query
    String selectQuery = "SELECT * FROM " + TABLE_CONTENT;
    Cursor cursor = db.rawQuery(selectQuery, null);

    // Looping through all rows and adding to list
    if (cursor.moveToFirst()) {
      do {
        int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CONTENT_ID));
        byte[] image = cursor.getBlob(cursor.getColumnIndexOrThrow(COLUMN_IMAGE_CONTENT));
        String content = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TEXT_CONTENT));
        String title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE_CONTENT));
        String createdAt = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CREATED_AT_CONTENT));
        int click = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CLICK_CONTENT));

        Content contentItem = new Content(image, content, title, createdAt, click);
        contentItem.setId(id); // Assuming the Content class has a setId method
        contentList.add(contentItem);
      } while (cursor.moveToNext());
    }

    cursor.close();
    db.close();
    return contentList;
  }

  public Content getContentById(int id) {
    SQLiteDatabase db = this.getReadableDatabase();

    // Query to get content by ID
    String selectQuery = "SELECT * FROM " + TABLE_CONTENT + " WHERE " + COLUMN_CONTENT_ID + " = ?";
    Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(id)});

    if (cursor != null) {
      cursor.moveToFirst();

      byte[] image = cursor.getBlob(cursor.getColumnIndexOrThrow(COLUMN_IMAGE_CONTENT));
      String content = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TEXT_CONTENT));
      String title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE_CONTENT));
      String createdAt = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CREATED_AT_CONTENT));
      int click = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CLICK_CONTENT));

      Content contentItem = new Content(image, content, title, createdAt, click);
      contentItem.setId(id); // Assuming the Content class has a setId method

      cursor.close();
      db.close();
      return contentItem;
    } else {
      db.close();
      return null; // Return null if the content with the given ID is not found
    }
  }

  public List<Content> searchContents(String query) {
    List<Content> contentList = new ArrayList<>();
    SQLiteDatabase db = this.getReadableDatabase();

    // Sử dụng câu lệnh SELECT với LIKE để tìm kiếm theo tiêu đề hoặc nội dung
    String sql = "SELECT * FROM " + TABLE_CONTENT + " WHERE " + COLUMN_TITLE_CONTENT + " LIKE ? OR " + COLUMN_TEXT_CONTENT + " LIKE ?";
    String[] selectionArgs = new String[]{"%" + query + "%", "%" + query + "%"};

    Cursor cursor = db.rawQuery(sql, selectionArgs);

    if (cursor.moveToFirst()) {
      do {
        // Lấy dữ liệu từ cursor và thêm vào danh sách
        Content content = new Content();
        content.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CONTENT_ID)));
        content.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE_CONTENT)));
        content.setContent(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TEXT_CONTENT)));
        content.setCreatedAt(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CREATED_AT_CONTENT)));
        content.setClick(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CLICK_CONTENT)));
        content.setImage(cursor.getBlob(cursor.getColumnIndexOrThrow(COLUMN_IMAGE_CONTENT)));

        contentList.add(content);
      } while (cursor.moveToNext());
    }

    cursor.close();
    db.close();

    return contentList;
  }

  public void incrementClickCount(int contentId) {
    SQLiteDatabase db = this.getWritableDatabase();

    // Truy vấn số lượt click hiện tại từ cơ sở dữ liệu
    Cursor cursor = db.query("table_content", new String[]{"click"}, "id = ?", new String[]{String.valueOf(contentId)}, null, null, null);

    int currentClickCount = 0;
    if (cursor.moveToFirst()) {
      currentClickCount = cursor.getInt(cursor.getColumnIndexOrThrow("click"));
    }
    cursor.close();

    // Tăng số lượt click lên 1
    ContentValues values = new ContentValues();
    values.put("click", currentClickCount + 1);

    // Cập nhật vào cơ sở dữ liệu
    db.update("table_content", values, "id = ?", new String[]{String.valueOf(contentId)});
    db.close();
  }

  public Cursor getOrderCountByStatus() {
    // Câu lệnh SQL để đếm số lượng đơn hàng theo trạng thái
    String query = "SELECT " + COLUMN_STATUS_ORDER + ", COUNT(" + COLUMN_ORDER_ID + ") AS order_count " +
      "FROM " + TABLE_ORDER + " " +
      "GROUP BY " + COLUMN_STATUS_ORDER;

    SQLiteDatabase db = this.getReadableDatabase();

    Cursor cursor = null;
    if (db != null) {
      cursor = db.rawQuery(query, null);
    }

    // Kiểm tra nếu cursor không null và ghi log số lượng bản ghi
    if (cursor != null) {
      Log.d("Order count by status", "count = " + cursor.getCount());
    } else {
      Log.d("Order count by status", "No data found");
    }

    return cursor;
  }


  public Cursor getRevenueByDate() {
    // Câu lệnh SQL để tính doanh thu theo ngày cho trạng thái 'DELIVERED'
    String query = "SELECT strftime('%Y-%m-%d', date(substr(" + COLUMN_CREATED_AT_ORDER + ", 7, 4) || '-' || substr(" + COLUMN_CREATED_AT_ORDER + ", 4, 2) || '-' || substr(" + COLUMN_CREATED_AT_ORDER + ", 1, 2))) AS date, " +
      "SUM(total_amount) AS total_revenue " +
      "FROM " + TABLE_ORDER + " " +
      "WHERE " + COLUMN_STATUS_ORDER + " = 'DELIVERED' " +
      "GROUP BY date " +
      "ORDER BY date";

    SQLiteDatabase db = this.getReadableDatabase();
    Cursor cursor = null;

    try {
      if (db != null) {
        cursor = db.rawQuery(query, null);
      }
    } catch (Exception e) {
      Log.e("DatabaseError", "Error executing query: " + e.getMessage());
    }

    if (cursor != null) {
      Log.d("Revenue by Date", "count = " + cursor.getCount());
    } else {
      Log.d("Revenue by Date", "No data found");
    }

    return cursor;
  }

  public Cursor getRevenueByMonth() {
    // Câu lệnh SQL để tính doanh thu theo tháng cho trạng thái 'DELIVERED'
    String query = "SELECT strftime('%Y-%m', date(substr(" + COLUMN_CREATED_AT_ORDER + ", 7, 4) || '-' || substr(" + COLUMN_CREATED_AT_ORDER + ", 4, 2) || '-' || substr(" + COLUMN_CREATED_AT_ORDER + ", 1, 2))) AS month, " +
      "SUM(total_amount) AS total_revenue " +
      "FROM " + TABLE_ORDER + " " +
      "WHERE " + COLUMN_STATUS_ORDER + " = 'DELIVERED' " +
      "GROUP BY month " +
      "ORDER BY month";

    SQLiteDatabase db = this.getReadableDatabase();
    Cursor cursor = null;

    try {
      if (db != null) {
        cursor = db.rawQuery(query, null);
      }
    } catch (Exception e) {
      Log.e("DatabaseError", "Error executing query: " + e.getMessage());
    }

    if (cursor != null) {
      Log.d("Revenue by Month", "count = " + cursor.getCount());
    } else {
      Log.d("Revenue by Month", "No data found");
    }

    return cursor;
  }

  public Cursor getRevenueByYear() {
    // Câu lệnh SQL để tính doanh thu theo năm cho trạng thái 'DELIVERED'
    String query = "SELECT strftime('%Y', date(substr(" + COLUMN_CREATED_AT_ORDER + ", 7, 4) || '-' || substr(" + COLUMN_CREATED_AT_ORDER + ", 4, 2) || '-' || substr(" + COLUMN_CREATED_AT_ORDER + ", 1, 2))) AS year, " +
      "SUM(total_amount) AS total_revenue " +
      "FROM " + TABLE_ORDER + " " +
      "WHERE " + COLUMN_STATUS_ORDER + " = 'DELIVERED' " +
      "GROUP BY year " +
      "ORDER BY year";

    SQLiteDatabase db = this.getReadableDatabase();
    Cursor cursor = null;

    try {
      if (db != null) {
        cursor = db.rawQuery(query, null);
      }
    } catch (Exception e) {
      Log.e("DatabaseError", "Error executing query: " + e.getMessage());
    }

    if (cursor != null) {
      Log.d("Revenue by Year", "count = " + cursor.getCount());
    } else {
      Log.d("Revenue by Year", "No data found");
    }

    return cursor;
  }

}
