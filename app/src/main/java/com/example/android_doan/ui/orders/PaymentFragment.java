package com.example.android_doan.ui.orders;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.android_doan.LoginResponse;
import com.example.android_doan.PaymentResponse;
import com.example.android_doan.R;
import com.example.android_doan.model.Cart;
import com.example.android_doan.network.ApiService;
import com.example.android_doan.network.RetrofitClient;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentFragment extends Fragment {
    private TextView txtDiaChi;
    private TextView txtTongTien;
    private Spinner spinnerThanhToan;
    private Button btnThanhToan;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orders_payment, container, false);

        txtDiaChi = view.findViewById(R.id.txt_dia_chi);
        txtTongTien = view.findViewById(R.id.txt_tong_tien);
        spinnerThanhToan = view.findViewById(R.id.spinner_thanh_toan);
        btnThanhToan = view.findViewById(R.id.btn_thanh_toan);


        // Thiết lập dữ liệu cho Spinner
        String[] options = {"Thanh toán MOMO", "COD"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerThanhToan.setAdapter(adapter);


        btnThanhToan.setOnClickListener(v -> {
            String selectedOption = spinnerThanhToan.getSelectedItem().toString();
            boolean isMomo = selectedOption.equals("Thanh toán MOMO");
            initiatePayment(isMomo);
        });

        loadUserAddress();
        loadCartTotalPrice();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadCartTotalPrice(); // cập nhật lại mỗi lần fragment được hiển thị
    }



    private void loadUserAddress() {
        // Giả sử bạn đã lưu token và userId trong SharedPreferences
        SharedPreferences prefs = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String token = prefs.getString("token", "");
        String userId = prefs.getString("userId", "");

        if (token.isEmpty() || userId.isEmpty()) {
            Toast.makeText(getContext(), "Chưa đăng nhập", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService apiService = RetrofitClient.getApiService();
        apiService.getUserById("Bearer " + token, userId).enqueue(new Callback<LoginResponse.User>() {
            @Override
            public void onResponse(Call<LoginResponse.User> call, Response<LoginResponse.User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String address = response.body().getAddress();  // Đảm bảo getAddress() có trong model
                    txtDiaChi.setText(address != null ? address : "Không có địa chỉ");
                } else {
                    Toast.makeText(getContext(), "Lỗi khi lấy thông tin người dùng", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse.User> call, Throwable t) {
                Log.e("PaymentFragment", "Lỗi API getUserById", t);
                Toast.makeText(getContext(), "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadCartTotalPrice() {
        SharedPreferences prefs = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String token = prefs.getString("token", "");

        if (token.isEmpty()) {
            Toast.makeText(getContext(), "Chưa đăng nhập", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService apiService = RetrofitClient.getApiService();
        apiService.getCart("Bearer " + token).enqueue(new Callback<Cart>() {
            @Override
            public void onResponse(Call<Cart> call, Response<Cart> response) {
                if (response.isSuccessful() && response.body() != null) {
                    double total = response.body().getTotal_price();
                    txtTongTien.setText(String.format("%,.0f", total)); // Hiển thị dạng 12,000
                }
            }

            @Override
            public void onFailure(Call<Cart> call, Throwable t) {
                Log.e("PaymentFragment", "Lỗi API getCart", t);
                Toast.makeText(getContext(), "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void clearCartAfterPayment() {
        SharedPreferences prefs = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String token = prefs.getString("token", "");

        if (token.isEmpty()) return;

        ApiService apiService = RetrofitClient.getApiService();
        apiService.clearCart("Bearer " + token).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.d("PaymentFragment", "Đã xóa giỏ hàng sau thanh toán");
                } else {
                    Log.e("PaymentFragment", "Xóa giỏ hàng thất bại, code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("PaymentFragment", "Lỗi khi xóa giỏ hàng: " + t.getMessage());
            }
        });
    }


    private void initiatePayment(boolean isMomo) {
        SharedPreferences prefs = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String token = prefs.getString("token", "");

        if (token.isEmpty()) {
            Toast.makeText(getContext(), "Chưa đăng nhập", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService apiService = RetrofitClient.getApiService();
        Call<PaymentResponse> call = apiService.initiatePayment("Bearer " + token);

        call.enqueue(new Callback<PaymentResponse>() {
            @Override
            public void onResponse(Call<PaymentResponse> call, Response<PaymentResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String paymentUrl = response.body().getPaymentUrl();

                    if (isMomo && paymentUrl != null && !paymentUrl.isEmpty()) {
                        // 👉 Nếu là MOMO thì mở đường dẫn
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(paymentUrl));
                        startActivity(browserIntent);
                    }

                    clearCartAfterPayment(); // Xóa giỏ hàng cho cả MOMO và COD
                    if (!isMomo) {
                        Toast.makeText(getContext(), "Thanh toán COD thành công", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        Log.e("PAYMENT_ERROR", "Code: " + response.code() + ", Body: " + errorBody);

                    } catch (Exception e) {
                        Log.e("PAYMENT_ERROR", "Không đọc được errorBody", e);
                    }

                    Toast.makeText(getContext(), "Lỗi khi tạo thanh toán", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PaymentResponse> call, Throwable t) {
                Log.e("PaymentFragment", "API lỗi khi gọi initiatePayment", t);
                Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



}