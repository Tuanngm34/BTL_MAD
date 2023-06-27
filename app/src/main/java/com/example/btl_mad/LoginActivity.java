package com.example.btl_mad;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button btnLogin;
    private MyDatabaseHelper databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Xử lý khi ấn vào text đăng kí
        TextView signupLinkTextView = findViewById(R.id.signupLinkTextView);
        signupLinkTextView.setOnClickListener(view -> {
            // Xử lý khi TextView được nhấp vào
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        btnLogin = findViewById(R.id.btnLogin);

        databaseHelper = new MyDatabaseHelper(this);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();

                boolean loginSuccessfull = databaseHelper.checkLogin(username, password);

                // Kiểm tra tài khoản và mật khẩu
                if (loginSuccessfull) {
                    // Chuyển sang giao diện activity_main
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    // Hiển thị thông báo khi tài khoản hoặc mật khẩu không đúng
                    Toast.makeText(LoginActivity.this, "Tài khoản hoặc mật khẩu không đúng!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}