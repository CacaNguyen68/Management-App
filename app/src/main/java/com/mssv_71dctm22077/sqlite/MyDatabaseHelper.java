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


  private static final String TABLE_ORDER_ITEM = "table_order_item";
  private static final String COLUMN_ORDER_ITEM_ID = "order_item_id";
  private static final String COLUMN_ORDER_ID_ITEM = "order_id";
  private static final String COLUMN_PRODUCT_ID_ORDER_ITEM = "product_id";
  private static final String COLUMN_QUANTITY_ORDER_ITEM = "quantity";


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

    String queryOrder = "CREATE TABLE " + TABLE_ORDER + " (" + COLUMN_ORDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_USER_ID_ORDER + " INTEGER, " + COLUMN_CREATED_AT_ORDER + " TEXT)";
    db.execSQL(queryOrder);

    String queryOrderItem = "CREATE TABLE " + TABLE_ORDER_ITEM + " (" + COLUMN_ORDER_ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_ORDER_ID_ITEM + " INTEGER, " + COLUMN_PRODUCT_ID_ORDER_ITEM + " INTEGER, " + COLUMN_QUANTITY_ORDER_ITEM + " INTEGER, " + "FOREIGN KEY(" + COLUMN_ORDER_ID_ITEM + ") REFERENCES " + TABLE_ORDER + "(" + COLUMN_ORDER_ID + "))";
    db.execSQL(queryOrderItem);
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

  // Thêm phương thức để lấy thông tin người dùng từ số điện thoại
  public User getUserByPhone(String phone) {
    SQLiteDatabase db = this.getReadableDatabase();
    String query = "SELECT * FROM " + TABLE_USER + " WHERE " + COLUMN_PHONE_USER + "=?";
    Cursor cursor = db.rawQuery(query, new String[]{phone});

    if (cursor != null && cursor.moveToFirst()) {
      // Lấy thông tin từ cursor và tạo đối tượng User
      int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID_USER));
      String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_USER));
      String dateOfBirth = cursor.getString(cursor.getColumnIndex(COLUMN_DOB_USER));
      String email = cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL_USER));
      String userType = cursor.getString(cursor.getColumnIndex(COLUMN_USER_TYPE));
      String createdAt = cursor.getString(cursor.getColumnIndex(COLUMN_CREATED_USER));
      String createdBy = cursor.getString(cursor.getColumnIndex(COLUMN_USER_CREATED_USER));
      byte[] image = cursor.getBlob(cursor.getColumnIndex(COLUMN_IMAGE_USER));

      User user = new User(id, name, dateOfBirth, phone, email, userType, createdAt, createdBy, image);
      cursor.close();
      return user;
    }

    cursor.close();
    return null; // Nếu không tìm thấy user
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

    Cursor cursor = db.query(TABLE_USER, new String[]{COLUMN_ID_USER, COLUMN_NAME_USER, COLUMN_DOB_USER, COLUMN_PHONE_USER, COLUMN_EMAIL_USER, COLUMN_USER_TYPE, COLUMN_CREATED_USER, COLUMN_IMAGE_USER}, COLUMN_ID_USER + "=?", new String[]{String.valueOf(id)}, null, null, null, null);

    if (cursor != null) {
      if (cursor.moveToFirst()) {
        user = new User(cursor.getInt(cursor.getColumnIndex(COLUMN_ID_USER)), cursor.getString(cursor.getColumnIndex(COLUMN_NAME_USER)), cursor.getString(cursor.getColumnIndex(COLUMN_DOB_USER)), cursor.getString(cursor.getColumnIndex(COLUMN_PHONE_USER)), cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL_USER)), cursor.getString(cursor.getColumnIndex(COLUMN_USER_TYPE)), cursor.getString(cursor.getColumnIndex(COLUMN_CREATED_USER)), cursor.getString(cursor.getColumnIndex(COLUMN_USER_CREATED_USER)), cursor.getBlob(cursor.getColumnIndex(COLUMN_IMAGE_USER)));
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

  //  câu truy vấn lấy chi tiết 1 sản phẩm
  public Cursor getProductById(int id) {
    SQLiteDatabase db = this.getReadableDatabase();
    return db.rawQuery("SELECT * FROM products WHERE id = ?", new String[]{String.valueOf(id)});
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


  // Lấy ngày hiện tại dd-MM-yyyy
  private String getCurrentDate() {
    Date today = new Date();
    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
    return formatter.format(today);
  }

}
