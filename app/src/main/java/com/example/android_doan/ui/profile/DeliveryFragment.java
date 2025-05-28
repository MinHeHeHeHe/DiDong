/*package com.example.android_doan.ui.profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android_doan.R;
import com.example.android_doan.adapter.DeliveryOrdersAdapter;
import com.example.android_doan.model.Delivery;
import com.example.android_doan.network.ApiService;
import com.example.android_doan.network.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeliveryFragment extends Fragment {

    private RecyclerView recyclerView;
    private DeliveryOrdersAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_order_history, container, false);
        recyclerView = view.findViewById(R.id.recycler_delivery_history);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        loadDeliveredOrders();
        return view;
    }

    private void loadDeliveredOrders() {
        SharedPreferences prefs = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String token = prefs.getString("token", "");

        if (token.isEmpty()) {
            Toast.makeText(getContext(), "Token không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService apiService = RetrofitClient.getApiService();
        Call<List<Delivery>> call = apiService.getAllDeliveredDeliveries("Bearer " + token);
        call.enqueue(new Callback<List<Delivery>>() {
            @Override
            public void onResponse(@NonNull Call<List<Delivery>> call, @NonNull Response<List<Delivery>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Delivery> deliveries = response.body();
                    adapter = new DeliveryOrdersAdapter(deliveries);
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(getContext(), "Không có dữ liệu đơn hàng đã giao", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Delivery>> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối server", Toast.LENGTH_SHORT).show();
                Log.e("DeliveryFragment", "API call failed: ", t);
            }
        });
    }
}*/
