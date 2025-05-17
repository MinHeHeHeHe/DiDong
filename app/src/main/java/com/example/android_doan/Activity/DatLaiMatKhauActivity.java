package com.example.android_doan.Activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import com.example.android_doan.R;
import com.example.android_doan.ResetPasswordRequest;
import com.example.android_doan.ResetPasswordResponse;
import com.example.android_doan.network.ApiService;
import com.example.android_doan.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;

public class DatLaiMatKhauActivity extends AppCompatActivity {

    private EditText edtNewPassword, edtRepeatPassword;
    private ImageView imgEyeNew, imgEyeRepeat;
    private Button btnConfirm;
    private String code; // mã xác thực được truyền từ QuenMatKhauActivity

    private boolean isNewPasswordVisible = false;
    private boolean isRepeatPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dat_lai_mat_khau);

        // Lấy mã xác thực từ Intent
        code = getIntent().getStringExtra("code");

        // Ánh xạ view
        edtNewPassword = findViewById(R.id.txtNewPassword);
        edtRepeatPassword = findViewById(R.id.txtRepeatPassword);
        imgEyeNew = findViewById(R.id.image_eye_slash);
        imgEyeRepeat = findViewById(R.id.image_eye_slash_repeat);
        btnConfirm = findViewById(R.id.btnConfirmNewPassword);

        // Mặc định ẩn mật khẩu
        edtNewPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        edtRepeatPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());

        // Xử lý icon mắt mật khẩu mới
        imgEyeNew.setOnClickListener(v -> {
            isNewPasswordVisible = !isNewPasswordVisible;
            if (isNewPasswordVisible) {
                edtNewPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                edtNewPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
            edtNewPassword.setSelection(edtNewPassword.getText().length());
        });

        // Xử lý icon mắt nhập lại mật khẩu
        imgEyeRepeat.setOnClickListener(v -> {
            isRepeatPasswordVisible = !isRepeatPasswordVisible;
            if (isRepeatPasswordVisible) {
                edtRepeatPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                edtRepeatPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
            edtRepeatPassword.setSelection(edtRepeatPassword.getText().length());
        });

        // Sự kiện xác nhận
        btnConfirm.setOnClickListener(v -> {
            String newPassword = edtNewPassword.getText().toString().trim();
            String repeatPassword = edtRepeatPassword.getText().toString().trim();

            if (newPassword.isEmpty() || repeatPassword.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ mật khẩu", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!newPassword.equals(repeatPassword)) {
                Toast.makeText(this, "Mật khẩu không khớp", Toast.LENGTH_SHORT).show();
                return;
            }

            // Gọi API reset password
            ApiService apiService = RetrofitClient.getApiService();
            ResetPasswordRequest request = new ResetPasswordRequest(code, newPassword);

            apiService.resetPassword(request).enqueue(new Callback<ResetPasswordResponse>() {
                @Override
                public void onResponse(Call<ResetPasswordResponse> call, Response<ResetPasswordResponse> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(DatLaiMatKhauActivity.this, "Đặt lại mật khẩu thành công!", Toast.LENGTH_SHORT).show();

                        // Quay lại màn hình đăng nhập
                        Intent intent = new Intent(DatLaiMatKhauActivity.this, DangNhapActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();

                    } else {
                        Toast.makeText(DatLaiMatKhauActivity.this, "Mã xác thực không hợp lệ hoặc đã hết hạn", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResetPasswordResponse> call, Throwable t) {
                    Toast.makeText(DatLaiMatKhauActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
