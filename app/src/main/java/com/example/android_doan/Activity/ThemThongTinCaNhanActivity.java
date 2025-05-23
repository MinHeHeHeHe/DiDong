package com.example.android_doan.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.android_doan.R;
import com.example.android_doan.ThemThongTinCaNhanRequest;
import com.example.android_doan.ThemThongTinCaNhanResponse;
import com.example.android_doan.network.ApiService;
import com.example.android_doan.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ThemThongTinCaNhanActivity extends AppCompatActivity {

    private EditText edtUsername, edtAddress, edtDateOfBirth, edtPhone;
    private Button btnLuu;
    private String userId, email, password, rePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_them_thong_tin_ca_nhan);

        edtUsername = findViewById(R.id.edt_username);
        edtAddress = findViewById(R.id.edt_address);
        edtDateOfBirth = findViewById(R.id.edt_date_of_birth);
        edtPhone = findViewById(R.id.edt_phone);
        btnLuu = findViewById(R.id.btn_save_info);

        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");
        email = intent.getStringExtra("email");
        password = intent.getStringExtra("password");
        rePassword = intent.getStringExtra("rePassword");

        btnLuu.setOnClickListener(v -> {
            String username = edtUsername.getText().toString().trim();
            String address = edtAddress.getText().toString().trim();
            String dateOfBirth = edtDateOfBirth.getText().toString().trim();
            String phone = edtPhone.getText().toString().trim();

            if (username.isEmpty() || phone.isEmpty() || address.isEmpty() || dateOfBirth.isEmpty()) {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            ThemThongTinCaNhanRequest request = new ThemThongTinCaNhanRequest(
                    username, dateOfBirth, address, phone, "user");

            // Lấy token từ SharedPreferences
            String token = getSharedPreferences("MyPrefs", MODE_PRIVATE).getString("token", "");

            ApiService apiService = RetrofitClient.getApiService();
            apiService.updateUser("Bearer " + token, userId, request)
                    .enqueue(new Callback<ThemThongTinCaNhanResponse>() {
                        @Override
                        public void onResponse(Call<ThemThongTinCaNhanResponse> call, Response<ThemThongTinCaNhanResponse> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(ThemThongTinCaNhanActivity.this, "Thêm thông tin thành công", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(ThemThongTinCaNhanActivity.this, MainActivity.class));
                                finish();
                            } else {
                                Toast.makeText(ThemThongTinCaNhanActivity.this, "Thêm thông tin thất bại", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ThemThongTinCaNhanResponse> call, Throwable t) {
                            Toast.makeText(ThemThongTinCaNhanActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        // nút quay lại
        ImageView imgBack = findViewById(R.id.image_arrow_left);
        imgBack.setOnClickListener(v -> {
            Intent intent_back = new Intent(ThemThongTinCaNhanActivity.this, DangNhapActivity.class);
            intent_back.putExtra("email", email);  // Truyền lại email
            startActivity(intent_back);
            finish();
        });

    }
}
