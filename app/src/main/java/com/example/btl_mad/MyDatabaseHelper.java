package com.example.btl_mad;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    // Thông tin về cơ sở dữ liệu
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "TimeSheet.sqlite";

    // Tên bảng và tên cột cho bảng users
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_USERS_ID = "id";
    private static final String COLUMN_USERS_ACCOUNT = "account";
    private static final String COLUMN_USERS_PASSWORD = "password";

    // Tên bảng và tên cột cho bảng worktime
    private static final String TABLE_WORKTIME = "worktime";
    private static final String COLUMN_WORKTIME_ID = "id";
    private static final String COLUMN_WORKTIME_ACCOUNT = "account";
    private static final String COLUMN_WORKTIME_DATE = "Ngaylam";
    private static final String COLUMN_WORKTIME_TIME1 = "Thoigianlam1";
    private static final String COLUMN_WORKTIME_TIME2 = "Thoigianlam2";

    public MyDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tạo bảng users
        String createUsersTable = "CREATE TABLE " + TABLE_USERS + "(" +
                COLUMN_USERS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_USERS_ACCOUNT + " TEXT," +
                COLUMN_USERS_PASSWORD + " TEXT" +
                ")";
        db.execSQL(createUsersTable);

        // Tạo bảng worktime
        String createWorktimeTable = "CREATE TABLE " + TABLE_WORKTIME + "(" +
                COLUMN_WORKTIME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_WORKTIME_ACCOUNT + " TEXT," +
                COLUMN_WORKTIME_DATE + " TEXT," +
                COLUMN_WORKTIME_TIME1 + " TEXT," +
                COLUMN_WORKTIME_TIME2 + " TEXT" +
                ")";
        db.execSQL(createWorktimeTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Xử lý nâng cấp cơ sở dữ liệu nếu cần
        // Phương thức này sẽ được gọi khi DATABASE_VERSION được tăng lên
    }

    public boolean checkLogin(String account, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COLUMN_USERS_ACCOUNT + " = ? AND " + COLUMN_USERS_PASSWORD + " = ?";
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
        String selection = COLUMN_USERS_ACCOUNT + " = ?";
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
        values.put(COLUMN_USERS_ACCOUNT, account);
        values.put(COLUMN_USERS_PASSWORD, password);
        long newRowId = db.insert(TABLE_USERS, null, values);

        if (newRowId != -1) {
            // Thêm dòng mới vào bảng worktime với thuộc tính "account" tương ứng
            ContentValues worktimeValues = new ContentValues();
            worktimeValues.put(COLUMN_WORKTIME_ACCOUNT, account);
            long newWorktimeRowId = db.insert(TABLE_WORKTIME, null, worktimeValues);

            if (newWorktimeRowId == -1) {
                // Xóa tài khoản vừa thêm nếu không thể thêm dòng mới vào bảng worktime
                db.delete(TABLE_USERS, COLUMN_USERS_ID + " = ?", new String[]{String.valueOf(newRowId)});
                newRowId = -1;
            }
        }

        cursor.close();
        db.close();

        return newRowId != -1;
    }

    public List<String> getAllWorktimeData() {
        List<String> worktimeDataList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_WORKTIME_DATE, COLUMN_WORKTIME_TIME1, COLUMN_WORKTIME_TIME2};
        Cursor cursor = db.query(TABLE_WORKTIME, columns, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                String date = cursor.getString(cursor.getColumnIndex(COLUMN_WORKTIME_DATE));
                String time1 = cursor.getString(cursor.getColumnIndex(COLUMN_WORKTIME_TIME1));
                String time2 = cursor.getString(cursor.getColumnIndex(COLUMN_WORKTIME_TIME2));

                // Tạo chuỗi dữ liệu từ các cột
                String worktimeData = "Ngày: " + date +
                        ", Check-in: " + time1 +
                        ", Check-out: " + time2;

                worktimeDataList.add(worktimeData);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return worktimeDataList;
    }


}
