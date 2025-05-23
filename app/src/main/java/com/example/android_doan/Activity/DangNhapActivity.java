package com.example.android_doan.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.android_doan.LoginRequest;
import com.example.android_doan.LoginResponse;
import com.example.android_doan.R;
import com.example.android_doan.network.ApiService;
import com.example.android_doan.network.RetrofitClient;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DangNhapActivity extends AppCompatActivity {
    private EditText edtEmail, edtPassword;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.layout_dang_nhap);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.dangnhap), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        edtEmail = findViewById(R.id.txtEmail);
        edtPassword = findViewById(R.id.txtMatKhau);
        Button btnDangNhap = findViewById(R.id.button_rectangle);

        prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        btnDangNhap.setOnClickListener(v -> {
            String email = edtEmail.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();
            // Tạo sẵn tài khoản test
            LoginRequest request = new LoginRequest(email, password);

            ApiService apiService = RetrofitClient.getApiService();
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ email và mật khẩu", Toast.LENGTH_SHORT).show();
                return;
            }
            apiService.login(request).enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        String username = response.body().getUser().getUsername();
                        String date_of_birth = response.body().getUser().getDob();
                        String phone = response.body().getUser().getPhone();
                        String address = response.body().getUser().getAddress();
                        String userId = response.body().getUser().getId();
                        String avatarUrl = response.body().getUser().getImageUrl();
                        Log.d("DEBUG", "avatarUrl từ SharedPreferences: " + avatarUrl);
                        Log.d("DEBUG", "Login response: " + new Gson().toJson(response.body()));

                        // Lưu token vào SharedPreferences ở đây
                        String token = response.body().getToken();
                        getSharedPreferences("MyPrefs", MODE_PRIVATE)
                                .edit()
                                .putString("username", username)
                                .putString("avatarUrl", avatarUrl)
                                .putString("date_of_birth", date_of_birth)
                                .putString("phone", phone)
                                .putString("address", address)
                                .putString("token", token)
                                .putString("userId", userId)
                                .apply();
                        Toast.makeText(DangNhapActivity.this, "Chào " + username, Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(DangNhapActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(DangNhapActivity.this, "Sai email hoặc mật khẩu", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    Toast.makeText(DangNhapActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });


        // Gắn sự kiện click cho TextView "Đăng ký ngay"
        TextView tvDangKy = findViewById(R.id.text_chua_co_tai_khoan);
        tvDangKy.setOnClickListener(v -> {
            Intent intent = new Intent(DangNhapActivity.this, DangKyActivity.class);
            startActivity(intent);
        });

        tvDangKy.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    tvDangKy.setTypeface(null, Typeface.BOLD);
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    tvDangKy.setTypeface(null, Typeface.NORMAL);
                    break;
            }
            return false;
        });

        // Gắn sự kiện click cho TextView "Quên mật khẩu"
        TextView tvQuenMatKhau = findViewById(R.id.txtQuenMatKhau);
        tvQuenMatKhau.setOnClickListener(v -> {
            Intent intent = new Intent(DangNhapActivity.this, QuenMatKhauActivity.class);
            startActivity(intent);
        });

        tvQuenMatKhau.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    tvQuenMatKhau.setTypeface(null, Typeface.BOLD);
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    tvQuenMatKhau.setTypeface(null, Typeface.NORMAL);
                    break;
            }
            return false;
        });
        // thêm sự kiện che mk
        ImageView imgEye = findViewById(R.id.image_eye_slash);
        edtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance()); // mặc định ẩn

        imgEye.setOnClickListener(v -> {
            if (edtPassword.getTransformationMethod() instanceof PasswordTransformationMethod) {
                // Hiện mật khẩu
                edtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                imgEye.setImageResource(R.drawable.image_eye_slash); // thay icon nếu có
            } else {
                // Ẩn mật khẩu
                edtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                imgEye.setImageResource(R.drawable.image_eye_slash); // icon mắt bị gạch
            }

            // Di chuyển con trỏ về cuối chuỗi
            edtPassword.setSelection(edtPassword.getText().length());
        });
    }
}