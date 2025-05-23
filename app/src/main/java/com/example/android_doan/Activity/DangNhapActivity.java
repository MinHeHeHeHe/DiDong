package com.example.android_doan.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
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

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ email và mật khẩu", Toast.LENGTH_SHORT).show();
                return;
            }

            LoginRequest request = new LoginRequest(email, password);
            ApiService apiService = RetrofitClient.getApiService();

            apiService.login(request).enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        String username = response.body().getUser().getUsername();
                        String token = response.body().getToken();

                        // Lưu token
                        prefs.edit().putString("token", token).apply();

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

        // Đăng ký
        TextView tvDangKy = findViewById(R.id.text_chua_co_tai_khoan);
        tvDangKy.setOnClickListener(v -> startActivity(new Intent(this, DangKyActivity.class)));
        tvDangKy.setOnTouchListener((v, event) -> {
            tvDangKy.setTypeface(null,
                    (event.getAction() == MotionEvent.ACTION_DOWN) ? Typeface.BOLD : Typeface.NORMAL);
            return false;
        });

        // Quên mật khẩu
        TextView tvQuenMatKhau = findViewById(R.id.txtQuenMatKhau);
        tvQuenMatKhau.setOnClickListener(v -> startActivity(new Intent(this, QuenMatKhauActivity.class)));
        tvQuenMatKhau.setOnTouchListener((v, event) -> {
            tvQuenMatKhau.setTypeface(null,
                    (event.getAction() == MotionEvent.ACTION_DOWN) ? Typeface.BOLD : Typeface.NORMAL);
            return false;
        });

        // Hiện/ẩn mật khẩu
        ImageView imgEye = findViewById(R.id.image_eye_slash);
        edtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());

        imgEye.setOnClickListener(v -> {
            boolean isHidden = edtPassword.getTransformationMethod() instanceof PasswordTransformationMethod;
            edtPassword.setTransformationMethod(isHidden
                    ? HideReturnsTransformationMethod.getInstance()
                    : PasswordTransformationMethod.getInstance());
            imgEye.setImageResource(R.drawable.image_eye_slash);
            edtPassword.setSelection(edtPassword.getText().length());
        });
    }
}
