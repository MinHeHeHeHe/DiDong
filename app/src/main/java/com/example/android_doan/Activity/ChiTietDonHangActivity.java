package com.example.android_doan.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android_doan.R;
import com.example.android_doan.adapter.ItemDeliveryMultiAdapter;
import com.example.android_doan.model.Delivery;
import com.example.android_doan.network.ApiService;
import com.example.android_doan.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChiTietDonHangActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ItemDeliveryMultiAdapter adapter;
    private List<Object> itemList = new ArrayList<>();
    private String deliveryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_item);

        recyclerView = findViewById(R.id.recycler_delivery_items);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ItemDeliveryMultiAdapter(itemList);
        recyclerView.setAdapter(adapter);

        deliveryId = getIntent().getStringExtra("deliveryId");

        // Gắn sự kiện cho nút quay lại
        ImageView imgBack = findViewById(R.id.img_chevron_left);
        imgBack.setOnClickListener(v -> {
            finish(); // Quay về fragment trước đó
        });

        fetchDeliveryDetails();
    }

    private void fetchDeliveryDetails() {
        SharedPreferences prefs = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String token = prefs.getString("token", "");

        ApiService apiService = RetrofitClient.getApiService();
        apiService.getDeliveryById(deliveryId, "Bearer " + token).enqueue(new Callback<Delivery>() {
            @Override
            public void onResponse(Call<Delivery> call, Response<Delivery> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Delivery delivery = response.body();
                    itemList.clear();
                    if (delivery.getOrder() != null) {
                        if (delivery.getOrder().getPizzas() != null)
                            itemList.addAll(delivery.getOrder().getPizzas());
                        if (delivery.getOrder().getDrinks() != null)
                            itemList.addAll(delivery.getOrder().getDrinks());
                        if (delivery.getOrder().getSides() != null)
                            itemList.addAll(delivery.getOrder().getSides());
                        if (delivery.getOrder().getSalads() != null)
                            itemList.addAll(delivery.getOrder().getSalads());
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(ChiTietDonHangActivity.this, "Không tìm thấy đơn hàng", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Delivery> call, Throwable t) {
                Toast.makeText(ChiTietDonHangActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                Log.e("ChiTietDonHang", "API error", t);
            }
        });
    }
}
