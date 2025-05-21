package com.example.android_doan.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.android_doan.R;
import com.example.android_doan.RegisterRequest;
import com.example.android_doan.RegisterResponse;
import com.example.android_doan.network.ApiService;
import com.example.android_doan.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;

public class DangKyActivity extends AppCompatActivity {

    private EditText edtEmail, edtPassword, edtRePassword;
    private Button btnDangKy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_dang_ky);

        edtEmail = findViewById(R.id.txt_DangKy_Email);
        edtPassword = findViewById(R.id.txt_DangKy_MatKhau);
        edtRePassword = findViewById(R.id.txt_DangKy_NhapLaiMatKhau);
        btnDangKy = findViewById(R.id.button_rectangle1);

        // Nhận lại dữ liệu từ Intent (nếu có)
        Intent receivedIntent = getIntent();
        edtEmail.setText(receivedIntent.getStringExtra("email"));
        edtPassword.setText(receivedIntent.getStringExtra("password"));
        edtRePassword.setText(receivedIntent.getStringExtra("rePassword"));

        btnDangKy.setOnClickListener(v -> {
            String email = edtEmail.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();
            String rePassword = edtRePassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty() || rePassword.isEmpty()) {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(rePassword)) {
                Toast.makeText(this, "Mật khẩu nhập lại không khớp", Toast.LENGTH_SHORT).show();
                return;
            }

            // Nếu đăng ký thì gọi API createUser như bình thường
            RegisterRequest request = new RegisterRequest(
                    "defaultUsername", password, email,
                    "Chưa cập nhật", "2000-01-01", "0000", "user"
            );

            ApiService apiService = RetrofitClient.getApiService();
            apiService.register(request).enqueue(new Callback<RegisterResponse>() {
                @Override
                public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        String userId = response.body().getId();
                        Toast.makeText(DangKyActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(DangKyActivity.this, "Email đã được sử dụng!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<RegisterResponse> call, Throwable t) {
                    Toast.makeText(DangKyActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
        ImageView imgBack = findViewById(R.id.image_arrow_left);
        imgBack.setOnClickListener(v -> finish());

        // Ẩn/hiện mật khẩu
        ImageView imgEye = findViewById(R.id.image_eye_slash);
        imgEye.setOnClickListener(v -> togglePasswordVisibility(edtPassword, imgEye));
        ImageView imgEye1 = findViewById(R.id.image_eye_slash_repeat);
        imgEye1.setOnClickListener(v -> togglePasswordVisibility(edtRePassword, imgEye1));
    }

    private void togglePasswordVisibility(EditText editText, ImageView icon) {
        if (editText.getTransformationMethod() instanceof PasswordTransformationMethod) {
            editText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
        editText.setSelection(editText.getText().length());
    }
}
