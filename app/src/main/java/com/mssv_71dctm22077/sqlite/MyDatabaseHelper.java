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

import com.mssv_71dctm22077.model.Product;
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

  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT);
    onCreate(db);
  }

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
  public User getUser(long id) {
    SQLiteDatabase db = this.getReadableDatabase();
    User user = null;

    Cursor cursor = db.query(TABLE_USER,
      new String[]{COLUMN_ID_USER, COLUMN_NAME_USER, COLUMN_DOB_USER, COLUMN_PHONE_USER, COLUMN_EMAIL_USER, COLUMN_USER_TYPE, COLUMN_CREATED_USER, COLUMN_IMAGE_USER},
      COLUMN_ID_USER + "=?",
      new String[]{String.valueOf(id)}, null, null, null, null);

    if (cursor != null) {
      if (cursor.moveToFirst()) {
        user = new User(
          cursor.getInt(cursor.getColumnIndex(COLUMN_ID_USER)),
          cursor.getString(cursor.getColumnIndex(COLUMN_NAME_USER)),
          cursor.getString(cursor.getColumnIndex(COLUMN_DOB_USER)),
          cursor.getString(cursor.getColumnIndex(COLUMN_PHONE_USER)),
          cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL_USER)),
          cursor.getString(cursor.getColumnIndex(COLUMN_USER_TYPE)),
          cursor.getString(cursor.getColumnIndex(COLUMN_CREATED_USER)),
          cursor.getString(cursor.getColumnIndex(COLUMN_USER_CREATED_USER)),
          cursor.getBlob(cursor.getColumnIndex(COLUMN_IMAGE_USER))
        );
      }
      cursor.close();
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
    String selection = COLUMN_NAME_USER + " LIKE ? OR " + COLUMN_EMAIL_USER + " LIKE ? OR "+ COLUMN_PHONE_USER +" LIKE ?";
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
    Cursor cursor = db.query(TABLE_CATEGORY,
      new String[]{COLUMN_NAME_CATEGORY},
      COLUMN_ID_CATEGORY + "=?",
      new String[]{String.valueOf(categoryId)},
      null, null, null, null);
    if (cursor != null) {
      if (cursor.moveToFirst()) {
        categoryName = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_CATEGORY));
      }
      cursor.close();
    }

    Log.d("Danh muc:" ,categoryName);
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

//  câu truy vấn lấy chi tiết 1 sản phẩm
  public Cursor getProductById(int id) {
    SQLiteDatabase db = this.getReadableDatabase();
    return db.rawQuery("SELECT * FROM products WHERE id = ?", new String[]{String.valueOf(id)});
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
  contentValues.put("productName", name);
  contentValues.put("productPrice", price);
  contentValues.put("categoryId", categoryId);
  contentValues.put("createdAt", createdAt);
  contentValues.put("userCreated", userCreated);
  contentValues.put("productImage", image);

  db.update("products", contentValues, "id = ?", new String[]{String.valueOf(id)});
}


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

        productList.add(new Product(id, name, price, categoryId, createdAt, userCreated,image));
      } while (cursor.moveToNext());
    }
    cursor.close();
    return productList;
  }


//  câu truy vấn vẽ chart pei
public Cursor getProductCountByCategory() {
  String query = "SELECT " + COLUMN_NAME_CATEGORY + ", COUNT(" + TABLE_PRODUCT + "." + COLUMN_ID_PRODUCT + ") AS product_count " +
    "FROM " + TABLE_CATEGORY +
    " LEFT JOIN " + TABLE_PRODUCT +
    " ON " + TABLE_PRODUCT + "." + COLUMN_CATEGORY_ID + " = " + TABLE_CATEGORY + "." + COLUMN_ID_CATEGORY +
    " GROUP BY " + COLUMN_NAME_CATEGORY;
  SQLiteDatabase db = this.getReadableDatabase();

  Cursor cursor = null;
  if (db != null) {
    cursor = db.rawQuery(query, null);
  }
  Log.d("Product count by category", "count = " + cursor.getCount());
  return cursor;
}


  // Lấy ngày hiện tại dd-MM-yyyy
  private String getCurrentDate() {
    Date today = new Date();
    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
    return formatter.format(today);
  }


}
