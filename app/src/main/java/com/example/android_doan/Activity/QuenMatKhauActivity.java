package com.example.android_doan.Activity;

import android.os.Bundle;
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

import com.example.android_doan.R;
import com.example.android_doan.network.ApiService;
import com.example.android_doan.network.RetrofitClient;
import com.example.android_doan.ForgotPasswordRequest;
import com.example.android_doan.ForgotPasswordResponse;
import com.example.android_doan.ResetPasswordRequest;
import com.example.android_doan.ResetPasswordResponse;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;


public class QuenMatKhauActivity extends AppCompatActivity {

    private EditText edtEmail, edtCode, edtNewPassword, edtConfirmPassword;
    private Button btnVerifyEmail, btnConfirmPassword;

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
        edtNewPassword = findViewById(R.id.txtQuenMatKhau_MatKhauMoi);
        edtConfirmPassword = findViewById(R.id.txtQuenMatKhau_MatKhauLai);
        btnVerifyEmail = findViewById(R.id.btn_verify_email);
        btnConfirmPassword = findViewById(R.id.btn_confirm_new_password);

        ImageView imgEye = findViewById(R.id.image_eye_slash);
        edtNewPassword.setTransformationMethod(PasswordTransformationMethod.getInstance()); // Mặc định ẩn

        imgEye.setOnClickListener(v -> {
            if (edtNewPassword.getTransformationMethod() instanceof PasswordTransformationMethod) {
                edtNewPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                imgEye.setImageResource(R.drawable.image_eye_slash); // icon mắt mở (nếu có)
            } else {
                edtNewPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                imgEye.setImageResource(R.drawable.image_eye_slash); // icon mắt đóng
            }
            edtNewPassword.setSelection(edtNewPassword.getText().length());
        });

        ImageView imgEye2 = findViewById(R.id.image_eye_slash1);
        edtConfirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());

        imgEye2.setOnClickListener(v -> {
            if (edtConfirmPassword.getTransformationMethod() instanceof PasswordTransformationMethod) {
                edtConfirmPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                imgEye2.setImageResource(R.drawable.image_eye_slash); // icon mắt mở
            } else {
                edtConfirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                imgEye2.setImageResource(R.drawable.image_eye_slash); // icon mắt gạch
            }
            edtConfirmPassword.setSelection(edtConfirmPassword.getText().length());
        });


        // Nút quay lại
        imgBack.setOnClickListener(v -> finish());
        imgBack.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) v.setAlpha(0.6f);
            else v.setAlpha(1.0f);
            return false;
        });

        // Gửi mã xác thực qua email
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
                            if (response.isSuccessful()) {
                                Toast.makeText(QuenMatKhauActivity.this, "Mã xác thực đã gửi!", Toast.LENGTH_SHORT).show();
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

        // Đổi mật khẩu mới
        btnConfirmPassword.setOnClickListener(v -> {
            String code = edtCode.getText().toString().trim();
            String newPassword = edtNewPassword.getText().toString().trim();
            String confirm = edtConfirmPassword.getText().toString().trim();

            if (code.isEmpty() || newPassword.isEmpty() || confirm.isEmpty()) {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!newPassword.equals(confirm)) {
                Toast.makeText(this, "Mật khẩu không khớp", Toast.LENGTH_SHORT).show();
                return;
            }

            ApiService apiService = RetrofitClient.getApiService();
            apiService.resetPassword(new ResetPasswordRequest(code, newPassword))
                    .enqueue(new Callback<ResetPasswordResponse>() {
                        @Override
                        public void onResponse(Call<ResetPasswordResponse> call, Response<ResetPasswordResponse> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(QuenMatKhauActivity.this, "Đặt lại mật khẩu thành công!", Toast.LENGTH_SHORT).show();
                                finish(); // Quay về màn hình đăng nhập
                            } else {
                                Toast.makeText(QuenMatKhauActivity.this, "Mã xác thực không đúng hoặc hết hạn!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResetPasswordResponse> call, Throwable t) {
                            Toast.makeText(QuenMatKhauActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }
}
