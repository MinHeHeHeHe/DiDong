package com.example.android_doan.ui.profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.android_doan.LoginResponse;
import com.example.android_doan.R;
import com.example.android_doan.ThemThongTinCaNhanRequest;
import com.example.android_doan.ThemThongTinCaNhanResponse;
import com.example.android_doan.network.ApiService;
import com.example.android_doan.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChinhSuaThongTinActivity extends AppCompatActivity {

    EditText edtUsername, edtDob, edtPhone, edtAddress;
    String userRole = "";
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
        String userId = prefs.getString("userId", null);
        String token = prefs.getString("token", null);
        Log.d("DEBUG", "userId: " + userId);
        Log.d("DEBUG", "token: " + token);
        ApiService apiService = RetrofitClient.getApiService();
        Call<LoginResponse.User> call = apiService.getUserById("Bearer " + token, userId);

        call.enqueue(new Callback<LoginResponse.User>() {
            @Override
            public void onResponse(Call<LoginResponse.User> call, Response<LoginResponse.User> response) {
                if (response.isSuccessful()) {
                    LoginResponse.User user = response.body();
                    edtUsername.setText(user.getUsername());
                    edtDob.setText(user.getDob());
                    edtPhone.setText(user.getPhone());
                    edtAddress.setText(user.getAddress());

                } else {
                    Log.e("API_ERROR", "Code: " + response.code() + ", Message: " + response.message());
                    Toast.makeText(getApplicationContext(), "Không tải được thông tin", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse.User> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Lỗi kết nối", Toast.LENGTH_SHORT).show();
            }
        });

        // Sự kiện nút cập nhật thông tin
        btnThayDoi.setOnClickListener(v -> {
            String newUsername = edtUsername.getText().toString().trim();
            String newDob = edtDob.getText().toString().trim();
            String newPhone = edtPhone.getText().toString().trim();
            String newAddress = edtAddress.getText().toString().trim();


            ThemThongTinCaNhanRequest request = new ThemThongTinCaNhanRequest(newUsername, newDob, newPhone, newAddress);

            Call<ThemThongTinCaNhanResponse> updateCall = apiService.updateUser("Bearer " + token, userId, request);
            updateCall.enqueue(new Callback<ThemThongTinCaNhanResponse>() {
                @Override
                public void onResponse(Call<ThemThongTinCaNhanResponse> call, Response<ThemThongTinCaNhanResponse> response) {
                    if (response.isSuccessful()) {
                        if (!response.body().isSuccess()) {
                            Toast.makeText(getApplicationContext(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Cập nhật thất bại", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Cập nhật thất bại (HTTP " + response.code() + ")", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<ThemThongTinCaNhanResponse> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Lỗi kết nối khi cập nhật", Toast.LENGTH_SHORT).show();
                }
            });
        });

        // Gắn sự kiện cho nút quay lại
        ImageView imgBack = findViewById(R.id.img_chevron_left);
        imgBack.setOnClickListener(v -> {
            finish(); // Quay về fragment trước đó
        });


    }
}