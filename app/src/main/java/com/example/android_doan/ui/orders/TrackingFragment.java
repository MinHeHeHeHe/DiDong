package com.example.android_doan.ui.orders;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.android_doan.R;
import com.example.android_doan.model.Delivery;
import com.example.android_doan.network.ApiService;
import com.example.android_doan.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrackingFragment extends Fragment {

    private TextView txtGiaoHangStatus;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orders_tracking, container, false);
        ConstraintLayout layout = view.findViewById(R.id.fragment_tracking);
        layout.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white));

        txtGiaoHangStatus = view.findViewById(R.id.txt_giao_hang_status);

        getDeliveryStatus();

        return view;
    }

    private void getDeliveryStatus() {
        SharedPreferences sharedPref = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String token = sharedPref.getString("token", null);
        String deliveryId = sharedPref.getString("lastDeliveryId", null); // deliveryId nên được lưu khi tạo đơn

        if (token == null || deliveryId == null) {
            txtGiaoHangStatus.setText("Không tìm thấy đơn hàng.");
            return;
        }

        ApiService apiService = RetrofitClient.getApiService();
        Call<Delivery> call = apiService.getDeliveryById(deliveryId, "Bearer " + token);
        call.enqueue(new Callback<Delivery>() {
            @Override
            public void onResponse(Call<Delivery> call, Response<Delivery> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String status = response.body().getStatus(); // phải có getter `getStatus()` trong class Delivery
                    if ("pending".equals(status))
                        txtGiaoHangStatus.setText("Đang giao");
                    else if ("delivered".equals(status))
                        txtGiaoHangStatus.setText("Đã giao");
                } else {
                    txtGiaoHangStatus.setText("Không thể lấy trạng thái đơn hàng.");
                }
            }

            @Override
            public void onFailure(Call<Delivery> call, Throwable t) {
                txtGiaoHangStatus.setText("Lỗi khi gọi API.");
            }
        });
    }
}
