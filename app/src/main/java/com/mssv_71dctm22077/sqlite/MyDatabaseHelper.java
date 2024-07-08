package com.mssv_71dctm22077.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MyDatabaseHelper extends SQLiteOpenHelper {

  private Context context;
  private static final String DATABASE_NAME = "ManagementCosplay.db";
  private static int DATABASE_VERSION = 1;

  private static final String TABLE_DANHMUC = "table_danhmuc";
  private static final String COLUMN_ID_DANHMUC = "id";
  private static final String COLUMN_NAME_DANHMUC = "ten_danh_muc";
  private static final String COLUMN_CREATED_DANHMUC = "ngay_khoi_tao";

  public MyDatabaseHelper(@Nullable Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
    this.context = context;
  }


  @Override
  public void onCreate(SQLiteDatabase db) {
    String query = "CREATE TABLE " + TABLE_DANHMUC + "(" +
      COLUMN_ID_DANHMUC + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
      COLUMN_NAME_DANHMUC + " TEXT, " +
      COLUMN_CREATED_DANHMUC + " TEXT" +
      ")";db.execSQL(query);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_DANHMUC);
    onCreate(db);
  }

  public Cursor getAllDanhMuc(){
    String query ="SELECT * FROM "+TABLE_DANHMUC;
    SQLiteDatabase db = this.getReadableDatabase();

    Cursor cursor = null;
    if (db != null){
      cursor = db.rawQuery(query,null);
    }
    return cursor;
  }

  public void addDanhMuc(String tenDanhMuc) {
    if (tenDanhMuc.length()>0) {
      SQLiteDatabase db = this.getWritableDatabase();
      ContentValues cv = new ContentValues();

      cv.put(COLUMN_NAME_DANHMUC, tenDanhMuc);
      cv.put(COLUMN_CREATED_DANHMUC, getCurrentDate());

      long result = db.insert(TABLE_DANHMUC, null, cv);
      if (result == -1) {
        Toast.makeText(context, "Thất bại", Toast.LENGTH_SHORT).show();
      } else {
        Toast.makeText(context, "Thành công!", Toast.LENGTH_SHORT).show();
      }
    }else {
      Toast.makeText(context, "Tên danh mục không được rỗng", Toast.LENGTH_SHORT).show();
    }
  }

  // Lấy ngày hiện tại
  private String getCurrentDate() {
    Date today = new Date();
    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
    return formatter.format(today);
  }
}
