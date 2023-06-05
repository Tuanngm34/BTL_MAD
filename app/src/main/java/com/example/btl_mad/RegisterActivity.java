package com.example.btl_mad;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private EditText editNewTextEmail;
    private EditText editTextPassword;
    private EditText confirmTextPassword;
    private Button btnSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Xử lý sau khi ấn vào text để chuyển sang trang đăng nhập
        TextView sign_in = findViewById(R.id.sign_in);
        sign_in.setOnClickListener(view -> {
            // Xử lý khi TextView được nhấp vào
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        //Xử lý đăng kí
        editNewTextEmail = findViewById(R.id.editNewTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        confirmTextPassword = findViewById(R.id.confirmTextPassword);
        btnSignup = findViewById(R.id.btnSignup);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newUsername = editNewTextEmail.getText().toString();
                String newPassword = editTextPassword.getText().toString();
                String confirmPassword = confirmTextPassword.getText().toString();

                    // Kiểm tra tài khoản đã tồn tại hay chưa
                if (checkExistingUsername(newUsername)) {
                    // Hiển thị thông báo khi tài khoản đã tồn tại
                    Toast.makeText(RegisterActivity.this, "Tài khoản đã tồn tại!", Toast.LENGTH_SHORT).show();
                } else if (!newPassword.equals(confirmPassword)) {
                    // Hiển thị thông báo khi mật khẩu không khớp
                    Toast.makeText(RegisterActivity.this, "Mật khẩu không khớp!", Toast.LENGTH_SHORT).show();
                } else if (newUsername.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
                    // Kiểm tra xem các trường đã được nhập hay chưa
                    Toast.makeText(RegisterActivity.this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                } else {
                    // Thêm tài khoản mới vào cơ sở dữ liệu
                    addNewUserToDatabase(newUsername, newPassword);

                    // Hiển thị thông báo khi tài khoản được tạo thành công
                    Toast.makeText(RegisterActivity.this, "Đã tạo tài khoản thành công!", Toast.LENGTH_SHORT).show();

                    // Chuyển hướng sang trang đăng nhập
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

    }

    // Hàm kiểm tra tài khoản đã tồn tại hay chưa
    private boolean checkExistingUsername(String username) {
        // Kiểm tra username trong cơ sở dữ liệu
        // Ở đây chỉ là ví dụ, bạn cần thay thế phần này bằng code xử lý cụ thể với cơ sở dữ liệu của bạn
        String savedUsername = "admin";

        return username.equals(savedUsername);
    }

    // Hàm thêm tài khoản mới vào cơ sở dữ liệu
    private void addNewUserToDatabase(String username, String password) {
        // Thêm username và password vào cơ sở dữ liệu
        // Ở đây chỉ là ví dụ, bạn cần thay thế phần này bằng code xử lý cụ thể với cơ sở dữ liệu của bạn
        // Ví dụ:
        // database.insertUser(username, password);
    }
}
