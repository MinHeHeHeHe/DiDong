package com.example.android_doan;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.Intent;
import android.widget.TextView;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.View;


public class DangNhapActivity extends AppCompatActivity {

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

    }
}