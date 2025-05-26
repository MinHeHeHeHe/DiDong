package com.example.android_doan.ui.orders;

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
import com.example.android_doan.adapter.CartMultiAdapter;
import com.example.android_doan.model.Cart;
import com.example.android_doan.model.CartDisplayItem;
import com.example.android_doan.model.CartDisplayItem.Type;
import com.example.android_doan.network.ApiService;
import com.example.android_doan.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartFragment extends Fragment {

    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orders_cart_list, container, false);
        recyclerView = view.findViewById(R.id.recycler_cart_items);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        loadCart(); // Gọi khi fragment được tạo lần đầu

        return view;
    }

    private void loadCart() {
        SharedPreferences prefs = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String token = prefs.getString("token", null);

        if (token == null || token.isEmpty()) {
            Toast.makeText(getContext(), "Không tìm thấy token, bạn cần đăng nhập lại!", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService apiService = RetrofitClient.getApiService();
        apiService.getCart("Bearer " + token).enqueue(new Callback<Cart>() {
            @Override
            public void onResponse(Call<Cart> call, Response<Cart> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Cart cart = response.body();
                    List<CartDisplayItem> allItems = new ArrayList<>();

                    for (int i = 0; i < cart.getPizzas().size(); i++) {
                        allItems.add(new CartDisplayItem(Type.PIZZA, cart.getPizzas().get(i), i));
                    }
                    for (int i = 0; i < cart.getDrinks().size(); i++) {
                        allItems.add(new CartDisplayItem(Type.DRINK, cart.getDrinks().get(i), i));
                    }
                    for (int i = 0; i < cart.getSides().size(); i++) {
                        allItems.add(new CartDisplayItem(Type.SIDE, cart.getSides().get(i), i));
                    }
                    for (int i = 0; i < cart.getSalads().size(); i++) {
                        allItems.add(new CartDisplayItem(Type.SALAD, cart.getSalads().get(i), i));
                    }

                    CartMultiAdapter adapter = new CartMultiAdapter(getContext(), allItems);
                    recyclerView.setAdapter(adapter);
                } ///else {
                  ///  Toast.makeText(getContext(), "Không thể tải giỏ hàng", Toast.LENGTH_SHORT).show();
               /// }
            }

            @Override
            public void onFailure(Call<Cart> call, Throwable t) {
                Log.e("CART_API_ERROR", "Lỗi: " + t.getMessage(), t);
                Toast.makeText(getContext(), "Lỗi khi tải giỏ hàng: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        loadCart(); // Tải lại giỏ hàng mỗi khi fragment hiển thị lại
    }


}