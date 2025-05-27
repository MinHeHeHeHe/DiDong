package com.example.android_doan.ui.map;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.android_doan.R;
import com.example.android_doan.network.ApiService;
import com.example.android_doan.network.RetrofitClient;
import com.example.android_doan.ui.orders.TrackingFragment;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapFragment extends Fragment {

    private Button btnConfirm;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        btnConfirm = view.findViewById(R.id.btn_confirm);

        btnConfirm.setOnClickListener(v -> confirmDelivery());

        return view;
    }

    private void confirmDelivery() {
        SharedPreferences prefs = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String token = prefs.getString("token", "");
        String deliveryId = prefs.getString("deliveryId", "");

        if (token.isEmpty() || deliveryId.isEmpty()) {
            Toast.makeText(getContext(), "Thiếu token hoặc deliveryId", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService apiService = RetrofitClient.getApiService();
        apiService.confirmDelivery("Bearer " + token, deliveryId).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Đã xác nhận giao hàng", Toast.LENGTH_SHORT).show();

                    // 👉 Cập nhật giao diện TrackingFragment nếu đang hiển thị
                    FragmentManager fm = requireActivity().getSupportFragmentManager();
                    for (Fragment fragment : fm.getFragments()) {
                        if (fragment instanceof TrackingFragment) {
                            ((TrackingFragment) fragment).updateDeliveryStatus("Đã giao");
                            break;
                        }
                    }
                } else {
                    Toast.makeText(getContext(), "Xác nhận thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
