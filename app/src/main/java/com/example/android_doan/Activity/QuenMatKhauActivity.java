package com.example.android_doan.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.android_doan.ForgotPasswordRequest;
import com.example.android_doan.ForgotPasswordResponse;
import com.example.android_doan.R;
import com.example.android_doan.VerifyCodeRequest;
import com.example.android_doan.VerifyCodeResponse;
import com.example.android_doan.network.ApiService;
import com.example.android_doan.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuenMatKhauActivity extends AppCompatActivity {

    private EditText edtEmail, edtCode;
    private Button btnVerifyEmail, btnXacNhanMa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_quen_mat_khau);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.quenmatkhau), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Ánh xạ
        ImageView imgBack = findViewById(R.id.image_arrow_left);
        edtEmail = findViewById(R.id.txtQuenMatKhau_Email);
        edtCode = findViewById(R.id.txtQuenMatKhau_MaXacThuc);
        btnVerifyEmail = findViewById(R.id.btn_verify_email);
        btnXacNhanMa = findViewById(R.id.btn_xacnhan_ma_xacthuc);

        // Quay lại
        imgBack.setOnClickListener(v -> finish());
        imgBack.setOnTouchListener((v, event) -> {
            v.setAlpha(event.getAction() == MotionEvent.ACTION_DOWN ? 0.6f : 1.0f);
            return false;
        });

        // Gửi mã xác thực
        btnVerifyEmail.setOnClickListener(v -> {
            String email = edtEmail.getText().toString().trim();
            if (email.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập email", Toast.LENGTH_SHORT).show();
                return;
            }

            ApiService apiService = RetrofitClient.getApiService();
            apiService.forgotPassword(new ForgotPasswordRequest(email))
                    .enqueue(new Callback<ForgotPasswordResponse>() {
                        @Override
                        public void onResponse(Call<ForgotPasswordResponse> call, Response<ForgotPasswordResponse> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                // In log JSON để kiểm tra có token không
                                Log.d("FORGOT", "Token trả về: " + response.body().getToken());
                                Log.d("FORGOT", "Message trả về: " + response.body().getMessage());

                                String token = response.body().getToken();  //  Đảm bảo getToken() không null

                                if (token != null && !token.isEmpty()) {
                                    getSharedPreferences("MyPrefs", MODE_PRIVATE)
                                            .edit()
                                            .putString("resetToken", token)
                                            .apply();
                                    Toast.makeText(QuenMatKhauActivity.this, "Mã xác thực đã gửi!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(QuenMatKhauActivity.this, "Token null. Backend không trả về!", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(QuenMatKhauActivity.this, "Không tìm thấy email!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ForgotPasswordResponse> call, Throwable t) {
                            Toast.makeText(QuenMatKhauActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        // Xác nhận mã
        /*btnXacNhanMa.setOnClickListener(v -> {
            String savedToken = getSharedPreferences("MyPrefs", MODE_PRIVATE).getString("resetToken", "");
            Log.d("FORGOT", "Token đã lưu trong SharedPreferences: " + savedToken);

            if (savedToken.isEmpty()) {
                Toast.makeText(this, "Không tìm thấy mã xác thực. Vui lòng gửi lại email.", Toast.LENGTH_SHORT).show();
                return;
            }

            ApiService apiService = RetrofitClient.getApiService();
            apiService.verifyCode(new VerifyCodeRequest(savedToken))
                    .enqueue(new Callback<VerifyCodeResponse>() {
                        @Override
                        public void onResponse(Call<VerifyCodeResponse> call, Response<VerifyCodeResponse> response) {
                            if (response.isSuccessful()) {
                                Intent intent = new Intent(QuenMatKhauActivity.this, DatLaiMatKhauActivity.class);
                                intent.putExtra("code", savedToken);
                                startActivity(intent);
                            } else {
                                Toast.makeText(QuenMatKhauActivity.this, "Mã xác thực không hợp lệ", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<VerifyCodeResponse> call, Throwable t) {
                            Toast.makeText(QuenMatKhauActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }); */
    }
}