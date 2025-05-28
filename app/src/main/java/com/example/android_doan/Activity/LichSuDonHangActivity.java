package com.example.android_doan.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
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

public class LichSuDonHangActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DeliveryOrdersAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_profile_order_history); // dùng lại layout

        recyclerView = findViewById(R.id.recycler_delivery_history);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadDeliveredOrders(); // gọi API như trong DeliveryFragment
        // Gắn sự kiện cho nút quay lại
        ImageView imgBack = findViewById(R.id.img_chevron_left);
        imgBack.setOnClickListener(v -> {
            finish(); // Quay về fragment trước đó
        });
    }

    private void loadDeliveredOrders() {
        SharedPreferences prefs = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String token = prefs.getString("token", "");

        if (token.isEmpty()) {
            Toast.makeText(this, "Token không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService apiService = RetrofitClient.getApiService();
        Call<List<Delivery>> call = apiService.getAllDeliveredDeliveries("Bearer " + token);
        call.enqueue(new Callback<List<Delivery>>() {
            @Override
            public void onResponse(Call<List<Delivery>> call, Response<List<Delivery>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter = new DeliveryOrdersAdapter(LichSuDonHangActivity.this, response.body());
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(LichSuDonHangActivity.this, "Không có đơn hàng đã giao", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Delivery>> call, Throwable t) {
                Toast.makeText(LichSuDonHangActivity.this, "Lỗi kết nối server", Toast.LENGTH_SHORT).show();
                Log.e("DeliveryActivity", "API call failed", t);
            }
        });
    }
}


