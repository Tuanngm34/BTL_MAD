package com.example.btl_mad;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import java.util.Locale;

public class HomeFragment extends Fragment {
    private TextView editTextTime;
    private Handler handler;
    private Runnable runnable;

    private  Button checkInButton;
    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        editTextTime = view.findViewById(R.id.editTextTime);
        TextView editTextDate = view.findViewById(R.id.editTextDate);
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
                String username = "admin";
                performCheckIn(username);
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
    private boolean isCheckedIn = false; // Biến kiểm tra đã check-in hay chưa
    private void performCheckIn(String account) {
        MyDatabaseHelper dbHelper = new MyDatabaseHelper(getContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String currentDate = getCurrentDate();
        String currentTime = getCurrentTime();

        // Kiểm tra xem đã có dữ liệu cho ngày hiện tại hay chưa
        String query = "SELECT * FROM worktime WHERE Ngaylam = ? AND account = ?";
        Cursor cursor = db.rawQuery(query, new String[]{currentDate, account});
        if (cursor.getCount() > 0) {
            // Đã có dữ liệu cho ngày hiện tại và tài khoản hiện tại
            if (isCheckedIn) {
                // Ngày hiện tại đã được check-in, không thực hiện cập nhật và hiển thị thông báo
                Toast.makeText(getContext(), "Bạn đã hoàn thành chấm công hôm nay rồi!", Toast.LENGTH_SHORT).show();
            } else {
                // Ngày hiện tại chưa được check-in, cập nhật cột "Thoigianlam2" và đặt isCheckedIn thành true
                ContentValues values = new ContentValues();
                values.put("Thoigianlam2", currentTime);
                db.update("worktime", values, "Ngaylam = ? AND account = ?", new String[]{currentDate, account});
                isCheckedIn = true;
                Toast.makeText(getContext(), "Check in thành công!", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Chưa có dữ liệu cho ngày hiện tại và tài khoản hiện tại, chèn dữ liệu mới
            ContentValues values = new ContentValues();
            values.put("Ngaylam", currentDate);
            values.put("account", account);
            values.put("Thoigianlam1", currentTime);
            db.insert("worktime", null, values);
            isCheckedIn = true; // Đặt isCheckedIn thành true khi thực hiện check-in lần đầu tiên
            Toast.makeText(getContext(), "Check in thành công!", Toast.LENGTH_SHORT).show();
        }

        cursor.close();
        db.close();

        // Vô hiệu hóa button check-in nếu đã check-in cho ngày hiện tại
        if (isCheckedIn) {
            checkInButton.setEnabled(false);
            Toast.makeText(getContext(), "Bạn đã hoàn thành chấm công ngày hôm nay rồi!", Toast.LENGTH_SHORT).show();
        }
    }

    private String getCurrentDate() {
        // Lấy ngày hiện tại
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd", Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }

    private String getCurrentTime() {
        // Lấy giờ và phút hiện tại
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return timeFormat.format(calendar.getTime());
    }
}
