package com.example.btl_mad;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class HomeFragment extends Fragment {
    private TextView editTextTime, editTextDate;
    private Button checkInButton;
    private Handler handler;
    private Runnable runnable;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        editTextTime = view.findViewById(R.id.editTextTime);
        editTextDate = view.findViewById(R.id.editTextDate);
        checkInButton = view.findViewById(R.id.checkInButton);

        // Lấy thời gian hiện tại
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        SimpleDateFormat dateFormat = new SimpleDateFormat("'Ngày' dd 'tháng' MM 'năm' yyyy", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        String currentTime = timeFormat.format(calendar.getTime());
        String currentDate = dateFormat.format(calendar.getTime());

        // Đặt thời gian vào TextView
        editTextTime.setText(currentTime);
        editTextDate.setText(currentDate);

        // Cập nhật thời gian thực
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                // Lấy thời gian hiện tại
                Calendar calendar = Calendar.getInstance();
                String currentTime = timeFormat.format(calendar.getTime());

                // Cập nhật thời gian vào TextView
                editTextTime.setText(currentTime);

                // Lặp lại sau mỗi 1 giây
                handler.postDelayed(this, 1000);
            }
        };

        // Bắt đầu cập nhật thời gian thực khi Fragment được tạo
        handler.postDelayed(runnable, 0);

        // Bắt sự kiện khi nhấn vào nút Check In
        checkInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Thực hiện công việc khi nút "check in" được nhấn
                performCheckIn();
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Dừng cập nhật thời gian thực khi Fragment bị hủy
        handler.removeCallbacks(runnable);
    }

    private void performCheckIn() {
        MyDatabaseHelper dbHelper = new MyDatabaseHelper(getContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String currentDate = getCurrentDate();
        String currentTime = getCurrentTime();

        // Kiểm tra xem đã có dữ liệu cho ngày hiện tại hay chưa
        String query = "SELECT * FROM Thoigianlamviec WHERE Ngaylam = ?";
        Cursor cursor = db.rawQuery(query, new String[]{currentDate});
        if (cursor.getCount() > 0) {
            // Đã có dữ liệu cho ngày hiện tại, cập nhật cột "Thoigianlam2"
            ContentValues values = new ContentValues();
            values.put("Thoigianlam2", currentTime);
            db.update("Thoigianlamviec", values, "Ngaylam = ?", new String[]{currentDate});
        } else {
            // Chưa có dữ liệu cho ngày hiện tại, chèn dữ liệu mới
            ContentValues values = new ContentValues();
            values.put("Ngaylam", currentDate);
            values.put("Thoigianlam1", currentTime);
            db.insert("Thoigianlamviec", null, values);
        }

        cursor.close();
        db.close();

        Toast.makeText(getContext(), "Check in thành công!", Toast.LENGTH_SHORT).show();
    }

    private String getCurrentDate() {
        // Lấy ngày hiện tại
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }

    private String getCurrentTime() {
        // Lấy giờ và phút hiện tại
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return timeFormat.format(calendar.getTime());
    }
}
