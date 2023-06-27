package com.example.btl_mad;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    // Thông tin về cơ sở dữ liệu
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "MyDatabase.db";

    // Tên bảng và tên cột
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_ACCOUNT = "account";
    private static final String COLUMN_PASSWORD = "password";

    public MyDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tạo bảng users
        String createTableQuery = "CREATE TABLE " + TABLE_USERS + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_ACCOUNT + " TEXT," +
                COLUMN_PASSWORD + " TEXT" +
                ")";
        db.execSQL(createTableQuery);
        // Tạo bảng Thoigianlamviec
        String createTime = "CREATE TABLE Thoigianlamviec (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "Ngaylam TEXT," +
                "Thoigianlam1 TEXT," +
                "Thoigianlam2 TEXT" +
                ")";
        db.execSQL(createTime);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Xử lý nâng cấp cơ sở dữ liệu nếu cần
        // Phương thức này sẽ được gọi khi DATABASE_VERSION được tăng lên
    }

    public boolean checkLogin(String account, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COLUMN_ACCOUNT + " = ? AND " + COLUMN_PASSWORD + " = ?";
        String[] selectionArgs = {account, password};
        Cursor cursor = db.query(TABLE_USERS, null, selection, selectionArgs, null, null, null);

        boolean loginSuccessful = cursor.getCount() > 0;

        cursor.close();
        db.close();

        return loginSuccessful;
    }

    public boolean registerAccount(String account, String password) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Kiểm tra xem tài khoản đã tồn tại hay chưa
        String selection = COLUMN_ACCOUNT + " = ?";
        String[] selectionArgs = {account};
        Cursor cursor = db.query(TABLE_USERS, null, selection, selectionArgs, null, null, null);

        if (cursor.getCount() > 0) {
            // Tài khoản đã tồn tại
            cursor.close();
            db.close();
            return false;
        }

        // Tài khoản chưa tồn tại, thêm vào cơ sở dữ liệu
        ContentValues values = new ContentValues();
        values.put(COLUMN_ACCOUNT, account);
        values.put(COLUMN_PASSWORD, password);
        long newRowId = db.insert(TABLE_USERS, null, values);

        cursor.close();
        db.close();

        return newRowId != -1;
    }

}
