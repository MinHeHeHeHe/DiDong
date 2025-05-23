package com.example.android_doan.ui.profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.android_doan.R;

public class ChinhSuaThongTinActivity extends AppCompatActivity {

    EditText edtUsername, edtDob, edtPhone, edtAddress;
    Button btnThayDoi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chinh_sua_thong_tin);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Khởi tạo view
        edtUsername = findViewById(R.id.txt_full_name);
        edtDob = findViewById(R.id.txt_full_birthday);
        edtPhone = findViewById(R.id.txt_full_phone);
        edtAddress = findViewById(R.id.txt_full_address);
        btnThayDoi = findViewById(R.id.btn_change);

        // Lấy dữ liệu từ SharedPreferences
        SharedPreferences prefs = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String username = prefs.getString("username", "Người dùng");
        String dob = prefs.getString("dob", "");
        String phone = prefs.getString("phone", "");
        String address = prefs.getString("address", "");
        Log.d("DEBUG", "dob, phone,addres từ SharedPreferences: " + dob + phone + address);
        // Hiển thị lên EditText
        edtUsername.setText(username);
        edtDob.setText(dob);
        edtPhone.setText(phone);
        edtAddress.setText(address);

        // Gắn sự kiện cho nút quay lại
        ImageView imgBack = findViewById(R.id.img_chevron_left);
        imgBack.setOnClickListener(v -> {
            finish(); // Quay về fragment trước đó
        });


    }
}
