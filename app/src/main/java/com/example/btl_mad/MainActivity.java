package com.example.btl_mad;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
public class MainActivity extends AppCompatActivity {
    private EditText editTextTime, editTextDate;
    private Handler handler;
    private Runnable runnable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextTime = findViewById(R.id.editTextTime);
        editTextTime.setEnabled(false); // Vô hiệu hóa EditText

        editTextDate = findViewById(R.id.editTextDate);
        editTextDate.setEnabled(false);

        // Lấy thời gian hiện tại
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        SimpleDateFormat date = new SimpleDateFormat( "'Ngày' dd 'tháng' MM 'năm' yyyy", Locale.getDefault());
        String currentTime = time.format(calendar.getTime());
        String currentDate = date.format(calendar.getTime());


        // Đặt thời gian và ngày vào EditText
        editTextTime.setText(currentTime);
        editTextDate.setText(currentDate);

        // Cập nhật thời gian thực
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                // Lấy thời gian hiện tại
                Calendar calendar = Calendar.getInstance();
                String currentTime = time.format(calendar.getTime());

                // Cập nhật thời gian vào EditText
                editTextTime.setText(currentTime);

                // Lặp lại sau mỗi 1 giây
                handler.postDelayed(this, 1000);
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Bắt đầu cập nhật thời gian thực khi hoạt động resume
        handler.postDelayed(runnable, 0);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Dừng cập nhật thời gian thực khi hoạt động pause
        handler.removeCallbacks(runnable);
    }
}
