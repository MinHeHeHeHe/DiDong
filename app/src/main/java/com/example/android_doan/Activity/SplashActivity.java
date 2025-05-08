package com.example.android_doan.Activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;

import com.example.android_doan.R;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_splash);

        // Sử dụng Handler để delay 2 giây trước khi chuyển đến MainActivity
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                // Chuyển sang layout_dang_nhap
                startActivity(new Intent(SplashActivity.this, DangNhapActivity.class));
                finish(); // Đóng SplashActivity
            }
        }, 2000); // 2 giây delay
    }
}