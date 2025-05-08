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
        EdgeToEdge.enable(this);
        setContentView(R.layout.layout_dang_ky);

        // Ánh xạ layout
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.button_rectangle1), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Gán ID các trường trong giao diện
        edtEmail = findViewById(R.id.txt_DangKy_Email);
        edtPassword = findViewById(R.id.txt_DangKy_MatKhau);
        edtRePassword = findViewById(R.id.txt_DangKy_NhapLaiMatKhau);
        btnDangKy = findViewById(R.id.button_rectangle1);

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

            // Tạo đối tượng request
            RegisterRequest request = new RegisterRequest(
                    "defaultUsername",          // username (tạm fix cứng)
                    password,
                    email,
                    "Chưa cập nhật",            // address
                    "2000-01-01",               // date_of_birth
                    "user"
            );


            // Gọi API
            ApiService apiService = RetrofitClient.getApiService();
            apiService.register(request).enqueue(new Callback<RegisterResponse>() {
                @Override
                public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(DangKyActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                        finish(); // Quay lại màn hình trước (đăng nhập)
                    } else {
                        Toast.makeText(DangKyActivity.this, "Đăng ký thất bại!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<RegisterResponse> call, Throwable t) {
                    Toast.makeText(DangKyActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
        // Gắn sự kiện cho nút quay lại
        ImageView imgBack = findViewById(R.id.image_arrow_left);
        imgBack.setOnClickListener(v -> {
            finish(); // Quay về fragment trước đó
        });

        imgBack.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    v.setAlpha(0.6f); // làm mờ khi nhấn
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    v.setAlpha(1.0f); // trở lại bình thường
                    break;
            }
            return false;
        });

        // thêm sự kiện che mk
        ImageView imgEye = findViewById(R.id.image_eye_slash);
        edtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance()); // mặc định ẩn

        imgEye.setOnClickListener(v -> {
            if (edtPassword.getTransformationMethod() instanceof PasswordTransformationMethod) {
                // Hiện mật khẩu
                edtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                imgEye.setImageResource(R.drawable.image_eye_slash); // thay icon nếu có
            } else {
                // Ẩn mật khẩu
                edtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                imgEye.setImageResource(R.drawable.image_eye_slash); // icon mắt bị gạch
            }

            // Di chuyển con trỏ về cuối chuỗi
            edtPassword.setSelection(edtPassword.getText().length());
        });
    }
}
